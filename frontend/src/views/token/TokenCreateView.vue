<template>
  <div class="token-create-container">
    <div class="create-card glass-card">
      <div class="card-header">✨ 发布Token</div>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" class="create-form">
        <el-form-item label="Token名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入Token名称" size="large" />
        </el-form-item>
        
        <el-form-item label="Token描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入Token描述"
            size="large"
          />
        </el-form-item>
        
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0.01" :precision="2" size="large" />
          <span class="price-hint">元</span>
        </el-form-item>
        
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="1" size="large" />
          <span class="stock-hint">个</span>
        </el-form-item>
        
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" size="large" style="width: 100%">
            <el-option label="通用Token" :value="1" />
            <el-option label="API Token" :value="2" />
            <el-option label="会员Token" :value="3" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <div class="form-actions">
            <el-button type="primary" @click="handleSubmit" :loading="loading" class="submit-btn">
              🚀 提交发布
            </el-button>
            <el-button @click="handleCancel" class="cancel-btn">
              取消
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createToken } from '@/api/token'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  name: '',
  description: '',
  price: 0,
  stock: 1,
  categoryId: 1
})

const rules = {
  name: [{ required: true, message: '请输入Token名称', trigger: 'blur' }],
  description: [{ required: true, message: '请输入Token描述', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

const handleSubmit = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await createToken(form)
    ElMessage.success('发布成功，等待审核 🎉')
    router.push('/tokens')
  } catch (error) {
    console.error('发布失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  router.back()
}
</script>

<style scoped>
.token-create-container {
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
  font-size: 24px;
  font-weight: bold;
  color: var(--text-primary);
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.create-form {
  max-width: 600px;
}

.price-hint,
.stock-hint {
  margin-left: 10px;
  color: var(--text-muted);
  line-height: 40px;
}

.form-actions {
  display: flex;
  gap: 15px;
}

.submit-btn {
  width: 200px;
  height: 45px;
  font-size: 16px;
}

.cancel-btn {
  width: 120px;
  height: 45px;
  font-size: 16px;
}
</style>
