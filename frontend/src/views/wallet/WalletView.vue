<template>
  <div class="wallet-container">
    <!-- 余额卡片 -->
    <div class="balance-card">
      <div class="balance-decoration">
        <div class="deco-circle deco-1"></div>
        <div class="deco-circle deco-2"></div>
        <div class="deco-circle deco-3"></div>
      </div>
      <div class="balance-content">
        <div class="balance-header">
          <span class="wallet-icon">💰</span>
          <span class="wallet-label">我的钱包</span>
        </div>
        <div class="balance-amount">
          <span class="currency">¥</span>
          <span class="amount">{{ wallet?.balance?.toFixed(2) || '0.00' }}</span>
        </div>
        <div class="balance-info">
          <span>🔒 冻结金额: ¥{{ wallet?.frozenAmount?.toFixed(2) || '0.00' }}</span>
        </div>
        <div class="balance-actions">
          <el-button type="primary" class="action-btn recharge-btn" @click="showRecharge = true">
            <span class="btn-icon">⚡</span>
            <span>充值</span>
          </el-button>
          <el-button class="action-btn withdraw-btn" @click="showWithdraw = true">
            <span class="btn-icon">💸</span>
            <span>提现</span>
          </el-button>
          <el-button class="action-btn record-btn" @click="router.push('/orders')">
            <span class="btn-icon">📋</span>
            <span>订单</span>
          </el-button>
        </div>
      </div>
    </div>

    <!-- 交易记录 -->
    <div class="transaction-card glass-card">
      <div class="card-header">
        <span class="header-title">📊 交易记录</span>
        <el-radio-group v-model="filterType" size="small" class="filter-group">
          <el-radio-button :value="null">全部</el-radio-button>
          <el-radio-button :value="1">充值</el-radio-button>
          <el-radio-button :value="2">消费</el-radio-button>
          <el-radio-button :value="3">提现</el-radio-button>
        </el-radio-group>
      </div>
      
      <el-empty v-if="transactions.length === 0 && !loading" description="暂无交易记录" />
      
      <div v-else class="transaction-list">
        <div v-for="tx in transactions" :key="tx.id" class="transaction-item">
          <div class="tx-icon" :class="tx.type === 1 ? 'income' : 'expense'">
            {{ tx.type === 1 ? '⚡' : tx.type === 2 ? '🛒' : '💸' }}
          </div>
          <div class="tx-info">
            <div class="tx-desc">{{ tx.description }}</div>
            <div class="tx-time">{{ formatDate(tx.createdAt) }}</div>
          </div>
          <div class="tx-amount" :class="tx.type === 1 ? 'income' : 'expense'">
            {{ tx.type === 1 ? '+' : '-' }}¥{{ tx.amount?.toFixed(2) }}
          </div>
        </div>
      </div>
    </div>

    <!-- 充值对话框 -->
    <el-dialog v-model="showRecharge" width="480px" class="anime-dialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <span class="dialog-icon">⚡</span>
          <h3>钱包充值</h3>
        </div>
        
        <div class="amount-presets">
          <el-button
            v-for="amt in [50, 100, 200, 500, 1000]"
            :key="amt"
            :type="rechargeForm.amount === amt ? 'primary' : ''"
            @click="rechargeForm.amount = amt"
          >
            ¥{{ amt }}
          </el-button>
        </div>

        <el-input-number
          v-model="rechargeForm.amount"
          :min="1"
          :max="100000"
          :precision="2"
          size="large"
          class="amount-input"
        />

        <div class="payment-methods">
          <span class="method-label">选择支付方式</span>
          <div class="method-options">
            <div
              class="method-option"
              :class="{ active: rechargeForm.paymentMethod === 'alipay' }"
              @click="rechargeForm.paymentMethod = 'alipay'"
            >
              <span class="method-icon">🔵</span>
              <span>支付宝</span>
            </div>
            <div
              class="method-option"
              :class="{ active: rechargeForm.paymentMethod === 'wechat' }"
              @click="rechargeForm.paymentMethod = 'wechat'"
            >
              <span class="method-icon">🟢</span>
              <span>微信支付</span>
            </div>
            <div
              class="method-option"
              :class="{ active: rechargeForm.paymentMethod === 'bank' }"
              @click="rechargeForm.paymentMethod = 'bank'"
            >
              <span class="method-icon">🏦</span>
              <span>银行卡</span>
            </div>
          </div>
        </div>

        <el-button
          type="primary"
          size="large"
          class="confirm-btn"
          :loading="recharging"
          @click="handleRecharge"
        >
          {{ recharging ? '充值中...' : '确认充值' }}
        </el-button>
      </div>
    </el-dialog>

    <!-- 提现对话框 -->
    <el-dialog v-model="showWithdraw" width="480px" class="anime-dialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <span class="dialog-icon">💸</span>
          <h3>钱包提现</h3>
        </div>

        <el-input-number
          v-model="withdrawForm.amount"
          :min="1"
          :max="wallet?.balance || 0"
          :precision="2"
          size="large"
          class="amount-input"
        />

        <el-input
          v-model="withdrawForm.bankCard"
          placeholder="请输入银行卡号"
          size="large"
          class="bank-input"
        />

        <el-button
          type="primary"
          size="large"
          class="confirm-btn"
          :loading="withdrawing"
          @click="handleWithdraw"
        >
          {{ withdrawing ? '提交中...' : '确认提现' }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getWalletInfo, recharge, withdraw, getTransactionList } from '@/api/wallet'

const router = useRouter()
const wallet = ref<any>(null)
const transactions = ref<any[]>([])
const loading = ref(false)
const showRecharge = ref(false)
const showWithdraw = ref(false)
const recharging = ref(false)
const withdrawing = ref(false)
const filterType = ref<number | null>(null)

const rechargeForm = reactive({ amount: 100, paymentMethod: 'alipay' })
const withdrawForm = reactive({ amount: 0, bankCard: '' })

const formatDate = (date: string) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

const loadData = async () => {
  loading.value = true
  try {
    wallet.value = await getWalletInfo()
    const data = await getTransactionList({ 
      page: 1, 
      size: 20,
      type: filterType.value
    })
    transactions.value = data.records || []
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

watch(filterType, () => {
  loadData()
})

const handleRecharge = async () => {
  if (rechargeForm.amount <= 0) {
    ElMessage.warning('请输入有效的充值金额')
    return
  }
  recharging.value = true
  try {
    await recharge(rechargeForm)
    ElMessage.success('充值成功！⚡')
    showRecharge.value = false
    loadData()
  } catch (error) {
    console.error('充值失败:', error)
    ElMessage.error('充值失败，请重试')
  } finally {
    recharging.value = false
  }
}

const handleWithdraw = async () => {
  if (withdrawForm.amount <= 0) {
    ElMessage.warning('请输入有效的提现金额')
    return
  }
  if (!withdrawForm.bankCard) {
    ElMessage.warning('请输入银行卡号')
    return
  }
  withdrawing.value = true
  try {
    await withdraw(withdrawForm)
    ElMessage.success('提现申请已提交！💸')
    showWithdraw.value = false
    loadData()
  } catch (error) {
    console.error('提现失败:', error)
    ElMessage.error('提现失败，请重试')
  } finally {
    withdrawing.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.wallet-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  position: relative;
  z-index: 1;
}

.balance-card {
  position: relative;
  border-radius: 24px;
  overflow: hidden;
  padding: 40px;
  margin-bottom: 30px;
  background: linear-gradient(135deg, #ff6b9d 0%, #c44dff 50%, #4dc8ff 100%);
  box-shadow: 0 20px 60px rgba(255, 107, 157, 0.3);
}

.balance-decoration {
  position: absolute;
  top: 0; left: 0;
  width: 100%; height: 100%;
  pointer-events: none;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.deco-1 { width: 200px; height: 200px; top: -50px; right: -50px; }
.deco-2 { width: 150px; height: 150px; bottom: -30px; left: 10%; }
.deco-3 { width: 100px; height: 100px; top: 20%; left: 5%; }

.balance-content {
  position: relative;
  z-index: 1;
  color: white;
}

.balance-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.wallet-icon {
  font-size: 24px;
}

.wallet-label {
  font-size: 16px;
  opacity: 0.9;
}

.balance-amount {
  display: flex;
  align-items: baseline;
  gap: 5px;
  margin-bottom: 10px;
}

.currency {
  font-size: 24px;
  opacity: 0.8;
}

.amount {
  font-size: 48px;
  font-weight: bold;
}

.balance-info {
  opacity: 0.8;
  margin-bottom: 30px;
}

.balance-actions {
  display: flex;
  gap: 15px;
}

.action-btn {
  flex: 1;
  height: 48px;
  border-radius: 24px;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-icon {
  font-size: 18px;
}

.recharge-btn {
  background: white;
  border-color: white;
  color: #ff6b9d;
}

.withdraw-btn,
.record-btn {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.3);
  color: white;
}

.glass-card {
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: var(--border-card);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: var(--text-primary);
}

.transaction-list {
  max-height: 500px;
  overflow-y: auto;
}

.transaction-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-radius: 12px;
  margin-bottom: 10px;
  background: rgba(255, 255, 255, 0.03);
  transition: all 0.3s;
}

.transaction-item:hover {
  background: rgba(255, 255, 255, 0.08);
}

.tx-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 15px;
}

.tx-icon.income {
  background: rgba(77, 255, 203, 0.15);
}

.tx-icon.expense {
  background: rgba(255, 107, 157, 0.15);
}

.tx-info {
  flex: 1;
}

.tx-desc {
  font-weight: 500;
  margin-bottom: 4px;
  color: var(--text-primary);
}

.tx-time {
  font-size: 12px;
  color: var(--text-muted);
}

.tx-amount {
  font-size: 18px;
  font-weight: bold;
}

.tx-amount.income {
  color: #4dffcb;
}

.tx-amount.expense {
  color: #ff6b9d;
}

.dialog-content {
  padding: 20px;
}

.dialog-header {
  text-align: center;
  margin-bottom: 30px;
}

.dialog-icon {
  font-size: 48px;
  display: block;
  margin-bottom: 10px;
}

.dialog-header h3 {
  margin: 0;
  font-size: 24px;
  color: var(--text-primary);
}

.amount-presets {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.amount-presets .el-button {
  flex: 1;
  min-width: 60px;
}

.amount-input {
  width: 100%;
  margin-bottom: 20px;
}

.payment-methods {
  margin-bottom: 30px;
}

.method-label {
  display: block;
  margin-bottom: 10px;
  color: var(--text-secondary);
}

.method-options {
  display: flex;
  gap: 15px;
}

.method-option {
  flex: 1;
  padding: 15px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
  color: var(--text-primary);
}

.method-option:hover {
  background: rgba(255, 255, 255, 0.1);
}

.method-option.active {
  border-color: var(--primary-pink);
  background: rgba(255, 107, 157, 0.1);
}

.method-icon {
  font-size: 24px;
  display: block;
  margin-bottom: 5px;
}

.confirm-btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
  border-radius: 25px;
}

.bank-input {
  width: 100%;
  margin-bottom: 20px;
}
</style>
