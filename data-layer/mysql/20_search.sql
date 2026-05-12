-- 模块: 查找
-- 负责人: 马之涵
-- 日期: 2026-04-26

INSERT IGNORE INTO knowledge_point (neo4j_id, name, code, parent_id, module, level, difficulty, description, core_points)
VALUES
('KP_SEARCH_001', '查找', 'SEARCH', NULL, 'SEARCH', 1, 'MEDIUM', '在一组数据中定位目标元素的位置或是否存在，是检索、索引、推荐等功能的基础能力', '查找目标定义、成功/失败结果、静态与动态查找区别；示例: 在学生名单中找"张三"是否存在'),
('KP_SEARCH_002', '顺序查找-原理', 'SEQUENTIAL_SEARCH_PRINCIPLE', (SELECT id FROM knowledge_point WHERE neo4j_id = 'KP_SEARCH_001' LIMIT 1), 'SEARCH', 3, 'EASY', '从第1个元素开始逐个比对，直到找到目标或全部比完，逻辑最直观', '逐个比较、命中即停、最坏需看完全部元素；示例: 点名时按花名册从上到下找人'),
('KP_SEARCH_003', '顺序查找-实现优化', 'SEQUENTIAL_SEARCH_IMPLEMENTATION', (SELECT id FROM knowledge_point WHERE neo4j_id = 'KP_SEARCH_002' LIMIT 1), 'SEARCH', 3, 'MEDIUM', '顺序查找在代码层的常见写法和小优化，重点是减少判断和分支', '哨兵法减少边界判断、for/while写法、数组与链表实现差异；示例: 在日志列表中扫描某个错误码');
