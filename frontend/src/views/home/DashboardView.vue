<template>
  <div class="dashboard-container">
    <!-- 轮播图 -->
    <el-carousel height="380px" class="hero-banner" indicator-position="outside">
      <el-carousel-item v-for="item in banners" :key="item.id">
        <div class="banner-content" :style="{ background: item.bgColor }">
          <div class="banner-decoration">
            <div class="deco-circle deco-1"></div>
            <div class="deco-circle deco-2"></div>
            <div class="deco-circle deco-3"></div>
          </div>
          <div class="banner-text">
            <h1>{{ item.title }}</h1>
            <p>{{ item.description }}</p>
            <el-button type="primary" size="large" round class="banner-btn" @click="router.push('/tokens')">
              <span>立即选购</span>
              <span class="btn-arrow">→</span>
            </el-button>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 数据统计 -->
    <div class="stats-section">
      <div class="stat-item" v-for="stat in stats" :key="stat.label">
        <div class="stat-icon" :style="{ background: stat.bgColor }">
          <el-icon :size="28" :color="stat.color"><component :is="stat.icon" /></el-icon>
        </div>
        <div class="stat-info">
          <h3>{{ stat.value }}</h3>
          <p>{{ stat.label }}</p>
        </div>
      </div>
    </div>

    <!-- 热门Token -->
    <div class="section">
      <div class="section-header">
        <h2><span class="section-emoji">🔥</span> 热门Token</h2>
        <el-button text type="primary" class="view-more-btn" @click="router.push('/tokens')">
          查看更多 <span class="btn-arrow">→</span>
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col v-for="token in hotTokens" :key="token.id" :xs="24" :sm="12" :md="8" :lg="6">
          <div class="anime-token-card" @click="router.push(`/tokens/${token.id}`)">
            <div class="token-badge" v-if="token.hot">
              <span class="badge-fire">🔥</span> HOT
            </div>
            <div class="token-image-wrapper">
              <div class="token-image" :style="{ background: token.bgColor }">
                <span class="token-emoji">{{ token.emoji }}</span>
              </div>
            </div>
            <div class="token-info">
              <h3>{{ token.name }}</h3>
              <p class="description">{{ token.description }}</p>
              <div class="token-meta">
                <span class="price">
                  <span class="currency">¥</span>{{ token.price }}
                </span>
                <span class="sales">已售 {{ token.sales || 0 }}</span>
              </div>
              <div class="rating">
                <el-rate v-model="token.rating" disabled show-score text-color="#ffd84d" />
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- Token分类 -->
    <div class="section">
      <div class="section-header">
        <h2><span class="section-emoji">📂</span> Token分类</h2>
      </div>
      <el-row :gutter="20">
        <el-col v-for="category in categories" :key="category.id" :xs="12" :sm="8" :md="4">
          <div class="anime-category-card" @click="router.push(`/tokens?category=${category.id}`)">
            <div class="category-icon" :style="{ background: category.bgColor }">
              <el-icon :size="36" :color="category.color"><component :is="category.icon" /></el-icon>
            </div>
            <h3>{{ category.name }}</h3>
            <p>{{ category.count }}个Token</p>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 为什么选择我们 -->
    <div class="section features-section">
      <h2><span class="section-emoji">💎</span> 为什么选择 Token Shop</h2>
      <el-row :gutter="30">
        <el-col v-for="feature in features" :key="feature.title" :xs="24" :md="12" :lg="6">
          <div class="anime-feature-card">
            <div class="feature-icon" :style="{ background: feature.bgColor }">
              <el-icon :size="40" :color="feature.color"><component :is="feature.icon" /></el-icon>
            </div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { 
  ShoppingBag, User, Money, ChatDotRound, 
  Cpu, Document, Coin, Connection,
  Lock, TrendCharts, Service, Clock
} from '@element-plus/icons-vue'

const router = useRouter()

const banners = [
  {
    id: 1,
    title: '✨ AI API Token 交易平台',
    description: '汇聚顶级AI模型Token，安全、快捷、专业',
    bgColor: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    id: 2,
    title: '🎮 企业级Token聚合服务',
    description: '一站式Token管理，助力企业数字化转型',
    bgColor: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    id: 3,
    title: '🎉 新用户注册享8折优惠',
    description: '限时优惠，立即注册体验',
    bgColor: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  }
]

const stats = [
  { label: '在售Token', value: '1,286', icon: ShoppingBag, color: '#4dc8ff', bgColor: 'rgba(77, 200, 255, 0.15)' },
  { label: '注册用户', value: '5,680', icon: User, color: '#4dffcb', bgColor: 'rgba(77, 255, 203, 0.15)' },
  { label: '今日交易额', value: '¥128,500', icon: Money, color: '#ffd84d', bgColor: 'rgba(255, 216, 77, 0.15)' },
  { label: '好评率', value: '99.2%', icon: ChatDotRound, color: '#ff6b9d', bgColor: 'rgba(255, 107, 157, 0.15)' }
]

const hotTokens = [
  {
    id: 1,
    name: 'API调用Token - 1000次',
    description: '包含1000次API调用次数，适用于开发测试',
    price: 99.00,
    sales: 356,
    rating: 4.8,
    hot: true,
    emoji: '🔌',
    bgColor: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    id: 2,
    name: '会员Token - 月度',
    description: '月度会员专属Token，享受会员特权',
    price: 19.90,
    sales: 892,
    rating: 4.9,
    hot: true,
    emoji: '👑',
    bgColor: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    id: 3,
    name: '数据访问Token - 标准版',
    description: '标准版数据访问权限，有效期30天',
    price: 49.00,
    sales: 234,
    rating: 4.7,
    hot: false,
    emoji: '📊',
    bgColor: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    id: 4,
    name: 'AI绘画Token - 500次',
    description: '支持多种AI绘画模型，创作无限可能',
    price: 199.00,
    sales: 167,
    rating: 4.9,
    hot: true,
    emoji: '🎨',
    bgColor: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
  }
]

const categories = [
  { id: 1, name: 'API Token', icon: Cpu, color: '#4dc8ff', bgColor: 'rgba(77, 200, 255, 0.15)', count: 456 },
  { id: 2, name: '会员Token', icon: Coin, color: '#4dffcb', bgColor: 'rgba(77, 255, 203, 0.15)', count: 234 },
  { id: 3, name: '数据Token', icon: Document, color: '#ffd84d', bgColor: 'rgba(255, 216, 77, 0.15)', count: 189 },
  { id: 4, name: 'AI Token', icon: Connection, color: '#ff6b9d', bgColor: 'rgba(255, 107, 157, 0.15)', count: 407 }
]

const features = [
  {
    title: '安全可靠',
    description: '采用银行级加密技术，保障您的交易安全',
    icon: Lock,
    color: '#4dc8ff',
    bgColor: 'rgba(77, 200, 255, 0.15)'
  },
  {
    title: '实时数据',
    description: '实时监控Token使用情况，数据一目了然',
    icon: TrendCharts,
    color: '#4dffcb',
    bgColor: 'rgba(77, 255, 203, 0.15)'
  },
  {
    title: '专业服务',
    description: '7×24小时在线客服，随时为您解答问题',
    icon: Service,
    color: '#ffd84d',
    bgColor: 'rgba(255, 216, 77, 0.15)'
  },
  {
    title: '快速交付',
    description: '支付成功后即时交付，无需等待',
    icon: Clock,
    color: '#ff6b9d',
    bgColor: 'rgba(255, 107, 157, 0.15)'
  }
]
</script>

<style scoped>
.dashboard-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px 40px;
  position: relative;
  z-index: 1;
}

/* 轮播图 */
.hero-banner {
  margin-bottom: 40px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
}

.banner-content {
  height: 380px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.banner-decoration {
  position: absolute;
  top: 0; left: 0;
  width: 100%; height: 100%;
  pointer-events: none;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.08);
}

.deco-1 { width: 200px; height: 200px; top: -50px; right: -50px; }
.deco-2 { width: 150px; height: 150px; bottom: -30px; left: 10%; }
.deco-3 { width: 100px; height: 100px; top: 20%; left: 5%; }

.banner-text {
  text-align: center;
  color: #fff;
  position: relative;
  z-index: 1;
}

.banner-text h1 {
  font-size: 40px;
  margin-bottom: 15px;
  text-shadow: 2px 2px 8px rgba(0,0,0,0.3);
}

.banner-text p {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.banner-btn {
  height: 48px;
  padding: 0 40px;
  font-size: 16px;
  background: rgba(255, 255, 255, 0.2) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  backdrop-filter: blur(10px);
}

.banner-btn:hover {
  background: rgba(255, 255, 255, 0.3) !important;
  transform: translateY(-3px);
}

.btn-arrow {
  margin-left: 8px;
  transition: transform 0.3s;
}

.banner-btn:hover .btn-arrow {
  transform: translateX(4px);
}

/* 数据统计 */
.stats-section {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 50px;
}

.stat-item {
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: var(--border-card);
  border-radius: 16px;
  padding: 25px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  transition: all 0.3s;
}

.stat-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
  border-color: rgba(255, 107, 157, 0.3);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info h3 {
  font-size: 26px;
  color: var(--text-primary);
  margin: 0;
}

.stat-info p {
  color: var(--text-secondary);
  margin: 5px 0 0;
  font-size: 14px;
}

/* 通用 section */
.section {
  margin-bottom: 50px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.section-header h2 {
  font-size: 24px;
  color: var(--text-primary);
  margin: 0;
}

.section-emoji {
  margin-right: 8px;
}

.view-more-btn {
  color: var(--primary-pink) !important;
  font-size: 14px;
}

.view-more-btn:hover .btn-arrow {
  transform: translateX(4px);
}

/* Token 卡片 */
.anime-token-card {
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: var(--border-card);
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  margin-bottom: 20px;
  position: relative;
}

.anime-token-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
  border-color: rgba(255, 107, 157, 0.3);
}

.token-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: linear-gradient(135deg, #ff6b9d, #ff8c4d);
  color: #fff;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  z-index: 2;
  box-shadow: 0 4px 15px rgba(255, 107, 157, 0.4);
}

.badge-fire {
  margin-right: 4px;
}

.token-image-wrapper {
  height: 160px;
  overflow: hidden;
}

.token-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.token-emoji {
  font-size: 64px;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

.token-info {
  padding: 15px;
}

.token-info h3 {
  font-size: 16px;
  color: var(--text-primary);
  margin: 0 0 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.description {
  font-size: 13px;
  color: var(--text-secondary);
  margin: 0 0 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.token-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.price {
  font-size: 20px;
  font-weight: bold;
  color: var(--primary-pink);
}

.currency {
  font-size: 14px;
  margin-right: 2px;
}

.sales {
  font-size: 12px;
  color: var(--text-muted);
}

.rating {
  display: flex;
  justify-content: flex-end;
}

/* 分类卡片 */
.anime-category-card {
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: var(--border-card);
  border-radius: 16px;
  padding: 30px 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  margin-bottom: 20px;
}

.anime-category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
  border-color: rgba(255, 107, 157, 0.3);
}

.category-icon {
  width: 72px;
  height: 72px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 15px;
}

.anime-category-card h3 {
  margin: 0 0 8px;
  color: var(--text-primary);
  font-size: 16px;
}

.anime-category-card p {
  color: var(--text-secondary);
  font-size: 13px;
  margin: 0;
}

/* 特性卡片 */
.features-section h2 {
  text-align: center;
  font-size: 28px;
  margin-bottom: 40px;
  color: var(--text-primary);
}

.anime-feature-card {
  text-align: center;
  padding: 30px;
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: var(--border-card);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  transition: all 0.3s;
  margin-bottom: 20px;
}

.anime-feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
  border-color: rgba(255, 107, 157, 0.3);
}

.feature-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.anime-feature-card h3 {
  margin: 0 0 10px;
  color: var(--text-primary);
  font-size: 18px;
}

.anime-feature-card p {
  color: var(--text-secondary);
  font-size: 14px;
  margin: 0;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .stats-section {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .banner-text h1 {
    font-size: 28px;
  }
  
  .banner-text p {
    font-size: 16px;
  }
}
</style>
