import { createRouter, createWebHashHistory } from 'vue-router'
import DashboardView from '@/views/DashboardView.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', name: 'dashboard', component: DashboardView, meta: { title: '学习总览' } },
    { path: '/learn', name: 'learn', component: () => import('@/views/KnowledgeView.vue'), meta: { title: '课程知识树' } },
    { path: '/problems', name: 'problems', component: () => import('@/views/ProblemsView.vue'), meta: { title: '经典题库' } },
    { path: '/problems/:slug', name: 'problem-detail', component: () => import('@/views/ProblemDetailView.vue'), meta: { title: '在线刷题', wide: true } },
    { path: '/wrong-book', name: 'wrong-book', component: () => import('@/views/WrongBookView.vue'), meta: { title: '错题本' } },
    { path: '/exams', name: 'exams', component: () => import('@/views/ExamsView.vue'), meta: { title: '模拟考试' } },
    { path: '/analytics', name: 'analytics', component: () => import('@/views/AnalyticsView.vue'), meta: { title: '学习分析' } },
    { path: '/admin', name: 'admin', component: () => import('@/views/AdminView.vue'), meta: { title: '内容管理' } },
  ],
  scrollBehavior: () => ({ top: 0 }),
})

export default router

