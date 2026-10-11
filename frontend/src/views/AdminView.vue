<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { api } from '@/api'

interface Audit { total: number; publishable: number; blocked: number; byLicense: Record<string, number> }
const audit = ref<Audit>({ total: 0, publishable: 0, blocked: 0, byLicense: {} })
onMounted(async () => { audit.value = (await api.get<Audit>('/admin/content-audit')).data })
</script>

<template>
  <div class="stack">
    <div class="section-head"><div><h2>内容治理后台</h2><p>MVP 仅提供只读审核看板；写操作将在 RBAC 与审计日志完成后开放。</p></div><span class="pill blocked">管理员视图</span></div>
    <div class="stat-row">
      <div class="stat-card"><span class="label">内容条目</span><div class="value">{{ audit.total }}</div><span class="hint">全部</span></div>
      <div class="stat-card"><span class="label">可发布</span><div class="value">{{ audit.publishable }}</div><span class="hint">原创或已授权</span></div>
      <div class="stat-card"><span class="label">阻止发布</span><div class="value">{{ audit.blocked }}</div><span class="hint">等待审核</span></div>
      <div class="stat-card"><span class="label">授权覆盖</span><div class="value">{{ audit.total ? Math.round(audit.publishable / audit.total * 100) : 0 }}%</div><span class="hint">内容安全线</span></div>
    </div>
    <div class="audit-grid">
      <section class="card card-pad">
        <div class="section-head"><div><h3>授权状态分布</h3><p>数据库实时统计</p></div></div>
        <div class="policy-list"><div v-for="(count, key) in audit.byLicense" :key="key" class="policy-item"><b>{{ key }}</b><span>{{ count }} 个条目</span></div></div>
      </section>
      <section class="card card-pad">
        <div class="section-head"><div><h3>发布规则</h3><p>任何导入任务都必须经过这条边界</p></div></div>
        <div class="policy-list">
          <div class="policy-item"><b>ORIGINAL / AUTHORIZED</b><span>允许展示题面；授权内容需保存许可依据与范围。</span></div>
          <div class="policy-item"><b>LINK_ONLY / METADATA_ONLY</b><span>只展示必要元数据和外部链接，不缓存原题与测试数据。</span></div>
          <div class="policy-item" style="border-color:#ef6b3b"><b>RESTRICTED</b><span>禁止前台发布，并记录下架原因与审核人。</span></div>
        </div>
      </section>
    </div>
  </div>
</template>
