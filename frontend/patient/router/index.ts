import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/home' },
    { path: '/home', component: () => import('@/pages/index/index.vue') },
    { path: '/diagnosis', component: () => import('@/pages/diagnosis/index.vue') },
    { path: '/prescribe', component: () => import('@/pages/prescribe/index.vue') },
    { path: '/history', component: () => import('@/pages/history/index.vue') },
    { path: '/profile', component: () => import('@/pages/profile/index.vue') }
  ]
})

export default router
