<template>
  <div class="herbs-page">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="药材名称">
          <el-input v-model="searchForm.keyword" placeholder="输入药材名称搜索" clearable />
        </el-form-item>
        <el-form-item label="四气">
          <el-select v-model="searchForm.nature" placeholder="全部" clearable>
            <el-option label="寒" value="寒" />
            <el-option label="热" value="热" />
            <el-option label="温" value="温" />
            <el-option label="凉" value="凉" />
            <el-option label="平" value="平" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 表格 -->
    <el-card>
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="name" label="药材名称" width="120" />
        <el-table-column prop="nameLatin" label="拉丁名" width="180" />
        <el-table-column prop="nature" label="四气" width="80">
          <template #default="{ row }">
            <el-tag :type="getNatureType(row.nature)">{{ row.nature }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="flavor" label="五味" width="120" />
        <el-table-column prop="meridian" label="归经" width="150" />
        <el-table-column prop="efficacy" label="功效" show-overflow-tooltip />
        <el-table-column prop="toxicityLevel" label="毒性" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.toxicityLevel === 0" type="success">无毒</el-tag>
            <el-tag v-else-if="row.toxicityLevel === 1" type="warning">小毒</el-tag>
            <el-tag v-else-if="row.toxicityLevel === 2" type="danger">有毒</el-tag>
            <el-tag v-else type="danger">大毒</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref<any[]>([])

const searchForm = reactive({
  keyword: '',
  nature: ''
})

const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

const getNatureType = (nature: string) => {
  const map: Record<string, string> = {
    '寒': 'info',
    '凉': 'info',
    '平': '',
    '温': 'warning',
    '热': 'danger'
  }
  return map[nature] || ''
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  searchForm.keyword = ''
  searchForm.nature = ''
  handleSearch()
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/herbs/search', {
      params: {
        keyword: searchForm.keyword
      }
    })
    tableData.value = res.data || []
    pagination.total = tableData.value.length
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleView = (row: any) => {
  console.log('查看', row)
}

const handleEdit = (row: any) => {
  console.log('编辑', row)
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.herbs-page {
  .search-card {
    margin-bottom: 20px;
  }
  
  .el-pagination {
    margin-top: 20px;
    justify-content: flex-end;
  }
}
</style>