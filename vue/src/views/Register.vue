<template>
  <div class="register-box">
    <el-form class="register-form" ref="formRef" :model="data.form" :rules="data.rules" @submit.prevent="register">
      <h2>用户注册</h2>

      <el-form-item class="form-item" prop="name">
        <label>姓名：</label>
        <div class="input-wrapper">
          <el-input
              type="text"
              v-model="data.form.name"
              placeholder="請輸入您的名稱"
              :prefix-icon="User"
              size="large"
          />
        </div>
      </el-form-item>

      <el-form-item class="form-item" prop="username">
        <label>账号：</label>
        <div class="input-wrapper">
          <el-input
              type="text"
              v-model="data.form.username"
              placeholder="請輸入賬號"
              :prefix-icon="User"
              size="large"
          />
        </div>
      </el-form-item>

      <el-form-item class="form-item" prop="password">
        <label>密码：</label>
        <div class="input-wrapper">
          <el-input
              type="password"
              v-model="data.form.password"
              placeholder="請輸入密碼"
              :prefix-icon="Lock"
              size="large"
              show-password
          />
        </div>
      </el-form-item>

      <el-form-item class="form-item" prop="confirmpassword">
        <label>确认：</label>
        <div class="input-wrapper">
          <el-input
              type="password"
              v-model="data.form.confirmpassword"
              placeholder="請確認密碼"
              :prefix-icon="Lock"
              size="large"
              show-password
          />
        </div>
      </el-form-item>

      <el-button native-type="submit" class="btn-submit">註冊</el-button>

      <p class="login-link">
        <a href="/login">已有賬號？ 登錄</a>
      </p>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from "@/utils/request.js"
import { ElMessage } from "element-plus"
import { User, Lock } from "@element-plus/icons-vue"

const formRef = ref()
const router = useRouter()
const validatePass = (rule, value, callback) => {
  if(!value) {
    callback(new Error('請再次輸入密碼'))
  }
  else if (value !== data.form.password) {
    callback(new Error('兩次輸入的密碼不一致'))
  } else {
    callback()
  }
}
const data = reactive({
  form: {
    name: '',
    username: '',
    password: '',
    confirmpassword: ''
  },
  rules: {
    name: [
      {required: true, message: '請輸入名稱', trigger: 'blur'},
      {min: 1, max: 12, message: '長度在2到10個字符之間', trigger: 'blur'}
    ],
    username: [
      {required: true, message: '請輸入賬號', trigger: 'blur'},
      {min: 4, max: 12, message: '長度在6到12個字符之間', trigger: 'blur'}
    ],
    password: [
      {required: true, message: '請輸入密碼', trigger: 'blur'},
      {min: 8, message: '至少8個字符', trigger: 'blur'}
    ],
    confirmpassword: [
      {validator: validatePass, trigger: 'blur'},
    ]
  },
  loading: false
})

const register = () => {
  formRef.value.validate((valid) => {
    if(valid) {
      data.loading = true
      request.post('/register', data.form)
          .then(res => {
            if (res.code === '200') {
              ElMessage.success({
                message: '註冊成功',
                duration: 1500,
                onClose: () => {
                  router.push('/login')
                }
              })
            } else {
              ElMessage.error(res.msg)
            }
          })
          .finally(() => {
            data.loading = false
          })
    }
  })
}

</script>

<style scoped>
* {
  box-sizing: border-box;
}

.register-box {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: url("@/assets/landscape.jpg") no-repeat center center;
  background-size: cover;
  backdrop-filter: blur(8px);
}

.register-form {
  width: 360px;
  padding: 25px;
  display: flex;
  text-align: center;
  flex-direction: column;
  border-radius: 16px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(5px) brightness(1.05);
  background: linear-gradient(135deg, rgba(80, 200, 120, 0.2), rgba(162, 221, 244, 0.2));
}

.register-form h2 {
  color: #4EC28A;
  font-size: 32px;
  font-weight: 700;
  letter-spacing: 1.5px;
  margin-bottom: 10px;
  text-shadow:
      0 0 4px rgba(162, 221, 244, 0.5),
      0 2px 6px rgba(0, 0, 0, 0.25),
      1px 1px 0px rgba(255, 255, 255, 0.3);
}

.register-form h2::after {
  content: "";
  display: block;
  margin: 4px auto 0;
  width: 60%;
  height: 2px;
  background: linear-gradient(to right, #A2DDF4, #50C878);
  opacity: 0.5;
  border-radius: 2px;
}

/* ✅ 表單項目樣式 */
.form-item {
  margin-bottom: 18px;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  text-align: left;
}

.form-item label {
  color: #0074b3;
  font-size: 15px;
  margin-bottom: 4px;
  padding-left: 2px;
}

/* ✅ input 包裝與 underline */
.input-wrapper {
  width: 80%;
}

:deep(.el-input__wrapper) {
  padding: 10px 12px;
  font-size: 15px;
  border: none;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.7);
  box-shadow: 2px 2px 6px rgba(80, 200, 120, 0.15);
  transition: box-shadow 0.3s;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 8px rgba(80, 200, 120, 0.5) !important;
}

:deep(.el-input__inner) {
  padding: 0;
}

:deep(.el-input__prefix) {
  margin-right: 8px;
  color: #4EC28A;
}



/* ✅ 錯誤提示樣式一致性 */
:deep(.el-form-item__error) {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 0px;
  padding-left: 48px;
}

/* ✅ 按鈕優化 */
.btn-submit {
  background: linear-gradient(135deg, #50C878, #A2DDF4);
  color: white;
  width: 80%;
  align-self: center;
  font-size: 18px;
  border: none;
  padding: 12px 0;
  border-radius: 12px;
  cursor: pointer;
  box-shadow: 0 8px 16px rgba(80, 200, 120, 0.3);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.btn-submit::before {
  content: '';
  position: absolute;
  top: 0;
  left: -75%;
  width: 50%;
  height: 100%;
  background: linear-gradient(
      120deg,
      rgba(255, 255, 255, 0.2) 0%,
      rgba(255, 255, 255, 0.4) 50%,
      rgba(255, 255, 255, 0.2) 100%
  );
  transform: skewX(-20deg);
}

.btn-submit:hover::before {
  animation: shimmer 1s ease forwards;
}

@keyframes shimmer {
  0% { left: -75%; }
  100% { left: 125%; }
}

.btn-submit:hover {
  filter: brightness(1.1);
  box-shadow: 0 10px 20px rgba(80, 200, 120, 0.4);
}

/* ✅ 已有帳號連結 */
.login-link {
  margin-top: 15px;
  text-align: right;
}

.login-link a {
  color: #0074b3;
  text-decoration: none;
  font-size: 14px;
  cursor: pointer;
}

</style>