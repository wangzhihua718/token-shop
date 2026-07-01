<template>
  <div class="order-detail-container">
    <div class="detail-card glass-card" v-loading="loading">
      <div class="header">
        <span>📋 订单详情</span>
        <el-button @click="router.back()">← 返回</el-button>
      </div>
      
      <el-descriptions :column="2" border class="anime-descriptions">
        <el-descriptions-item label="订单号">{{ order?.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(order?.status)" effect="dark">
            {{ getStatusText(order?.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="总金额">¥{{ order?.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ order?.createdAt }}</el-descriptions-item>
      </el-descriptions>
      
      <h3 class="section-title">🛒 订单商品</h3>
      <el-table :data="order?.items || []" border class="anime-table">
        <el-table-column prop="tokenName" label="Token名称" />
        <el-table-column prop="unitPrice" label="单价">
          <template #default="{ row }">¥{{ row.unitPrice }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" />
        <el-table-column prop="subtotal" label="小计">
          <template #default="{ row }">¥{{ row.subtotal }}</template>
        </el-table-column>
      </el-table>
      
      <div class="actions" v-if="order?.status === 0">
        <el-button type="primary" @click="handlePay" class="pay-btn">💳 立即支付</el-button>
        <el-button type="danger" @click="handleCancel" class="cancel-btn">❌ 取消订单</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderDetail, cancelOrder } from '@/api/order'

const route = useRoute()
const router = useRouter()
const order = ref<any>(null)
const loading = ref(false)

const loadOrderDetail = async () => {
  loading.value = true
  try {
    order.value = await getOrderDetail(Number(route.params.id))
  } catch (error) {
    console.error('加载订单详情失败:', error)
  } finally {
    loading.value = false
  }
}

const getStatusType = (status?: number) => {
  if (!status) return 'info'
  const types: Record<number, string> = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'success', 4: 'info' }
  return types[status] || 'info'
}

const getStatusText = (status?: number) => {
  if (!status) return '未知'
  const texts: Record<number, string> = { 0: '待支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消' }
  return texts[status] || '未知'
}

const handlePay = () => {
  router.push('/wallet')
}

const handleCancel = async () => {
  try {
    await ElMessageBox.confirm('确定要取消这个订单吗？', '提示', { type: 'warning' })
    await cancelOrder(Number(route.params.id))
    ElMessage.success('订单已取消')
    loadOrderDetail()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消订单失败:', error)
    }
  }
}

onMounted(() => {
  loadOrderDetail()
})
</script>

<style scoped>
.order-detail-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.glass-card {
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: var(--border-card);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  padding: 30px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: bold;
  color: var(--text-primary);
}

.section-title {
  margin: 30px 0 20px;
  color: var(--text-primary);
}

.actions {
  margin-top: 30px;
  display: flex;
  gap: 15px;
}

.pay-btn,
.cancel-btn {
  width: 180px;
  height: 45px;
  font-size: 16px;
}
</style>
