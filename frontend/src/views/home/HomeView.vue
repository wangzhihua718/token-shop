<template>
  <div class="home-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <div class="logo" @click="router.push('/home')">
            <span class="logo-icon">🌸</span>
            <span class="logo-text">Token Shop</span>
          </div>
          <el-menu
            mode="horizontal"
            :router="true"
            :default-active="activeMenu"
            class="nav-menu"
            background-color="transparent"
            text-color="rgba(255,255,255,0.7)"
            active-text-color="#ff6b9d"
          >
            <el-menu-item index="/home">🏠 首页</el-menu-item>
            <el-menu-item index="/tokens">🎮 Token市场</el-menu-item>
            <el-menu-item index="/orders">📦 我的订单</el-menu-item>
            <el-menu-item index="/wallet">💰 我的钱包</el-menu-item>
            <el-menu-item index="/favorites">⭐ 我的收藏</el-menu-item>
          </el-menu>
          <div class="user-info">
            <el-dropdown @command="handleCommand">
              <span class="user-name">
                <el-avatar :size="36" :src="userStore.userInfo?.avatar || defaultAvatar" class="anime-avatar" />
                <span class="username">{{ userStore.userInfo?.username || '冒险者' }}</span>
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">👤 个人中心</el-dropdown-item>
                  <el-dropdown-item command="stats">📊 数据看板</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>🚪 退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
      <el-footer class="footer">
        <div class="footer-content">
          <p>© 2026 Token Shop - 专业的二次元Token交易平台 🌸</p>
          <div class="footer-links">
            <a href="#">关于我们</a>
            <a href="#">帮助中心</a>
            <a href="#">服务条款</a>
            <a href="#">隐私政策</a>
          </div>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ArrowDown } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'stats') {
    router.push('/stats')
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.el-header {
  background: rgba(10, 10, 26, 0.8) !important;
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  height: 64px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 24px;
  margin-right: 40px;
  cursor: pointer;
}

.logo-icon {
  font-size: 28px;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.logo-text {
  background: linear-gradient(135deg, #ff6b9d, #c44dff, #4dc8ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: bold;
}

.nav-menu {
  flex: 1;
  border-bottom: none;
  background: transparent !important;
}

.nav-menu .el-menu-item {
  color: rgba(255, 255, 255, 0.7) !important;
  transition: all 0.3s;
}

.nav-menu .el-menu-item:hover {
  color: #ff6b9d !important;
  background: rgba(255, 107, 157, 0.1) !important;
}

.nav-menu .el-menu-item.is-active {
  color: #ff6b9d !important;
  border-bottom: 2px solid #ff6b9d !important;
}

.user-info {
  margin-left: 20px;
}

.user-name {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: var(--text-primary);
}

.anime-avatar {
  border: 2px solid var(--primary-pink);
  box-shadow: 0 0 15px rgba(255, 107, 157, 0.3);
}

.username {
  margin-right: 4px;
}

.footer {
  background: rgba(10, 10, 26, 0.9);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  color: var(--text-secondary);
  padding: 40px 0;
  margin-top: auto;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  text-align: center;
}

.footer-links {
  margin-top: 15px;
  display: flex;
  justify-content: center;
  gap: 30px;
}

.footer-links a {
  color: var(--text-muted);
  text-decoration: none;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: var(--primary-pink);
}
</style>
