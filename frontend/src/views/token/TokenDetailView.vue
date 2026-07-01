<template>
  <div class="token-detail-container">
    <el-page-header @back="router.back()" :title="'返回'" class="back-btn" />
    
    <el-row :gutter="30" v-loading="loading">
      <!-- 左侧：图片和基本信息 -->
      <el-col :xs="24" :lg="14">
        <div class="image-card glass-card">
          <div class="token-image" :style="{ background: token?.bgColor || defaultBg }">
            <span class="token-emoji">{{ token?.emoji || '🎫' }}</span>
          </div>
        </div>

        <!-- Token详情 -->
        <div class="detail-card glass-card" style="margin-top: 20px">
          <div class="card-header">
            <span>📝 商品详情</span>
          </div>
          <div class="detail-content">{{ token?.description || '暂无描述' }}</div>
        </div>

        <!-- 用户评价 -->
        <div class="reviews-card glass-card" style="margin-top: 20px">
          <div class="card-header">
            <span>💬 用户评价 ({{ reviews.length }})</span>
            <el-button type="primary" size="small" @click="showReviewDialog = true">
              ✍️ 写评价
            </el-button>
          </div>
          <el-empty v-if="reviews.length === 0" description="暂无评价" />
          <div v-else class="review-list">
            <div v-for="review in reviews" :key="review.id" class="review-item">
              <div class="review-header">
                <el-avatar :size="36" class="anime-avatar">{{ review.username[0] }}</el-avatar>
                <div class="review-user">
                  <span class="username">{{ review.username }}</span>
                  <el-rate v-model="review.rating" disabled size="small" />
                </div>
                <span class="review-date">{{ review.date }}</span>
              </div>
              <p class="review-content">{{ review.content }}</p>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 右侧：购买信息 -->
      <el-col :xs="24" :lg="10">
        <div class="purchase-card glass-card">
          <h1 class="token-title">{{ token?.name }}</h1>
          <div class="price-section">
            <span class="current-price">
              <span class="currency">¥</span>{{ token?.price }}
            </span>
            <span class="original-price" v-if="token?.originalPrice">¥{{ token.originalPrice }}</span>
            <el-tag type="danger" effect="dark" v-if="token?.discount">省¥{{ (token.originalPrice - token.price).toFixed(2) }}</el-tag>
          </div>

          <div class="info-list">
            <div class="info-item">
              <span class="label">📂 分类</span>
              <span class="value">{{ getCategoryName(token?.categoryId) }}</span>
            </div>
            <div class="info-item">
              <span class="label">📦 库存</span>
              <span class="value" :class="{ low: (token?.stock || 0) < 10 }">
                {{ token?.stock || 0 }} 件
              </span>
            </div>
            <div class="info-item">
              <span class="label">🛒 已售</span>
              <span class="value">{{ token?.sales || 0 }} 件</span>
            </div>
            <div class="info-item">
              <span class="label">⭐ 评分</span>
              <el-rate v-model="token?.rating" disabled show-score text-color="#ffd84d" />
            </div>
          </div>

          <div class="purchase-actions">
            <div class="quantity-selector">
              <span class="label">购买数量</span>
              <el-input-number
                v-model="quantity"
                :min="1"
                :max="token?.stock || 0"
                size="large"
              />
            </div>
            <div class="total-price">
              <span>合计</span>
              <span class="price">
                <span class="currency">¥</span>{{ (quantity * (token?.price || 0)).toFixed(2) }}
              </span>
            </div>
            <el-button
              type="primary"
              size="large"
              class="buy-btn"
              :disabled="!token?.stock"
              @click="handleBuy"
            >
              🛒 立即购买
            </el-button>
            <el-button
              size="large"
              class="favorite-btn"
              :icon="isFavorite ? '⭐' : '☆'"
              :type="isFavorite ? 'warning' : 'info'"
              @click="toggleFavorite"
            >
              {{ isFavorite ? '已收藏' : '加入收藏' }}
            </el-button>
          </div>
        </div>

        <!-- 卖家信息 -->
        <div class="seller-card glass-card" style="margin-top: 20px">
          <div class="card-header">🏪 卖家信息</div>
          <div class="seller-info">
            <el-avatar :size="48" class="anime-avatar">{{ seller?.name?.[0] || 'T' }}</el-avatar>
            <div class="seller-details">
              <h4>{{ seller?.name || 'Token Shop 官方' }}</h4>
              <p>
                <el-tag size="small" type="success" effect="dark">认证商家</el-tag>
                <el-tag size="small" effect="dark">好评率 99%</el-tag>
              </p>
            </div>
            <el-button type="primary" text @click="router.push('/profile')">
              联系卖家 →
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 评价对话框 -->
    <el-dialog v-model="showReviewDialog" title="✍️ 写评价" width="500px" class="anime-dialog">
      <el-form :model="reviewForm" label-width="80px">
        <el-form-item label="评分">
          <el-rate v-model="reviewForm.rating" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input
            v-model="reviewForm.content"
            type="textarea"
            :rows="4"
            placeholder="请分享您的使用体验..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showReviewDialog = false">取消</el-button>
        <el-button type="primary" @click="submitReview">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTokenDetail } from '@/api/token'
import { createOrder } from '@/api/order'

const route = useRoute()
const router = useRouter()
const token = ref<any>(null)
const loading = ref(false)
const quantity = ref(1)
const isFavorite = ref(false)
const showReviewDialog = ref(false)
const defaultBg = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'

const seller = ref({
  name: 'Token Shop 官方',
  rating: 4.9
})

const reviews = ref([
  {
    id: 1,
    username: '张***3',
    rating: 5,
    content: '非常好用的Token，API响应速度快，文档齐全，强烈推荐！',
    date: '2026-06-28'
  },
  {
    id: 2,
    username: '李***8',
    rating: 4,
    content: '性价比很高，适合开发测试使用。',
    date: '2026-06-25'
  }
])

const reviewForm = ref({
  rating: 5,
  content: ''
})

const getCategoryName = (categoryId?: number) => {
  const categories: Record<number, string> = {
    1: '数据Token',
    2: 'API Token',
    3: '会员Token'
  }
  return categories[categoryId || 0] || '未知分类'
}

const loadTokenDetail = async () => {
  loading.value = true
  try {
    token.value = await getTokenDetail(Number(route.params.id))
  } catch (error) {
    console.error('加载Token详情失败:', error)
  } finally {
    loading.value = false
  }
}

const handleBuy = async () => {
  try {
    const order = await createOrder({
      tokenId: token.value.id,
      quantity: quantity.value
    })
    ElMessage.success('订单创建成功 🎉')
    router.push(`/orders/${order.id}`)
  } catch (error) {
    console.error('创建订单失败:', error)
  }
}

const toggleFavorite = () => {
  isFavorite.value = !isFavorite.value
  ElMessage.success(isFavorite.value ? '已加入收藏 ⭐' : '已取消收藏')
}

const submitReview = () => {
  if (!reviewForm.value.content) {
    ElMessage.warning('请输入评价内容')
    return
  }
  reviews.value.unshift({
    id: Date.now(),
    username: '我',
    rating: reviewForm.value.rating,
    content: reviewForm.value.content,
    date: new Date().toISOString().split('T')[0]
  })
  showReviewDialog.value = false
  reviewForm.value.content = ''
  ElMessage.success('评价提交成功 ✨')
}

onMounted(() => {
  loadTokenDetail()
})
</script>

<style scoped>
.token-detail-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px 40px;
  position: relative;
  z-index: 1;
}

.back-btn {
  margin-bottom: 20px;
}

.image-card {
  padding: 0;
  overflow: hidden;
}

.token-image {
  width: 100%;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.token-emoji {
  font-size: 120px;
  filter: drop-shadow(0 8px 16px rgba(0, 0, 0, 0.3));
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
  margin-bottom: 15px;
  font-size: 18px;
  font-weight: bold;
  color: var(--text-primary);
}

.detail-content {
  line-height: 1.8;
  color: var(--text-secondary);
}

.purchase-card {
  position: sticky;
  top: 84px;
}

.token-title {
  font-size: 24px;
  color: var(--text-primary);
  margin: 0 0 15px;
}

.price-section {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.current-price {
  font-size: 32px;
  font-weight: bold;
  color: var(--primary-pink);
}

.currency {
  font-size: 18px;
  margin-right: 2px;
}

.original-price {
  font-size: 16px;
  color: var(--text-muted);
  text-decoration: line-through;
}

.info-list {
  margin: 20px 0;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.info-item .label {
  color: var(--text-secondary);
}

.info-item .value {
  color: var(--text-primary);
}

.info-item .value.low {
  color: #f56c6c;
}

.purchase-actions {
  margin-top: 20px;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.quantity-selector .label {
  color: var(--text-secondary);
}

.total-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  margin-bottom: 15px;
}

.total-price .price {
  font-size: 24px;
  font-weight: bold;
  color: var(--primary-pink);
}

.buy-btn {
  width: 100%;
  margin-top: 20px;
  height: 48px;
  font-size: 16px;
}

.favorite-btn {
  width: 100%;
  margin-top: 10px;
  height: 48px;
}

.seller-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.seller-details {
  flex: 1;
}

.seller-details h4 {
  margin: 0 0 8px;
  color: var(--text-primary);
}

.seller-details p {
  margin: 0;
}

.anime-avatar {
  border: 2px solid var(--primary-pink);
  box-shadow: 0 0 15px rgba(255, 107, 157, 0.3);
}

.review-list {
  max-height: 400px;
  overflow-y: auto;
}

.review-item {
  padding: 15px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.review-item:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.review-user {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: bold;
  margin-bottom: 4px;
  color: var(--text-primary);
}

.review-date {
  color: var(--text-muted);
  font-size: 12px;
}

.review-content {
  margin: 0;
  color: var(--text-secondary);
  line-height: 1.6;
}
</style>
