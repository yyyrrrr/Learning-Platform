import http from '@/api/index.js'

// ----------------------------------------------------------------
// 代码在线运行 API
// owner：代码实现与在线运行
// ----------------------------------------------------------------

/** 提交代码执行请求，返回步骤序列供可视化 */
export function runCode(data) {
  return http.post('/code/run', data)
}

/** 获取推荐学习路径 */
export function getRecommendedPath(startNodeId) {
  return http.get('/knowledge/path', { params: { startNodeId } })
}
