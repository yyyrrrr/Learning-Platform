<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <div class="page-placeholder">&ndash;&gt;-->
<!--&lt;!&ndash;    <el-empty description="习题模块 — 待开发">&ndash;&gt;-->
<!--&lt;!&ndash;      <template #description>&ndash;&gt;-->
<!--&lt;!&ndash;        <p><strong>模块：</strong>习题模块</p>&ndash;&gt;-->
<!--&lt;!&ndash;        <p><strong>owner：</strong>习题模块负责人</p>&ndash;&gt;-->
<!--&lt;!&ndash;        <p><strong>主目录：</strong><code>frontend/src/views/Exercise/</code></p>&ndash;&gt;-->
<!--&lt;!&ndash;        <p><strong>API：</strong><code>src/api/modules/exercise.js</code></p>&ndash;&gt;-->
<!--&lt;!&ndash;        <p><strong>做题结果：</strong>通过 <code>src/api/modules/statistics.js</code> 的 <code>recordBehavior()</code> 上报给统计模块</p>&ndash;&gt;-->
<!--&lt;!&ndash;      </template>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-empty>&ndash;&gt;-->
<!--&lt;!&ndash;  </div>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;.page-placeholder { display: flex; align-items: center; justify-content: center; min-height: 400px; }&ndash;&gt;-->
<!--&lt;!&ndash;p { margin: 4px 0; color: #606266; font-size: 14px; }&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->
<!--<template>-->
<!--  <div class="exercise-list-page">-->
<!--    <div class="page-header">-->
<!--      <h1>习题练习</h1>-->
<!--      <p>通过练习巩固所学知识点</p>-->
<!--    </div>-->

<!--    &lt;!&ndash; 筛选工具栏 &ndash;&gt;-->
<!--    <div class="filter-toolbar">-->
<!--      <el-form :inline="true" :model="filterForm">-->
<!--        <el-form-item label="知识点">-->
<!--          <el-select-->
<!--              v-model="filterForm.knowledgeId"-->
<!--              placeholder="全部"-->
<!--              clearable-->
<!--              @change="handleFilter"-->
<!--          >-->
<!--            <el-option-->
<!--                v-for="item in knowledgeOptions"-->
<!--                :key="item.id"-->
<!--                :label="item.name"-->
<!--                :value="item.id"-->
<!--            />-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="难度">-->
<!--          <el-select-->
<!--              v-model="filterForm.difficulty"-->
<!--              placeholder="全部"-->
<!--              clearable-->
<!--              @change="handleFilter"-->
<!--          >-->
<!--            <el-option label="简单" value="EASY" />-->
<!--            <el-option label="中等" value="MEDIUM" />-->
<!--            <el-option label="困难" value="HARD" />-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="状态">-->
<!--          <el-select-->
<!--              v-model="filterForm.status"-->
<!--              placeholder="全部"-->
<!--              clearable-->
<!--              @change="handleFilter"-->
<!--          >-->
<!--            <el-option label="未尝试" value="unattempted" />-->
<!--            <el-option label="已通过" value="passed" />-->
<!--            <el-option label="尝试过" value="attempted" />-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item>-->
<!--          <el-input-->
<!--              v-model="filterForm.keyword"-->
<!--              placeholder="搜索习题标题或描述"-->
<!--              clearable-->
<!--              @clear="handleFilter"-->
<!--              @keyup.enter="handleFilter"-->
<!--          >-->
<!--            <template #append>-->
<!--              <el-button :icon="Search" @click="handleFilter" />-->
<!--            </template>-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--    </div>-->

<!--    &lt;!&ndash; 习题列表 &ndash;&gt;-->
<!--    <div class="exercise-list" v-loading="loading">-->
<!--      <template v-if="exerciseList.length > 0">-->
<!--        <el-card-->
<!--            v-for="ex in exerciseList"-->
<!--            :key="ex.id"-->
<!--            class="exercise-card"-->
<!--            shadow="hover"-->
<!--            @click="goToDetail(ex.id)"-->
<!--        >-->
<!--          <div class="card-header">-->
<!--            <div class="title-section">-->
<!--              <span class="exercise-title">{{ ex.title }}</span>-->
<!--              <el-tag :type="getDifficultyType(ex.difficulty)" size="small">-->
<!--                {{ ex.difficulty }}-->
<!--              </el-tag>-->
<!--            </div>-->
<!--            <div class="meta-section">-->
<!--              <span class="pass-rate">-->
<!--                <el-icon><TrendCharts /></el-icon>-->
<!--                通过率: {{ (ex.passRate * 100).toFixed(1) }}%-->
<!--              </span>-->
<!--              <span class="update-time">最后更新: {{ formatTime(ex.updateTime) }}</span>-->
<!--            </div>-->
<!--          </div>-->
<!--          <p class="exercise-desc">{{ ex.description }}</p>-->
<!--          <div class="card-footer">-->
<!--            <span class="knowledge-tag">-->
<!--              <el-icon><Collection /></el-icon>-->
<!--              {{ ex.knowledgeName || '未分类' }}-->
<!--            </span>-->
<!--            <div class="action-buttons">-->
<!--              <el-button-->
<!--                  v-if="ex.userStatus === 'passed'"-->
<!--                  type="success"-->
<!--                  :icon="Check"-->
<!--                  size="small"-->
<!--                  @click.stop-->
<!--                  disabled-->
<!--              >已通过</el-button>-->
<!--              <el-button-->
<!--                  v-else-if="ex.userStatus === 'attempted'"-->
<!--                  type="warning"-->
<!--                  :icon="Clock"-->
<!--                  size="small"-->
<!--                  @click.stop-->
<!--              >继续挑战</el-button>-->
<!--              <el-button-->
<!--                  v-else-->
<!--                  type="primary"-->
<!--                  :icon="ArrowRight"-->
<!--                  size="small"-->
<!--                  @click.stop="goToDetail(ex.id)"-->
<!--              >开始做题</el-button>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-card>-->
<!--        &lt;!&ndash; 分页 &ndash;&gt;-->
<!--        <div class="pagination-container">-->
<!--          <el-pagination-->
<!--              v-model:current-page="filterForm.page"-->
<!--              v-model:page-size="filterForm.pageSize"-->
<!--              :page-sizes="[10, 20, 50]"-->
<!--              layout="total, sizes, prev, pager, next, jumper"-->
<!--              :total="total"-->
<!--              @size-change="handleSizeChange"-->
<!--              @current-change="handleCurrentChange"-->
<!--          />-->
<!--        </div>-->
<!--      </template>-->
<!--      <el-empty v-else description="暂无习题" />-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import http from '@/api/index.js'-->
<!--import { ref, onMounted, computed } from 'vue'-->
<!--import { useRouter } from 'vue-router'-->
<!--import { Search, Check, Clock, ArrowRight, TrendCharts, Collection } from '@element-plus/icons-vue'-->
<!--import { getExerciseList } from '@/api/modules/exercise'-->
<!--import { useKnowledgeStore } from '@/stores/knowledge'-->

<!--const router = useRouter()-->
<!--const knowledgeStore = useKnowledgeStore()-->

<!--// 数据与状态-->
<!--const loading = ref(false)-->
<!--const exerciseList = ref([])-->
<!--const total = ref(0)-->
<!--const knowledgeOptions = ref([]) // 应来自knowledgeStore或单独API-->
<!--const exercises = ref([])-->

<!--// 筛选表单-->
<!--const filterForm = ref({-->
<!--  knowledgeId: undefined,-->
<!--  difficulty: undefined,-->
<!--  status: undefined,-->
<!--  keyword: '',-->
<!--  page: 1,-->
<!--  pageSize: 20-->
<!--})-->

<!--// 计算属性-->
<!--const getDifficultyType = (difficulty) => {-->
<!--  const map = { EASY: 'success', MEDIUM: 'warning', HARD: 'danger' }-->
<!--  return map[difficulty] || 'info'-->
<!--}-->

<!--// 方法-->
<!--const fetchExerciseList = async () => {-->
<!--  loading.value = true-->
<!--  try {-->
<!--    const response = await http.get('/exercise/list', {-->
<!--      params: { page: 1, pageSize: 20 }-->
<!--    })-->

<!--    // 在组合式API中，直接使用ref的值-->
<!--    exercises.value = response.data?.records || []-->

<!--  } catch (error) {-->
<!--    console.error('加载失败:', error)-->
<!--    exercises.value = []-->
<!--  } finally {-->
<!--    loading.value = false-->
<!--  }-->
<!--  // loading.value = true-->
<!--  // try {-->
<!--  //   // 调用API，传入筛选参数-->
<!--  //   const res = await getExerciseList(filterForm.value.knowledgeId, {-->
<!--  //     difficulty: filterForm.value.difficulty,-->
<!--  //     status: filterForm.value.status,-->
<!--  //     keyword: filterForm.value.keyword,-->
<!--  //     page: filterForm.value.page,-->
<!--  //     pageSize: filterForm.value.pageSize-->
<!--  //   })-->
<!--  //   // 假设接口返回 { data: { records: [], total: 0 } }-->
<!--  //   exerciseList.value = res.data.records || []-->
<!--  //   total.value = res.data.total || 0-->
<!--  // } catch (error) {-->
<!--  //   console.error('获取习题列表失败', error)-->
<!--  //   exerciseList.value = []-->
<!--  //   total.value = 0-->
<!--  // } finally {-->
<!--  //   loading.value = false-->
<!--  // }-->
<!--}-->

<!--const handleFilter = () => {-->
<!--  filterForm.value.page = 1 // 筛选时重置到第一页-->
<!--  fetchExerciseList()-->
<!--}-->

<!--const handleSizeChange = (val) => {-->
<!--  filterForm.value.pageSize = val-->
<!--  fetchExerciseList()-->
<!--}-->

<!--const handleCurrentChange = (val) => {-->
<!--  filterForm.value.page = val-->
<!--  fetchExerciseList()-->
<!--}-->

<!--const goToDetail = (id) => {-->
<!--  // 跳转到详情页，假设路由名为 ExerciseDetail，路径为 /exercise/:id-->
<!--  // 您需要确保路由已由路由owner配置好-->
<!--  router.push({ name: 'ExerciseDetail', params: { id } })-->
<!--  // 或者使用路径-->
<!--  // router.push(`/exercise/${id}`)-->
<!--}-->

<!--const formatTime = (timeStr) => {-->
<!--  // 简单的时间格式化，可以使用dayjs等库-->
<!--  if (!timeStr) return '-'-->
<!--  return new Date(timeStr).toLocaleDateString()-->
<!--}-->

<!--// 生命周期-->
<!--onMounted(() => {-->
<!--  fetchExerciseList()-->
<!--  // 初始化知识点选项，可以从全局状态或API获取-->
<!--  // knowledgeOptions.value = knowledgeStore.knowledgeList.map(k => ({ id: k.id, name: k.name }))-->
<!--})-->
<!--</script>-->

<!--<style scoped>-->
<!--.exercise-list-page {-->
<!--  padding: 24px;-->
<!--  max-width: 1200px;-->
<!--  margin: 0 auto;-->
<!--}-->

<!--.page-header {-->
<!--  margin-bottom: 24px;-->
<!--}-->
<!--.page-header h1 {-->
<!--  font-size: 24px;-->
<!--  font-weight: 600;-->
<!--  color: #1f2329;-->
<!--  margin-bottom: 8px;-->
<!--}-->
<!--.page-header p {-->
<!--  color: #8a919f;-->
<!--  font-size: 14px;-->
<!--}-->

<!--.filter-toolbar {-->
<!--  background: #fff;-->
<!--  padding: 16px 20px;-->
<!--  border-radius: 8px;-->
<!--  margin-bottom: 16px;-->
<!--  border: 1px solid #e4e7ed;-->
<!--}-->

<!--.exercise-list {-->
<!--  min-height: 400px;-->
<!--}-->

<!--.exercise-card {-->
<!--  margin-bottom: 16px;-->
<!--  cursor: pointer;-->
<!--  transition: all 0.2s;-->
<!--}-->
<!--.exercise-card:hover {-->
<!--  transform: translateY(-2px);-->
<!--  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);-->
<!--}-->

<!--.card-header {-->
<!--  display: flex;-->
<!--  justify-content: space-between;-->
<!--  align-items: flex-start;-->
<!--  margin-bottom: 12px;-->
<!--}-->
<!--.title-section {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  gap: 12px;-->
<!--}-->
<!--.exercise-title {-->
<!--  font-size: 18px;-->
<!--  font-weight: 600;-->
<!--  color: #1f2329;-->
<!--}-->
<!--.meta-section {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  gap: 20px;-->
<!--  font-size: 13px;-->
<!--  color: #8a919f;-->
<!--}-->
<!--.pass-rate,-->
<!--.update-time {-->
<!--  display: inline-flex;-->
<!--  align-items: center;-->
<!--  gap: 4px;-->
<!--}-->

<!--.exercise-desc {-->
<!--  color: #4e5969;-->
<!--  font-size: 14px;-->
<!--  line-height: 1.6;-->
<!--  margin-bottom: 16px;-->
<!--  display: -webkit-box;-->
<!--  -webkit-line-clamp: 2;-->
<!--  -webkit-box-orient: vertical;-->
<!--  overflow: hidden;-->
<!--  text-overflow: ellipsis;-->
<!--}-->

<!--.card-footer {-->
<!--  display: flex;-->
<!--  justify-content: space-between;-->
<!--  align-items: center;-->
<!--}-->
<!--.knowledge-tag {-->
<!--  display: inline-flex;-->
<!--  align-items: center;-->
<!--  gap: 4px;-->
<!--  color: #165dff;-->
<!--  font-size: 13px;-->
<!--  padding: 4px 8px;-->
<!--  background-color: #f2f3f5;-->
<!--  border-radius: 4px;-->
<!--}-->
<!--.action-buttons {-->
<!--  flex-shrink: 0;-->
<!--}-->

<!--.pagination-container {-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  padding: 24px 0;-->
<!--}-->
<!--</style>-->
<template>
  <div class="exercise-list-page">
    <div class="page-header">
      <h1>习题练习</h1>
      <p>通过练习巩固所学知识点</p>
    </div>

    <div class="exercise-list" v-loading="loading">
<!--      <div v-if="debug" class="debug-panel">-->
<!--&lt;!&ndash;        <h3>🛠️ 调试信息</h3>&ndash;&gt;-->
<!--&lt;!&ndash;        <p>加载状态: {{ loading }}</p>&ndash;&gt;-->
<!--&lt;!&ndash;        <p>数据长度: {{ exerciseList.length }}</p>&ndash;&gt;-->
<!--&lt;!&ndash;        <p>API调用: 通过 api/modules/exercise.js 调用</p>&ndash;&gt;-->
<!--&lt;!&ndash;        <p>当前页面: {{ filterForm.page }}</p>&ndash;&gt;-->
<!--&lt;!&ndash;        <p>分页大小: {{ filterForm.pageSize }}</p>&ndash;&gt;-->
<!--      </div>-->

      <!-- 搜索框 -->
      <div class="search-box">
        <el-input
            v-model="filterForm.keyword"
            placeholder="搜索习题..."
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
            style="width: 300px; margin-bottom: 20px;"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
      </div>

      <template v-if="exerciseList.length > 0">
        <div v-for="ex in exerciseList" :key="ex.id" class="exercise-item">
          <div class="item-header">
            <h3>{{ ex.id }}. {{ ex.title }}</h3>
            <el-tag :type="getDifficultyTag(ex.difficulty)" size="small">
              {{ getDifficultyText(ex.difficulty) }}
            </el-tag>
          </div>
          <p class="description">{{ ex.description }}</p>
          <div class="meta">
            <span class="knowledge">
              <el-icon><Collection /></el-icon>
              知识点: {{ ex.knowledgeName || '未分类' }}
            </span>
            <span class="pass-rate">
              <el-icon><TrendCharts /></el-icon>
              通过率: {{ ex.passRate || 0 }}%
            </span>
            <span class="time-limit">
              <el-icon><Timer /></el-icon>
              限时: {{ ex.timeLimit || 60 }}秒
            </span>
            <span class="type">
              <el-icon><Document /></el-icon>
              题型: {{ getTypeText(ex.type) }}
            </span>
          </div>
          <div class="actions">
            <el-button type="primary" @click="startExercise(ex.id)">开始做题</el-button>
            <el-button
                type="info"
                size="small"
                @click="showAnalysis(ex)"
            >
              查看解析
            </el-button>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-container" v-if="total > 0">
          <el-pagination
              v-model:current-page="filterForm.page"
              v-model:page-size="filterForm.pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handlePageSizeChange"
              @current-change="handlePageChange"
          />
        </div>
      </template>

      <div v-else-if="!loading" class="empty-state">
        <el-empty description="暂无习题数据" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Collection, TrendCharts, Timer, Document } from '@element-plus/icons-vue'
import { getExerciseList } from '@/api/modules/exercise.js'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const exerciseList = ref([])
const total = ref(0)
const debug = ref(true)  // 调试模式

// 筛选表单
const filterForm = ref({
  knowledgeId: null,
  difficulty: null,
  keyword: '',
  page: 1,
  pageSize: 20
})

// 工具函数
const getDifficultyTag = (difficulty) => {
  const map = { EASY: 'success', MEDIUM: 'warning', HARD: 'danger' }
  return map[difficulty] || 'info'
}

const getDifficultyText = (difficulty) => {
  const map = { EASY: '简单', MEDIUM: '中等', HARD: '困难' }
  return map[difficulty] || difficulty
}

const getTypeText = (type) => {
  const map = {
    SINGLE_CHOICE: '单选题',
    MULTIPLE_CHOICE: '多选题',
    FILL_BLANK: '填空题',
    PROGRAMMING: '编程题'
  }
  return map[type] || type
}

// API调用
const fetchExerciseList = async () => {
  console.log('🔄 开始获取习题列表（通过API模块）')
  loading.value = true

  try {
    // 正确：通过API模块调用，遵循框架规范
    const response = await getExerciseList(filterForm.value.knowledgeId, {
      page: filterForm.value.page,
      pageSize: filterForm.value.pageSize,
      keyword: filterForm.value.keyword || undefined,
      difficulty: filterForm.value.difficulty || undefined
    })

    console.log('✅ API模块返回:', response)

    // 注意：根据框架指南，响应已在axios拦截器中解包
    // 所以response是后端返回的data字段，包含records和total
    if (response && response.records) {
      exerciseList.value = response.records || []
      total.value = response.total || 0
      console.log('🎉 数据加载成功:', exerciseList.value.length, '条')
      console.log('📊 总分页数:', total.value)
    } else {
      console.warn('⚠️ 数据格式不符合预期:', response)
      exerciseList.value = []
      total.value = 0
    }

  } catch (error) {
    console.error('❌ 加载失败:', error)
    ElMessage.error('加载失败: ' + (error.message || '未知错误'))
    exerciseList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 事件处理
const handleSearch = () => {
  filterForm.value.page = 1
  fetchExerciseList()
}

const handlePageSizeChange = (size) => {
  filterForm.value.pageSize = size
  filterForm.value.page = 1
  fetchExerciseList()
}

const handlePageChange = (page) => {
  filterForm.value.page = page
  fetchExerciseList()
}


// const goToDetail = (id) => {
//   console.log('跳转到习题详情:', id)
//   // 根据路由指南，应该提需求给路由owner
//   // 暂时用控制台日志代替
//   ElMessage.info(`跳转到习题 ${id} 的详情页`)
//   router.push(`/exercise/${id}`)
// }

const startExercise = (id) => {
  console.log('跳转到习题详情:', id)
  router.push(`/exercise/${id}`)
}
const showAnalysis = (exercise) => {
  ElMessage.info(`查看解析: ${exercise.title}`)
  console.log('习题解析:', exercise)
}

// 生命周期
onMounted(() => {
  console.log('🚀 组件挂载，开始加载数据')
  fetchExerciseList()
})
</script>

<style scoped>
.exercise-list-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}
.page-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #1f2329;
  margin-bottom: 8px;
}
.page-header p {
  color: #8a919f;
  font-size: 14px;
}

.debug-panel {
  background: #f0f0f0;
  padding: 15px;
  margin: 10px 0 20px 0;
  border-left: 4px solid #409eff;
  border-radius: 4px;
}
.debug-panel h3 {
  margin-top: 0;
  color: #409eff;
}
.debug-panel p {
  margin: 5px 0;
  font-size: 13px;
  color: #666;
}

.search-box {
  margin-bottom: 20px;
}

.exercise-item {
  border: 1px solid #e4e7ed;
  padding: 20px;
  margin-bottom: 16px;
  border-radius: 8px;
  background: white;
  transition: all 0.3s ease;
}
.exercise-item:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.item-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2329;
  margin: 0;
  flex: 1;
}

.description {
  color: #4e5969;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 16px;
  min-height: 22px;
}

.meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  font-size: 13px;
  color: #8a919f;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}
.meta span {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
.meta .el-icon {
  font-size: 14px;
}

.actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 24px 0;
  margin-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #8a919f;
}
</style>