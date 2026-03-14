import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台' }
      },
      {
        path: 'herbs',
        name: 'Herbs',
        component: () => import('@/views/herbs/index.vue'),
        meta: { title: '药材库管理' }
      },
      {
        path: 'formulas',
        name: 'Formulas',
        component: () => import('@/views/formulas/index.vue'),
        meta: { title: '方剂库管理' }
      },
      {
        path: 'prescriptions',
        name: 'Prescriptions',
        component: () => import('@/views/prescriptions/index.vue'),
        meta: { title: '处方记录' }
      },
      {
        path: 'consultations',
        name: 'Consultations',
        component: () => import('@/views/consultations/index.vue'),
        meta: { title: '问诊记录' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/users/index.vue'),
        meta: { title: '用户管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router