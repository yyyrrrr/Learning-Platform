<template>
  <div class="video-page">
    <section class="page-header">
      <div>
        <h1>视频讲解</h1>
        <p>按知识点查看配套讲解视频，并自动记录观看进度。</p>
      </div>
      <el-button :icon="Refresh" :loading="loading" @click="fetchVideoList">刷新</el-button>
    </section>

    <section class="toolbar">
      <el-form :inline="true" @submit.prevent>
        <el-form-item label="知识点 ID">
          <el-input-number
            v-model="filter.knowledgeId"
            :min="1"
            :controls="false"
            placeholder="全部"
            class="knowledge-input"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="fetchVideoList">筛选</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </section>

    <section class="video-workspace" v-loading="loading">
      <aside class="video-list">
        <div class="list-header">
          <span>视频列表</span>
          <el-tag size="small" effect="plain">{{ videos.length }} 个</el-tag>
        </div>

        <el-empty v-if="!loading && videos.length === 0" description="暂无视频数据" />

        <button
          v-for="item in videos"
          :key="item.id"
          class="video-item"
          :class="{ active: selectedVideo?.id === item.id }"
          type="button"
          @click="selectVideo(item)"
        >
          <span class="video-title">{{ item.title }}</span>
          <span class="video-meta">
            知识点 {{ item.knowledgePointId }}
            <template v-if="item.duration"> · {{ formatDuration(item.duration) }}</template>
          </span>
        </button>
      </aside>

      <main class="player-panel">
        <template v-if="selectedVideo">
          <div class="player-header">
            <div>
              <h2>{{ selectedVideo.title }}</h2>
              <p>{{ selectedVideo.description || '暂无视频说明' }}</p>
            </div>
            <el-tag :type="watchState.isCompleted ? 'success' : 'info'" effect="plain">
              {{ watchState.isCompleted ? '已完成' : '学习中' }}
            </el-tag>
          </div>

          <video
            ref="videoRef"
            class="native-player"
            :src="selectedVideo.videoUrl"
            :poster="selectedVideo.thumbnailUrl || undefined"
            controls
            preload="metadata"
            @loadedmetadata="handleLoadedMetadata"
            @timeupdate="handleTimeUpdate"
            @pause="flushProgress"
            @ended="handleEnded"
          />

          <div class="progress-panel">
            <div class="progress-copy">
              <span>观看进度</span>
              <strong>{{ watchState.watchProgress }}%</strong>
            </div>
            <el-progress
              :percentage="watchState.watchProgress"
              :status="watchState.isCompleted ? 'success' : undefined"
            />
            <div class="watch-meta">
              <span>已观看 {{ formatDuration(watchState.watchDuration) }}</span>
              <span v-if="watchState.lastWatchTime">上次观看 {{ formatTime(watchState.lastWatchTime) }}</span>
            </div>
          </div>
        </template>

        <el-empty v-else description="请选择一个视频开始学习" />
      </main>
    </section>
  </div>
</template>

<script setup>
import { nextTick, onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import { getVideoList, getWatchRecord, saveWatchProgress } from '@/api/modules/video.js'

const loading = ref(false)
const videos = ref([])
const selectedVideo = ref(null)
const videoRef = ref(null)
const pendingResumeSeconds = ref(null)
const lastReportAt = ref(0)

const filter = reactive({
  knowledgeId: undefined,
})

const watchState = reactive({
  watchProgress: 0,
  watchDuration: 0,
  lastWatchTime: '',
  isCompleted: 0,
})

const fetchVideoList = async () => {
  loading.value = true
  try {
    const list = await getVideoList(filter.knowledgeId)
    videos.value = Array.isArray(list) ? list : []
    if (videos.value.length === 0) {
      selectedVideo.value = null
      resetWatchState()
      return
    }
    if (!selectedVideo.value || !videos.value.some((item) => item.id === selectedVideo.value.id)) {
      await selectVideo(videos.value[0])
    }
  } catch (error) {
    videos.value = []
    selectedVideo.value = null
    resetWatchState()
    console.error('Failed to load video list', error)
  } finally {
    loading.value = false
  }
}

const selectVideo = async (video) => {
  selectedVideo.value = video
  resetWatchState()
  pendingResumeSeconds.value = null

  try {
    const record = await getWatchRecord(video.id)
    applyWatchRecord(record)
    pendingResumeSeconds.value = watchState.watchDuration
    await nextTick()
    handleLoadedMetadata()
  } catch (error) {
    console.error('Failed to load watch record', error)
  }
}

const resetFilter = () => {
  filter.knowledgeId = undefined
  fetchVideoList()
}

const resetWatchState = () => {
  watchState.watchProgress = 0
  watchState.watchDuration = 0
  watchState.lastWatchTime = ''
  watchState.isCompleted = 0
}

const applyWatchRecord = (record) => {
  if (!record) return
  watchState.watchProgress = clampProgress(record.watchProgress || 0)
  watchState.watchDuration = record.watchDuration || 0
  watchState.lastWatchTime = record.lastWatchTime || ''
  watchState.isCompleted = record.isCompleted || 0
}

const handleLoadedMetadata = () => {
  const player = videoRef.value
  if (!player || pendingResumeSeconds.value === null || pendingResumeSeconds.value <= 0) return

  const duration = Number.isFinite(player.duration) ? player.duration : selectedVideo.value?.duration
  if (duration > 0 && watchState.watchProgress < 100) {
    player.currentTime = Math.min(Math.max(0, duration - 0.2), pendingResumeSeconds.value)
  }
  pendingResumeSeconds.value = null
}

const handleTimeUpdate = () => {
  const player = videoRef.value
  if (!player || !selectedVideo.value) return

  syncLocalProgress()
  const now = Date.now()
  if (now - lastReportAt.value > 10000) {
    lastReportAt.value = now
    reportProgress(false)
  }
}

const handleEnded = () => {
  watchState.watchProgress = 100
  watchState.isCompleted = 1
  syncLocalProgress()
  reportProgress(true)
}

const flushProgress = () => {
  syncLocalProgress()
  reportProgress(true)
}

const syncLocalProgress = () => {
  const player = videoRef.value
  if (!player) return

  const duration = Number.isFinite(player.duration) && player.duration > 0
    ? player.duration
    : selectedVideo.value?.duration

  watchState.watchDuration = Math.max(0, Math.round(player.currentTime || 0))
  if (duration > 0) {
    watchState.watchProgress = clampProgress(Math.floor((watchState.watchDuration / duration) * 100))
  }
  if (watchState.watchProgress >= 95) {
    watchState.isCompleted = 1
  }
}

const reportProgress = async (showError) => {
  if (!selectedVideo.value) return

  try {
    const record = await saveWatchProgress({
      videoId: selectedVideo.value.id,
      watchProgress: watchState.watchProgress,
      watchDuration: watchState.watchDuration,
    })
    applyWatchRecord(record)
  } catch (error) {
    console.error('Failed to save watch progress', error)
    if (showError) {
      ElMessage.error('观看进度保存失败')
    }
  }
}

const clampProgress = (value) => Math.max(0, Math.min(100, Number(value) || 0))

const formatDuration = (seconds) => {
  const total = Math.max(0, Number(seconds) || 0)
  const minute = Math.floor(total / 60)
  const second = total % 60
  return `${minute}:${String(second).padStart(2, '0')}`
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

onMounted(fetchVideoList)
onBeforeUnmount(flushProgress)
</script>

<style scoped>
.video-page {
  min-height: 100%;
  padding: 24px;
  color: #1f2937;
  background: #f6f7fb;
}

.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 16px;
}

.page-header h1 {
  margin: 0 0 6px;
  font-size: 26px;
  font-weight: 700;
}

.page-header p {
  margin: 0;
  color: #667085;
}

.toolbar {
  padding: 16px;
  margin-bottom: 16px;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
}

.knowledge-input {
  width: 180px;
}

.video-workspace {
  display: grid;
  grid-template-columns: minmax(260px, 340px) minmax(0, 1fr);
  gap: 16px;
  min-height: 540px;
}

.video-list,
.player-panel {
  min-width: 0;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
}

.video-list {
  padding: 12px;
  overflow: auto;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4px 4px 12px;
  color: #344054;
  font-weight: 600;
}

.video-item {
  display: block;
  width: 100%;
  padding: 12px;
  margin-bottom: 8px;
  text-align: left;
  background: #ffffff;
  border: 1px solid #edf0f5;
  border-radius: 8px;
  cursor: pointer;
  transition: border-color 0.2s ease, background 0.2s ease;
}

.video-item:hover,
.video-item.active {
  background: #eef5ff;
  border-color: #409eff;
}

.video-title {
  display: block;
  margin-bottom: 6px;
  color: #101828;
  font-size: 15px;
  font-weight: 600;
}

.video-meta {
  color: #667085;
  font-size: 13px;
}

.player-panel {
  padding: 18px;
  display: flex;
  flex-direction: column;
}

.player-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 16px;
}

.player-header h2 {
  margin: 0 0 6px;
  font-size: 22px;
}

.player-header p {
  margin: 0;
  color: #667085;
  line-height: 1.6;
}

.native-player {
  width: 100%;
  aspect-ratio: 16 / 9;
  background: #111827;
  border-radius: 8px;
}

.progress-panel {
  padding-top: 16px;
}

.progress-copy,
.watch-meta {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 8px;
  color: #667085;
  font-size: 13px;
}

.progress-copy strong {
  color: #101828;
}

.watch-meta {
  margin-top: 8px;
  margin-bottom: 0;
}

@media (max-width: 900px) {
  .video-page {
    padding: 16px;
  }

  .page-header,
  .player-header,
  .progress-copy,
  .watch-meta {
    flex-direction: column;
  }

  .video-workspace {
    grid-template-columns: 1fr;
  }
}
</style>
