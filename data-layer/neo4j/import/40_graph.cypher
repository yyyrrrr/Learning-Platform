// ===================== 图模块 =====================
// 负责人: XXX
// 日期: 2026-05-08

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
