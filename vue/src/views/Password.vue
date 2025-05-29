<template>
  <div class="password-container">
    <el-card class="password-card" shadow="hover">
      <div class="password-header">
        <h2><el-icon><Lock /></el-icon> 修改密碼</h2>
        <p class="password-tip">請確保密碼複雜度，建議包含字母、數字和特殊字符</p>
      </div>

      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
          label-position="left"
          status-icon
      >
        <el-form-item label="原密碼" prop="password">
          <el-input
              v-model="form.password"
              type="password"
              show-password
              placeholder="請輸入當前使用的密碼"
              clearable
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="新密碼" prop="newPassword">
          <el-input
              v-model="form.newPassword"
              type="password"
              show-password
              placeholder="8-20位字符，包含字母和數字"
              clearable
          >
            <template #prefix>
              <el-icon><Key /></el-icon>
            </template>
          </el-input>
          <div class="password-strength" :class="getPasswordStrength(form.newPassword)">
            密碼強度: {{ getPasswordStrengthText(form.newPassword) }}
          </div>
        </el-form-item>

        <el-form-item label="確認密碼" prop="confirmPassword">
          <el-input
              v-model="form.confirmPassword"
              type="password"
              show-password
              placeholder="請再次輸入新密碼"
              clearable
          >
            <template #prefix>
              <el-icon><Check /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item class="submit-btn">
          <el-button
              type="primary"
              @click="handleSubmit"
              :loading="loading"
              round
          >
            <template #icon>
              <el-icon><Refresh /></el-icon>
            </template>
            确认修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Lock, Key, Check, Refresh } from '@element-plus/icons-vue'
import request from "@/utils/request.js"

const formRef = ref()
const loading = ref(false)

const form = reactive({
  password: '',
  newPassword: '',
  confirmPassword: ''
})

// 密码强度计算
const getPasswordStrength = (password) => {
  if (!password) return 'none'
  const hasLetter = /[a-zA-Z]/.test(password)
  const hasNumber = /\d/.test(password)
  const hasSpecial = /[^a-zA-Z0-9]/.test(password)
  const length = password.length

  if (length <= 5 ) return 'weak'
  // if (length >= 8 && hasLetter && hasNumber && hasSpecial) return 'strong'
  if (length >= 8 && hasLetter && hasNumber) return 'strong'
  if (length >= 6 && (hasLetter || hasNumber)) return 'medium'
  return 'weak'
}

const getPasswordStrengthText = (password) => {
  const strength = getPasswordStrength(password)
  return {
    'none': '未输入',
    'weak': '弱',
    'medium': '中',
    'strong': '强'
  }[strength]
}

// 验证规则
const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('請輸入原密碼'))
  } else {
    callback()
  }
}

const validateNewPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('請輸入新密碼'))
  } else if (value.length < 4) {
    callback(new Error('密碼長度不能少於8位'))
  } else if (!/[a-zA-Z]/.test(value) || !/\d/.test(value)) {
    callback(new Error('密碼需包含字母和數字'))
  } else {
    callback()
  }
}

const validateConfirmPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('請再次輸入密碼'))
  } else if (value !== form.newPassword) {
    callback(new Error('兩次輸入的密碼不一致'))
  } else {
    callback()
  }
}

const rules = reactive({
  password: [{ validator: validatePassword, trigger: 'blur' }],
  newPassword: [{ validator: validateNewPassword, trigger: 'blur' }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }]
})

// 提交修改
const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      const user = JSON.parse(localStorage.getItem('login-user'))
      const payload = {
        id: user.id,
        role: user.role,
        password: form.password,
        newPassword: form.newPassword
      }

      const res = await request.put('/updatePassword', payload)

      if (res.code === '200') {
        ElMessage.success({
          message: '密碼修改成功，請重新登錄',
          duration: 1500,
          onClose: () => {
            localStorage.removeItem('login-user')
            window.location.href = '/login'
          }
        })
      } else {
        ElMessage.error(res.msg || '密碼修改失敗')
      }
    } catch (error) {
      ElMessage.error('請求出錯：' + error.message)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.password-container {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
}

.password-card {
  width: 600px;
  border-radius: 12px;
  padding: 30px;
}

.password-header {
  text-align: center;
  margin-bottom: 30px;
}

.password-header h2 {
  color: var(--el-color-primary);
  font-size: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.password-tip {
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-top: 8px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

.submit-btn {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.password-strength {
  font-size: 12px;
  margin-top: 6px;
}

.password-strength.weak {
  color: var(--el-color-danger);
}

.password-strength.medium {
  color: var(--el-color-warning);
}

.password-strength.strong {
  color: var(--el-color-success);
}

:deep(.el-input__wrapper) {
  padding: 0 15px;
}

:deep(.el-input-group__prepend) {
  padding: 0 10px;
}
</style>