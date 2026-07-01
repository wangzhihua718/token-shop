<template>
  <div class="admin-container">
    <el-card>
      <template #header>管理后台</template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="用户管理" name="users">
          <p>用户管理功能开发中...</p>
        </el-tab-pane>
        <el-tab-pane label="Token审核" name="tokens">
          <p>Token审核功能开发中...</p>
        </el-tab-pane>
        <el-tab-pane label="订单管理" name="orders">
          <p>订单管理功能开发中...</p>
        </el-tab-pane>
        <el-tab-pane label="数据统计" name="stats">
          <p>数据统计功能开发中...</p>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const activeTab = ref('users')
</script>

<style scoped>
.admin-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
</style>
