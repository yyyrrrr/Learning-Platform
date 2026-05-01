package com.learningplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphService {

    @Autowired
    private Neo4jClient neo4jClient;

    /**
     * 获取完整图谱数据：所有节点和关系
     */
    public Map<String, Object> getGraphData() {
        List<Map<String, Object>> nodes = getAllNodes();
        List<Map<String, Object>> edges = getAllEdges();

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodes);
        result.put("edges", edges);
        return result;
    }

    /**
     * 获取所有知识点节点
     */
    private List<Map<String, Object>> getAllNodes() {
        return neo4jClient.query(
                "MATCH (n:KnowledgePoint) RETURN n.neo4j_id AS id, n.name AS name, " +
                        "n.code AS code, n.module AS module, n.level AS level, " +
                        "n.difficulty AS difficulty, n.description AS description, " +
                        "n.core_points AS corePoints ORDER BY n.module, n.level"
        )
                .fetchAs(Map.class)
                .mappedBy((typeSystem, record) -> {
                    Map<String, Object> node = new HashMap<>();
                    node.put("id", record.get("id").asString());
                    node.put("name", record.get("name").asString());
                    node.put("code", record.get("code").asString());
                    node.put("module", record.get("module").asString());
                    node.put("level", record.get("level").asInt());
                    node.put("difficulty", record.get("difficulty").asString());
                    node.put("description", record.get("description").asString());
                    node.put("corePoints", record.get("corePoints").asString());
                    return node;
                })
                .all()
                .stream().toList();
    }

    /**
     * 获取所有关系
     */
    private List<Map<String, Object>> getAllEdges() {
        return neo4jClient.query(
                "MATCH (a:KnowledgePoint)-[r]->(b:KnowledgePoint) " +
                        "RETURN a.neo4j_id AS source, b.neo4j_id AS target, " +
                        "type(r) AS type, r.reason AS reason, r.order AS sortOrder"
        )
                .fetchAs(Map.class)
                .mappedBy((typeSystem, record) -> {
                    Map<String, Object> edge = new HashMap<>();
                    edge.put("source", record.get("source").asString());
                    edge.put("target", record.get("target").asString());
                    edge.put("type", record.get("type").asString());
                    if (record.get("reason") != null) {
                        edge.put("reason", record.get("reason").asString());
                    }
                    if (record.get("sortOrder") != null) {
                        edge.put("order", record.get("sortOrder").asInt());
                    }
                    return edge;
                })
                .all()
                .stream().toList();
    }

    /**
     * 获取单个节点详情
     */
    public Map<String, Object> getNodeDetail(String nodeId) {
        // 查询节点本身
        Map<String, Object> node = neo4jClient.query(
                "MATCH (n:KnowledgePoint {neo4j_id: $id}) " +
                        "RETURN n.neo4j_id AS id, n.name AS name, n.code AS code, " +
                        "n.module AS module, n.level AS level, n.difficulty AS difficulty, " +
                        "n.description AS description, n.core_points AS corePoints"
        )
                .bind(nodeId).to("id")
                .fetchAs(Map.class)
                .mappedBy((typeSystem, record) -> {
                    Map<String, Object> n = new HashMap<>();
                    n.put("id", record.get("id").asString());
                    n.put("name", record.get("name").asString());
                    n.put("code", record.get("code").asString());
                    n.put("module", record.get("module").asString());
                    n.put("level", record.get("level").asInt());
                    n.put("difficulty", record.get("difficulty").asString());
                    n.put("description", record.get("description").asString());
                    n.put("corePoints", record.get("corePoints").asString());
                    return n;
                })
                .one().orElse(null);

        if (node == null) {
            return null;
        }

        // 查询关联节点
        List<Map<String, Object>> related = neo4jClient.query(
                "MATCH (n:KnowledgePoint {neo4j_id: $id})-[r]-(m:KnowledgePoint) " +
                        "RETURN m.neo4j_id AS id, m.name AS name, type(r) AS relationType"
        )
                .bind(nodeId).to("id")
                .fetchAs(Map.class)
                .mappedBy((typeSystem, record) -> {
                    Map<String, Object> r = new HashMap<>();
                    r.put("id", record.get("id").asString());
                    r.put("name", record.get("name").asString());
                    r.put("relationType", record.get("relationType").asString());
                    return r;
                })
                .all()
                .stream().toList();

        node.put("relatedNodes", related);
        return node;
    }

    /**
     * 按关键词搜索节点
     */
    public List<Map<String, Object>> searchNodes(String keyword) {
        return neo4jClient.query(
                "MATCH (n:KnowledgePoint) " +
                        "WHERE n.name CONTAINS $keyword OR n.code CONTAINS $keyword OR n.description CONTAINS $keyword " +
                        "RETURN n.neo4j_id AS id, n.name AS name, n.code AS code, " +
                        "n.module AS module, n.level AS level, n.difficulty AS difficulty, " +
                        "n.description AS description, n.core_points AS corePoints"
        )
                .bind(keyword).to("keyword")
                .fetchAs(Map.class)
                .mappedBy((typeSystem, record) -> {
                    Map<String, Object> n = new HashMap<>();
                    n.put("id", record.get("id").asString());
                    n.put("name", record.get("name").asString());
                    n.put("code", record.get("code").asString());
                    n.put("module", record.get("module").asString());
                    n.put("level", record.get("level").asInt());
                    n.put("difficulty", record.get("difficulty").asString());
                    n.put("description", record.get("description").asString());
                    n.put("corePoints", record.get("corePoints").asString());
                    return n;
                })
                .all()
                .stream().toList();
    }
}
