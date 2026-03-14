<script setup lang="ts">
import { ref, reactive } from 'vue'

// 症状输入
const symptoms = ref('')
const selectedSymptoms = ref<string[]>([])

// 常见症状标签
const commonSymptoms = [
  '失眠', '头痛', '乏力', '心悸', '盗汗', '便秘', 
  '食欲不振', '咳嗽', '腰酸', '健忘', '多梦', '头晕'
]

// 舌象选项
const tongueOptions = [
  { label: '淡红舌', value: 'pale_red' },
  { label: '红舌', value: 'red' },
  { label: '紫舌', value: 'purple' },
  { label: '淡白舌', value: 'pale' }
]

const tongueType = ref('')
const pulseType = ref('')

// 脉象选项
const pulseOptions = [
  { label: '平脉', value: 'normal' },
  { label: '浮脉', value: 'floating' },
  { label: '沉脉', value: 'deep' },
  { label: '数脉', value: 'rapid' },
  { label: '迟脉', value: 'slow' },
  { label: '弦脉', value: 'wiry' }
]

// 添加症状标签
const addSymptom = (symptom: string) => {
  if (!selectedSymptoms.value.includes(symptom)) {
    selectedSymptoms.value.push(symptom)
  }
}

// 移除症状
const removeSymptom = (index: number) => {
  selectedSymptoms.value.splice(index, 1)
}

// 提交问诊
const loading = ref(false)
const submitDiagnosis = () => {
  if (selectedSymptoms.value.length === 0) {
    uni.showToast({ title: '请选择症状', icon: 'none' })
    return
  }
  
  loading.value = true
  
  // TODO: 调用 AI 辨证接口
  setTimeout(() => {
    loading.value = false
    uni.showModal({
      title: '辨证结果',
      content: '根据您描述的症状，初步判断为：心脾两虚证\n\n建议方剂：归脾汤加减',
      showCancel: false
    })
  }, 2000)
}
</script>

<template>
  <view class="container">
    <!-- 症状输入区 -->
    <view class="section">
      <view class="section-title">请描述您的主要症状</view>
      
      <!-- 已选症状 -->
      <view class="selected-symptoms" v-if="selectedSymptoms.length > 0">
        <view 
          v-for="(item, index) in selectedSymptoms" 
          :key="item"
          class="symptom-tag active"
          @click="removeSymptom(index)"
        >
          {{ item }} ✕
        </view>
      </view>
      
      <!-- 常见症状标签 -->
      <view class="symptom-tags">
        <view 
          v-for="item in commonSymptoms" 
          :key="item"
          :class="['symptom-tag', selectedSymptoms.includes(item) ? 'selected' : '']"
          @click="addSymptom(item)"
        >
          {{ item }}
        </view>
      </view>
      
      <!-- 自定义输入 -->
      <textarea 
        v-model="symptoms"
        class="symptom-input"
        placeholder="或输入其他症状描述..."
      />
    </view>
    
    <!-- 舌象脉象 -->
    <view class="section">
      <view class="section-title">舌象（可选）</view>
      <view class="option-group">
        <view 
          v-for="item in tongueOptions" 
          :key="item.value"
          :class="['option-item', tongueType === item.value ? 'active' : '']"
          @click="tongueType = item.value"
        >
          {{ item.label }}
        </view>
      </view>
    </view>
    
    <view class="section">
      <view class="section-title">脉象（可选）</view>
      <view class="option-group">
        <view 
          v-for="item in pulseOptions" 
          :key="item.value"
          :class="['option-item', pulseType === item.value ? 'active' : '']"
          @click="pulseType = item.value"
        >
          {{ item.label }}
        </view>
      </view>
    </view>
    
    <!-- 提交按钮 -->
    <view class="submit-area">
      <button class="submit-btn" :loading="loading" @click="submitDiagnosis">
        开始辨证
      </button>
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

.section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }
}

.symptom-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  margin-bottom: 20rpx;
  
  .symptom-tag {
    padding: 16rpx 28rpx;
    background: #f5f5f5;
    border-radius: 30rpx;
    font-size: 28rpx;
    color: #666;
    
    &.selected {
      background: #67C23A;
      color: #fff;
    }
  }
}

.selected-symptoms {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-bottom: 20rpx;
  
  .symptom-tag.active {
    padding: 12rpx 20rpx;
    background: #e8f5e9;
    border-radius: 30rpx;
    font-size: 26rpx;
    color: #67C23A;
    border: 1rpx solid #67C23A;
  }
}

.symptom-input {
  width: 100%;
  height: 160rpx;
  padding: 20rpx;
  background: #f9f9f9;
  border-radius: 12rpx;
  font-size: 28rpx;
}

.option-group {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  
  .option-item {
    padding: 16rpx 32rpx;
    background: #f5f5f5;
    border-radius: 30rpx;
    font-size: 28rpx;
    color: #666;
    
    &.active {
      background: #67C23A;
      color: #fff;
    }
  }
}

.submit-area {
  margin-top: 40rpx;
  
  .submit-btn {
    width: 100%;
    height: 90rpx;
    background: linear-gradient(135deg, #67C23A, #85ce61);
    color: #fff;
    font-size: 32rpx;
    font-weight: bold;
    border-radius: 45rpx;
    border: none;
  }
}
</style>