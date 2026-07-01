<template>
  <div class="stats-container">
    <!-- 概览卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card glass-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: rgba(77, 200, 255, 0.15)">
              <el-icon :size="32" color="#4dc8ff"><ShoppingCart /></el-icon>
            </div>
            <div class="stat-data">
              <h3>{{ stats.totalOrders }}</h3>
              <p>总订单数</p>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card glass-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: rgba(77, 255, 203, 0.15)">
              <el-icon :size="32" color="#4dffcb"><Money /></el-icon>
            </div>
            <div class="stat-data">
              <h3>¥{{ stats.totalSpent }}</h3>
              <p>总消费金额</p>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card glass-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: rgba(255, 216, 77, 0.15)">
              <el-icon :size="32" color="#ffd84d"><Coin /></el-icon>
            </div>
            <div class="stat-data">
              <h3>{{ stats.tokensOwned }}</h3>
              <p>持有Token数</p>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card glass-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: rgba(255, 107, 157, 0.15)">
              <el-icon :size="32" color="#ff6b9d"><TrendCharts /></el-icon>
            </div>
            <div class="stat-data">
              <h3>{{ stats.monthlyGrowth }}%</h3>
              <p>月增长率</p>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <div class="chart-card glass-card">
          <div class="card-header">
            <span>📈 消费趋势</span>
            <el-radio-group v-model="chartPeriod" size="small">
              <el-radio-button label="week">近7天</el-radio-button>
              <el-radio-button label="month">近30天</el-radio-button>
              <el-radio-button label="year">近1年</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-container">
            <div class="chart-placeholder">
              <el-icon :size="48" color="var(--text-muted)"><TrendCharts /></el-icon>
              <p>消费趋势图表</p>
              <p class="hint">集成 ECharts 后可展示详细数据</p>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="chart-card glass-card">
          <div class="card-header">🥧 Token类型分布</div>
          <div class="chart-container">
            <div class="chart-placeholder">
              <el-icon :size="48" color="var(--text-muted)"><PieChart /></el-icon>
              <p>Token类型分布</p>
              <p class="hint">集成 ECharts 后可展示详细数据</p>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 最近交易 -->
    <div class="transactions-card glass-card" style="margin-top: 20px">
      <div class="card-header">📋 最近交易记录</div>
      <el-table :data="recentTransactions" stripe class="anime-table">
        <el-table-column prop="date" label="日期" width="180" />
        <el-table-column prop="type" label="类型">
          <template #default="{ row }">
            <el-tag :type="row.type === '收入' ? 'success' : 'danger'" effect="dark">
              {{ row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="amount" label="金额" width="150">
          <template #default="{ row }">
            <span :style="{ color: row.type === '收入' ? '#4dffcb' : '#ff6b9d' }">
              {{ row.type === '收入' ? '+' : '-' }}¥{{ row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="150">
          <template #default="{ row }">¥{{ row.balance }}</template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ShoppingCart, Money, Coin, TrendCharts, PieChart } from '@element-plus/icons-vue'

const stats = ref({
  totalOrders: 28,
  totalSpent: 3580.50,
  tokensOwned: 156,
  monthlyGrowth: 12.5
})

const chartPeriod = ref('week')

const recentTransactions = ref([
  { date: '2026-06-30', type: '支出', description: '购买 API调用Token - 1000次', amount: 99.00, balance: 901.00 },
  { date: '2026-06-29', type: '收入', description: '钱包充值', amount: 500.00, balance: 1000.00 },
  { date: '2026-06-28', type: '支出', description: '购买 会员Token - 月度', amount: 19.90, balance: 500.00 },
  { date: '2026-06-27', type: '支出', description: '购买 数据访问Token - 标准版', amount: 49.00, balance: 519.90 },
  { date: '2026-06-26', type: '收入', description: '订单退款', amount: 99.00, balance: 568.90 }
])
</script>

<style scoped>
.stats-container {
  padding: 20px;
  max-width: 1400px;
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
  padding: 20px;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-data h3 {
  font-size: 24px;
  color: var(--text-primary);
  margin: 0;
}

.stat-data p {
  color: var(--text-secondary);
  margin: 5px 0 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
  color: var(--text-primary);
}

.chart-card {
  margin-bottom: 20px;
}

.chart-container {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-placeholder {
  text-align: center;
  color: var(--text-muted);
}

.chart-placeholder p {
  margin: 10px 0 5px;
  font-size: 16px;
  color: var(--text-secondary);
}

.chart-placeholder .hint {
  font-size: 12px;
  color: var(--text-muted);
}

.transactions-card {
  margin-top: 20px;
}
</style>
