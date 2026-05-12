# 数据层目录

## 目录结构

```
data-layer/
├── docker/
│   ├── docker-compose.yml       # MySQL + Neo4j 容器配置
│   ├── .env                     # 环境变量
│   └── entrypoint.sh            # Neo4j 自动导入入口
├── mysql/                       # ✅ 所有 SQL 文件放这里
│   ├── 00_schema.sql            # 基础表结构（数据层负责人维护）
│   ├── 10_exercise.sql          # 习题模块（负责人: 李卓优）
│   └── 20_search.sql            # 查找模块（负责人: 马之涵）
├── neo4j/                       # ✅ 所有 Neo4j 相关文件放这里
│   ├── schema.cql               # 索引和约束
│   ├── auto_import.sh           # 自动导入脚本
│   └── import/                  # ✅ Cypher 文件放这里
│       ├── 10_set.cypher        # 集合模块
│       ├── 20_linear_list.cypher
│       ├── 30_tree.cypher
│       ├── 40_graph.cypher
│       ├── 50_search.cypher
│       ├── 60_sort.cypher
│       ├── 90_cross_module.cypher   # 跨模块关联
│       └── tu_knowledge_points.cypher  # 图模块（备用版本）
└── dictionary/
    └── knowledge_point_dict.md  # 知识点数据字典
```

---

## 快速启动

```bash
cd docker
docker-compose up -d
```

| 服务 | 地址 | 凭据 |
|------|------|------|
| MySQL | `localhost:13306` | root / root123 |
| Neo4j | `http://localhost:17474` | neo4j / neo4j123 |
| Neo4j Bolt | `bolt://localhost:17687` | neo4j / neo4j123 |

---

## 工作机制

### MySQL 数据导入

Docker 启动时自动执行 `mysql/` 目录下所有 `.sql` 文件（按文件名排序）。

- **`00_schema.sql`**：基础表结构（由数据层负责人统一维护）
- **`10_exercise.sql`**、**`20_search.sql`**：各模块负责人提交的 DDL+DML

> 每个 `.sql` 文件可以同时包含建表语句（CREATE TABLE）和数据插入语句（INSERT）。MySQL 官方镜像会自动按文件名顺序依次执行。

### Neo4j 数据导入

Docker 启动时自动执行以下流程：
1. 执行 `schema.cql` 创建索引和约束
2. 扫描 `import/` 目录下所有 `.cypher` 文件（按文件名排序），逐个执行

> 每个模块一个 `.cypher` 文件，包含该模块的 CREATE 节点和 CREATE 关系语句。

---

## 协同流程

### 新增/修改知识点（Neo4j）

```
step 1: 在 dictionary/knowledge_point_dict.md 中填写知识点
step 2: 在 neo4j/import/ 下新建或修改对应模块的 .cypher 文件
step 3: 提 PR 给数据层负责人审核
step 4: 合并后重启容器自动导入
```

### 新增/修改业务数据（MySQL）

```
step 1: 在 mysql/ 下新建带数字编号的 .sql 文件（参考已有文件格式）
        也可直接修改自己模块的 .sql 文件
step 2: 提 PR 给数据层负责人审核
step 3: 合并后重启容器自动导入
```

### 文件命名规则

- **SQL 文件**: `NN_模块名.sql`（NN 为两位数字，如 `10_exercise.sql`）
- **Cypher 文件**: `NN_模块名.cypher`（NN 为两位数字，如 `30_tree.cypher`）
- **数字决定执行顺序**，有外键依赖时注意编号先后

---

## 各模块 SQL 文件说明

| 文件 | 维护人 | 内容说明 |
|------|--------|----------|
| `00_schema.sql` | 数据层负责人 | 核心表结构（用户、知识点、学习记录等） |
| `10_exercise.sql` | 李卓优 | 习题表 + 习题数据 |
| `20_search.sql` | 马之涵 | 查找模块知识点数据 |

## 各模块 Cypher 文件说明

| 文件 | 维护人 | 内容说明 |
|------|--------|----------|
| `10_set.cypher` | 待认领 | 集合模块 |
| `20_linear_list.cypher` | 待认领 | 线性表模块 |
| `30_tree.cypher` | 待认领 | 树模块 |
| `40_graph.cypher` | 待认领 | 图模块 |
| `50_search.cypher` | 马之涵 | 查找模块 |
| `60_sort.cypher` | 待认领 | 排序模块 |
| `90_cross_module.cypher` | 数据层负责人 | 跨模块关联关系 |

---

## 常用命令

```bash
# 重启所有服务
cd docker && docker-compose restart

# 重建数据库（清空所有数据）
cd docker && docker-compose down -v && docker-compose up -d

# 仅重启 Neo4j
docker-compose restart neo4j

# 查看 MySQL 导入日志
docker-compose logs mysql

# 查看 Neo4j 导入日志
docker-compose logs neo4j

# 进入 MySQL 命令行
docker-compose exec mysql mysql -uroot -proot123 learning_platform

# 进入 Neo4j 命令行
docker-compose exec neo4j cypher-shell -u neo4j -p neo4j123
```

---

## 注意事项

- 端口已调整为 13306/17474/17687，避免与本地服务冲突
- 数据持久化在 Docker Volumes 中，`down -v` 会清空数据
- SQL 文件使用 `CREATE TABLE IF NOT EXISTS` 可安全重复执行
- Cypher 文件因包含 `CREATE` 语句，重复执行会报唯一约束冲突，需清空数据卷后重来
