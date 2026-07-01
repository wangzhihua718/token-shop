<template>
  <div class="register-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="orb orb-1"></div>
      <div class="orb orb-2"></div>
      <div class="orb orb-3"></div>
    </div>

    <div class="register-card">
      <div class="card-header">
        <h1 class="logo">
          <span class="logo-icon">🌸</span>
          <span class="logo-text">Token Shop</span>
        </h1>
        <p class="subtitle">创建账号，开始你的冒险之旅 ✨</p>
      </div>

      <el-form :model="registerForm" :rules="rules" ref="formRef" class="register-form">
        <div class="form-item">
          <label class="form-label">用户名</label>
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名 (3-20个字符)"
            size="large"
          >
            <template #prefix>
              <span class="input-icon">👤</span>
            </template>
          </el-input>
        </div>

        <div class="form-item">
          <label class="form-label">手机号</label>
          <el-input
            v-model="registerForm.phone"
            placeholder="请输入手机号"
            size="large"
          >
            <template #prefix>
              <span class="input-icon">📱</span>
            </template>
          </el-input>
        </div>

        <div class="form-item">
          <label class="form-label">邮箱</label>
          <el-input
            v-model="registerForm.email"
            placeholder="请输入邮箱地址"
            size="large"
          >
            <template #prefix>
              <span class="input-icon">📧</span>
            </template>
          </el-input>
        </div>

        <div class="form-item">
          <label class="form-label">密码</label>
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码 (6-20个字符)"
            size="large"
            show-password
          >
            <template #prefix>
              <span class="input-icon">🔒</span>
            </template>
          </el-input>
        </div>

        <div class="form-item">
          <label class="form-label">确认密码</label>
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            size="large"
            show-password
          >
            <template #prefix>
              <span class="input-icon">🔒</span>
            </template>
          </el-input>
        </div>

        <el-button
          type="primary"
          size="large"
          class="register-btn"
          :loading="loading"
          @click="handleRegister"
        >
          {{ loading ? '注册中...' : '创建账号 🚀' }}
        </el-button>

        <div class="form-footer">
          <span>已有账号？</span>
          <router-link to="/login" class="login-link">立即登录 →</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/user'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  phone: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await register({
      username: registerForm.username,
      phone: registerForm.phone,
      email: registerForm.email,
      password: registerForm.password
    })
    ElMessage.success('注册成功，请登录 🎉')
    router.push('/login')
  } catch (error) {
    console.error('注册失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.bg-decoration {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.3;
  animation: float 20s ease-in-out infinite;
}

.orb-1 {
  width: 400px;
  height: 400px;
  background: #ff6b9d;
  top: -100px;
  left: -100px;
}

.orb-2 {
  width: 300px;
  height: 300px;
  background: #c44dff;
  bottom: -50px;
  right: -50px;
  animation-delay: -5s;
}

.orb-3 {
  width: 200px;
  height: 200px;
  background: #4dc8ff;
  top: 50%;
  left: 50%;
  animation-delay: -10s;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(30px, -30px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
}

.register-card {
  width: 100%;
  max-width: 480px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 24px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  position: relative;
  z-index: 1;
}

.card-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 28px;
  margin: 0;
}

.logo-icon {
  font-size: 36px;
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

.register-form {
  margin-top: 20px;
}

.form-item {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  color: var(--text-secondary);
  font-size: 14px;
}

.input-icon {
  font-size: 18px;
}

.register-btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
  border-radius: 25px;
  background: linear-gradient(135deg, #ff6b9d, #c44dff) !important;
  border: none !important;
  box-shadow: 0 5px 20px rgba(255, 107, 157, 0.4);
  margin-top: 10px;
  transition: all 0.3s;
}

.register-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(255, 107, 157, 0.6);
}

.form-footer {
  text-align: center;
  color: var(--text-secondary);
  margin-top: 20px;
}

.login-link {
  color: var(--primary-pink);
  text-decoration: none;
  margin-left: 8px;
  font-weight: 500;
  transition: all 0.3s;
}

.login-link:hover {
  color: var(--primary-purple);
  text-shadow: 0 0 10px rgba(255, 107, 157, 0.5);
}
</style>
