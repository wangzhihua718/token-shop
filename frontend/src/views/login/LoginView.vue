<template>
  <div class="login-container">
    <!-- 背景星星 -->
    <div class="stars">
      <div v-for="i in 50" :key="i" class="star" :style="getStarStyle(i)"></div>
    </div>

    <!-- 装饰元素 -->
    <div class="floating-shapes">
      <div class="shape shape-1">✦</div>
      <div class="shape shape-2">⋆</div>
      <div class="shape shape-3">✧</div>
      <div class="shape shape-4">⟡</div>
    </div>

    <el-card class="login-card">
      <template #header>
        <div class="login-header">
          <h1 class="logo">
            <span class="logo-icon">🌸</span>
            <span class="logo-text">Token Shop</span>
          </h1>
          <p class="subtitle">欢迎来到二次元Token世界 ✨</p>
        </div>
      </template>

      <el-form :model="loginForm" :rules="rules" ref="formRef" label-width="0" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            size="large"
            class="anime-input"
          >
            <template #prefix>
              <span class="input-icon">👤</span>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            size="large"
            show-password
            @keyup.enter="handleLogin"
            class="anime-input"
          >
            <template #prefix>
              <span class="input-icon">🔒</span>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            <span v-if="!loading">开始冒险 ⚔️</span>
            <span v-else>登录中...</span>
          </el-button>
        </el-form-item>

        <div class="login-footer">
          <span>还没有账号？</span>
          <router-link to="/register" class="register-link">立即注册 ✨</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const getStarStyle = (i: number) => ({
  left: `${Math.random() * 100}%`,
  top: `${Math.random() * 100}%`,
  animationDelay: `${Math.random() * 3}s`,
  animationDuration: `${2 + Math.random() * 3}s`
})

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const data = await login(loginForm)
    userStore.setToken(data.token)
    ElMessage.success('登录成功，冒险开始！⚔️')
    router.push('/home')
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

/* 星星背景 */
.stars {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.star {
  position: absolute;
  width: 2px;
  height: 2px;
  background: white;
  border-radius: 50%;
  animation: twinkle 3s ease-in-out infinite;
}

@keyframes twinkle {
  0%, 100% { opacity: 0.2; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.5); }
}

/* 浮动装饰 */
.floating-shapes {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.shape {
  position: absolute;
  font-size: 24px;
  color: rgba(255, 107, 157, 0.3);
  animation: float 6s ease-in-out infinite;
}

.shape-1 { top: 10%; left: 10%; animation-delay: 0s; }
.shape-2 { top: 20%; right: 15%; animation-delay: 1s; color: rgba(196, 77, 255, 0.3); }
.shape-3 { bottom: 30%; left: 20%; animation-delay: 2s; color: rgba(77, 200, 255, 0.3); }
.shape-4 { bottom: 15%; right: 10%; animation-delay: 3s; }

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

.login-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.1) !important;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
  border-radius: 24px !important;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3), 0 0 40px rgba(255, 107, 157, 0.2) !important;
  position: relative;
  z-index: 1;
}

.login-header {
  text-align: center;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 32px;
  margin: 0;
}

.logo-icon {
  font-size: 40px;
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

.subtitle {
  color: var(--text-secondary);
  margin: 10px 0 0;
  font-size: 14px;
}

.login-form {
  margin-top: 20px;
}

.anime-input :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.05) !important;
  border-radius: 12px !important;
  box-shadow: none !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  transition: all 0.3s;
}

.anime-input :deep(.el-input__wrapper:hover) {
  border-color: rgba(255, 107, 157, 0.5) !important;
}

.anime-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary-pink) !important;
  box-shadow: 0 0 0 2px rgba(255, 107, 157, 0.2) !important;
}

.input-icon {
  font-size: 18px;
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
  border-radius: 25px;
  background: linear-gradient(135deg, #ff6b9d, #c44dff) !important;
  border: none !important;
  box-shadow: 0 5px 20px rgba(255, 107, 157, 0.4);
  transition: all 0.3s;
}

.login-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(255, 107, 157, 0.6);
}

.login-footer {
  text-align: center;
  color: var(--text-secondary);
  margin-top: 20px;
}

.register-link {
  color: var(--primary-pink);
  text-decoration: none;
  margin-left: 8px;
  font-weight: 500;
  transition: all 0.3s;
}

.register-link:hover {
  color: var(--primary-purple);
  text-shadow: 0 0 10px rgba(255, 107, 157, 0.5);
}
</style>
