import http from '@/api/index.js'

// ----------------------------------------------------------------
// 习题模块 API
// owner：习题模块
// ----------------------------------------------------------------

/** 获取某知识点的练习题列表 */
export function getExerciseList(knowledgeId, params) {
  return http.get(`/exercise/list`, { params: { knowledgeId, ...params } })
}

/** 提交答题 */
export function submitAnswer(data) {
  return http.post('/exercise/submit', data)
}

/** 获取错题记录 */
export function getWrongList() {
  return http.get('/exercise/wrong')
}

/** 获取单个习题详情 */
export function getExerciseDetail(id) {
    return http.get(`/exercise/${id}`)
}

/** 创建新习题 */
export function createExercise(data) {
    return http.post('/exercise', data)
}

/** 更新习题 */
export function updateExercise(id, data) {
    return http.put(`/exercise/${id}`, data)
}

/** 删除习题 */
export function deleteExercise(id) {
    return http.delete(`/exercise/${id}`)
}

/** 根据知识点获取习题 */
export function getExercisesByKnowledge(knowledgeId) {
    return http.get(`/exercise/knowledge/${knowledgeId}`)
}
