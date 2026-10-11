<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { api, type KnowledgeStage } from '@/api'

const stages = ref<KnowledgeStage[]>([])
const loading = ref(true)
onMounted(async () => {
  try { stages.value = (await api.get<KnowledgeStage[]>('/learning/knowledge-tree')).data }
  finally { loading.value = false }
})
</script>

<template>
  <div class="stack">
    <div class="section-head">
      <div><h2>从 Java 基础走向算法赛场</h2><p>完成前置节点后解锁新模块，每个节点都配套讲解与练习。</p></div>
      <span class="pill easy">当前阶段：基础巩固</span>
    </div>
    <div v-if="loading" class="loading">正在加载学习路径…</div>
    <div v-else class="stage-list">
      <section v-for="(stage, index) in stages" :key="stage.id" class="stage-card">
        <div class="stage-top">
          <div><span class="eyebrow">Stage {{ index + 1 }}</span><h3>{{ stage.title }}</h3><p>{{ stage.description }}</p></div>
          <div><div class="section-head"><small>阶段进度</small><b>{{ stage.progress }}%</b></div><el-progress :percentage="stage.progress" :show-text="false" :stroke-width="8" color="#087e72" /></div>
        </div>
        <div class="node-track">
          <article v-for="node in stage.nodes" :key="node.id" class="node" :class="node.state.toLowerCase()">
            <span class="pill">{{ node.state === 'MASTERED' ? '已掌握' : node.state === 'LEARNING' ? '学习中' : node.state === 'READY' ? '可开始' : '未解锁' }}</span>
            <b>{{ node.title }}</b>
            <small>{{ node.completed }} / {{ node.total }} 个练习</small>
          </article>
        </div>
      </section>
    </div>
  </div>
</template>

