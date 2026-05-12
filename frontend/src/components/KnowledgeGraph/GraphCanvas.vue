<template>
  <div ref="chartRef" class="graph-canvas"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  nodes: { type: Array, default: () => [] },
  edges: { type: Array, default: () => [] },
  highlightIds: { type: Array, default: () => [] },
  selectedId: { type: String, default: '' },
})

const emit = defineEmits(['node-click'])

const chartRef = ref(null)
let chart = null

const moduleColors = {
  SET: '#909399',
  LINEAR_LIST: '#409EFF',
  TREE: '#67C23A',
  GRAPH: '#E6A23C',
  SEARCH: '#F56C6C',
  SORT: '#7B68EE',
}

const moduleLabels = {
  SET: '集合',
  LINEAR_LIST: '线性表',
  TREE: '树',
  GRAPH: '图',
  SEARCH: '查找',
  SORT: '排序',
}

function getNodeSize(level) {
  if (level === 1) return 50
  if (level === 2) return 35
  return 25
}

function buildOption() {
  const { nodes, edges, highlightIds, selectedId } = props

  const categories = Object.keys(moduleColors).map((key) => ({
    name: moduleLabels[key] || key,
  }))
  const categoryIndex = {}
  Object.keys(moduleColors).forEach((key, i) => {
    categoryIndex[key] = i
  })

  const graphNodes = nodes.map((node) => {
    const isHighlighted = highlightIds.length === 0 || highlightIds.includes(node.id)
    const isSelected = selectedId === node.id
    const color = moduleColors[node.module] || '#909399'

    return {
      id: node.id,
      name: node.name,
      category: categoryIndex[node.module] ?? 0,
      symbolSize: getNodeSize(node.level),
      value: node.name,
      label: {
        show: true,
        fontSize: isSelected ? 14 : 11,
        color: isHighlighted ? '#303133' : '#C0C4CC',
        fontWeight: isSelected ? 'bold' : 'normal',
      },
      itemStyle: {
        color: isHighlighted ? color : '#DCDFE6',
        borderColor: isSelected ? '#409EFF' : 'transparent',
        borderWidth: isSelected ? 3 : 0,
        shadowBlur: isSelected ? 15 : 0,
        shadowColor: isSelected ? 'rgba(64,158,255,0.4)' : 'transparent',
        opacity: isHighlighted ? 1 : 0.3,
      },
      emphasis: {
        itemStyle: {
          borderColor: color,
          borderWidth: 2,
          shadowBlur: 20,
          shadowColor: color + '66',
        },
        label: {
          fontSize: 14,
          fontWeight: 'bold',
        },
      },
    }
  })

  const graphEdges = edges.map((edge) => {
    const isRelated = highlightIds.length === 0 ||
      highlightIds.includes(edge.source) || highlightIds.includes(edge.target)

    return {
      source: edge.source,
      target: edge.target,
      lineStyle: {
        color: edge.type === 'PARENT_OF' ? '#C0C4CC' : '#E6A23C',
        width: edge.type === 'PARENT_OF' ? 2 : 1.5,
        type: edge.type === 'RELATED_TO' ? 'dashed' : 'solid',
        curveness: 0.2,
        opacity: isRelated ? 0.7 : 0.1,
      },
      tooltip: {
        formatter: () => {
          if (edge.type === 'PARENT_OF') return '包含关系'
          return edge.reason || '关联关系'
        },
      },
    }
  })

  return {
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255,255,255,0.95)',
      borderColor: '#EBEEF5',
      textStyle: { color: '#303133', fontSize: 13 },
      formatter: (params) => {
        if (params.dataType === 'node') {
          const node = nodes.find((n) => n.id === params.data.id)
          if (!node) return params.name
          const diffMap = { EASY: '简单', MEDIUM: '中等', HARD: '困难' }
          const diffColor = { EASY: '#67C23A', MEDIUM: '#E6A23C', HARD: '#F56C6C' }
          return `<div style="max-width:280px">
            <div style="font-weight:bold;font-size:14px;margin-bottom:6px">${node.name}</div>
            <div style="color:#909399;font-size:12px;margin-bottom:4px">${node.code}</div>
            <div style="margin-bottom:4px">${node.description}</div>
            <div style="font-size:12px">
              <span style="color:${diffColor[node.difficulty]}">${diffMap[node.difficulty] || node.difficulty}</span>
              · ${moduleLabels[node.module] || node.module}
            </div>
          </div>`
        }
        return null
      },
    },
    legend: {
      data: Object.values(moduleLabels),
      top: 10,
      left: 'center',
      textStyle: { fontSize: 12 },
    },
    series: [
      {
        type: 'graph',
        layout: 'force',
        data: graphNodes,
        links: graphEdges,
        categories: categories.map((c, i) => ({
          ...c,
          itemStyle: { color: Object.values(moduleColors)[i] },
        })),
        roam: true,
        draggable: true,
        force: {
          repulsion: 300,
          gravity: 0.1,
          edgeLength: [80, 200],
          friction: 0.6,
          layoutAnimation: true,
        },
        emphasis: {
          focus: 'adjacency',
          blurScope: 'coordinateSystem',
        },
        scaleLimit: { min: 0.3, max: 3 },
        lineStyle: { curveness: 0.2 },
        edgeSymbol: ['none', 'arrow'],
        edgeSymbolSize: 8,
      },
    ],
  }
}

function initChart() {
  if (!chartRef.value) return
  chart = echarts.init(chartRef.value)
  chart.setOption(buildOption())
  chart.on('click', 'series.graph', (params) => {
    if (params.dataType === 'node') {
      emit('node-click', params.data.id)
    }
  })
}

watch(
  () => [props.nodes, props.edges, props.highlightIds, props.selectedId],
  () => {
    if (chart) {
      chart.setOption(buildOption(), { notMerge: false })
    }
  },
  { deep: true },
)

onMounted(() => {
  nextTick(initChart)
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  chart?.dispose()
  window.removeEventListener('resize', handleResize)
})

function handleResize() {
  chart?.resize()
}

defineExpose({
  resize: handleResize,
  focusNode(id) {
    if (!chart) return
    chart.dispatchAction({
      type: 'focusNodeAdjacency',
      seriesIndex: 0,
      dataIndex: props.nodes.findIndex((n) => n.id === id),
    })
  },
})
</script>

<style scoped>
.graph-canvas {
  width: 100%;
  height: 100%;
  min-height: 500px;
}
</style>
