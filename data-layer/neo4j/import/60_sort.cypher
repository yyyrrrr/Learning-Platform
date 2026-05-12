// ===================== 排序模块 =====================
// 负责人: XXX
// 日期: 2026-05-08

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
