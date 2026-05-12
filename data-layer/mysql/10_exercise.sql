-- 模块: 习题
-- 负责人: 李卓优
-- 日期: 2026-05-06

DROP TABLE IF EXISTS `exercise`;
CREATE TABLE `exercise` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `knowledge_point_id` bigint NOT NULL COMMENT '知识点ID',
  `title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '题目',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '题型：CHOICE, FILL, PROGRAMMING',
  `difficulty` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '难度：EASY, MEDIUM, HARD',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类别：基础题, 考研题, 面试题',
  `options` json DEFAULT NULL COMMENT '选择题选项',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '答案',
  `analysis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '解析',
  `time_limit` int DEFAULT 60 COMMENT '时间限制(秒)',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int DEFAULT 0 COMMENT '逻辑删除标识',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '题目描述',
  `initial_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '编程题初始代码',
  `test_cases` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '测试用例(JSON格式)',
  `pass_rate` double DEFAULT 0 COMMENT '通过率',
  `total_attempts` int DEFAULT 0 COMMENT '总尝试次数',
  `correct_attempts` int DEFAULT 0 COMMENT '正确次数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_knowledge_point_id`(`knowledge_point_id` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_difficulty`(`difficulty` ASC) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='练习题表';

DROP TABLE IF EXISTS `user_exercise_record`;
CREATE TABLE `user_exercise_record` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `exercise_id` BIGINT NOT NULL COMMENT '题目ID',
    `user_answer` TEXT COMMENT '用户答案',
    `is_correct` TINYINT COMMENT '是否正确：0-否，1-是',
    `score` INT COMMENT '得分',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    INDEX idx_user_id (`user_id`),
    INDEX idx_exercise_id (`exercise_id`),
    INDEX idx_submit_time (`submit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户做题记录表';

INSERT INTO `exercise` VALUES (1, 1, '数组的时间复杂度', 'SINGLE_CHOICE', 'EASY', 'BASIC', '[\"A. O(1)\", \"B. O(n)\", \"C. O(log n)\", \"D. O(n²)\"]', 'A', '数组支持随机访问，访问任意元素的时间复杂度是常数O(1)。', 30, 1, '2026-05-05 02:58:46', '2026-05-05 02:58:46', 0, '在数组中访问第i个元素的时间复杂度是：', NULL, NULL, 85.5, 200, 171);
INSERT INTO `exercise` VALUES (2, 1, '数据结构分类', 'MULTIPLE_CHOICE', 'MEDIUM', 'BASIC', '[\"A. 树\", \"B. 图\", \"C. 栈\", \"D. 队列\", \"E. 集合\", \"F. 字典\"]', 'A,B', '非线性数据结构中元素之间存在一对多或多对多的关系，包括树、图等。栈和队列是线性结构。', 45, 2, '2026-05-05 02:58:46', '2026-05-05 02:58:46', 0, '以下哪些属于非线性数据结构？', NULL, NULL, 70.3, 150, 105);

INSERT INTO `knowledge_point` VALUES (1, NULL, '数据结构基础', 'DS_BASIC', NULL, '数据结构', 1, 'EASY', '数据结构基本概念、分类和特点', NULL, '2026-05-05 03:25:08', '2026-05-05 03:25:08', 0);
INSERT INTO `knowledge_point` VALUES (2, NULL, '链表', 'LINKED_LIST', NULL, '数据结构', 2, 'MEDIUM', '单链表、双向链表、循环链表的基本操作', NULL, '2026-05-05 03:25:08', '2026-05-05 03:25:08', 0);
INSERT INTO `knowledge_point` VALUES (3, NULL, '栈', 'STACK', NULL, '数据结构', 2, 'MEDIUM', '栈的基本操作和应用场景', NULL, '2026-05-05 03:25:08', '2026-05-05 03:25:08', 0);
INSERT INTO `knowledge_point` VALUES (4, NULL, '队列', 'QUEUE', NULL, '数据结构', 2, 'MEDIUM', '队列的基本操作和应用场景', NULL, '2026-05-05 03:25:08', '2026-05-05 03:25:08', 0);
INSERT INTO `knowledge_point` VALUES (5, NULL, '树', 'TREE', NULL, '数据结构', 3, 'HARD', '二叉树、二叉搜索树、平衡树', NULL, '2026-05-05 03:25:08', '2026-05-05 03:25:08', 0);
INSERT INTO `knowledge_point` VALUES (6, NULL, '图', 'GRAPH', NULL, '数据结构', 3, 'HARD', '图的表示、遍历和算法', NULL, '2026-05-05 03:25:08', '2026-05-05 03:25:08', 0);
