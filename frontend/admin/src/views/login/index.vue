<template>
  <div class="login-container">
    <div class="login-card">
      <h2>中药智能抓配平台</h2>
      <p class="subtitle">管理端登录</p>
      
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" prefix-icon="User" placeholder="用户名" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" prefix-icon="Lock" type="password" placeholder="密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  
  // TODO: 调用实际登录接口
  setTimeout(() => {
    localStorage.setItem('token', 'demo-token')
    ElMessage.success('登录成功')
    router.push('/')
    loading.value = false
  }, 1000)
}
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);
  
  .login-card {
    width: 400px;
    padding: 40px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 20px 60px rgba(0,0,0,0.1);
    
    h2 {
      text-align: center;
      color: #303133;
      margin-bottom: 8px;
    }
    
    .subtitle {
      text-align: center;
      color: #909399;
      margin-bottom: 30px;
    }
  }
}
</style>