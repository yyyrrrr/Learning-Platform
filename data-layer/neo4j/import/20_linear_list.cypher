// ===================== 线性表模块 =====================
// 负责人: XXX
// 日期: 2026-05-08

CREATE (kp_ll:KnowledgePoint {
  neo4j_id: 'KP_LL_001',
  name: '线性表',
  code: 'LINEAR_LIST',
  module: 'LINEAR_LIST',
  level: 1,
  difficulty: 'EASY',
  description: '线性表是由n个数据元素组成的有限序列，是最基本、最常用的数据结构',
  core_points: '线性表定义、顺序存储、链式存储'
});

CREATE (kp_stack:KnowledgePoint {
  neo4j_id: 'KP_LL_002',
  name: '栈',
  code: 'STACK',
  module: 'LINEAR_LIST',
  level: 2,
  difficulty: 'EASY',
  description: '栈是一种后进先出(LIFO)的线性表，只允许在一端进行插入和删除操作',
  core_points: '栈的定义、顺序栈、链栈、栈的应用'
});

CREATE (kp_stack_push:KnowledgePoint {
  neo4j_id: 'KP_LL_003',
  name: '栈的基本操作',
  code: 'STACK_OPS',
  module: 'LINEAR_LIST',
  level: 3,
  difficulty: 'EASY',
  description: '栈的基本操作包括入栈(push)、出栈(pop)、取栈顶元素等',
  core_points: 'push、pop、top、isEmpty'
});

CREATE (kp_queue:KnowledgePoint {
  neo4j_id: 'KP_LL_004',
  name: '队列',
  code: 'QUEUE',
  module: 'LINEAR_LIST',
  level: 2,
  difficulty: 'EASY',
  description: '队列是一种先进先出(FIFO)的线性表，一端插入、另一端删除',
  core_points: '队列定义、顺序队列、链队列、循环队列'
});

CREATE (kp_queue_ops:KnowledgePoint {
  neo4j_id: 'KP_LL_005',
  name: '队列的基本操作',
  code: 'QUEUE_OPS',
  module: 'LINEAR_LIST',
  level: 3,
  difficulty: 'EASY',
  description: '队列的基本操作包括入队(enqueue)、出队(dequeue)、取队头元素等',
  core_points: 'enqueue、dequeue、front、isEmpty'
});

CREATE (kp_string:KnowledgePoint {
  neo4j_id: 'KP_LL_006',
  name: '串',
  code: 'STRING',
  module: 'LINEAR_LIST',
  level: 2,
  difficulty: 'MEDIUM',
  description: '串是由零个或多个字符组成的有限序列，又称为字符串',
  core_points: '串的定义、串的存储、串的模式匹配'
});

CREATE (kp_kmp:KnowledgePoint {
  neo4j_id: 'KP_LL_007',
  name: 'KMP算法',
  code: 'KMP',
  module: 'LINEAR_LIST',
  level: 3,
  difficulty: 'HARD',
  description: 'KMP算法是一种高效的字符串模式匹配算法，通过预处理next数组避免重复比较',
  core_points: 'next数组、模式匹配、时间复杂度O(n+m)'
});

CREATE (kp_array:KnowledgePoint {
  neo4j_id: 'KP_LL_008',
  name: '数组',
  code: 'ARRAY',
  module: 'LINEAR_LIST',
  level: 2,
  difficulty: 'EASY',
  description: '数组是由相同类型的数据元素构成的有限序列，支持随机访问',
  core_points: '数组定义、顺序存储、多维数组、稀疏矩阵'
});

CREATE (kp_glist:KnowledgePoint {
  neo4j_id: 'KP_LL_009',
  name: '广义表',
  code: 'GENERALIZED_LIST',
  module: 'LINEAR_LIST',
  level: 2,
  difficulty: 'MEDIUM',
  description: '广义表是线性表的推广，表中的元素可以是原子也可以是子表',
  core_points: '广义表定义、存储结构、head/tail操作'
});

// 线性表层级关系
MATCH (p1:KnowledgePoint {neo4j_id: 'KP_LL_001'}),
      (c1:KnowledgePoint {neo4j_id: 'KP_LL_002'})
CREATE (p1)-[:PARENT_OF {order: 1}]->(c1);

MATCH (p2:KnowledgePoint {neo4j_id: 'KP_LL_001'}),
      (c2:KnowledgePoint {neo4j_id: 'KP_LL_004'})
CREATE (p2)-[:PARENT_OF {order: 2}]->(c2);

MATCH (p3:KnowledgePoint {neo4j_id: 'KP_LL_001'}),
      (c3:KnowledgePoint {neo4j_id: 'KP_LL_006'})
CREATE (p3)-[:PARENT_OF {order: 3}]->(c3);

MATCH (p4:KnowledgePoint {neo4j_id: 'KP_LL_001'}),
      (c4:KnowledgePoint {neo4j_id: 'KP_LL_008'})
CREATE (p4)-[:PARENT_OF {order: 4}]->(c4);

MATCH (p5:KnowledgePoint {neo4j_id: 'KP_LL_001'}),
      (c5:KnowledgePoint {neo4j_id: 'KP_LL_009'})
CREATE (p5)-[:PARENT_OF {order: 5}]->(c5);

MATCH (p6:KnowledgePoint {neo4j_id: 'KP_LL_002'}),
      (c6:KnowledgePoint {neo4j_id: 'KP_LL_003'})
CREATE (p6)-[:PARENT_OF {order: 1}]->(c6);

MATCH (p7:KnowledgePoint {neo4j_id: 'KP_LL_004'}),
      (c7:KnowledgePoint {neo4j_id: 'KP_LL_005'})
CREATE (p7)-[:PARENT_OF {order: 1}]->(c7);

MATCH (p8:KnowledgePoint {neo4j_id: 'KP_LL_006'}),
      (c8:KnowledgePoint {neo4j_id: 'KP_LL_007'})
CREATE (p8)-[:PARENT_OF {order: 1}]->(c8);

// 线性表关联关系
MATCH (a:KnowledgePoint {neo4j_id: 'KP_LL_002'}),
      (b:KnowledgePoint {neo4j_id: 'KP_LL_004'})
CREATE (a)-[:RELATED_TO {reason: '同为受限线性表'}]->(b);
