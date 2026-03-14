<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
const router = useRouter()
const route = useRoute()
const features = [
  { icon: '🩺', title: '智能问诊', desc: 'AI辅助辨证开方', path: '/diagnosis' },
  { icon: '🌿', title: '手动抓配', desc: '自主选择药材', path: '/prescribe' },
  { icon: '📋', title: '处方记录', desc: '查看历史处方', path: '/history' },
  { icon: '⚖️', title: '配伍审核', desc: '安全性检查', path: '/prescribe' }
]
const navs = [
  { text: '首页', path: '/home', icon: '🏠' },
  { text: '问诊', path: '/diagnosis', icon: '🩺' },
  { text: '抓配', path: '/prescribe', icon: '🌿' },
  { text: '记录', path: '/history', icon: '📋' },
  { text: '我的', path: '/profile', icon: '👤' }
]
</script>

<template>
  <div>
    <div class="top-banner">
      <div style="font-size:22px;font-weight:700;margin-bottom:8px">中药智能抓配平台</div>
      <div style="font-size:14px;opacity:.92">AI辨证 · 安全配伍 · 专业可靠</div>
    </div>
    <div class="feature-grid">
      <div class="feature-item" v-for="item in features" :key="item.title" @click="router.push(item.path)">
        <div class="icon">{{ item.icon }}</div>
        <div style="font-size:16px;font-weight:700;margin:10px 0 6px">{{ item.title }}</div>
        <div class="muted" style="font-size:13px">{{ item.desc }}</div>
      </div>
    </div>
    <div class="mobile-page">
      <div class="warn-box">⚠️ 本平台仅供中医药知识参考学习，不构成医疗诊断建议。如有疾病请前往正规医疗机构就诊。</div>
    </div>
    <nav class="bottom-nav">
      <a v-for="item in navs" :key="item.path" href="javascript:;" :class="{ active: route.path === item.path }" @click="router.push(item.path)">
        <span>{{ item.icon }}</span><span>{{ item.text }}</span>
      </a>
    </nav>
  </div>
</template>
