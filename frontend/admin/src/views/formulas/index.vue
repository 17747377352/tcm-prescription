<template>
  <div class="formula-page">
    <el-card class="search-card">
      <el-form :inline="true">
        <el-form-item label="方剂名称">
          <el-input v-model="keyword" placeholder="搜索方剂名称" clearable @keyup.enter="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="name" label="方剂名称" min-width="180" />
        <el-table-column prop="source" label="出处" width="180" />
        <el-table-column prop="principle" label="治则" width="180" />
        <el-table-column prop="indication" label="主治" show-overflow-tooltip />
        <el-table-column prop="created_at" label="创建时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import request from '@/utils/request'

const keyword = ref('')
const loading = ref(false)
const tableData = ref<any[]>([])

const loadData = async () => {
  try {
    loading.value = true
    const res = await request.get('/formulas/list', { params: { keyword: keyword.value } })
    tableData.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.search-card { margin-bottom: 20px; }
</style>
