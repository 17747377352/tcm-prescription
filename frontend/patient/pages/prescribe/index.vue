<script setup lang="ts">
import { ref, computed } from 'vue'

// 搜索关键词
const searchKeyword = ref('')

// 已选药材
const selectedHerbs = ref<{ name: string; dosage: number }[]>([])

// 药材搜索结果
const searchResults = ref<any[]>([])

// 配伍检查结果
const compatibilityResult = ref<{
  safe: boolean;
  errors: string[];
  warnings: string[];
} | null>(null)

// 搜索药材
const handleSearch = () => {
  if (!searchKeyword.value) {
    searchResults.value = []
    return
  }
  
  // TODO: 调用搜索接口
  searchResults.value = [
    { id: 1, name: '人参', nature: '温', flavor: '甘', dosage: '3-9g' },
    { id: 2, name: '黄芪', nature: '温', flavor: '甘', dosage: '9-30g' },
    { id: 3, name: '白术', nature: '温', flavor: '苦', dosage: '6-12g' }
  ].filter(item => item.name.includes(searchKeyword.value))
}

// 添加药材
const addHerb = (herb: any) => {
  if (!selectedHerbs.value.find(h => h.name === herb.name)) {
    selectedHerbs.value.push({ name: herb.name, dosage: 9 })
  }
}

// 移除药材
const removeHerb = (index: number) => {
  selectedHerbs.value.splice(index, 1)
}

// 提交审核
const submitForReview = () => {
  if (selectedHerbs.value.length === 0) {
    uni.showToast({ title: '请选择药材', icon: 'none' })
    return
  }
  
  // TODO: 调用配伍检查接口
  compatibilityResult.value = {
    safe: true,
    errors: [],
    warnings: ['【毒性提示】附子 为有毒药材，用量需严格控制(3-15g)']
  }
  
  uni.showModal({
    title: '配伍审核',
    content: compatibilityResult.value.safe 
      ? '配伍检查通过，未发现禁忌问题' 
      : '存在配伍禁忌，请调整处方',
    showCancel: false
  })
}
</script>

<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input 
        v-model="searchKeyword"
        class="search-input"
        placeholder="搜索药材名称/功效..."
        @confirm="handleSearch"
      />
      <button class="search-btn" @click="handleSearch">搜索</button>
    </view>
    
    <!-- 搜索结果 -->
    <view class="search-results" v-if="searchResults.length > 0">
      <view 
        v-for="item in searchResults" 
        :key="item.id"
        class="result-item"
        @click="addHerb(item)"
      >
        <view class="herb-info">
          <text class="name">{{ item.name }}</text>
          <text class="attr">{{ item.nature }} | {{ item.flavor }}</text>
          <text class="dosage">用量: {{ item.dosage }}</text>
        </view>
        <text class="add-btn">+ 添加</text>
      </view>
    </view>
    
    <!-- 已选药材 -->
    <view class="selected-section" v-if="selectedHerbs.length > 0">
      <view class="section-header">
        <text class="title">已选药材 ({{ selectedHerbs.length }}味)</text>
        <text class="clear" @click="selectedHerbs = []">清空</text>
      </view>
      
      <view class="herb-list">
        <view v-for="(item, index) in selectedHerbs" :key="item.name" class="herb-item">
          <view class="herb-detail">
            <text class="name">{{ item.name }}</text>
            <input 
              v-model.number="item.dosage"
              class="dosage-input"
              type="number"
            />
            <text class="unit">克</text>
          </view>
          <text class="remove-btn" @click="removeHerb(index)">✕</text>
        </view>
      </view>
      
      <!-- 提交审核 -->
      <button class="submit-btn" @click="submitForReview">
        提交配伍审核
      </button>
    </view>
    
    <!-- 空状态 -->
    <view class="empty" v-if="selectedHerbs.length === 0 && searchResults.length === 0">
      <text class="empty-text">搜索并添加药材开始抓配</text>
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

.search-bar {
  display: flex;
  gap: 16rpx;
  margin-bottom: 20rpx;
  
  .search-input {
    flex: 1;
    height: 80rpx;
    padding: 0 24rpx;
    background: #fff;
    border-radius: 40rpx;
    font-size: 28rpx;
  }
  
  .search-btn {
    width: 140rpx;
    height: 80rpx;
    background: #67C23A;
    color: #fff;
    font-size: 28rpx;
    border-radius: 40rpx;
    border: none;
  }
}

.search-results {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  
  .result-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx;
    border-bottom: 1rpx solid #eee;
    
    &:last-child {
      border-bottom: none;
    }
    
    .herb-info {
      .name {
        display: block;
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 8rpx;
      }
      
      .attr, .dosage {
        font-size: 24rpx;
        color: #999;
        margin-right: 16rpx;
      }
    }
    
    .add-btn {
      padding: 12rpx 24rpx;
      background: #e8f5e9;
      color: #67C23A;
      font-size: 26rpx;
      border-radius: 30rpx;
    }
  }
}

.selected-section {
  margin-top: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    
    .title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }
    
    .clear {
      font-size: 26rpx;
      color: #999;
    }
  }
  
  .herb-list {
    .herb-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #f5f5f5;
      
      .herb-detail {
        display: flex;
        align-items: center;
        gap: 16rpx;
        
        .name {
          font-size: 30rpx;
          color: #333;
          width: 120rpx;
        }
        
        .dosage-input {
          width: 100rpx;
          height: 60rpx;
          background: #f5f5f5;
          border-radius: 8rpx;
          text-align: center;
          font-size: 28rpx;
        }
        
        .unit {
          font-size: 26rpx;
          color: #999;
        }
      }
      
      .remove-btn {
        font-size: 28rpx;
        color: #999;
        padding: 10rpx;
      }
    }
  }
  
  .submit-btn {
    width: 100%;
    height: 88rpx;
    margin-top: 30rpx;
    background: linear-gradient(135deg, #67C23A, #85ce61);
    color: #fff;
    font-size: 32rpx;
    font-weight: bold;
    border-radius: 44rpx;
    border: none;
  }
}

.empty {
  text-align: center;
  padding: 100rpx 0;
  
  .empty-text {
    font-size: 28rpx;
    color: #999;
  }
}
</style>