# CLAUDE.md

本文档为 Claude Code (claude.ai/code) 提供本仓库的工作指导。

## 项目概述

基于 Spring Boot 的《数据结构》课程智慧学习平台后端。采用双数据库架构：MySQL 存储结构化数据，Neo4j 存储知识图谱。

## 构建与运行

- **构建**: `mvn clean package`
- **运行**: `mvn spring-boot:run` 或直接运行 `LearningPlatformApplication`
- **运行测试**: `mvn test`
- **运行单个测试**: `mvn test -Dtest=ClassName`
- **API 基础地址**: `http://localhost:8080/api`

## 开发环境

- JDK 17+
- Maven 3.6+
- MySQL 8.0+（运行 `sql/init.sql` 初始化）
- Neo4j 5.0+（bolt://localhost:7687）

数据库凭据硬编码在 `application.yml` 中（MySQL root/root，Neo4j neo4j/neo4j）。

## 架构

### 双数据库
- **MySQL**: 存储用户、习题、学习进度、内容、视频、AI 对话记录等结构化数据。通过 **MyBatis Plus** 访问。
- **Neo4j**: 存储知识图谱节点和关系。通过 **Spring Data Neo4j**（`spring-boot-starter-data-neo4j`）访问。

### 代码约定
- **Controllers** 返回 `Result<T>`（`common/Result.java`），统一包装 `code`、`message`、`data`。
- **Entities** 继承 `BaseEntity`，自动提供 `createTime`、`updateTime` 和逻辑删除字段 `isDeleted`。
- **MyBatis Plus 自动填充**: `config/MyMetaObjectHandler` 在插入时自动设置 `createTime`，在插入/更新时自动设置 `updateTime`。
- **MyBatis Plus 逻辑删除**: 全局配置；`isDeleted=1` 表示已删除。
- **Mapper 扫描**: 主应用类上的 `@MapperScan("com.learningplatform.repository")`。
- **JSON 处理**: 使用 Fastjson2。

### 包结构
- `controller/` — REST 控制器，基础路径 `/api`
- `service/` — 业务逻辑
- `repository/` — MyBatis Mapper / Neo4j Repository
- `entity/` — JPA/MyBatis 实体类
- `config/` — Spring 配置类
- `common/` — 公共类，如 `Result<T>`
- `utils/` — 工具类

## 协作规范

本项目为多人协作项目，请遵守以下规则以保持代码库一致性。

### 开工前必读
- 阅读 `docs/项目架构.md` 了解当前架构和 TODO 清单。
- 阅读 `docs/分工.md` 了解各模块负责人。
- 阅读 `docs/要求.md` 了解完整功能需求。

### 代码改动
- **改动最小化**：只改需要的部分，不重构无关代码。
- **Fail fast**：让异常自然冒泡，不要 catch 后静默忽略。
- 不写假设性防御代码（"万一将来需要" 的 guard clause）。在系统边界（用户输入、外部 API）做验证，内部代码信任调用方。
- 遵循现有包结构和命名规范。

### 文档同步
- **新增或修改表结构时**，同步更新 `sql/init.sql`。
- **做出架构决策或解决 TODO 项时**，同步更新 `docs/项目架构.md`。
- **模块负责人变更时**，同步更新 `docs/分工.md`。
- **API 契约变更时**，在 PR 描述中说明，以便前端负责人调整。
- **所有文档使用中文编写**（代码和注释除外）。

### Git 工作流
- 在自己的 `feature/<模块>-<描述>` 分支上开发。
- 完成后向 `main` 分支发起 Pull Request；PR 需要至少一次 review 才能合并。
- 禁止向 `main` 分支 force-push。

### 测试
- 提交 PR 前运行 `mvn test`。
- 新增接口后，用 `curl` 或本地请求手动验证。

## 文档索引

项目需求和团队分工等文档位于 `docs/` 目录下，均为中文：
- `docs/要求.md` — 需求文档
- `docs/分工.md` — 团队分工
- `docs/项目架构.md` — 架构说明（含待确定 TODO 清单）
- `docs/可参考项目.md` — 参考项目分析
- `docs/待补充能力清单.md` — 现有架构从未涉及的关键能力缺口（与 TODO 清单并列）
