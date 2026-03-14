<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
const router = useRouter()
const route = useRoute()
const menuItems = [
  { icon: '📋', title: '体质辨识', desc: '完成体质问卷获取个性化建议' },
  { icon: '⭐', title: '收藏处方', desc: '查看收藏的处方' },
  { icon: '📝', title: '健康档案', desc: '管理个人健康信息' },
  { icon: '❓', title: '帮助中心', desc: '常见问题解答' },
  { icon: '📞', title: '联系客服', desc: '在线咨询' }
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
  <div class="mobile-page">
    <div class="card" style="display:flex;justify-content:space-between;align-items:center">
      <div style="display:flex;align-items:center;gap:12px">
        <div style="width:56px;height:56px;border-radius:50%;background:#e8f5e9;display:flex;align-items:center;justify-content:center;font-size:24px">👤</div>
        <div>
          <div style="font-size:18px;font-weight:700">用户</div>
          <div class="success" style="font-size:13px;margin-top:4px">体质：平和质</div>
        </div>
      </div>
      <button class="btn btn-secondary">编辑</button>
    </div>

    <div class="card">
      <div class="list-item" v-for="item in menuItems" :key="item.title">
        <div style="display:flex;align-items:center;gap:12px">
          <span style="font-size:22px">{{ item.icon }}</span>
          <div>
            <div>{{ item.title }}</div>
            <div class="muted" style="font-size:12px;margin-top:4px">{{ item.desc }}</div>
          </div>
        </div>
        <span class="muted">›</span>
      </div>
    </div>

    <div class="warn-box">本平台仅供中医药知识参考学习，不构成医疗诊断建议。</div>
    <div class="muted" style="text-align:center;margin-top:14px;font-size:12px">版本 1.0.0</div>
    <nav class="bottom-nav">
      <a v-for="item in navs" :key="item.path" href="javascript:;" :class="{ active: route.path === item.path }" @click="router.push(item.path)"><span>{{ item.icon }}</span><span>{{ item.text }}</span></a>
    </nav>
  </div>
</template>
