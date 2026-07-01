<template>
  <div class="profile-container">
    <div class="profile-card glass-card">
      <div class="card-header">👤 ���人中心</div>
      
      <div class="avatar-section">
        <el-upload
          class="avatar-uploader"
          action="/api/v1/users/avatar"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
        >
          <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar" />
          <div v-else class="avatar-placeholder">
            <span class="avatar-icon">👤</span>
          </div>
        </el-upload>
        <div class="avatar-info">
          <h3>{{ userInfo.username || '冒险者' }}</h3>
          <p>点击上传头像</p>
        </div>
      </div>

      <el-form :model="userInfo" label-width="100px" class="profile-form">
        <el-form-item label="用户名">
          <el-input v-model="userInfo.username" disabled />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userInfo.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdate" class="save-btn">
            💾 保存修改
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateUserInfo } from '@/api/user'

const userInfo = ref({
  username: '',
  phone: '',
  email: '',
  avatar: ''
})

const loadUserInfo = async () => {
  try {
    userInfo.value = await getUserInfo()
  } catch (error) {
    console.error('加载用户信息失败:', error)
  }
}

const handleAvatarSuccess = (response: any) => {
  userInfo.value.avatar = response.data
  ElMessage.success('头像上传成功 ✨')
}

const handleUpdate = async () => {
  try {
    await updateUserInfo(userInfo.value)
    ElMessage.success('信息更新成功 🎉')
  } catch (error) {
    console.error('更新信息失败:', error)
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
  max-width: 800px;
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

.card-header {
  font-size: 20px;
  font-weight: bold;
  color: var(--text-primary);
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
}

.avatar-uploader {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid var(--primary-pink);
  box-shadow: 0 0 20px rgba(255, 107, 157, 0.3);
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff6b9d, #c44dff);
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 40px;
}

.avatar-info h3 {
  margin: 0 0 5px;
  color: var(--text-primary);
}

.avatar-info p {
  margin: 0;
  color: var(--text-muted);
  font-size: 13px;
}

.profile-form {
  max-width: 500px;
}

.save-btn {
  width: 200px;
  height: 45px;
  font-size: 16px;
}
</style>
