<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { request } from '@/utils/request'
const router = useRouter()
const route = useRoute()
const symptoms = ref('')
const selectedSymptoms = ref<string[]>([])
const commonSymptoms = ['失眠', '头痛', '乏力', '心悸', '盗汗', '便秘', '食欲不振', '咳嗽', '腰酸', '健忘', '多梦', '头晕']
const tongueOptions = ['淡红舌', '红舌', '紫舌', '淡白舌']
const pulseOptions = ['平脉', '浮脉', '沉脉', '数脉', '迟脉', '弦脉']
const tongueType = ref('')
const pulseType = ref('')
const loading = ref(false)
const navs = [
  { text: '首页', path: '/home', icon: '🏠' },
  { text: '问诊', path: '/diagnosis', icon: '🩺' },
  { text: '抓配', path: '/prescribe', icon: '🌿' },
  { text: '记录', path: '/history', icon: '📋' },
  { text: '我的', path: '/profile', icon: '👤' }
]
const addSymptom = (symptom: string) => { if (!selectedSymptoms.value.includes(symptom)) selectedSymptoms.value.push(symptom) }
const removeSymptom = (index: number) => selectedSymptoms.value.splice(index, 1)
const buildSyndrome = () => {
  const merged = [...selectedSymptoms.value, ...symptoms.value.split(/[，,、\s]+/).filter(Boolean)]
  if (merged.some(x => ['失眠', '心悸', '健忘', '乏力', '多梦'].includes(x))) return { syndrome: '心脾两虚证', principle: '补益心脾，养血安神', formula: '归脾汤加减' }
  if (merged.some(x => ['头痛', '口苦', '急躁'].includes(x))) return { syndrome: '肝火上炎证', principle: '清肝泻火', formula: '龙胆泻肝汤加减' }
  return { syndrome: '气血两虚证', principle: '益气养血', formula: '八珍汤加减' }
}
const submitDiagnosis = async () => {
  if (selectedSymptoms.value.length === 0 && !symptoms.value.trim()) return alert('请填写症状')
  loading.value = true
  try {
    const diagnosis = buildSyndrome()
    await request({ url: '/prescription/save-demo', method: 'POST', data: { type: 'AI开方', composition: JSON.stringify({ syndrome: diagnosis.syndrome, principle: diagnosis.principle, formula: diagnosis.formula, symptoms: [...selectedSymptoms.value, symptoms.value].filter(Boolean), tongueType: tongueType.value, pulseType: pulseType.value }) } })
    alert(`初步辨证：${diagnosis.syndrome}\n治则：${diagnosis.principle}\n推荐方剂：${diagnosis.formula}`)
    router.push('/history')
  } finally { loading.value = false }
}
</script>

<template>
  <div class="mobile-page">
    <div class="card">
      <div class="section-title">请描述您的主要症状</div>
      <div class="chip-wrap" style="margin-bottom:12px" v-if="selectedSymptoms.length">
        <span class="chip active" v-for="(item,index) in selectedSymptoms" :key="item" @click="removeSymptom(index)">{{ item }} ✕</span>
      </div>
      <div class="chip-wrap" style="margin-bottom:12px">
        <span class="chip" :class="{active:selectedSymptoms.includes(item)}" v-for="item in commonSymptoms" :key="item" @click="addSymptom(item)">{{ item }}</span>
      </div>
      <textarea v-model="symptoms" class="textarea" placeholder="或输入其他症状描述..."></textarea>
    </div>

    <div class="card">
      <div class="section-title">舌象（可选）</div>
      <div class="chip-wrap"><span class="chip" :class="{active:tongueType===item}" v-for="item in tongueOptions" :key="item" @click="tongueType=item">{{ item }}</span></div>
    </div>

    <div class="card">
      <div class="section-title">脉象（可选）</div>
      <div class="chip-wrap"><span class="chip" :class="{active:pulseType===item}" v-for="item in pulseOptions" :key="item" @click="pulseType=item">{{ item }}</span></div>
    </div>

    <button class="btn btn-block" :disabled="loading" @click="submitDiagnosis">{{ loading ? '辨证中...' : '开始辨证' }}</button>
    <nav class="bottom-nav">
      <a v-for="item in navs" :key="item.path" href="javascript:;" :class="{ active: route.path === item.path }" @click="router.push(item.path)"><span>{{ item.icon }}</span><span>{{ item.text }}</span></a>
    </nav>
  </div>
</template>
