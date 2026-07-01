<template>
  <div class="token-list-container">
    <!-- 搜索和筛选栏 -->
    <div class="search-card glass-card">
      <el-row :gutter="20" align="middle">
        <el-col :span="8">
          <el-input
            v-model="searchKeyword"
            placeholder="🔍 搜索Token名称或描述"
            clearable
            size="large"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">搜索</el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-select v-model="selectedCategory" placeholder="全部分类" clearable size="large" @change="handleSearch">
            <el-option label="全部分类" :value="undefined" />
            <el-option label="API Token" :value="2" />
            <el-option label="会员Token" :value="3" />
            <el-option label="数据Token" :value="1" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="sortBy" placeholder="排序方式" size="large" @change="handleSearch">
            <el-option label="默认排序" value="default" />
            <el-option label="价格从低到高" value="price_asc" />
            <el-option label="价格从高到低" value="price_desc" />
            <el-option label="销量最高" value="sales_desc" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" size="large" @click="handleCreate" style="width: 100%">
            ✨ 发布Token
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- Token列表 -->
    <div class="token-grid">
      <el-empty v-if="tokens.length === 0 && !loading" description="暂无Token数据" />
      
      <div v-else class="grid-layout">
        <div v-for="token in tokens" :key="token.id" class="anime-token-card" @click="handleViewDetail(token.id)">
          <div class="card-badge" v-if="token.hot">
            <span class="badge-fire">🔥</span> HOT
          </div>
          <div class="token-image" :style="{ background: token.bgColor || defaultBg }">
            <span class="token-emoji">{{ token.emoji || '🎫' }}</span>
            <div class="image-overlay">
              <el-button type="primary" circle size="small">👁️</el-button>
            </div>
          </div>
          <div class="token-info">
            <h3 class="token-name">{{ token.name }}</h3>
            <p class="token-description">{{ token.description }}</p>
            <div class="token-stats">
              <span class="sales">🛒 {{ token.sales || 0 }}人购买</span>
              <span class="rating">⭐ {{ token.rating || 0 }}</span>
            </div>
            <div class="token-footer">
              <span class="token-price">
                <span class="currency">¥</span>{{ token.price }}
              </span>
              <span class="token-stock" :class="{ low: (token.stock || 0) < 10 }">
                库存 {{ token.stock || 0 }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[12, 24, 48]"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getTokenList } from '@/api/token'

const router = useRouter()
const tokens = ref<any[]>([])
const searchKeyword = ref('')
const selectedCategory = ref<number | undefined>(undefined)
const sortBy = ref('default')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const loading = ref(false)
const defaultBg = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'

const loadTokens = async () => {
  loading.value = true
  try {
    const data = await getTokenList({
      page: currentPage.value,
      size: pageSize.value,
      categoryId: selectedCategory.value,
      keyword: searchKeyword.value
    })
    tokens.value = (data.records || []).map((t: any) => ({
      ...t,
      bgColor: t.bgColor || defaultBg,
      emoji: t.emoji || '🎫'
    }))
    total.value = data.total || 0
  } catch (error) {
    console.error('加载Token列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadTokens()
}

const handleCreate = () => {
  router.push('/tokens/create')
}

const handleViewDetail = (id: number) => {
  router.push(`/tokens/${id}`)
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  loadTokens()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadTokens()
}

onMounted(() => {
  loadTokens()
})
</script>

<style scoped>
.token-list-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.search-card {
  margin-bottom: 30px;
  padding: 20px;
}

.token-grid {
  min-height: 400px;
}

.grid-layout {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.anime-token-card {
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: var(--border-card);
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  position: relative;
}

.anime-token-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
  border-color: rgba(255, 107, 157, 0.3);
}

.card-badge {
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

.token-image {
  height: 180px;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.token-emoji {
  font-size: 64px;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

.image-overlay {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.anime-token-card:hover .image-overlay {
  opacity: 1;
}

.token-info {
  padding: 15px;
}

.token-name {
  margin: 0 0 8px;
  font-size: 16px;
  color: var(--text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.token-description {
  margin: 0 0 12px;
  font-size: 13px;
  color: var(--text-secondary);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.5;
}

.token-stats {
  display: flex;
  gap: 15px;
  margin-bottom: 12px;
  font-size: 12px;
  color: var(--text-muted);
}

.token-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}

.token-price {
  font-size: 20px;
  font-weight: bold;
  color: var(--primary-pink);
}

.currency {
  font-size: 14px;
  margin-right: 2px;
}

.token-stock {
  font-size: 12px;
  color: var(--text-muted);
}

.token-stock.low {
  color: #f56c6c;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding: 20px 0;
}

@media (max-width: 768px) {
  .grid-layout {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  }
}
</style>
