<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { api } from '@/api'

interface Overview { studyDays: number; currentStreak: number; acceptedToday: number; availableProblems: number; weeklyGoal: number; overallProgress: number; mastery: number[]; masteryLabels: string[] }
const data = ref<Overview>({ studyDays: 0, currentStreak: 0, acceptedToday: 0, availableProblems: 0, weeklyGoal: 0, overallProgress: 0, mastery: [], masteryLabels: [] })
onMounted(async () => { data.value = (await api.get<Overview>('/learning/overview')).data })
</script>

<template>
  <div class="stack">
    <div class="section-head"><div><h2>学习分析</h2><p>先看能力结构，再决定下一道题做什么。</p></div><span class="pill easy">每次提交后更新</span></div>
    <div class="stat-row">
      <div class="stat-card"><span class="label">学习天数</span><div class="value">{{ data.studyDays }}</div><span class="hint">累计</span></div>
      <div class="stat-card"><span class="label">连续记录</span><div class="value">{{ data.currentStreak }}</div><span class="hint">天</span></div>
      <div class="stat-card"><span class="label">题库规模</span><div class="value">{{ data.availableProblems }}</div><span class="hint">含元数据占位</span></div>
      <div class="stat-card"><span class="label">整体掌握</span><div class="value">{{ data.overallProgress }}%</div><span class="hint">稳步上升</span></div>
    </div>
    <div class="grid-2">
      <section class="card card-pad">
        <div class="section-head"><div><h3>知识点掌握度</h3><p>按最近练习表现估算</p></div></div>
        <div class="bar-chart">
          <div v-for="(value, index) in data.mastery" :key="data.masteryLabels[index]" class="bar-row"><span>{{ data.masteryLabels[index] }}</span><div class="bar-track"><div class="bar-fill" :style="{ width: `${value}%` }"></div></div><b>{{ value }}%</b></div>
        </div>
      </section>
      <section class="card card-pad">
        <div class="section-head"><div><h3>下一步建议</h3><p>按短板优先级排列</p></div></div>
        <div class="policy-list">
          <div class="policy-item"><b>巩固滑动窗口</b><span>先复习模板，再做 2 道中等题</span></div>
          <div class="policy-item"><b>开始搜索入门</b><span>掌握队列后进入 BFS，暂不急于刷难题</span></div>
          <div class="policy-item"><b>周末完成一次模拟</b><span>重点记录时间分配，不只看最终得分</span></div>
        </div>
      </section>
    </div>
  </div>
</template>

