<template>
  <div class="page-placeholder">
    <el-empty description="AI 辅助模块 — 待开发">
      <template #description>
        <p><strong>模块：</strong>AI 辅助功能</p>
        <p><strong>owner：</strong>AI 辅助功能负责人</p>
        <p><strong>主目录：</strong><code>frontend/src/views/AIChat/</code></p>
        <p><strong>API：</strong><code>src/api/modules/ai.js</code>（问答、代码分析、代码生成）</p>
        <p><strong>注意：</strong>模型选型和 API Key 管理由后端架构 owner 确定后再接入</p>
      </template>
    </el-empty>
  </div>
</template>

<style scoped>
.page-placeholder { display: flex; align-items: center; justify-content: center; min-height: 400px; }
p { margin: 4px 0; color: #606266; font-size: 14px; }
</style>
