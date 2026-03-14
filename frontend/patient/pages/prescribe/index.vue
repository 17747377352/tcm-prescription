<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { request } from '@/utils/request'
const router = useRouter()
const route = useRoute()
const searchKeyword = ref('')
const selectedHerbs = ref<{ name: string; dosage: number }[]>([])
const searchResults = ref<any[]>([])
const compatibilityResult = ref<any>(null)
const checking = ref(false)
const navs = [
  { text: '首页', path: '/home', icon: '🏠' },
  { text: '问诊', path: '/diagnosis', icon: '🩺' },
  { text: '抓配', path: '/prescribe', icon: '🌿' },
  { text: '记录', path: '/history', icon: '📋' },
  { text: '我的', path: '/profile', icon: '👤' }
]
const handleSearch = async () => {
  if (!searchKeyword.value) return (searchResults.value = [])
  const res: any = await request({ url: `/herbs/search?keyword=${encodeURIComponent(searchKeyword.value)}`, method: 'GET' })
  searchResults.value = res.data || []
}
const addHerb = (herb: any) => { if (!selectedHerbs.value.find(h => h.name === herb.name)) selectedHerbs.value.push({ name: herb.name, dosage: Number(herb.dosageMin || 9) }) }
const removeHerb = (index: number) => selectedHerbs.value.splice(index, 1)
const submitForReview = async () => {
  if (selectedHerbs.value.length === 0) return alert('请选择药材')
  checking.value = true
  try {
    const res: any = await request({ url: '/prescription/check-compatibility', method: 'POST', data: { herbs: selectedHerbs.value } })
    compatibilityResult.value = res.data
    await request({ url: '/prescription/save-demo', method: 'POST', data: { type: '手动抓配', composition: JSON.stringify(selectedHerbs.value) } })
    alert(compatibilityResult.value.safe ? `审核通过，评分：${compatibilityResult.value.score}` : `存在风险，评分：${compatibilityResult.value.score}`)
    router.push('/history')
  } finally { checking.value = false }
}
</script>

<template>
  <div class="mobile-page">
    <div class="card" style="display:flex;gap:12px;align-items:center">
      <input v-model="searchKeyword" class="input" placeholder="搜索药材名称/功效..." @keyup.enter="handleSearch" />
      <button class="btn" @click="handleSearch">搜索</button>
    </div>

    <div class="card" v-if="searchResults.length">
      <div class="list-item" v-for="item in searchResults" :key="item.id">
        <div>
          <div style="font-weight:700">{{ item.name }}</div>
          <div class="muted" style="font-size:13px;margin-top:4px">{{ item.nature || '-' }} | {{ item.flavor || '-' }} | {{ item.dosageMin || '-' }}-{{ item.dosageMax || '-' }}g</div>
        </div>
        <button class="btn btn-secondary" @click="addHerb(item)">添加</button>
      </div>
    </div>

    <div class="card" v-if="selectedHerbs.length">
      <div class="section-title">已选药材（{{ selectedHerbs.length }}味）</div>
      <div class="list-item" v-for="(item,index) in selectedHerbs" :key="item.name">
        <div style="display:flex;align-items:center;gap:10px">
          <strong style="min-width:72px">{{ item.name }}</strong>
          <input v-model.number="item.dosage" type="number" class="input" style="width:88px;padding:8px 10px" />
          <span class="muted">克</span>
        </div>
        <button class="btn btn-secondary" @click="removeHerb(index)">删除</button>
      </div>

      <div class="warn-box" v-if="compatibilityResult" style="margin-top:12px">
        <div style="font-weight:700;margin-bottom:8px">审核评分：{{ compatibilityResult.score }}</div>
        <div v-for="item in compatibilityResult.errors" :key="item" class="danger">{{ item }}</div>
        <div v-for="item in compatibilityResult.warnings" :key="item">{{ item }}</div>
      </div>

      <button class="btn btn-block" style="margin-top:14px" :disabled="checking" @click="submitForReview">{{ checking ? '审核中...' : '提交配伍审核' }}</button>
    </div>

    <div class="card muted" v-if="selectedHerbs.length===0 && searchResults.length===0">搜索并添加药材开始抓配</div>
    <nav class="bottom-nav">
      <a v-for="item in navs" :key="item.path" href="javascript:;" :class="{ active: route.path === item.path }" @click="router.push(item.path)"><span>{{ item.icon }}</span><span>{{ item.text }}</span></a>
    </nav>
  </div>
</template>
