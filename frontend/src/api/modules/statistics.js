import http from '@/api/index.js'

// ----------------------------------------------------------------
// 学习跟踪与统计 API
// owner：学习跟踪与统计分析
// ----------------------------------------------------------------

/** 记录学习行为事件（知识点浏览、开始学习等） */
export function recordBehavior(data) {
  return http.post('/learning/record', data)
}

/** 获取当前用户全部学习进度 */
export function getLearningProgress() {
  return http.get('/statistics/progress/all')
}

/** 更新知识点学习状态 */
export function updateProgress(data) {
  return http.put('/statistics/progress', data)
}

/** 获取完整统计数据 */
export function getStatistics() {
  return http.get('/statistics/overview')
}

/** 获取学习概况数据 */
export function getOverviewDetail() {
  return http.get('/statistics/overview/detail')
}

/** 获取学习进度分析（柱状图数据） */
export function getProgressAnalysis() {
  return http.get('/statistics/progress')
}

/** 获取掌握度分析（雷达图数据） */
export function getMasteryAnalysis() {
  return http.get('/statistics/mastery')
}

/** 获取学习趋势数据（折线图数据） */
export function getTrendData(days = 7) {
  return http.get('/statistics/trend', { params: { days } })
}

/** 获取薄弱知识点列表 */
export function getWeakPoints(limit = 10) {
  return http.get('/statistics/weak-points', { params: { limit } })
}

/** 获取错题列表 */
export function getWrongQuestions(module = '', limit = 20) {
  return http.get('/statistics/wrong-questions', { params: { module, limit } })
}

/** 获取指定知识点的进度 */
export function getKnowledgePointProgress(knowledgePointId) {
  return http.get(`/statistics/progress/${knowledgePointId}`)
}
