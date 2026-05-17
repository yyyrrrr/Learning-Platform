import http from '@/api/index.js'

// ----------------------------------------------------------------
// 学习进度 API
// owner：学习目录 / 学习统计
// ----------------------------------------------------------------

/** 获取用户的学习进度 map（{ knowledgePointId: status }） */
export function getProgress() {
  return http.get('/learning/progress')
}
