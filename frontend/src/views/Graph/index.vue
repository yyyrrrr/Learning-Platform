<template>
  <div class="graph-page">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索知识点..."
        :prefix-icon="Search"
        clearable
        class="search-input"
        @input="handleSearch"
      />
      <el-select
        v-model="filterModule"
        placeholder="全部模块"
        clearable
        class="module-filter"
      >
        <el-option
          v-for="item in moduleOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button @click="resetView" :icon="RefreshRight">重置</el-button>
      <div class="stats">
        <el-tag size="small" type="info">{{ filteredNodes.length }} 个知识点</el-tag>
        <el-tag size="small" type="info">{{ filteredEdges.length }} 条关系</el-tag>
      </div>
    </div>

    <!-- 主体区域 -->
    <div class="main-content">
      <!-- 图谱画布 -->
      <div class="canvas-area">
        <GraphCanvas
          ref="canvasRef"
          :nodes="filteredNodes"
          :edges="filteredEdges"
          :highlight-ids="highlightIds"
          :selected-id="selectedNodeId"
          @node-click="handleNodeClick"
        />
      </div>

      <!-- 右侧详情面板 -->
      <transition name="slide">
        <div class="detail-panel" v-if="showDetail">
          <NodeDetail
            :node="nodeDetail"
            @close="closeDetail"
            @node-click="handleNodeClick"
          />
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Search, RefreshRight } from '@element-plus/icons-vue'
import { getGraphData, getNodeSummary, searchNodes } from '@/api/modules/graph.js'
import GraphCanvas from '@/components/KnowledgeGraph/GraphCanvas.vue'
import NodeDetail from '@/components/KnowledgeGraph/NodeDetail.vue'

const searchKeyword = ref('')
const filterModule = ref('')
const selectedNodeId = ref('')
const nodeDetail = ref(null)
const showDetail = ref(false)
const canvasRef = ref(null)

const allNodes = ref([])
const allEdges = ref([])
const searchedIds = ref([])
const useApi = ref(true)

const moduleOptions = [
  { value: 'SET', label: '集合' },
  { value: 'LINEAR_LIST', label: '线性表' },
  { value: 'TREE', label: '树' },
  { value: 'GRAPH', label: '图' },
  { value: 'SEARCH', label: '查找' },
  { value: 'SORT', label: '排序' },
]

const filteredNodes = computed(() => {
  let nodes = allNodes.value
  if (filterModule.value) {
    nodes = nodes.filter((n) => n.module === filterModule.value)
  }
  return nodes
})

const filteredEdges = computed(() => {
  const nodeIds = new Set(filteredNodes.value.map((n) => n.id))
  return allEdges.value.filter(
    (e) => nodeIds.has(e.source) && nodeIds.has(e.target),
  )
})

const highlightIds = computed(() => {
  if (searchedIds.value.length > 0) return searchedIds.value
  if (selectedNodeId.value) return [selectedNodeId.value]
  return []
})

async function loadData() {
  try {
    const data = await getGraphData()
    allNodes.value = data.nodes || []
    allEdges.value = data.edges || []
    useApi.value = true
  } catch {
    // API 不可用时使用内置模拟数据
    useApi.value = false
    loadMockData()
  }
}

function loadMockData() {
  allNodes.value = MOCK_NODES
  allEdges.value = MOCK_EDGES
}

let searchTimer = null
function handleSearch(val) {
  clearTimeout(searchTimer)
  if (!val.trim()) {
    searchedIds.value = []
    return
  }
  searchTimer = setTimeout(async () => {
    if (useApi.value) {
      try {
        const results = await searchNodes(val)
        searchedIds.value = results.map((n) => n.id)
        return
      } catch {
        // fallback to local search
      }
    }
    // 本地搜索
    const keyword = val.toLowerCase()
    searchedIds.value = allNodes.value
      .filter(
        (n) =>
          n.name.toLowerCase().includes(keyword) ||
          n.code.toLowerCase().includes(keyword) ||
          (n.description && n.description.toLowerCase().includes(keyword)),
      )
      .map((n) => n.id)
  }, 300)
}

async function handleNodeClick(nodeId) {
  selectedNodeId.value = nodeId
  showDetail.value = true

  if (useApi.value) {
    try {
      nodeDetail.value = await getNodeSummary(nodeId)
      return
    } catch {
      // fallback
    }
  }
  // 本地查找
  const node = allNodes.value.find((n) => n.id === nodeId)
  if (node) {
    const relatedNodes = allEdges.value
      .filter((e) => e.source === nodeId || e.target === nodeId)
      .map((e) => {
        const relId = e.source === nodeId ? e.target : e.source
        const relNode = allNodes.value.find((n) => n.id === relId)
        return relNode
          ? { id: relNode.id, name: relNode.name, relationType: e.type }
          : null
      })
      .filter(Boolean)
    nodeDetail.value = { ...node, relatedNodes }
  }
}

function closeDetail() {
  showDetail.value = false
  selectedNodeId.value = ''
  nodeDetail.value = null
}

function resetView() {
  searchKeyword.value = ''
  filterModule.value = ''
  searchedIds.value = []
  closeDetail()
}

// ============== 内置模拟数据 ==============
const MOCK_NODES = [
  // 集合
  { id: 'KP_SET_001', name: '集合', code: 'SET', module: 'SET', level: 1, difficulty: 'EASY', description: '集合是数学中的基本概念，表示具有某种特定性质的对象的总体', corePoints: '集合定义、集合表示、集合运算' },
  { id: 'KP_SET_002', name: '集合运算', code: 'SET_OPERATION', module: 'SET', level: 2, difficulty: 'MEDIUM', description: '集合的基本运算包括并集、交集、差集等', corePoints: '并集、交集、差集、补集' },
  // 线性表
  { id: 'KP_LL_001', name: '线性表', code: 'LINEAR_LIST', module: 'LINEAR_LIST', level: 1, difficulty: 'EASY', description: '线性表是由n个数据元素组成的有限序列', corePoints: '线性表定义、顺序存储、链式存储' },
  { id: 'KP_LL_002', name: '栈', code: 'STACK', module: 'LINEAR_LIST', level: 2, difficulty: 'EASY', description: '栈是一种后进先出(LIFO)的线性表', corePoints: '栈的定义、顺序栈、链栈、栈的应用' },
  { id: 'KP_LL_003', name: '栈的基本操作', code: 'STACK_OPS', module: 'LINEAR_LIST', level: 3, difficulty: 'EASY', description: '栈的基本操作包括入栈(push)、出栈(pop)等', corePoints: 'push、pop、top、isEmpty' },
  { id: 'KP_LL_004', name: '队列', code: 'QUEUE', module: 'LINEAR_LIST', level: 2, difficulty: 'EASY', description: '队列是一种先进先出(FIFO)的线性表', corePoints: '队列定义、顺序队列、链队列、循环队列' },
  { id: 'KP_LL_005', name: '队列的基本操作', code: 'QUEUE_OPS', module: 'LINEAR_LIST', level: 3, difficulty: 'EASY', description: '队列的基本操作包括入队、出队等', corePoints: 'enqueue、dequeue、front、isEmpty' },
  { id: 'KP_LL_006', name: '串', code: 'STRING', module: 'LINEAR_LIST', level: 2, difficulty: 'MEDIUM', description: '串是由零个或多个字符组成的有限序列', corePoints: '串的定义、串的存储、串的模式匹配' },
  { id: 'KP_LL_007', name: 'KMP算法', code: 'KMP', module: 'LINEAR_LIST', level: 3, difficulty: 'HARD', description: 'KMP算法是一种高效的字符串模式匹配算法', corePoints: 'next数组、模式匹配、时间复杂度O(n+m)' },
  { id: 'KP_LL_008', name: '数组', code: 'ARRAY', module: 'LINEAR_LIST', level: 2, difficulty: 'EASY', description: '数组是由相同类型的数据元素构成的有限序列', corePoints: '数组定义、顺序存储、多维数组、稀疏矩阵' },
  { id: 'KP_LL_009', name: '广义表', code: 'GENERALIZED_LIST', module: 'LINEAR_LIST', level: 2, difficulty: 'MEDIUM', description: '广义表是线性表的推广', corePoints: '广义表定义、存储结构、head/tail操作' },
  // 树
  { id: 'KP_TREE_001', name: '树', code: 'TREE', module: 'TREE', level: 1, difficulty: 'EASY', description: '树是一种非线性数据结构，具有层次关系', corePoints: '树的定义、基本术语、树的存储' },
  { id: 'KP_TREE_002', name: '二叉树', code: 'BINARY_TREE', module: 'TREE', level: 2, difficulty: 'MEDIUM', description: '二叉树是每个节点最多有两个子树的树结构', corePoints: '二叉树定义、性质、存储、遍历' },
  { id: 'KP_TREE_003', name: '二叉树遍历', code: 'BT_TRAVERSE', module: 'TREE', level: 3, difficulty: 'MEDIUM', description: '二叉树遍历包括前序、中序、后序和层序遍历', corePoints: '前序遍历、中序遍历、后序遍历、层序遍历' },
  { id: 'KP_TREE_004', name: '二叉排序树', code: 'BST', module: 'TREE', level: 2, difficulty: 'MEDIUM', description: '二叉排序树满足左小右大性质', corePoints: 'BST定义、插入、删除、查找' },
  { id: 'KP_TREE_005', name: 'AVL树', code: 'AVL_TREE', module: 'TREE', level: 3, difficulty: 'HARD', description: 'AVL树是自平衡二叉搜索树', corePoints: '平衡条件、旋转操作(LL/RR/LR/RL)' },
  { id: 'KP_TREE_006', name: '红黑树', code: 'RED_BLACK_TREE', module: 'TREE', level: 3, difficulty: 'HARD', description: '红黑树通过颜色标记和旋转维持平衡', corePoints: '红黑树性质、插入调整、删除调整' },
  { id: 'KP_TREE_007', name: 'B树', code: 'B_TREE', module: 'TREE', level: 3, difficulty: 'HARD', description: 'B树是一种自平衡多路搜索树', corePoints: 'B树定义、插入分裂、删除合并' },
  { id: 'KP_TREE_008', name: 'B+树', code: 'BPLUS_TREE', module: 'TREE', level: 3, difficulty: 'HARD', description: 'B+树是B树的变体，数据存储在叶子节点', corePoints: 'B+树定义、与B树区别、范围查询优势' },
  // 图
  { id: 'KP_GRAPH_001', name: '图', code: 'GRAPH', module: 'GRAPH', level: 1, difficulty: 'MEDIUM', description: '图是由顶点集合和边集合组成的非线性数据结构', corePoints: '图的定义、有向图、无向图、权值' },
  { id: 'KP_GRAPH_002', name: '图的存储结构', code: 'GRAPH_STORAGE', module: 'GRAPH', level: 2, difficulty: 'MEDIUM', description: '图的存储包括邻接矩阵、邻接表等', corePoints: '邻接矩阵、邻接表、存储空间分析' },
  { id: 'KP_GRAPH_003', name: '深度优先搜索(DFS)', code: 'DFS', module: 'GRAPH', level: 2, difficulty: 'MEDIUM', description: 'DFS沿着路径尽可能深入后回溯', corePoints: 'DFS递归实现、时间复杂度O(V+E)' },
  { id: 'KP_GRAPH_004', name: '广度优先搜索(BFS)', code: 'BFS', module: 'GRAPH', level: 2, difficulty: 'MEDIUM', description: 'BFS先访问所有相邻顶点再逐层扩展', corePoints: 'BFS队列实现、最短路径' },
  { id: 'KP_GRAPH_005', name: '最小生成树', code: 'MST', module: 'GRAPH', level: 3, difficulty: 'HARD', description: '最小生成树是连通无向图的最小权值生成树', corePoints: 'Prim算法、Kruskal算法' },
  { id: 'KP_GRAPH_006', name: 'Prim算法', code: 'PRIM', module: 'GRAPH', level: 3, difficulty: 'HARD', description: 'Prim算法从顶点开始每次选最小权值边扩展', corePoints: '贪心策略、时间复杂度O(V²)' },
  { id: 'KP_GRAPH_007', name: 'Kruskal算法', code: 'KRUSKAL', module: 'GRAPH', level: 3, difficulty: 'HARD', description: 'Kruskal算法按边权值从小到大选择', corePoints: '并查集、按权排序' },
  { id: 'KP_GRAPH_008', name: '最短路径', code: 'SHORTEST_PATH', module: 'GRAPH', level: 3, difficulty: 'HARD', description: '求两个顶点间权值之和最小的路径', corePoints: 'Dijkstra算法、Floyd算法' },
  { id: 'KP_GRAPH_009', name: 'Dijkstra算法', code: 'DIJKSTRA', module: 'GRAPH', level: 3, difficulty: 'HARD', description: 'Dijkstra算法求单源最短路径', corePoints: '贪心策略、不能处理负权边' },
  { id: 'KP_GRAPH_010', name: 'Floyd算法', code: 'FLOYD', module: 'GRAPH', level: 3, difficulty: 'HARD', description: 'Floyd算法求所有顶点对之间的最短路径', corePoints: '动态规划、时间复杂度O(V³)' },
  // 查找
  { id: 'KP_SEARCH_001', name: '查找', code: 'SEARCH', module: 'SEARCH', level: 1, difficulty: 'EASY', description: '查找是在数据集合中寻找满足条件的数据元素', corePoints: '查找定义、平均查找长度ASL' },
  { id: 'KP_SEARCH_002', name: '顺序查找', code: 'SEQ_SEARCH', module: 'SEARCH', level: 2, difficulty: 'EASY', description: '顺序查找从表的一端逐个比较', corePoints: '基本思想、时间复杂度O(n)' },
  { id: 'KP_SEARCH_003', name: '二分查找', code: 'BIN_SEARCH', module: 'SEARCH', level: 2, difficulty: 'MEDIUM', description: '二分查找在有序表中折半缩小查找范围', corePoints: '前提条件有序、时间复杂度O(logn)' },
  { id: 'KP_SEARCH_004', name: '哈希查找', code: 'HASH_SEARCH', module: 'SEARCH', level: 3, difficulty: 'HARD', description: '哈希查找通过哈希函数映射存储地址', corePoints: '哈希函数、冲突处理、装填因子' },
  { id: 'KP_SEARCH_005', name: '树表查找', code: 'TREE_SEARCH', module: 'SEARCH', level: 2, difficulty: 'MEDIUM', description: '利用树形结构组织数据进行查找', corePoints: 'BST查找、AVL查找、B树查找' },
  // 排序
  { id: 'KP_SORT_001', name: '排序', code: 'SORTING', module: 'SORT', level: 1, difficulty: 'EASY', description: '排序是将数据元素按关键字重新排列的过程', corePoints: '排序定义、稳定性、时间/空间复杂度' },
  { id: 'KP_SORT_002', name: '冒泡排序', code: 'BUBBLE_SORT', module: 'SORT', level: 2, difficulty: 'EASY', description: '相邻元素比较交换，每轮将最大元素冒泡到末尾', corePoints: '时间O(n²)、稳定排序' },
  { id: 'KP_SORT_003', name: '选择排序', code: 'SELECTION_SORT', module: 'SORT', level: 2, difficulty: 'EASY', description: '每轮选最小元素放到已排序部分末尾', corePoints: '时间O(n²)、不稳定' },
  { id: 'KP_SORT_004', name: '插入排序', code: 'INSERTION_SORT', module: 'SORT', level: 2, difficulty: 'EASY', description: '将元素插入到已排序序列的适当位置', corePoints: '时间O(n²)、稳定排序' },
  { id: 'KP_SORT_005', name: '希尔排序', code: 'SHELL_SORT', module: 'SORT', level: 2, difficulty: 'MEDIUM', description: '通过分组增量进行多轮插入排序', corePoints: '增量序列、时间复杂度与增量选择有关' },
  { id: 'KP_SORT_006', name: '快速排序', code: 'QUICK_SORT', module: 'SORT', level: 2, difficulty: 'MEDIUM', description: '通过分区操作递归排序，实践中最快', corePoints: '分区操作、平均O(nlogn)、不稳定' },
  { id: 'KP_SORT_007', name: '归并排序', code: 'MERGE_SORT', module: 'SORT', level: 2, difficulty: 'MEDIUM', description: '分治思想，分成两半排序后合并', corePoints: '合并操作、时间O(nlogn)、空间O(n)、稳定' },
  { id: 'KP_SORT_008', name: '堆排序', code: 'HEAP_SORT', module: 'SORT', level: 3, difficulty: 'HARD', description: '利用堆数据结构进行排序', corePoints: '大顶堆/小顶堆、建堆、时间O(nlogn)' },
]

const MOCK_EDGES = [
  // 集合
  { source: 'KP_SET_001', target: 'KP_SET_002', type: 'PARENT_OF', order: 1 },
  // 线性表
  { source: 'KP_LL_001', target: 'KP_LL_002', type: 'PARENT_OF', order: 1 },
  { source: 'KP_LL_001', target: 'KP_LL_004', type: 'PARENT_OF', order: 2 },
  { source: 'KP_LL_001', target: 'KP_LL_006', type: 'PARENT_OF', order: 3 },
  { source: 'KP_LL_001', target: 'KP_LL_008', type: 'PARENT_OF', order: 4 },
  { source: 'KP_LL_001', target: 'KP_LL_009', type: 'PARENT_OF', order: 5 },
  { source: 'KP_LL_002', target: 'KP_LL_003', type: 'PARENT_OF', order: 1 },
  { source: 'KP_LL_004', target: 'KP_LL_005', type: 'PARENT_OF', order: 1 },
  { source: 'KP_LL_006', target: 'KP_LL_007', type: 'PARENT_OF', order: 1 },
  { source: 'KP_LL_002', target: 'KP_LL_004', type: 'RELATED_TO', reason: '同为受限线性表' },
  // 树
  { source: 'KP_TREE_001', target: 'KP_TREE_002', type: 'PARENT_OF', order: 1 },
  { source: 'KP_TREE_002', target: 'KP_TREE_003', type: 'PARENT_OF', order: 1 },
  { source: 'KP_TREE_002', target: 'KP_TREE_004', type: 'PARENT_OF', order: 2 },
  { source: 'KP_TREE_004', target: 'KP_TREE_005', type: 'PARENT_OF', order: 1 },
  { source: 'KP_TREE_004', target: 'KP_TREE_006', type: 'PARENT_OF', order: 2 },
  { source: 'KP_TREE_001', target: 'KP_TREE_007', type: 'PARENT_OF', order: 2 },
  { source: 'KP_TREE_007', target: 'KP_TREE_008', type: 'PARENT_OF', order: 1 },
  { source: 'KP_TREE_005', target: 'KP_TREE_006', type: 'RELATED_TO', reason: '同为自平衡二叉搜索树' },
  { source: 'KP_TREE_007', target: 'KP_TREE_008', type: 'RELATED_TO', reason: '同为多路搜索树' },
  // 图
  { source: 'KP_GRAPH_001', target: 'KP_GRAPH_002', type: 'PARENT_OF', order: 1 },
  { source: 'KP_GRAPH_001', target: 'KP_GRAPH_003', type: 'PARENT_OF', order: 2 },
  { source: 'KP_GRAPH_001', target: 'KP_GRAPH_004', type: 'PARENT_OF', order: 3 },
  { source: 'KP_GRAPH_001', target: 'KP_GRAPH_005', type: 'PARENT_OF', order: 4 },
  { source: 'KP_GRAPH_001', target: 'KP_GRAPH_008', type: 'PARENT_OF', order: 5 },
  { source: 'KP_GRAPH_005', target: 'KP_GRAPH_006', type: 'PARENT_OF', order: 1 },
  { source: 'KP_GRAPH_005', target: 'KP_GRAPH_007', type: 'PARENT_OF', order: 2 },
  { source: 'KP_GRAPH_008', target: 'KP_GRAPH_009', type: 'PARENT_OF', order: 1 },
  { source: 'KP_GRAPH_008', target: 'KP_GRAPH_010', type: 'PARENT_OF', order: 2 },
  { source: 'KP_GRAPH_003', target: 'KP_GRAPH_004', type: 'RELATED_TO', reason: '同为图的遍历方式' },
  { source: 'KP_GRAPH_006', target: 'KP_GRAPH_007', type: 'RELATED_TO', reason: '同为最小生成树算法' },
  { source: 'KP_GRAPH_009', target: 'KP_GRAPH_010', type: 'RELATED_TO', reason: '同为最短路径算法' },
  // 查找
  { source: 'KP_SEARCH_001', target: 'KP_SEARCH_002', type: 'PARENT_OF', order: 1 },
  { source: 'KP_SEARCH_001', target: 'KP_SEARCH_003', type: 'PARENT_OF', order: 2 },
  { source: 'KP_SEARCH_001', target: 'KP_SEARCH_004', type: 'PARENT_OF', order: 3 },
  { source: 'KP_SEARCH_001', target: 'KP_SEARCH_005', type: 'PARENT_OF', order: 4 },
  { source: 'KP_SEARCH_005', target: 'KP_TREE_004', type: 'RELATED_TO', reason: '树表查找基于BST' },
  { source: 'KP_SEARCH_005', target: 'KP_TREE_007', type: 'RELATED_TO', reason: 'B树用于数据库索引' },
  // 排序
  { source: 'KP_SORT_001', target: 'KP_SORT_002', type: 'PARENT_OF', order: 1 },
  { source: 'KP_SORT_001', target: 'KP_SORT_003', type: 'PARENT_OF', order: 2 },
  { source: 'KP_SORT_001', target: 'KP_SORT_004', type: 'PARENT_OF', order: 3 },
  { source: 'KP_SORT_001', target: 'KP_SORT_005', type: 'PARENT_OF', order: 4 },
  { source: 'KP_SORT_001', target: 'KP_SORT_006', type: 'PARENT_OF', order: 5 },
  { source: 'KP_SORT_001', target: 'KP_SORT_007', type: 'PARENT_OF', order: 6 },
  { source: 'KP_SORT_001', target: 'KP_SORT_008', type: 'PARENT_OF', order: 7 },
  { source: 'KP_SORT_004', target: 'KP_SORT_005', type: 'RELATED_TO', reason: '希尔排序是插入排序的改进' },
  { source: 'KP_SORT_006', target: 'KP_SORT_007', type: 'RELATED_TO', reason: '同为分治类排序' },
  // 跨模块关联
  { source: 'KP_LL_002', target: 'KP_TREE_003', type: 'RELATED_TO', reason: '递归遍历本质是栈的应用' },
  { source: 'KP_GRAPH_004', target: 'KP_LL_004', type: 'RELATED_TO', reason: 'BFS实现需要队列' },
  { source: 'KP_SEARCH_003', target: 'KP_SORT_001', type: 'RELATED_TO', reason: '二分查找要求数据有序' },
  { source: 'KP_SORT_008', target: 'KP_TREE_001', type: 'RELATED_TO', reason: '堆排序基于完全二叉树' },
]

onMounted(loadData)
</script>

<style scoped>
.graph-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #EBEEF5;
  flex-shrink: 0;
}

.search-input {
  width: 260px;
}

.module-filter {
  width: 140px;
}

.stats {
  margin-left: auto;
  display: flex;
  gap: 8px;
}

.main-content {
  display: flex;
  flex: 1;
  min-height: 0;
  margin-top: 12px;
}

.canvas-area {
  flex: 1;
  min-width: 0;
  border-radius: 8px;
  background: #FAFAFA;
  border: 1px solid #EBEEF5;
  overflow: hidden;
}

.detail-panel {
  width: 320px;
  flex-shrink: 0;
  margin-left: 12px;
  border: 1px solid #EBEEF5;
  border-radius: 8px;
  background: #fff;
  overflow: hidden;
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.25s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateX(20px);
}
</style>
