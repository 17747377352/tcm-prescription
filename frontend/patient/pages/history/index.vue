<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { request } from '@/utils/request'
const router = useRouter()
const route = useRoute()
const prescriptions = ref<any[]>([])
const navs = [
  { text: '首页', path: '/home', icon: '🏠' },
  { text: '问诊', path: '/diagnosis', icon: '🩺' },
  { text: '抓配', path: '/prescribe', icon: '🌿' },
  { text: '记录', path: '/history', icon: '📋' },
  { text: '我的', path: '/profile', icon: '👤' }
]
const loadData = async () => {
  const res: any = await request({ url: '/prescription/history', method: 'GET' })
  prescriptions.value = (res.data || []).map((item: any) => {
    let composition: any = []
    try { composition = JSON.parse(item.composition || '[]') } catch { composition = [] }
    return {
      id: item.id,
      date: item.created_at,
      type: Number(item.type) === 1 ? 'AI开方' : '手动抓配',
      formula: Array.isArray(composition) ? '自拟方' : (composition.formula || 'AI推荐方'),
      syndrome: Array.isArray(composition) ? '-' : (composition.syndrome || '-'),
      herbs: Array.isArray(composition) ? composition.map((x: any) => x.name) : (composition.symptoms || []),
      status: '有效'
    }
  })
}
onMounted(loadData)
</script>

<template>
  <div class="mobile-page">
    <div class="card" v-for="item in prescriptions" :key="item.id">
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:10px">
        <strong class="success">{{ item.type }}</strong>
        <span class="muted" style="font-size:12px">{{ item.date }}</span>
      </div>
      <div style="line-height:1.9;font-size:14px">
        <div v-if="item.syndrome !== '-'"><span class="muted">辨证：</span>{{ item.syndrome }}</div>
        <div><span class="muted">方剂：</span>{{ item.formula }}</div>
        <div><span class="muted">组成：</span>{{ item.herbs.join('、') }}</div>
      </div>
    </div>
    <div class="card muted" v-if="prescriptions.length===0">暂无处方记录</div>
    <nav class="bottom-nav">
      <a v-for="item in navs" :key="item.path" href="javascript:;" :class="{ active: route.path === item.path }" @click="router.push(item.path)"><span>{{ item.icon }}</span><span>{{ item.text }}</span></a>
    </nav>
  </div>
</template>
