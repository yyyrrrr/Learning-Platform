#!/bin/bash
set -e

export LANG=C.UTF-8
export LC_ALL=C.UTF-8

echo "========================================="
echo "Neo4j Docker 数据导入工具"
echo "========================================="

NEO4J_URI="bolt://localhost:7687"
NEO4J_USER="neo4j"
NEO4J_PASSWORD="neo4j123"
IMPORT_DIR="/import"
SCRIPTS_DIR="/scripts"

echo "等待Neo4j启动..."
until cypher-shell -u "$NEO4J_USER" -p "$NEO4J_PASSWORD" -a "$NEO4J_URI" "RETURN 1;" > /dev/null 2>&1; do
    echo "Neo4j尚未就绪，等待中..."
    sleep 5
done
echo "Neo4j已就绪！"
echo ""

# 1. 执行Schema定义
echo "步骤1: 创建索引和约束..."
if [ -f "$SCRIPTS_DIR/schema.cql" ]; then
    cypher-shell -u "$NEO4J_USER" -p "$NEO4J_PASSWORD" -a "$NEO4J_URI" < "$SCRIPTS_DIR/schema.cql"
    echo "✓ Schema创建完成"
else
    echo "⚠ 未找到 schema.cql，跳过"
fi
echo ""

# 2. 逐个执行所有 .cypher 文件（按文件名排序）
echo "步骤2: 导入知识点数据..."
CYPHER_FILES=$(ls "$IMPORT_DIR"/*.cypher 2>/dev/null | sort)
if [ -z "$CYPHER_FILES" ]; then
    echo "⚠ 未找到任何 .cypher 文件"
else
    for f in $CYPHER_FILES; do
        filename=$(basename "$f")
        echo "  执行: $filename"
        cypher-shell -u "$NEO4J_USER" -p "$NEO4J_PASSWORD" -a "$NEO4J_URI" < "$f" || echo "  ⚠ $filename 执行出错（可能是重复导入，可忽略）"
    done
    echo "✓ 知识点数据导入完成"
fi
echo ""

# 3. 验证导入结果
echo "========================================="
echo "导入结果验证"
echo "========================================="
NODE_COUNT=$(cypher-shell -u "$NEO4J_USER" -p "$NEO4J_PASSWORD" -a "$NEO4J_URI" "MATCH (kp:KnowledgePoint) RETURN count(kp);" | tail -1)
echo "知识点节点数: $NODE_COUNT"

PARENT_COUNT=$(cypher-shell -u "$NEO4J_USER" -p "$NEO4J_PASSWORD" -a "$NEO4J_URI" "MATCH ()-[r:PARENT_OF]->() RETURN count(r);" | tail -1)
echo "层级关系数: $PARENT_COUNT"

RELATED_COUNT=$(cypher-shell -u "$NEO4J_USER" -p "$NEO4J_PASSWORD" -a "$NEO4J_URI" "MATCH ()-[r:RELATED_TO]->() RETURN count(r);" | tail -1)
echo "关联关系数: $RELATED_COUNT"

echo ""
echo "========================================="
echo "✓ 所有数据导入完成！"
echo "========================================="
