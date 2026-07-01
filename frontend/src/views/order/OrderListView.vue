<template>
  <div class="order-list-container">
    <div class="header-card glass-card">
      <h2>📦 我的订单</h2>
    </div>
    
    <div class="orders-card glass-card" style="margin-top: 20px">
      <el-table :data="orders" v-loading="loading" class="anime-table">
        <el-table-column prop="orderNo" label="订单号" />
        <el-table-column prop="totalAmount" label="总金额">
          <template #default="{ row }">
            <span class="amount">¥{{ row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="dark">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleViewDetail(row.id)">详情</el-button>
            <el-button
              v-if="row.status === 0"
              size="small"
              type="primary"
              @click="handlePay(row.id)"
            >支付</el-button>
            <el-button
              v-if="row.status === 0"
              size="small"
              type="danger"
              @click="handleCancel(row.id)"
            >取消</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        background
        @current-change="loadOrders"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, cancelOrder } from '@/api/order'

const router = useRouter()
const orders = ref<any[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadOrders = async () => {
  loading.value = true
  try {
    const data = await getOrderList({
      page: currentPage.value,
      size: pageSize.value
    })
    orders.value = data.records
    total.value = data.total
  } catch (error) {
    console.error('加载订单列表失败:', error)
  } finally {
    loading.value = false
  }
}

const getStatusType = (status: number) => {
  const types: Record<number, string> = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'success',
    4: 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status: number) => {
  const texts: Record<number, string> = {
    0: '待支付',
    1: '已支付',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return texts[status] || '未知'
}

const handleViewDetail = (id: number) => {
  router.push(`/orders/${id}`)
}

const handlePay = (id: number) => {
  router.push(`/orders/${id}`)
}

const handleCancel = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要取消这个订单吗？', '提示', {
      type: 'warning'
    })
    await cancelOrder(id)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消订单失败:', error)
    }
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.order-list-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.header-card {
  padding: 20px;
}

.header-card h2 {
  margin: 0;
  color: var(--text-primary);
}

.orders-card {
  padding: 20px;
}

.amount {
  color: var(--primary-pink);
  font-weight: bold;
}

.el-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
