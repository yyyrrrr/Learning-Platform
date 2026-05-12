// ===================== 树模块 =====================
// 负责人: XXX
// 日期: 2026-05-08

CREATE (kp_tree:KnowledgePoint {
  neo4j_id: 'KP_TREE_001',
  name: '树',
  code: 'TREE',
  module: 'TREE',
  level: 1,
  difficulty: 'EASY',
  description: '树是一种非线性数据结构，由n个有限节点组成，具有层次关系',
  core_points: '树的定义、基本术语、树的存储'
});

CREATE (kp_btree:KnowledgePoint {
  neo4j_id: 'KP_TREE_002',
  name: '二叉树',
  code: 'BINARY_TREE',
  module: 'TREE',
  level: 2,
  difficulty: 'MEDIUM',
  description: '二叉树是每个节点最多有两个子树的树结构，是最重要的树形结构',
  core_points: '二叉树定义、性质、存储、遍历'
});

CREATE (kp_btree_traverse:KnowledgePoint {
  neo4j_id: 'KP_TREE_003',
  name: '二叉树遍历',
  code: 'BT_TRAVERSE',
  module: 'TREE',
  level: 3,
  difficulty: 'MEDIUM',
  description: '二叉树遍历包括前序、中序、后序遍历和层序遍历',
  core_points: '前序遍历、中序遍历、后序遍历、层序遍历'
});

CREATE (kp_bst:KnowledgePoint {
  neo4j_id: 'KP_TREE_004',
  name: '二叉排序树',
  code: 'BST',
  module: 'TREE',
  level: 2,
  difficulty: 'MEDIUM',
  description: '二叉排序树是一棵满足左小右大性质的二叉树，支持高效查找',
  core_points: 'BST定义、插入、删除、查找'
});

CREATE (kp_avl:KnowledgePoint {
  neo4j_id: 'KP_TREE_005',
  name: 'AVL树',
  code: 'AVL_TREE',
  module: 'TREE',
  level: 3,
  difficulty: 'HARD',
  description: 'AVL树是自平衡二叉搜索树，任意节点的左右子树高度差不超过1',
  core_points: '平衡条件、旋转操作(LL/RR/LR/RL)、插入删除'
});

CREATE (kp_rbtree:KnowledgePoint {
  neo4j_id: 'KP_TREE_006',
  name: '红黑树',
  code: 'RED_BLACK_TREE',
  module: 'TREE',
  level: 3,
  difficulty: 'HARD',
  description: '红黑树是一种自平衡二叉搜索树，通过颜色标记和旋转维持平衡',
  core_points: '红黑树性质、插入调整、删除调整'
});

CREATE (kp_btree_db:KnowledgePoint {
  neo4j_id: 'KP_TREE_007',
  name: 'B树',
  code: 'B_TREE',
  module: 'TREE',
  level: 3,
  difficulty: 'HARD',
  description: 'B树是一种自平衡多路搜索树，常用于数据库和文件系统的索引',
  core_points: 'B树定义、m阶B树性质、插入分裂、删除合并'
});

CREATE (kp_bplus:KnowledgePoint {
  neo4j_id: 'KP_TREE_008',
  name: 'B+树',
  code: 'BPLUS_TREE',
  module: 'TREE',
  level: 3,
  difficulty: 'HARD',
  description: 'B+树是B树的变体，所有数据存储在叶子节点，叶子节点通过链表相连',
  core_points: 'B+树定义、与B树区别、范围查询优势'
});

// 树的关系
MATCH (p:KnowledgePoint {neo4j_id: 'KP_TREE_001'}),
      (c:KnowledgePoint {neo4j_id: 'KP_TREE_002'})
CREATE (p)-[:PARENT_OF {order: 1}]->(c);

MATCH (p2:KnowledgePoint {neo4j_id: 'KP_TREE_002'}),
      (c2:KnowledgePoint {neo4j_id: 'KP_TREE_003'})
CREATE (p2)-[:PARENT_OF {order: 1}]->(c2);

MATCH (p3:KnowledgePoint {neo4j_id: 'KP_TREE_002'}),
      (c3:KnowledgePoint {neo4j_id: 'KP_TREE_004'})
CREATE (p3)-[:PARENT_OF {order: 2}]->(c3);

MATCH (p4:KnowledgePoint {neo4j_id: 'KP_TREE_004'}),
      (c4:KnowledgePoint {neo4j_id: 'KP_TREE_005'})
CREATE (p4)-[:PARENT_OF {order: 1}]->(c4);

MATCH (p5:KnowledgePoint {neo4j_id: 'KP_TREE_004'}),
      (c5:KnowledgePoint {neo4j_id: 'KP_TREE_006'})
CREATE (p5)-[:PARENT_OF {order: 2}]->(c5);

MATCH (p6:KnowledgePoint {neo4j_id: 'KP_TREE_001'}),
      (c6:KnowledgePoint {neo4j_id: 'KP_TREE_007'})
CREATE (p6)-[:PARENT_OF {order: 2}]->(c6);

MATCH (p7:KnowledgePoint {neo4j_id: 'KP_TREE_007'}),
      (c7:KnowledgePoint {neo4j_id: 'KP_TREE_008'})
CREATE (p7)-[:PARENT_OF {order: 1}]->(c7);

// 树的关联关系
MATCH (a:KnowledgePoint {neo4j_id: 'KP_TREE_005'}),
      (b:KnowledgePoint {neo4j_id: 'KP_TREE_006'})
CREATE (a)-[:RELATED_TO {reason: '同为自平衡二叉搜索树'}]->(b);

MATCH (a2:KnowledgePoint {neo4j_id: 'KP_TREE_007'}),
      (b2:KnowledgePoint {neo4j_id: 'KP_TREE_008'})
CREATE (a2)-[:RELATED_TO {reason: '同为多路搜索树'}]->(b2);
