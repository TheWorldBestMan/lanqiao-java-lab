<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { api, statusText } from '@/api'

interface WrongProblem { slug: string; title: string; difficulty: string; lastStatus: string; updatedAt: string }
const items = ref<WrongProblem[]>([])
const loading = ref(true)
onMounted(async () => {
  try { items.value = (await api.get<WrongProblem[]>('/learning/wrong-problems')).data }
  finally { loading.value = false }
})
</script>

<template>
  <div class="stack">
    <div class="section-head"><div><h2>错题本</h2><p>失败不是扣分项，而是下一轮复习的入口。</p></div><span class="pill medium">待复习 {{ items.length }}</span></div>
    <div class="card card-pad">
      <div v-if="loading" class="loading">正在整理错题…</div>
      <div v-else-if="!items.length" class="empty"><div style="font-size: 34px; margin-bottom: 12px">◎</div>还没有需要复习的错题。完成几次在线提交后，这里会自动归档。</div>
      <div v-else class="problem-list">
        <RouterLink v-for="item in items" :key="item.slug" :to="`/problems/${item.slug}`" class="problem-row">
          <div><h3>{{ item.title }}</h3><p>最近结果：{{ statusText[item.lastStatus] || item.lastStatus }} · {{ new Date(item.updatedAt).toLocaleString('zh-CN') }}</p></div>
          <span class="text-link">再次练习 →</span>
        </RouterLink>
      </div>
    </div>
  </div>
</template>

