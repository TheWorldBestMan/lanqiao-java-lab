<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { DataAnalysis, Document, EditPen, Grid, Histogram, House, Menu as MenuIcon, Notebook, Reading } from '@element-plus/icons-vue'

const route = useRoute()
const collapsed = ref(false)
const items = [
  { to: '/', label: '学习总览', icon: House },
  { to: '/learn', label: '课程知识树', icon: Reading },
  { to: '/problems', label: '经典题库', icon: EditPen },
  { to: '/wrong-book', label: '错题本', icon: Notebook },
  { to: '/exams', label: '模拟考试', icon: Document },
  { to: '/analytics', label: '学习分析', icon: DataAnalysis },
  { to: '/admin', label: '内容管理', icon: Grid },
]
const currentTitle = computed(() => route.meta.title as string)
</script>

<template>
  <div class="app-shell" :class="{ 'is-collapsed': collapsed }">
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-mark">J</div>
        <div v-if="!collapsed">
          <strong>LanQiao</strong>
          <span>Java Lab</span>
        </div>
      </div>
      <nav>
        <RouterLink v-for="item in items" :key="item.to" :to="item.to" class="nav-item">
          <el-icon><component :is="item.icon" /></el-icon>
          <span v-if="!collapsed">{{ item.label }}</span>
        </RouterLink>
      </nav>
      <div class="sidebar-note" v-if="!collapsed">
        <span class="tiny-label">本周目标</span>
        <b>完成 5 道题</b>
        <el-progress :percentage="60" :stroke-width="6" :show-text="false" color="#ef6b3b" />
        <small>已完成 3 / 5</small>
      </div>
    </aside>

    <main>
      <header class="topbar">
        <button class="icon-button" aria-label="折叠菜单" @click="collapsed = !collapsed"><el-icon><MenuIcon /></el-icon></button>
        <div>
          <span class="eyebrow">蓝桥杯 · Java 备赛实验室</span>
          <h1>{{ currentTitle }}</h1>
        </div>
        <div class="profile">
          <div class="streak"><span>🔥</span><b>4</b> 天连胜</div>
          <div class="avatar">林</div>
        </div>
      </header>
      <section class="page-content" :class="{ wide: route.meta.wide }">
        <RouterView />
      </section>
    </main>
  </div>
</template>
