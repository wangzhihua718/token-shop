<template>
  <div class="favorite-container">
    <div class="header-card glass-card">
      <div class="header">
        <h2>⭐ 我的收藏</h2>
        <el-button type="primary" @click="router.push('/tokens')">去逛逛 →</el-button>
      </div>
    </div>
    
    <el-empty v-if="favorites.length === 0" description="还没有收藏任何Token" class="empty-state">
      <el-button type="primary" @click="router.push('/tokens')">去浏览Token</el-button>
    </el-empty>

    <div v-else class="grid-layout">
      <div v-for="token in favorites" :key="token.id" class="anime-token-card">
        <div class="card-header">
          <el-button 
            type="danger" 
            size="small" 
            circle
            @click="handleUnfavorite(token.id)"
          >⭐</el-button>
        </div>
        <div class="token-image" :style="{ background: token.bgColor || defaultBg }" @click="router.push(`/tokens/${token.id}`)">
          <span class="token-emoji">{{ token.emoji || '🎫' }}</span>
        </div>
        <div class="token-info">
          <h3 @click="router.push(`/tokens/${token.id}`)">{{ token.name }}</h3>
          <p class="description">{{ token.description }}</p>
          <div class="token-meta">
            <span class="price">¥{{ token.price }}</span>
            <el-button type="primary" size="small" @click="router.push(`/tokens/${token.id}`)">
              立即购买
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const favorites = ref<any[]>([])
const defaultBg = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'

const handleUnfavorite = (id: number) => {
  favorites.value = favorites.value.filter(item => item.id !== id)
  ElMessage.success('已取消收藏')
}
</script>

<style scoped>
.favorite-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.header-card {
  padding: 20px;
  margin-bottom: 20px;
}

.glass-card {
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: var(--border-card);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h2 {
  margin: 0;
  color: var(--text-primary);
}

.empty-state {
  margin-top: 100px;
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
  transition: all 0.3s;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  position: relative;
}

.anime-token-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
  border-color: rgba(255, 107, 157, 0.3);
}

.card-header {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 2;
}

.token-image {
  height: 180px;
  cursor: pointer;
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
  margin: 0 0 8px;
  font-size: 16px;
  cursor: pointer;
  color: var(--text-primary);
}

.token-info h3:hover {
  color: var(--primary-pink);
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
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: var(--primary-pink);
}
</style>
