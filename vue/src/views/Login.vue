<template>
  <div class="login-box">
    <el-form class="login-form" ref="formRef" :model="data.form" :rules="data.rules">
      <h2>綠沁琉璃管理後台</h2>
      <p class="msg-warn">公共场所不建议自动登录，以防账号丢失</p>

      <el-form-item class="form-item" prop="username">
        <el-input
            v-model="data.form.username"
            placeholder="请输入账号"
            :prefix-icon="User"
            size="large"
        />
      </el-form-item>

      <el-form-item class="form-item" prop="password">
        <el-input
            show-password
            v-model="data.form.password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            size="large"
        />
      </el-form-item>

      <el-form-item class="form-item" prop="role">
        <el-radio-group v-model="data.form.role">
          <el-radio :value="'User'">用户</el-radio>
          <el-radio :value="'Admin'">管理员</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-button @click="login" class="btn-submit">登录</el-button>

      <p class="register-link">
        <a href="/register">注册用户</a>
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

const data = reactive({
  form: {
    username: '',
    password: '',
    role: 'User'
  },
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    role: [{ required: true, message: '请选择角色', trigger: 'change' }]
  }
})

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          localStorage.setItem('login-user', JSON.stringify(res.data))
          ElMessage.success('登录成功')
          location.href = '/manager/home'
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.login-box {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: url("@/assets/landscape.jpg") no-repeat center center;
  background-size: cover;
  backdrop-filter: blur(8px);
}

.login-form {
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


.login-form h2 {
  color: #4EC28A; /* 實心的翡翠主色，避免透明過淡 */
  font-size: 32px;
  font-weight: 700;
  letter-spacing: 1.5px;
  margin-bottom: 1px;

  text-shadow:
      0 0 4px rgba(162, 221, 244, 0.9), /* 琉璃藍的柔光 */
      0 2px 6px rgba(0, 0, 0, 0.25),    /* 陰影立體感 */
      1px 1px 0px rgba(255, 255, 255, 0.4); /* 微微提亮描邊 */
}

.login-form h2::after {
  content: "";
  display: block;
  margin: -5px auto 0;
  width: 86%;
  height: 2px;
  background: linear-gradient(to right, #62ddaf, #22f668);
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
  box-shadow: 0 0 8px rgba(80, 200, 120, 0.5);
}

:deep(.el-input__inner) {
  padding: 0;
}

:deep(.el-input__prefix) {
  margin-right: 8px;
  color: #4EC28A;
}

.msg-warn {
  color: #0074b3;
  font-size: 15px;
  margin-bottom: 20px;
}

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
  backdrop-filter: brightness(1.05) saturate(1.2);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
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
  0% {
    left: -75%;
  }
  100% {
    left: 125%;
  }
}

.btn-submit:hover {
  filter: brightness(1.1);
  box-shadow: 0 10px 20px rgba(80, 200, 120, 0.4);
}

.register-link {
  text-align: left;
  margin-top: 15px;
  padding-left: 8px;
}

.register-link a {
  color: #caa500;
  text-decoration: none;
  font-size: 14px;
  cursor: pointer;
}
</style>
