<script setup lang="ts">
import { ref } from 'vue'

const features = [
  { icon: '🩺', title: '智能问诊', desc: 'AI辅助辨证开方', path: '/pages/diagnosis/index' },
  { icon: '🌿', title: '手动抓配', desc: '自主选择药材', path: '/pages/prescribe/index' },
  { icon: '📋', title: '处方记录', desc: '查看历史处方', path: '/pages/history/index' },
  { icon: '⚖️', title: '配伍审核', desc: '安全性检查', path: '/pages/prescribe/index' }
]
</script>

<template>
  <view class="container">
    <!-- 顶部横幅 -->
    <view class="banner">
      <view class="banner-content">
        <text class="title">中药智能抓配平台</text>
        <text class="subtitle">AI辨证 · 安全配伍 · 专业可靠</text>
      </view>
    </view>
    
    <!-- 功能入口 -->
    <view class="features">
      <view 
        v-for="item in features" 
        :key="item.title"
        class="feature-item"
        @click="uni.navigateTo({ url: item.path })"
      >
        <text class="icon">{{ item.icon }}</text>
        <text class="title">{{ item.title }}</text>
        <text class="desc">{{ item.desc }}</text>
      </view>
    </view>
    
    <!-- 免责声明 -->
    <view class="disclaimer">
      <text>⚠️ 本平台仅供中医药知识参考学习，不构成医疗诊断建议。如有疾病请前往正规医疗机构就诊。</text>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.banner {
  background: linear-gradient(135deg, #67C23A, #85ce61);
  padding: 60rpx 30rpx;
  
  .banner-content {
    color: #fff;
    
    .title {
      display: block;
      font-size: 44rpx;
      font-weight: bold;
      margin-bottom: 16rpx;
    }
    
    .subtitle {
      font-size: 28rpx;
      opacity: 0.9;
    }
  }
}

.features {
  display: flex;
  flex-wrap: wrap;
  padding: 30rpx;
  gap: 20rpx;
  
  .feature-item {
    width: calc(50% - 10rpx);
    background: #fff;
    border-radius: 16rpx;
    padding: 40rpx 30rpx;
    text-align: center;
    box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.05);
    
    .icon {
      font-size: 60rpx;
      display: block;
      margin-bottom: 16rpx;
    }
    
    .title {
      display: block;
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 8rpx;
    }
    
    .desc {
      font-size: 24rpx;
      color: #999;
    }
  }
}

.disclaimer {
  margin: 30rpx;
  padding: 20rpx;
  background: #fff9e6;
  border-radius: 12rpx;
  border: 1rpx solid #ffc107;
  
  text {
    font-size: 24rpx;
    color: #856404;
    line-height: 1.6;
  }
}
</style>