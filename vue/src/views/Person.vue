<template>
  <div class="profile-container">
    <el-card class="profile-card" shadow="hover">
      <div class="profile-header">
        <h2>个人信息</h2>
        <el-button
            type="primary"
            @click="toggleEditMode"
            :icon="isEditMode ? 'Check' : 'Edit'"
        >
          {{ isEditMode ? '保存信息' : '编辑信息' }}
        </el-button>
      </div>

      <el-form
          ref="formRef"
          :rules="rules"
          :model="form"
          label-width="100px"
          label-position="left"
      >
        <!-- 头像上传 -->
        <el-form-item label="头像" class="avatar-item">
          <el-upload
              class="avatar-uploader"
              action="https://black.ntubbirc.ggff.net/api/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :disabled="!isEditMode"
              :data="uploadData"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar"/>
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>

        <!-- 基本信息 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="名稱" prop="name">
              <el-input
                  v-model="form.name"
                  :disabled="!isEditMode"
                  placeholder="請輸入名稱"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="賬號" prop="username">
              <el-input
                  v-model="form.username"
                  disabled
                  placeholder="請輸入賬號"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工號" prop="no">
              <el-input
                  v-model="form.no"
                  disabled
                  placeholder="請輸入工號"
              />
            </el-form-item>
          </el-col>
          <el-col v-if="form.role === 'User'" :span="12">
            <el-form-item label="性別" prop="sex">
              <el-radio-group v-model="form.sex" :disabled="!isEditMode">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 用户特有信息 -->
        <template v-if="form.role === 'User'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="年齡" prop="age">
                <el-input-number
                    v-model="form.age"
                    :min="18"
                    :max="100"
                    :disabled="!isEditMode"
                    style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="個人簡介" prop="description">
            <el-input
                type="textarea"
                :rows="3"
                v-model="form.description"
                :disabled="!isEditMode"
                placeholder="請輸入個人簡介"
            />
          </el-form-item>
        </template>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request.js'

const formRef = ref()
const isEditMode = ref(false)

// 表單資料
const form = reactive({
  id: '',
  name: '',
  username: '',
  no: '',
  avatar: '',
  role: '',
  sex: '男',
  age: 18,
  description: ''
})

// 驗證規則
const rules = reactive({
  name: [
    { required: false, message: '請輸入名稱', trigger: 'blur' }
  ]
})

// 取得當前使用者
const user = JSON.parse(localStorage.getItem('login-user'))
form.role = user.role

// 根據角色定義 API 路徑
const apiBase = form.role === 'User' ? '/employee' : '/admin'

// 初始化資料
onMounted(() => {
  request.get(`${apiBase}/selectById/${user.id}`).then(res => {
    Object.assign(form, res.data)
  })
})

// 切換編輯模式
const toggleEditMode = () => {
  if (isEditMode.value) {
    formRef.value.validate(valid => {
      if (valid) {
        updateUser()
      }
    })
  }
  isEditMode.value = !isEditMode.value
}

// 更新使用者資料
const updateUser = () => {
  request.put(`${apiBase}/update`, form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      localStorage.setItem('login-user', JSON.stringify(form))
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 頭像上傳成功後處理
const handleAvatarSuccess = (res) => {
  form.avatar = res.data.url
  ElMessage.success('頭像上傳成功')
}

// 上傳附加參數
const uploadData = {
  username: user.username,
  name: user.name
}
</script>



<style scoped>
.profile-container {
  padding: 20px;
  display: flex;
  justify-content: center;
}

.profile-card {
  width: 800px;
  border-radius: 12px;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--el-border-color-light);
}

.profile-header h2 {
  margin: 0;
  color: var(--el-text-color-primary);
}

.avatar-item {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.avatar-uploader {
  text-align: center;
}

.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--el-border-color-light);
}

.el-form-item {
  margin-bottom: 22px;
}

.el-textarea :deep(.el-textarea__inner) {
  min-height: 80px;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
</style>