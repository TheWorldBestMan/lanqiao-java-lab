<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { api } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

interface Exam { id: string; title: string; durationMinutes: number; problemCount: number; status: string; note: string }
interface Session { id: string; durationMinutes: number; problems: { title: string }[] }
const exams = ref<Exam[]>([])
onMounted(async () => { exams.value = (await api.get<Exam[]>('/exams')).data })

async function start(exam: Exam) {
  if (exam.status !== 'AVAILABLE') return
  const session = (await api.post<Session>(`/exams/${exam.id}/sessions`)).data
  await ElMessageBox.alert(`已生成 ${session.problems.length} 道原创题，计时 ${session.durationMinutes} 分钟。完整考试作答流将在下一阶段接入。`, '模拟考试已创建')
  ElMessage.success('考试草稿已保存')
}
</script>

<template>
  <div class="stack">
    <div class="section-head"><div><h2>模拟考试</h2><p>在接近赛场的时间压力下练习策略，不使用真实未授权试卷。</p></div><span class="pill">草稿自动保存</span></div>
    <el-alert title="赛制说明" type="info" :closable="false" description="当前试卷由平台原创题组卷，仅模拟答题节奏；不声称是任何届蓝桥杯真实试题。" show-icon />
    <div class="grid-2">
      <article v-for="exam in exams" :key="exam.id" class="card exam-card">
        <div><span class="eyebrow">{{ exam.status === 'AVAILABLE' ? 'Ready' : 'Coming soon' }}</span><h3>{{ exam.title }}</h3><p>{{ exam.note }}</p></div>
        <div class="exam-meta"><span>⏱ {{ exam.durationMinutes }} 分钟</span><span>⌁ {{ exam.problemCount }} 道题</span></div>
        <el-button type="primary" color="#087e72" :disabled="exam.status !== 'AVAILABLE'" @click="start(exam)">{{ exam.status === 'AVAILABLE' ? '开始模拟' : '即将开放' }}</el-button>
      </article>
    </div>
  </div>
</template>

