// ============================================================
// 知识图谱数据 - 完整版
// 覆盖：集合、线性表、树、图、查找、排序
// ============================================================

// ===================== 集合模块 =====================
CREATE (kp1:KnowledgePoint {
  neo4j_id: 'KP_SET_001',
  name: '集合',
  code: 'SET',
  module: 'SET',
  level: 1,
  difficulty: 'EASY',
  description: '集合是数学中的基本概念，表示具有某种特定性质的对象的总体',
  core_points: '集合定义、集合表示、集合运算'
});

CREATE (kp2:KnowledgePoint {
  neo4j_id: 'KP_SET_002',
  name: '集合运算',
  code: 'SET_OPERATION',
  module: 'SET',
  level: 2,
  difficulty: 'MEDIUM',
  description: '集合的基本运算包括并集、交集、差集等',
  core_points: '并集、交集、差集、补集'
});

MATCH (parent:KnowledgePoint {neo4j_id: 'KP_SET_001'}),
      (child:KnowledgePoint {neo4j_id: 'KP_SET_002'})
CREATE (parent)-[:PARENT_OF {order: 1}]->(child);

// ===================== 线性表模块 =====================
// 根节点
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

// 栈
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

// 队列
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

// 串
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

// 数组
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

// 广义表
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

// 线性表关系
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

// ===================== 树模块 =====================
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

// ===================== 图模块 =====================
CREATE (kp_graph:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_001',
  name: '图',
  code: 'GRAPH',
  module: 'GRAPH',
  level: 1,
  difficulty: 'MEDIUM',
  description: '图是由顶点集合和边集合组成的非线性数据结构',
  core_points: '图的定义、有向图、无向图、权值'
});

CREATE (kp_graph_store:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_002',
  name: '图的存储结构',
  code: 'GRAPH_STORAGE',
  module: 'GRAPH',
  level: 2,
  difficulty: 'MEDIUM',
  description: '图的存储方式包括邻接矩阵、邻接表、十字链表等',
  core_points: '邻接矩阵、邻接表、存储空间分析'
});

CREATE (kp_dfs:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_003',
  name: '深度优先搜索(DFS)',
  code: 'DFS',
  module: 'GRAPH',
  level: 2,
  difficulty: 'MEDIUM',
  description: 'DFS是从某个顶点出发，沿着一条路径尽可能深入，然后回溯的遍历方式',
  core_points: 'DFS递归实现、DFS非递归、时间复杂度O(V+E)'
});

CREATE (kp_bfs:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_004',
  name: '广度优先搜索(BFS)',
  code: 'BFS',
  module: 'GRAPH',
  level: 2,
  difficulty: 'MEDIUM',
  description: 'BFS是从某个顶点出发，先访问所有相邻顶点，再逐层向外扩展的遍历方式',
  core_points: 'BFS队列实现、最短路径、时间复杂度O(V+E)'
});

CREATE (kp_mst:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_005',
  name: '最小生成树',
  code: 'MST',
  module: 'GRAPH',
  level: 3,
  difficulty: 'HARD',
  description: '最小生成树是连通无向图中包含所有顶点的最小权值生成树',
  core_points: 'Prim算法、Kruskal算法'
});

CREATE (kp_prim:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_006',
  name: 'Prim算法',
  code: 'PRIM',
  module: 'GRAPH',
  level: 3,
  difficulty: 'HARD',
  description: 'Prim算法从一个顶点开始，每次选择权值最小的边扩展生成树',
  core_points: '贪心策略、时间复杂度O(V²)或O(ElogV)'
});

CREATE (kp_kruskal:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_007',
  name: 'Kruskal算法',
  code: 'KRUSKAL',
  module: 'GRAPH',
  level: 3,
  difficulty: 'HARD',
  description: 'Kruskal算法按边权值从小到大选择边，用并查集判断是否成环',
  core_points: '并查集、按权排序、时间复杂度O(ElogE)'
});

CREATE (kp_sp:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_008',
  name: '最短路径',
  code: 'SHORTEST_PATH',
  module: 'GRAPH',
  level: 3,
  difficulty: 'HARD',
  description: '最短路径问题是求图中两个顶点之间权值之和最小的路径',
  core_points: 'Dijkstra算法、Floyd算法'
});

CREATE (kp_dijkstra:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_009',
  name: 'Dijkstra算法',
  code: 'DIJKSTRA',
  module: 'GRAPH',
  level: 3,
  difficulty: 'HARD',
  description: 'Dijkstra算法求单源最短路径，适用于权值非负的图',
  core_points: '贪心策略、时间复杂度O(V²)、不能处理负权边'
});

CREATE (kp_floyd:KnowledgePoint {
  neo4j_id: 'KP_GRAPH_010',
  name: 'Floyd算法',
  code: 'FLOYD',
  module: 'GRAPH',
  level: 3,
  difficulty: 'HARD',
  description: 'Floyd算法求所有顶点对之间的最短路径，基于动态规划',
  core_points: '动态规划、时间复杂度O(V³)、可处理负权边'
});

// 图的关系
MATCH (p:KnowledgePoint {neo4j_id: 'KP_GRAPH_001'}),
      (c:KnowledgePoint {neo4j_id: 'KP_GRAPH_002'})
CREATE (p)-[:PARENT_OF {order: 1}]->(c);

MATCH (p2:KnowledgePoint {neo4j_id: 'KP_GRAPH_001'}),
      (c2:KnowledgePoint {neo4j_id: 'KP_GRAPH_003'})
CREATE (p2)-[:PARENT_OF {order: 2}]->(c2);

MATCH (p3:KnowledgePoint {neo4j_id: 'KP_GRAPH_001'}),
      (c3:KnowledgePoint {neo4j_id: 'KP_GRAPH_004'})
CREATE (p3)-[:PARENT_OF {order: 3}]->(c3);

MATCH (p4:KnowledgePoint {neo4j_id: 'KP_GRAPH_001'}),
      (c4:KnowledgePoint {neo4j_id: 'KP_GRAPH_005'})
CREATE (p4)-[:PARENT_OF {order: 4}]->(c4);

MATCH (p5:KnowledgePoint {neo4j_id: 'KP_GRAPH_001'}),
      (c5:KnowledgePoint {neo4j_id: 'KP_GRAPH_008'})
CREATE (p5)-[:PARENT_OF {order: 5}]->(c5);

MATCH (p6:KnowledgePoint {neo4j_id: 'KP_GRAPH_005'}),
      (c6:KnowledgePoint {neo4j_id: 'KP_GRAPH_006'})
CREATE (p6)-[:PARENT_OF {order: 1}]->(c6);

MATCH (p7:KnowledgePoint {neo4j_id: 'KP_GRAPH_005'}),
      (c7:KnowledgePoint {neo4j_id: 'KP_GRAPH_007'})
CREATE (p7)-[:PARENT_OF {order: 2}]->(c7);

MATCH (p8:KnowledgePoint {neo4j_id: 'KP_GRAPH_008'}),
      (c8:KnowledgePoint {neo4j_id: 'KP_GRAPH_009'})
CREATE (p8)-[:PARENT_OF {order: 1}]->(c8);

MATCH (p9:KnowledgePoint {neo4j_id: 'KP_GRAPH_008'}),
      (c9:KnowledgePoint {neo4j_id: 'KP_GRAPH_010'})
CREATE (p9)-[:PARENT_OF {order: 2}]->(c9);

// 图的关联关系
MATCH (a:KnowledgePoint {neo4j_id: 'KP_GRAPH_003'}),
      (b:KnowledgePoint {neo4j_id: 'KP_GRAPH_004'})
CREATE (a)-[:RELATED_TO {reason: '同为图的遍历方式'}]->(b);

MATCH (a2:KnowledgePoint {neo4j_id: 'KP_GRAPH_006'}),
      (b2:KnowledgePoint {neo4j_id: 'KP_GRAPH_007'})
CREATE (a2)-[:RELATED_TO {reason: '同为最小生成树算法'}]->(b2);

MATCH (a3:KnowledgePoint {neo4j_id: 'KP_GRAPH_009'}),
      (b3:KnowledgePoint {neo4j_id: 'KP_GRAPH_010'})
CREATE (a3)-[:RELATED_TO {reason: '同为最短路径算法'}]->(b3);

// ===================== 查找模块 =====================
CREATE (kp_search:KnowledgePoint {
  neo4j_id: 'KP_SEARCH_001',
  name: '查找',
  code: 'SEARCH',
  module: 'SEARCH',
  level: 1,
  difficulty: 'EASY',
  description: '查找是在数据集合中寻找满足条件的数据元素的过程',
  core_points: '查找定义、平均查找长度ASL、查找分类'
});

CREATE (kp_seq_search:KnowledgePoint {
  neo4j_id: 'KP_SEARCH_002',
  name: '顺序查找',
  code: 'SEQ_SEARCH',
  module: 'SEARCH',
  level: 2,
  difficulty: 'EASY',
  description: '顺序查找从表的一端开始，逐个比较关键字，适用于顺序表和链表',
  core_points: '基本思想、时间复杂度O(n)、哨兵优化'
});

CREATE (kp_bin_search:KnowledgePoint {
  neo4j_id: 'KP_SEARCH_003',
  name: '二分查找',
  code: 'BIN_SEARCH',
  module: 'SEARCH',
  level: 2,
  difficulty: 'MEDIUM',
  description: '二分查找在有序表中通过折半缩小查找范围，效率高',
  core_points: '前提条件有序、时间复杂度O(logn)、判定树'
});

CREATE (kp_hash_search:KnowledgePoint {
  neo4j_id: 'KP_SEARCH_004',
  name: '哈希查找',
  code: 'HASH_SEARCH',
  module: 'SEARCH',
  level: 3,
  difficulty: 'HARD',
  description: '哈希查找通过哈希函数将关键字映射到存储地址，实现O(1)查找',
  core_points: '哈希函数、冲突处理(开放定址/链地址法)、装填因子'
});

CREATE (kp_tree_search:KnowledgePoint {
  neo4j_id: 'KP_SEARCH_005',
  name: '树表查找',
  code: 'TREE_SEARCH',
  module: 'SEARCH',
  level: 2,
  difficulty: 'MEDIUM',
  description: '树表查找利用树形结构组织数据，包括二叉排序树查找、平衡树查找等',
  core_points: 'BST查找、AVL查找、B树查找'
});

// 查找关系
MATCH (p:KnowledgePoint {neo4j_id: 'KP_SEARCH_001'}),
      (c:KnowledgePoint {neo4j_id: 'KP_SEARCH_002'})
CREATE (p)-[:PARENT_OF {order: 1}]->(c);

MATCH (p2:KnowledgePoint {neo4j_id: 'KP_SEARCH_001'}),
      (c2:KnowledgePoint {neo4j_id: 'KP_SEARCH_003'})
CREATE (p2)-[:PARENT_OF {order: 2}]->(c2);

MATCH (p3:KnowledgePoint {neo4j_id: 'KP_SEARCH_001'}),
      (c3:KnowledgePoint {neo4j_id: 'KP_SEARCH_004'})
CREATE (p3)-[:PARENT_OF {order: 3}]->(c3);

MATCH (p4:KnowledgePoint {neo4j_id: 'KP_SEARCH_001'}),
      (c4:KnowledgePoint {neo4j_id: 'KP_SEARCH_005'})
CREATE (p4)-[:PARENT_OF {order: 4}]->(c4);

// 查找与树的关联
MATCH (a:KnowledgePoint {neo4j_id: 'KP_SEARCH_005'}),
      (b:KnowledgePoint {neo4j_id: 'KP_TREE_004'})
CREATE (a)-[:RELATED_TO {reason: '树表查找基于二叉排序树'}]->(b);

MATCH (a2:KnowledgePoint {neo4j_id: 'KP_SEARCH_005'}),
      (b2:KnowledgePoint {neo4j_id: 'KP_TREE_007'})
CREATE (a2)-[:RELATED_TO {reason: 'B树用于数据库索引查找'}]->(b2);

// ===================== 排序模块 =====================
CREATE (kp_sort:KnowledgePoint {
  neo4j_id: 'KP_SORT_001',
  name: '排序',
  code: 'SORTING',
  module: 'SORT',
  level: 1,
  difficulty: 'EASY',
  description: '排序是将数据元素按关键字递增或递减顺序重新排列的过程',
  core_points: '排序定义、稳定性、时间/空间复杂度、适用场景'
});

CREATE (kp_bubble:KnowledgePoint {
  neo4j_id: 'KP_SORT_002',
  name: '冒泡排序',
  code: 'BUBBLE_SORT',
  module: 'SORT',
  level: 2,
  difficulty: 'EASY',
  description: '冒泡排序通过相邻元素比较交换，每轮将最大元素"冒泡"到末尾',
  core_points: '基本原理、时间O(n²)、空间O(1)、稳定排序'
});

CREATE (kp_selection:KnowledgePoint {
  neo4j_id: 'KP_SORT_003',
  name: '选择排序',
  code: 'SELECTION_SORT',
  module: 'SORT',
  level: 2,
  difficulty: 'EASY',
  description: '选择排序每轮从未排序部分选择最小元素放到已排序部分末尾',
  core_points: '基本原理、时间O(n²)、空间O(1)、不稳定'
});

CREATE (kp_insertion:KnowledgePoint {
  neo4j_id: 'KP_SORT_004',
  name: '插入排序',
  code: 'INSERTION_SORT',
  module: 'SORT',
  level: 2,
  difficulty: 'EASY',
  description: '插入排序将元素逐个插入到已排序序列的适当位置',
  core_points: '基本原理、时间O(n²)、空间O(1)、稳定排序、对近似有序数据高效'
});

CREATE (kp_shell:KnowledgePoint {
  neo4j_id: 'KP_SORT_005',
  name: '希尔排序',
  code: 'SHELL_SORT',
  module: 'SORT',
  level: 2,
  difficulty: 'MEDIUM',
  description: '希尔排序是插入排序的改进，通过分组增量缩小进行多轮插入排序',
  core_points: '增量序列、时间复杂度与增量选择有关、不稳定'
});

CREATE (kp_quick:KnowledgePoint {
  neo4j_id: 'KP_SORT_006',
  name: '快速排序',
  code: 'QUICK_SORT',
  module: 'SORT',
  level: 2,
  difficulty: 'MEDIUM',
  description: '快速排序通过分区操作将数组分为两部分，递归排序，是实践中最快的排序算法之一',
  core_points: '分区操作、pivot选择、平均O(nlogn)、最坏O(n²)、不稳定'
});

CREATE (kp_merge:KnowledgePoint {
  neo4j_id: 'KP_SORT_007',
  name: '归并排序',
  code: 'MERGE_SORT',
  module: 'SORT',
  level: 2,
  difficulty: 'MEDIUM',
  description: '归并排序将数组分成两半，分别排序后合并，基于分治思想',
  core_points: '分治思想、合并操作、时间O(nlogn)、空间O(n)、稳定排序'
});

CREATE (kp_heap:KnowledgePoint {
  neo4j_id: 'KP_SORT_008',
  name: '堆排序',
  code: 'HEAP_SORT',
  module: 'SORT',
  level: 3,
  difficulty: 'HARD',
  description: '堆排序利用堆这种数据结构进行排序，先建堆再逐个取出堆顶元素',
  core_points: '大顶堆/小顶堆、建堆、调整堆、时间O(nlogn)、空间O(1)、不稳定'
});

// 排序关系
MATCH (p:KnowledgePoint {neo4j_id: 'KP_SORT_001'}),
      (c:KnowledgePoint {neo4j_id: 'KP_SORT_002'})
CREATE (p)-[:PARENT_OF {order: 1}]->(c);

MATCH (p2:KnowledgePoint {neo4j_id: 'KP_SORT_001'}),
      (c2:KnowledgePoint {neo4j_id: 'KP_SORT_003'})
CREATE (p2)-[:PARENT_OF {order: 2}]->(c2);

MATCH (p3:KnowledgePoint {neo4j_id: 'KP_SORT_001'}),
      (c3:KnowledgePoint {neo4j_id: 'KP_SORT_004'})
CREATE (p3)-[:PARENT_OF {order: 3}]->(c3);

MATCH (p4:KnowledgePoint {neo4j_id: 'KP_SORT_001'}),
      (c4:KnowledgePoint {neo4j_id: 'KP_SORT_005'})
CREATE (p4)-[:PARENT_OF {order: 4}]->(c4);

MATCH (p5:KnowledgePoint {neo4j_id: 'KP_SORT_001'}),
      (c5:KnowledgePoint {neo4j_id: 'KP_SORT_006'})
CREATE (p5)-[:PARENT_OF {order: 5}]->(c5);

MATCH (p6:KnowledgePoint {neo4j_id: 'KP_SORT_001'}),
      (c6:KnowledgePoint {neo4j_id: 'KP_SORT_007'})
CREATE (p6)-[:PARENT_OF {order: 6}]->(c6);

MATCH (p7:KnowledgePoint {neo4j_id: 'KP_SORT_001'}),
      (c7:KnowledgePoint {neo4j_id: 'KP_SORT_008'})
CREATE (p7)-[:PARENT_OF {order: 7}]->(c7);

// 排序关联关系
MATCH (a:KnowledgePoint {neo4j_id: 'KP_SORT_004'}),
      (b:KnowledgePoint {neo4j_id: 'KP_SORT_005'})
CREATE (a)-[:RELATED_TO {reason: '希尔排序是插入排序的改进'}]->(b);

MATCH (a2:KnowledgePoint {neo4j_id: 'KP_SORT_006'}),
      (b2:KnowledgePoint {neo4j_id: 'KP_SORT_007'})
CREATE (a2)-[:RELATED_TO {reason: '同为分治类排序算法'}]->(b2);

// 排序与堆的关联
MATCH (a3:KnowledgePoint {neo4j_id: 'KP_SORT_008'}),
      (b3:KnowledgePoint {neo4j_id: 'KP_TREE_001'})
CREATE (a3)-[:RELATED_TO {reason: '堆排序基于完全二叉树结构'}]->(b3);

// ===================== 跨模块关联 =====================
// 栈与递归
MATCH (a:KnowledgePoint {neo4j_id: 'KP_LL_002'}),
      (b:KnowledgePoint {neo4j_id: 'KP_TREE_003'})
CREATE (a)-[:RELATED_TO {reason: '递归遍历本质是栈的应用'}]->(b);

// BFS与队列
MATCH (a2:KnowledgePoint {neo4j_id: 'KP_GRAPH_004'}),
      (b2:KnowledgePoint {neo4j_id: 'KP_LL_004'})
CREATE (a2)-[:RELATED_TO {reason: 'BFS实现需要队列辅助'}]->(b2);

// 二分查找与有序
MATCH (a3:KnowledgePoint {neo4j_id: 'KP_SEARCH_003'}),
      (b3:KnowledgePoint {neo4j_id: 'KP_SORT_001'})
CREATE (a3)-[:RELATED_TO {reason: '二分查找要求数据有序，排序是前提'}]->(b3);
