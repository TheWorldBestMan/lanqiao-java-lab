<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { api, difficultyText, type Difficulty, type ProblemSummary } from '@/api'

const problems = ref<ProblemSummary[]>([])
const keyword = ref('')
const difficulty = ref('')
const source = ref('')
const loading = ref(true)
const filtered = computed(() => problems.value.filter(p =>
  (!keyword.value || `${p.title}${p.tags.join('')}`.toLowerCase().includes(keyword.value.toLowerCase())) &&
  (!difficulty.value || p.difficulty === difficulty.value) && (!source.value || p.sourceType === source.value)))

onMounted(async () => {
  try { problems.value = (await api.get<ProblemSummary[]>('/problems')).data }
  finally { loading.value = false }
})
const difficultyClass = (d: Difficulty) => d.toLowerCase()
</script>

<template>
  <div class="stack">
    <div class="section-head">
      <div><h2>经典题库</h2><p>每道题都清楚标注难度、来源与内容授权状态。</p></div>
      <span class="pill">共 {{ filtered.length }} 题</span>
    </div>
    <div class="filter-bar">
      <el-input v-model="keyword" clearable placeholder="搜索题目或知识点" />
      <el-select v-model="difficulty" clearable placeholder="全部难度"><el-option label="简单" value="EASY" /><el-option label="中等" value="MEDIUM" /><el-option label="困难" value="HARD" /></el-select>
      <el-select v-model="source" clearable placeholder="全部来源"><el-option label="平台原创" value="ORIGINAL" /><el-option label="蓝桥杯" value="LANQIAO" /><el-option label="LeetCode" value="LEETCODE" /><el-option label="企业笔试" value="COMPANY" /></el-select>
    </div>
    <div v-if="loading" class="loading">正在加载题库…</div>
    <div v-else class="problem-list">
      <RouterLink v-for="p in filtered" :key="p.id" :to="`/problems/${p.slug}`" class="problem-row">
        <div>
          <h3>{{ p.title }}</h3><p>{{ p.summary }}</p>
          <div class="meta-row"><span class="source-chip">来源：{{ p.sourceLabel }}</span><span v-for="tag in p.tags" :key="tag" class="pill">{{ tag }}</span></div>
        </div>
        <div class="meta-row">
          <span class="pill" :class="difficultyClass(p.difficulty)">{{ difficultyText[p.difficulty] }}</span>
          <span v-if="!p.contentAvailable" class="pill blocked">仅元数据</span>
        </div>
      </RouterLink>
      <div v-if="!filtered.length" class="empty">没有匹配的题目，换个筛选条件试试。</div>
    </div>
  </div>
</template>

