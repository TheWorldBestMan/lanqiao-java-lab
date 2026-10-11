<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { VueMonacoEditor } from '@guolao/vue-monaco-editor'
import { ElMessage } from 'element-plus'
import { api, difficultyText, statusText, type ProblemDetail, type Submission } from '@/api'

const route = useRoute()
const problem = ref<ProblemDetail>()
const code = ref('')
const submitting = ref(false)
const result = ref<Submission>()
const resultTitle = computed(() => result.value ? (statusText[result.value.status] || result.value.status) : '准备就绪')

onMounted(async () => {
  problem.value = (await api.get<ProblemDetail>(`/problems/${route.params.slug}`)).data
  code.value = problem.value.templateCode || '// 此题仅提供来源元数据，暂不可在线作答。'
})

async function submit() {
  if (!problem.value?.contentAvailable) return
  submitting.value = true
  try {
    result.value = (await api.post<Submission>('/submissions', { problemId: problem.value.id, sourceCode: code.value })).data
    if (result.value.status === 'ACCEPTED') ElMessage.success('通过！这一步已经掌握。')
  } catch (error: any) {
    ElMessage.error(error?.response?.data?.message || '提交失败，请检查后端服务。')
  } finally { submitting.value = false }
}
</script>

<template>
  <div v-if="!problem" class="loading">正在打开题目工作台…</div>
  <section v-else class="workspace">
    <article class="problem-pane">
      <div class="meta-row"><span class="pill" :class="problem.difficulty.toLowerCase()">{{ difficultyText[problem.difficulty] }}</span><span class="source-chip">来源：{{ problem.sourceLabel }}</span></div>
      <h2>{{ problem.title }}</h2>
      <p style="color: var(--muted); line-height: 1.7">{{ problem.summary }}</p>
      <el-alert v-if="!problem.contentAvailable" title="版权内容未导入" type="warning" :closable="false" description="当前仅保留来源和难度元数据；取得授权前不会展示题面或测试数据。" show-icon />
      <pre v-else class="description">{{ problem.descriptionMd }}</pre>
      <div v-if="problem.contentAvailable" class="sample-grid">
        <div class="sample"><small>样例输入</small><pre>{{ problem.sampleInput }}</pre></div>
        <div class="sample"><small>样例输出</small><pre>{{ problem.sampleOutput }}</pre></div>
      </div>
      <div class="meta-row" style="margin-top: 20px"><span class="pill">{{ problem.timeLimitMs }} ms</span><span class="pill">{{ problem.memoryLimitMb }} MB</span><span v-for="tag in problem.tags" :key="tag" class="pill">{{ tag }}</span></div>
    </article>
    <div class="editor-pane">
      <div class="editor-toolbar"><span>Solution.java · Java 17</span><span class="editor-dot"></span></div>
      <VueMonacoEditor v-model:value="code" language="java" theme="vs-dark" :options="{ fontSize: 14, minimap: { enabled: false }, automaticLayout: true, tabSize: 4, padding: { top: 16 } }" />
      <div class="submit-bar">
        <div class="result-box"><b>{{ resultTitle }}</b><p>{{ result?.message || '代码只会发送到隔离的 Judge0 服务，不会在业务服务器执行。' }}</p></div>
        <el-button type="primary" color="#ef6b3b" :loading="submitting" :disabled="!problem.contentAvailable" @click="submit">提交评测</el-button>
      </div>
    </div>
  </section>
</template>
