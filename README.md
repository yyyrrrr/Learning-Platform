# 《数据结构》课程智慧学习平台

基于知识图谱的《数据结构》智慧学习平台，融合交互式学习、个性化跟踪、智能辅助功能。

## 技术栈

- **后端**: Spring Boot 3.2.4 + MyBatis Plus
- **数据库**: MySQL 8.0 + Neo4j
- **开发语言**: Java 17

## 项目结构

```
learning-platform/
├── src/main/
│   ├── java/com/learningplatform/
│   │   ├── controller/         # 控制层
│   │   ├── service/            # 服务层
│   │   ├── repository/         # 数据访问层
│   │   ├── entity/             # 实体类
│   │   ├── config/             # 配置类
│   │   ├── common/             # 公共类
│   │   └── utils/              # 工具类
│   └── resources/
│       ├── application.yml     # 配置文件
│       └── mapper/             # MyBatis映射文件
├── sql/
│   └── init.sql                # 数据库初始化脚本
├── docs/                       # 文档目录
│   ├── 要求.md                 # 项目需求文档
│   ├── 分工.md                 # 团队分工
│   └── 项目架构.md             # 架构说明
└── pom.xml
```

## 核心功能

1. **知识图谱构建** - 覆盖数据结构核心知识点
2. **交互式可视化** - 节点-边展示，支持缩放、搜索、高亮
3. **学习目录** - 层级目录，推荐学习路径
4. **知识点讲解** - 图文、代码、视频讲解
5. **习题模块** - 题库、答题、错题记录
6. **学习跟踪** - 学习进度统计、可视化报告
7. **AI辅助** - AI问答、代码分析、代码生成

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Neo4j 5.0+
- Maven 3.6+

### 启动步骤

1. 克隆项目
2. 初始化MySQL数据库：执行 `sql/init.sql`
3. 修改 `application.yml` 中的数据库配置
4. 启动Neo4j服务
5. 运行 `LearningPlatformApplication`
6. 访问 `http://localhost:8080/api`

## 团队分工

详见 `docs/分工.md`

## 开发规范

- 遵循Git工作流，通过PR合并代码
- 遵循代码规范，保持代码整洁
- 及时更新文档

## License

MIT