# API 接口规范

## 1. 基础协议

| 项目 | 约定 |
|------|------|
| Base URL | `http://localhost:8080/api`（生产环境由 Nginx 反向代理统一入口） |
| 内容格式 | `Content-Type: application/json` |
| 编码 | UTF-8 |
| 时间格式 | `yyyy-MM-dd HH:mm:ss`（数据库侧）/ `ISO 8601`（建议前端传输时使用） |
| 枚举传递 | String，大写下划线，如 `SINGLE_CHOICE`, `NOT_STARTED` |

---

## 2. 统一响应格式

全站统一使用 `Result<T>`，**废弃** `ApiResponse<T>` 的提法（后者在代码库中不存在，避免文档与代码矛盾）。

### 2.1 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

### 2.2 失败响应

```json
{
  "code": 400,
  "message": "参数校验失败: title: 标题不能为空",
  "data": null
}
```

### 2.3 分页响应结构

分页数据统一包装在业务 DTO 中，再由 `Result<T>` 包裹：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [ ... ],
    "total": 100,
    "page": 1,
    "pageSize": 20
  }
}
```

**分页字段固定名**：
- `page`：当前页码，**从 1 开始**
- `pageSize`：每页条数，默认 20，最大 100
- `total`：总记录数
- `records`：当前页数据列表

> **注意**：当前代码中 `ExerciseService` 已使用 MyBatis Plus `Page`，但 `PaginationInnerInterceptor` 未在配置中显式注册。请补充 `MybatisPlusConfig` 确保 `total` 能正确返回。

---

## 3. 错误码体系

### 3.1 HTTP 状态码与业务码映射

| HTTP 状态 | 业务 code | 含义 | 触发场景 |
|-----------|-----------|------|----------|
| 200 | 200 | 成功 | 正常返回 |
| 400 | 400 | 参数校验失败 | `@Valid` 校验失败、`MethodArgumentNotValidException` |
| 401 | 401 | 未认证 | 未登录或 Token 失效（预留） |
| 403 | 403 | 无权限 | 角色不匹配（预留） |
| 404 | 404 | 资源不存在 | 查询不到记录 |
| 500 | 500 | 业务异常 | `BusinessException`（默认） |
| 500 | 500 | 系统异常 | 未捕获的 `Exception` |

### 3.2 业务错误码区间划分

为便于定位问题来源，各模块按区间分配错误码：

| 模块 | 错误码区间 | 说明 |
|------|-----------|------|
| 公共/系统 | 500 | 默认系统异常 |
| 用户模块 | 1000 - 1099 | 登录、注册、用户信息 |
| 习题模块 | 1100 - 1199 | 题目、答题、判题 |
| 代码运行模块 | 1200 - 1299 | Piston 调用、编译、执行 |
| 知识图谱模块 | 1300 - 1399 | Neo4j 查询、节点/关系 |
| 知识点内容模块 | 1400 - 1499 | 内容、代码示例、视频 |
| 学习跟踪模块 | 1500 - 1599 | 进度、行为记录、统计 |
| AI 辅助模块 | 1600 - 1699 | 对话、代码分析、生成 |
| 视频模块 | 1700 - 1799 | 视频播放、观看记录 |

**使用方式**：
```java
throw new BusinessException(1101, "习题不存在");
throw new BusinessException(1501, "学习进度记录不存在");
```

---

## 4. 认证与鉴权（临时方案）

### 4.1 当前临时方案

在 JWT/Session 方案冻结前，统一使用请求头传递当前用户 ID：

```
X-User-Id: 1
```

- 若请求头未携带，后端日志警告并使用默认用户 `1L`（仅开发阶段容忍）。
- 各模块 Controller 中通过统一方法获取：
  ```java
  private Long getCurrentUserId(Long userId) {
      return userId != null ? userId : 1L;
  }
  ```

### 4.2 后续演进路径

认证方案确定后，统一替换为：
- **JWT**：`Authorization: Bearer <token>`，由网关/拦截器解析 token 注入 `UserContext`。
- **或 Session**：基于 Spring Security 的 `SecurityContextHolder`。

**规则**：任何模块不得在本地私自实现另一套认证逻辑。

---

## 5. 参数规范

### 5.1 路径参数

使用 `@PathVariable`，类型为 `Long` 或 `String`：
```
GET /api/exercise/{id}
GET /api/graph/node/{neo4jId}
```

### 5.2 查询参数

使用 `@RequestParam` 或 DTO 自动绑定，分页参数必须显式声明默认值：
```java
private Integer page = 1;
private Integer pageSize = 20;
```

### 5.3 请求体参数

使用 `@RequestBody @Valid DTO`，DTO 必须使用 `jakarta.validation` 注解（Spring Boot 3 已迁移至 `jakarta` 命名空间）。

---

## 6. 现有接口登记（已实现的 Controller）

以下接口基于现有代码整理，作为后续扩展的基准。

### 6.1 用户模块（`/api/user`）

| 方法 | 路径 | 说明 | 请求 |
|------|------|------|------|
| GET | `/user/{id}` | 根据 ID 查询用户 | Path: `id` |
| POST | `/user` | 注册用户 | Body: `User`（密码明文，后端 bcrypt 加密） |

### 6.2 知识点模块（`/api/knowledge`）

| 方法 | 路径 | 说明 | 请求 |
|------|------|------|------|
| GET | `/knowledge/list` | 查询全部知识点列表 | 无 |
| GET | `/knowledge/{id}` | 根据 ID 查询知识点 | Path: `id` |
| POST | `/knowledge` | 创建知识点 | Body: `CreateKnowledgePointRequest` |

### 6.3 习题模块（`/api/exercise`）

| 方法 | 路径 | 说明 | 请求 |
|------|------|------|------|
| GET | `/exercise/list` | 习题列表（分页+筛选） | Query: `knowledgeId`, `difficulty`, `type`, `keyword`, `page`, `pageSize` |
| GET | `/exercise/{id}` | 习题详情 | Path: `id` |
| POST | `/exercise` | 创建习题 | Body: `CreateExerciseRequest` |
| PUT | `/exercise/{id}` | 更新习题 | Path: `id`; Body: `UpdateExerciseRequest` |
| DELETE | `/exercise/{id}` | 删除习题（逻辑删除） | Path: `id` |
| POST | `/exercise/submit` | 提交答案 | Header: `X-User-Id`; Body: `SubmitAnswerRequest` |
| GET | `/exercise/wrong` | 获取当前用户错题列表 | Header: `X-User-Id`; Query: 同列表筛选 |
| GET | `/exercise/knowledge/{knowledgeId}` | 根据知识点查询习题 | Path: `knowledgeId` |

### 6.4 知识图谱模块（`/api/graph`）

| 方法 | 路径 | 说明 | 请求 |
|------|------|------|------|
| GET | `/graph/data` | 获取全图节点与边 | 无 |
| GET | `/graph/node/{id}` | 获取单个节点详情 | Path: `id`（neo4j_id） |
| GET | `/graph/search` | 按关键词搜索节点 | Query: `keyword` |

### 6.5 代码运行模块（`/api/code`）

| 方法 | 路径 | 说明 | 请求 |
|------|------|------|------|
| POST | `/code/run` | 自由运行代码 | Body: `CodeRunRequest` |
| POST | `/code/submit` | 提交代码并判题 | Body: `CodeSubmitRequest` |

---

## 7. 规划接口（基于模块文档待实现）

以下接口来源于各模块设计文档，**尚未在代码中实现**，作为后续开发的接口契约草稿。

### 7.1 学习跟踪与统计模块（`/api/statistics`, `/api/learning`）

| 方法 | 路径 | 说明 | 归属模块 |
|------|------|------|----------|
| POST | `/learning/record` | 记录学习行为 | 学习跟踪 |
| GET | `/statistics/overview` | 学习概况（总时长、已学知识点、做题数、正确率） | 学习跟踪 |
| GET | `/statistics/progress` | 各模块学习进度（柱状图数据） | 学习跟踪 |
| GET | `/statistics/mastery` | 各模块掌握度（雷达图数据） | 学习跟踪 |
| GET | `/statistics/trend` | 近 N 天学习趋势（折线图数据） | 学习跟踪 |
| GET | `/statistics/weak-points` | 薄弱知识点 TOP N | 学习跟踪 |
| GET | `/statistics/wrong-questions` | 错题集列表 | 学习跟踪 |
| GET | `/learning/progress/{knowledgePointId}` | 查询某知识点当前学习状态 | 学习跟踪 |

### 7.2 视频模块（`/api/video`）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/video/list` | 根据知识点查询视频列表 |
| GET | `/video/{id}` | 视频详情 |
| POST | `/video/watch-record` | 上报观看进度 |
| GET | `/video/watch-record/{videoId}` | 查询某视频观看进度 |

### 7.3 AI 辅助模块（Java 侧代理层，`/api/ai`）

> 当前 AI 核心能力由 Python 服务（`localhost:9000`）提供。若前端通过 Java 后端代理调用，需补充以下接口：

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/ai/chat` | 发起 AI 问答 |
| GET | `/ai/chat/history` | 查询某 session 的历史消息 |
| POST | `/ai/code/analysis` | 代码分析与纠错 |
| POST | `/ai/code/generate` | 代码生成 |

### 7.4 学习目录模块（`/api/learning-path`）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/learning-path/tree` | 获取层级目录树 |
| GET | `/learning-path/recommend` | 获取推荐学习路径 |

### 7.5 知识点内容模块（`/api/knowledge-content`）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/knowledge-content/{knowledgePointId}` | 获取某知识点的全部内容块 |
| GET | `/code-example/{knowledgePointId}` | 获取某知识点的代码示例列表 |

---

## 8. 异常处理约定

### 8.1 异常抛出层级

- **Controller 层**：只处理参数绑定和格式转换异常，不处理业务逻辑异常。
- **Service 层**：业务规则校验失败直接抛出 `BusinessException`。
- **Mapper/数据访问层**：不捕获 SQL 异常，让其通过 `GlobalExceptionHandler` 统一转换为 `Result.error(500, "系统异常")`。

### 8.2 校验注解使用

- Controller 入口参数必须使用 `@Valid` 或 `@Validated`。
- DTO 中使用 `jakarta.validation.constraints.*` 注解（非 `javax`）。
- 分组校验：若同一 DTO 在创建和更新时的校验规则不同，使用 `validation groups`（如 `CreateGroup.class`, `UpdateGroup.class`），禁止在 Service 中手动重复校验。

### 8.3 GlobalExceptionHandler 处理范围

当前 `GlobalExceptionHandler` 已覆盖：
- `BusinessException` → 业务错误码 + 消息
- `MethodArgumentNotValidException` → 400，取第一个字段错误
- `Exception` → 500，系统异常

**建议补充**：
- `ConstraintViolationException`（参数校验，如 `@RequestParam` 上的校验）
- `NoHandlerFoundException`（404 路径不存在）
- `HttpRequestMethodNotSupportedException`（405 方法不支持）

---

## 9. 前后端协作 checklist

任何模块在新增接口前，至少确认以下 6 项：

1. [ ] 接口路径是否遵循 `/api/{模块名}/{资源}` 的 RESTful 风格？
2. [ ] 响应是否统一包装为 `Result<T>`？
3. [ ] 分页接口是否包含 `records/total/page/pageSize`？
4. [ ] 错误场景是否抛出 `BusinessException` 并分配了业务错误码？
5. [ ] 是否需要登录？若需要，是否依赖 `X-User-Id`（或后续的统一认证方案）？
6. [ ] 是否在本文档的“现有/规划接口”章节登记？
