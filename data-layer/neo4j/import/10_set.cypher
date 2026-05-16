// Knowledge point data - SET module
// Goal: fewer nodes, richer relationships, closer to the compact graph style.

// Re-importing this file should replace only the SET module.
MATCH (kp:KnowledgePoint {module: 'SET'})
DETACH DELETE kp;

// =========================
// Nodes
// =========================
MERGE (setRoot:KnowledgePoint {neo4j_id: 'KP_SET_001'})
SET setRoot.name = '集合',
    setRoot.code = 'SET',
    setRoot.module = 'SET',
    setRoot.level = 1,
    setRoot.difficulty = 'EASY',
    setRoot.description = '集合是由确定且互异的元素构成的整体，是离散数学和数据结构中的基础抽象。',
    setRoot.core_points = '确定性、互异性、成员关系、集合运算、实现选型';

MERGE (representation:KnowledgePoint {neo4j_id: 'KP_SET_002'})
SET representation.name = '集合表示与成员关系',
    representation.code = 'SET_REPRESENTATION_MEMBERSHIP',
    representation.module = 'SET',
    representation.level = 2,
    representation.difficulty = 'EASY',
    representation.description = '用列举法、描述法和成员判定描述集合及元素是否属于集合。',
    representation.core_points = '列举法、描述法、属于、不属于、contains';

MERGE (properties:KnowledgePoint {neo4j_id: 'KP_SET_003'})
SET properties.name = '集合基本性质',
    properties.code = 'SET_PROPERTIES',
    properties.module = 'SET',
    properties.level = 2,
    properties.difficulty = 'EASY',
    properties.description = '集合强调元素确定、互异且无序，这些性质决定了判等和去重语义。',
    properties.core_points = '确定性、互异性、无序性、集合相等';

MERGE (subset:KnowledgePoint {neo4j_id: 'KP_SET_004'})
SET subset.name = '子集与幂集',
    subset.code = 'SUBSET_AND_POWER_SET',
    subset.module = 'SET',
    subset.level = 2,
    subset.difficulty = 'MEDIUM',
    subset.description = '子集刻画集合间包含关系，幂集由全部子集组成，规模随元素数指数增长。',
    subset.core_points = '子集、真子集、包含关系、幂集、2^n';

MERGE (operations:KnowledgePoint {neo4j_id: 'KP_SET_005'})
SET operations.name = '集合运算',
    operations.code = 'SET_OPERATIONS',
    operations.module = 'SET',
    operations.level = 2,
    operations.difficulty = 'MEDIUM',
    operations.description = '并、交、差、补等运算用于组合、过滤和比较集合。',
    operations.core_points = '并集、交集、差集、补集、运算律';

MERGE (unionNode:KnowledgePoint {neo4j_id: 'KP_SET_006'})
SET unionNode.name = '并集',
    unionNode.code = 'SET_UNION',
    unionNode.module = 'SET',
    unionNode.level = 3,
    unionNode.difficulty = 'EASY',
    unionNode.description = '并集保留两个集合的所有元素，并按集合语义去重。',
    unionNode.core_points = '合并、去重、交换律、结合律';

MERGE (intersection:KnowledgePoint {neo4j_id: 'KP_SET_007'})
SET intersection.name = '交集',
    intersection.code = 'SET_INTERSECTION',
    intersection.module = 'SET',
    intersection.level = 3,
    intersection.difficulty = 'EASY',
    intersection.description = '交集只保留两个集合共同拥有的元素。',
    intersection.core_points = '共同元素、过滤、匹配';

MERGE (difference:KnowledgePoint {neo4j_id: 'KP_SET_008'})
SET difference.name = '差集与补集',
    difference.code = 'SET_DIFFERENCE_COMPLEMENT',
    difference.module = 'SET',
    difference.level = 3,
    difference.difficulty = 'MEDIUM',
    difference.description = '差集保留只属于一个集合的元素，补集依赖全集定义。',
    difference.core_points = '方向性、全集、互补关系、德摩根律';

MERGE (storage:KnowledgePoint {neo4j_id: 'KP_SET_009'})
SET storage.name = '集合存储实现',
    storage.code = 'SET_STORAGE',
    storage.module = 'SET',
    storage.level = 2,
    storage.difficulty = 'MEDIUM',
    storage.description = '集合可用顺序表、链表、哈希表或位图实现，不同实现决定操作复杂度。',
    storage.core_points = '顺序存储、链式存储、哈希表、位图、复杂度';

MERGE (hashSet:KnowledgePoint {neo4j_id: 'KP_SET_010'})
SET hashSet.name = '哈希集合',
    hashSet.code = 'HASH_SET',
    hashSet.module = 'SET',
    hashSet.level = 3,
    hashSet.difficulty = 'MEDIUM',
    hashSet.description = '哈希集合通过哈希表实现快速成员判定，平均复杂度接近 O(1)。',
    hashSet.core_points = '哈希函数、冲突处理、负载因子、O(1) 查询';

MERGE (bitmapSet:KnowledgePoint {neo4j_id: 'KP_SET_011'})
SET bitmapSet.name = '位图集合',
    bitmapSet.code = 'BITMAP_SET',
    bitmapSet.module = 'SET',
    bitmapSet.level = 3,
    bitmapSet.difficulty = 'MEDIUM',
    bitmapSet.description = '位图集合适用于有限整数值域，利用位运算高效表示和计算集合。',
    bitmapSet.core_points = '值域限制、位运算、空间压缩、快速集合运算';

MERGE (application:KnowledgePoint {neo4j_id: 'KP_SET_012'})
SET application.name = '应用与复杂度选型',
    application.code = 'SET_APPLICATION_COMPLEXITY',
    application.module = 'SET',
    application.level = 2,
    application.difficulty = 'MEDIUM',
    application.description = '根据数据规模、值域范围和操作频率选择合适集合实现。',
    application.core_points = '数据去重、查询过滤、权限合并、复杂度分析、实现选型';

// =========================
// Hierarchical relationships
// =========================
MATCH (root:KnowledgePoint {neo4j_id: 'KP_SET_001'}),
      (representation:KnowledgePoint {neo4j_id: 'KP_SET_002'}),
      (properties:KnowledgePoint {neo4j_id: 'KP_SET_003'}),
      (subset:KnowledgePoint {neo4j_id: 'KP_SET_004'}),
      (operations:KnowledgePoint {neo4j_id: 'KP_SET_005'}),
      (storage:KnowledgePoint {neo4j_id: 'KP_SET_009'}),
      (application:KnowledgePoint {neo4j_id: 'KP_SET_012'})
CREATE (root)-[:PARENT_OF {order: 1}]->(representation),
       (root)-[:PARENT_OF {order: 2}]->(properties),
       (root)-[:PARENT_OF {order: 3}]->(subset),
       (root)-[:PARENT_OF {order: 4}]->(operations),
       (root)-[:PARENT_OF {order: 5}]->(storage),
       (root)-[:PARENT_OF {order: 6}]->(application);

MATCH (operations:KnowledgePoint {neo4j_id: 'KP_SET_005'}),
      (unionNode:KnowledgePoint {neo4j_id: 'KP_SET_006'}),
      (intersection:KnowledgePoint {neo4j_id: 'KP_SET_007'}),
      (difference:KnowledgePoint {neo4j_id: 'KP_SET_008'})
CREATE (operations)-[:PARENT_OF {order: 1}]->(unionNode),
       (operations)-[:PARENT_OF {order: 2}]->(intersection),
       (operations)-[:PARENT_OF {order: 3}]->(difference);

MATCH (storage:KnowledgePoint {neo4j_id: 'KP_SET_009'}),
      (hashSet:KnowledgePoint {neo4j_id: 'KP_SET_010'}),
      (bitmapSet:KnowledgePoint {neo4j_id: 'KP_SET_011'})
CREATE (storage)-[:PARENT_OF {order: 1}]->(hashSet),
       (storage)-[:PARENT_OF {order: 2}]->(bitmapSet);

// =========================
// Rich semantic relationships
// =========================
MATCH (representation:KnowledgePoint {neo4j_id: 'KP_SET_002'}),
      (properties:KnowledgePoint {neo4j_id: 'KP_SET_003'})
CREATE (representation)-[:RELATED_TO {reason: '成员判定依赖确定性，集合判等也依赖成员关系'}]->(properties);

MATCH (properties:KnowledgePoint {neo4j_id: 'KP_SET_003'}),
      (subset:KnowledgePoint {neo4j_id: 'KP_SET_004'})
CREATE (properties)-[:RELATED_TO {reason: '子集判断本质是逐个成员的确定性判定'}]->(subset);

MATCH (subset:KnowledgePoint {neo4j_id: 'KP_SET_004'}),
      (operations:KnowledgePoint {neo4j_id: 'KP_SET_005'})
CREATE (subset)-[:RELATED_TO {reason: '幂集生成和子集枚举常由集合运算组合得到'}]->(operations);

MATCH (unionNode:KnowledgePoint {neo4j_id: 'KP_SET_006'}),
      (intersection:KnowledgePoint {neo4j_id: 'KP_SET_007'}),
      (difference:KnowledgePoint {neo4j_id: 'KP_SET_008'})
CREATE (unionNode)-[:RELATED_TO {reason: '并集与交集都满足交换律和结合律'}]->(intersection),
       (intersection)-[:RELATED_TO {reason: '交集可视为保留，差集可视为过滤删除'}]->(difference),
       (difference)-[:RELATED_TO {reason: '差集与补集都强调元素排除关系'}]->(unionNode);

MATCH (operations:KnowledgePoint {neo4j_id: 'KP_SET_005'}),
      (storage:KnowledgePoint {neo4j_id: 'KP_SET_009'})
CREATE (operations)-[:RELATED_TO {reason: '同一集合运算在不同存储结构下复杂度差异明显'}]->(storage);

MATCH (hashSet:KnowledgePoint {neo4j_id: 'KP_SET_010'}),
      (bitmapSet:KnowledgePoint {neo4j_id: 'KP_SET_011'})
CREATE (hashSet)-[:RELATED_TO {reason: '二者都优化成员查询，但值域假设和空间模型不同'}]->(bitmapSet);

MATCH (representation:KnowledgePoint {neo4j_id: 'KP_SET_002'}),
      (hashSet:KnowledgePoint {neo4j_id: 'KP_SET_010'})
CREATE (representation)-[:RELATED_TO {reason: 'contains 语义在哈希集合中对应快速查找'}]->(hashSet);

MATCH (unionNode:KnowledgePoint {neo4j_id: 'KP_SET_006'}),
      (bitmapSet:KnowledgePoint {neo4j_id: 'KP_SET_011'})
CREATE (unionNode)-[:RELATED_TO {reason: '位图可以用按位或高效实现并集'}]->(bitmapSet);

MATCH (intersection:KnowledgePoint {neo4j_id: 'KP_SET_007'}),
      (bitmapSet:KnowledgePoint {neo4j_id: 'KP_SET_011'})
CREATE (intersection)-[:RELATED_TO {reason: '位图可以用按位与高效实现交集'}]->(bitmapSet);

MATCH (difference:KnowledgePoint {neo4j_id: 'KP_SET_008'}),
      (bitmapSet:KnowledgePoint {neo4j_id: 'KP_SET_011'})
CREATE (difference)-[:RELATED_TO {reason: '位图可以用按位取反和按位与实现差集或补集'}]->(bitmapSet);

MATCH (application:KnowledgePoint {neo4j_id: 'KP_SET_012'}),
      (operations:KnowledgePoint {neo4j_id: 'KP_SET_005'}),
      (storage:KnowledgePoint {neo4j_id: 'KP_SET_009'})
CREATE (application)-[:RELATED_TO {reason: '业务场景决定需要优先优化哪些集合运算'}]->(operations),
       (application)-[:RELATED_TO {reason: '复杂度和内存约束共同决定存储实现选型'}]->(storage);
