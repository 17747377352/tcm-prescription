<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon herbs">
              <el-icon><Guide /></el-icon>
            </div>
            <div class="stat-info">
              <div class="value">{{ stats.herbs }}</div>
              <div class="label">药材数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon formulas">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="value">{{ stats.formulas }}</div>
              <div class="label">方剂数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon prescriptions">
              <el-icon><Tickets /></el-icon>
            </div>
            <div class="stat-info">
              <div class="value">{{ stats.prescriptions }}</div>
              <div class="label">处方记录</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon users">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="value">{{ stats.users }}</div>
              <div class="label">注册用户</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 快捷操作 -->
    <el-card class="quick-actions">
      <template #header>
        <span>快捷操作</span>
      </template>
      <el-space wrap :size="20">
        <el-button type="primary" @click="$router.push('/herbs')">
          <el-icon><Plus /></el-icon>
          添加药材
        </el-button>
        <el-button type="success" @click="$router.push('/formulas')">
          <el-icon><Plus /></el-icon>
          添加方剂
        </el-button>
        <el-button type="warning" @click="initHerbs">
          <el-icon><Refresh /></el-icon>
          初始化药材数据
        </el-button>
      </el-space>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const stats = ref({
  herbs: 0,
  formulas: 0,
  prescriptions: 0,
  users: 0
})

const initHerbs = async () => {
  try {
    const res = await request.post('/herbs/init')
    ElMessage.success(res.message)
    loadStats()
  } catch (e) {
    ElMessage.error('初始化失败')
  }
}

const loadStats = async () => {
  // TODO: 调用实际接口
  stats.value = {
    herbs: 0,
    formulas: 0,
    prescriptions: 0,
    users: 0
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style lang="scss" scoped>
.dashboard {
  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      gap: 20px;
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28px;
        color: #fff;
        
        &.herbs { background: linear-gradient(135deg, #67C23A, #85ce61); }
        &.formulas { background: linear-gradient(135deg, #409EFF, #66b1ff); }
        &.prescriptions { background: linear-gradient(135deg, #E6A23C, #ebb563); }
        &.users { background: linear-gradient(135deg, #909399, #a6a9ad); }
      }
      
      .stat-info {
        .value {
          font-size: 28px;
          font-weight: bold;
          color: #303133;
        }
        .label {
          font-size: 14px;
          color: #909399;
          margin-top: 4px;
        }
      }
    }
  }
  
  .quick-actions {
    margin-top: 20px;
  }
}
</style>