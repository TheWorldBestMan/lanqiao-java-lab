<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { api, difficultyText, type ProblemSummary } from '@/api'

interface Overview {
  studyDays: number; currentStreak: number; acceptedToday: number; availableProblems: number
  weeklyGoal: number; overallProgress: number
}
const overview = ref<Overview>({ studyDays: 12, currentStreak: 4, acceptedToday: 0, availableProblems: 0, weeklyGoal: 5, overallProgress: 68 })
const recommendations = ref<ProblemSummary[]>([])

onMounted(async () => {
  const [stats, problems] = await Promise.all([
    api.get<Overview>('/learning/overview'), api.get<ProblemSummary[]>('/problems'),
  ])
  overview.value = stats.data
  recommendations.value = problems.data.filter(p => p.contentAvailable).slice(0, 3)
})
</script>

<template>
  <div class="stack">
    <section class="hero">
      <span class="hero-kicker">第 3 周 · 数组与双指针</span>
      <h2>把每一道题，变成下一次遇见时的直觉。</h2>
      <p>今天建议先完成 1 个知识点和 2 道巩固题。保持节奏，比一次刷很多更重要。</p>
      <div class="hero-actions">
        <RouterLink class="primary-btn" to="/learn">继续今日学习</RouterLink>
        <RouterLink class="ghost-btn" to="/problems">自由刷题</RouterLink>
      </div>
    </section>

    <div class="stat-row">
      <div class="stat-card"><span class="label">累计学习</span><div class="value">{{ overview.studyDays }}</div><span class="hint">天</span></div>
      <div class="stat-card"><span class="label">连续学习</span><div class="value">{{ overview.currentStreak }}</div><span class="hint">保持得很好</span></div>
      <div class="stat-card"><span class="label">今日通过</span><div class="value">{{ overview.acceptedToday }}</div><span class="hint">次提交</span></div>
      <div class="stat-card"><span class="label">总体进度</span><div class="value">{{ overview.overallProgress }}%</div><span class="hint">基础阶段</span></div>
    </div>

    <div class="grid-2">
      <section class="card card-pad">
        <div class="section-head">
          <div><h2>今日路线</h2><p>约 45 分钟，循序渐进</p></div>
          <RouterLink to="/learn" class="text-link">查看知识树 →</RouterLink>
        </div>
        <div class="problem-list">
          <div class="problem-row"><div><h3>01 · 复习数组遍历</h3><p>回顾边界、下标与常见输入模式</p></div><span class="pill easy">10 分钟</span></div>
          <div class="problem-row"><div><h3>02 · 学习滑动窗口</h3><p>理解窗口扩张和收缩的判断条件</p></div><span class="pill medium">20 分钟</span></div>
          <div class="problem-row"><div><h3>03 · 完成巩固题</h3><p>用一道原创题把模板写熟</p></div><span class="pill">15 分钟</span></div>
        </div>
      </section>

      <section class="card card-pad">
        <div class="section-head">
          <div><h2>推荐练习</h2><p>根据当前知识进度生成</p></div>
          <RouterLink to="/problems" class="text-link">全部题目 →</RouterLink>
        </div>
        <div class="problem-list">
          <RouterLink v-for="p in recommendations" :key="p.id" :to="`/problems/${p.slug}`" class="problem-row">
            <div><h3>{{ p.title }}</h3><p>{{ p.summary }}</p><div class="meta-row"><span v-for="tag in p.tags.slice(0,2)" :key="tag" class="pill">{{ tag }}</span></div></div>
            <span class="pill" :class="p.difficulty.toLowerCase()">{{ difficultyText[p.difficulty] }}</span>
          </RouterLink>
        </div>
      </section>
    </div>
  </div>
</template>

