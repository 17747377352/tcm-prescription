<script setup lang="ts">
import { ref } from 'vue'

const userInfo = ref({
  nickname: '用户',
  avatar: '',
  constitution: '平和质'
})

const menuItems = [
  { icon: '📋', title: '体质辨识', desc: '完成体质问卷获取个性化建议' },
  { icon: '⭐', title: '收藏处方', desc: '查看收藏的处方' },
  { icon: '📝', title: '健康档案', desc: '管理个人健康信息' },
  { icon: '❓', title: '帮助中心', desc: '常见问题解答' },
  { icon: '📞', title: '联系客服', desc: '在线咨询' }
]
</script>

<template>
  <view class="container">
    <!-- 用户信息 -->
    <view class="user-card">
      <view class="user-info">
        <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'" />
        <view class="info">
          <text class="name">{{ userInfo.nickname }}</text>
          <text class="constitution">体质：{{ userInfo.constitution }}</text>
        </view>
      </view>
      <text class="edit-btn">编辑</text>
    </view>
    
    <!-- 菜单列表 -->
    <view class="menu-list">
      <view v-for="item in menuItems" :key="item.title" class="menu-item">
        <text class="icon">{{ item.icon }}</text>
        <view class="content">
          <text class="title">{{ item.title }}</text>
          <text class="desc">{{ item.desc }}</text>
        </view>
        <text class="arrow">›</text>
      </view>
    </view>
    
    <!-- 免责声明 -->
    <view class="disclaimer">
      <text>本平台仅供中医药知识参考学习，不构成医疗诊断建议。</text>
    </view>
    
    <!-- 版本信息 -->
    <view class="version">
      <text>版本 1.0.0</text>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 120rpx;
}

.user-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  
  .user-info {
    display: flex;
    align-items: center;
    gap: 20rpx;
    
    .avatar {
      width: 100rpx;
      height: 100rpx;
      border-radius: 50%;
      background: #e8f5e9;
    }
    
    .info {
      .name {
        display: block;
        font-size: 34rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 8rpx;
      }
      
      .constitution {
        font-size: 26rpx;
        color: #67C23A;
      }
    }
  }
  
  .edit-btn {
    font-size: 26rpx;
    color: #67C23A;
    padding: 10rpx 20rpx;
    background: #e8f5e9;
    border-radius: 30rpx;
  }
}

.menu-list {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  
  .menu-item {
    display: flex;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .icon {
      font-size: 40rpx;
      margin-right: 20rpx;
    }
    
    .content {
      flex: 1;
      
      .title {
        display: block;
        font-size: 30rpx;
        color: #333;
        margin-bottom: 4rpx;
      }
      
      .desc {
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .arrow {
      font-size: 36rpx;
      color: #ccc;
    }
  }
}

.disclaimer {
  margin-top: 40rpx;
  padding: 20rpx;
  text-align: center;
  
  text {
    font-size: 24rpx;
    color: #999;
  }
}

.version {
  text-align: center;
  margin-top: 20rpx;
  
  text {
    font-size: 24rpx;
    color: #ccc;
  }
}
</style>