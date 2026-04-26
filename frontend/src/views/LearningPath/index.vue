<template>
  <div class="page-placeholder">
    <el-empty description="学习目录模块 — 待开发">
      <template #description>
        <p><strong>模块：</strong>学习目录功能</p>
        <p><strong>owner：</strong>学习目录功能负责人</p>
        <p><strong>主目录：</strong><code>frontend/src/views/LearningPath/</code></p>
        <p><strong>数据来源：</strong>调用 <code>src/api/modules/knowledge.js</code> 中的 <code>getKnowledgeTree()</code></p>
        <p><strong>进度状态：</strong>从 <code>src/stores/learning.js</code> 的 <code>useLearningStore</code> 读取</p>
      </template>
    </el-empty>
  </div>
</template>

<style scoped>
.page-placeholder { display: flex; align-items: center; justify-content: center; min-height: 400px; }
p { margin: 4px 0; color: #606266; font-size: 14px; }
</style>
