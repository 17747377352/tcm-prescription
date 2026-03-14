<script setup lang="ts">
import { ref } from 'vue'

const prescriptions = ref([
  {
    id: 1,
    date: '2024-03-14',
    type: 'AI开方',
    syndrome: '心脾两虚证',
    formula: '归脾汤加减',
    herbs: ['人参', '黄芪', '白术', '茯苓', '龙眼肉'],
    status: '有效'
  },
  {
    id: 2,
    date: '2024-03-10',
    type: '手动抓配',
    syndrome: '-',
    formula: '自拟方',
    herbs: ['枸杞子', '菊花', '决明子'],
    status: '有效'
  }
])
</script>

<template>
  <view class="container">
    <view class="prescription-list">
      <view v-for="item in prescriptions" :key="item.id" class="prescription-card">
        <view class="card-header">
          <text class="type">{{ item.type }}</text>
          <text class="date">{{ item.date }}</text>
        </view>
        
        <view class="card-body">
          <view class="row" v-if="item.syndrome !== '-'">
            <text class="label">辨证：</text>
            <text class="value">{{ item.syndrome }}</text>
          </view>
          <view class="row">
            <text class="label">方剂：</text>
            <text class="value">{{ item.formula }}</text>
          </view>
          <view class="row">
            <text class="label">组成：</text>
            <text class="value herbs">{{ item.herbs.join('、') }}</text>
          </view>
        </view>
        
        <view class="card-footer">
          <text :class="['status', item.status === '有效' ? 'active' : '']">
            {{ item.status }}
          </text>
          <text class="action">查看详情 →</text>
        </view>
      </view>
    </view>
    
    <view class="empty" v-if="prescriptions.length === 0">
      <text>暂无处方记录</text>
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

.prescription-list {
  .prescription-card {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 24rpx;
      background: #f9f9f9;
      
      .type {
        font-size: 26rpx;
        color: #67C23A;
        font-weight: bold;
      }
      
      .date {
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .card-body {
      padding: 24rpx;
      
      .row {
        display: flex;
        margin-bottom: 12rpx;
        
        .label {
          font-size: 28rpx;
          color: #999;
          width: 120rpx;
        }
        
        .value {
          font-size: 28rpx;
          color: #333;
          flex: 1;
          
          &.herbs {
            color: #67C23A;
          }
        }
      }
    }
    
    .card-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20rpx 24rpx;
      border-top: 1rpx solid #f5f5f5;
      
      .status {
        font-size: 24rpx;
        padding: 8rpx 16rpx;
        border-radius: 20rpx;
        background: #f5f5f5;
        color: #999;
        
        &.active {
          background: #e8f5e9;
          color: #67C23A;
        }
      }
      
      .action {
        font-size: 26rpx;
        color: #67C23A;
      }
    }
  }
}

.empty {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
}
</style>