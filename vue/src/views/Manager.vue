<template>
  <div>
    <!-- 导航栏 -->
    <div style="height: 60px; background-color: #42b883; display: flex; align-items: center; padding-left: 10px">
      <div style="width: 200px; display: flex; align-items: center; padding-left: 20px">
        <img style="width: 40px; margin-right: 5px;" src="@/assets/logo.png" alt="">
        <span style="font-size:20px; color: white;">後台管理系統</span>

      </div>
      <div style="flex: 1;"></div>
      <div style="width: 100px; display: flex; align-content: center">
        <!--        <el-icon :size="45"><Avatar /></el-icon>-->
        <img :src="data.user.avatar || randomAvatar " alt="" style=" margin-right: 5px; width: 40px; height: 40px; border-radius: 50%;">
        <span style="width: 200px; display: flex; align-items: center; font-size: 15px; color: white">{{ data.user.name }}</span>
      </div>
    </div>

    <!-- 主体 -->
    <div style="display: flex">
      <!-- 左侧导航栏 -->
      <div style="width: 150px; border-right:1px solid #ddd ; min-height: calc(100vh - 60px)">
        <el-menu router :default-active="router.currentRoute.value.path" style="border: 0">
          <el-menu-item index="/manager/home"><el-icon><House /></el-icon>系統首頁</el-menu-item>
          <el-menu-item index="/manager/data"><el-icon><DataAnalysis /></el-icon>數據統計</el-menu-item>
          <a href="https://liuli.ntubbirc.ggff.net/products.html" target="blank" style="text-decoration: none;">
            <el-menu-item>
              <el-icon><View /></el-icon>
              產品展示
            </el-menu-item>
          </a>
          <!-- 产品管理菜单，只對 Admin 顯示 -->
          <el-menu-item index="/manager/department" v-if="data.user.role === 'Admin'">
              <el-icon><officeBuilding /></el-icon>部門管理
          </el-menu-item>

          <!-- 产品管理菜单，只對 Admin 顯示 -->
          <el-sub-menu index="1" v-if="data.user.role === 'Admin'">
            <template #title>
              <el-icon><HelpFilled /></el-icon>
              <span>產品管理</span>
            </template>
            <el-menu-item index="/manager/category">
              <el-icon><Operation /></el-icon>產品類別
            </el-menu-item>
            <el-menu-item index="/manager/product">
              <el-icon><Present /></el-icon>產品管理
            </el-menu-item>
          </el-sub-menu>

          <!-- 用戶管理菜单，只對 Admin 顯示 -->
          <el-sub-menu index="2" v-if="data.user.role === 'Admin'">
            <template #title>
              <el-icon><UserFilled /></el-icon>
              <span>用戶管理</span>
            </template>
            <el-menu-item index="/manager/admin">
              <el-icon><Avatar /></el-icon>管理員信息
            </el-menu-item>
            <el-menu-item index="/manager/employee">
              <el-icon><User /></el-icon>員工信息
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/manager/person">
            <el-icon><User /></el-icon>個人信息
          </el-menu-item>
          <el-menu-item index="/manager/password">
            <el-icon><Lock /></el-icon>修改密码
          </el-menu-item>

          <!-- 产品管理菜单，只對 Admin 顯示 -->
          <el-menu-item index="/manager/log" v-if="data.user.role === 'Admin'">
            <el-icon><Clock /></el-icon>查看日誌
          </el-menu-item>
          <el-menu-item @click="logout">
            <el-icon><SwitchButton /></el-icon>退出登錄
          </el-menu-item>
        </el-menu>
      </div>
      <!--  右侧主体区域  -->
      <div style="flex:1; width: 0; background-color: #EFF9EB">
        <router-view @updateUser="updateUser"></router-view>
        <nav style="margin-bottom: 20px; font-size: 24px; font-weight: bold; padding: 20px">
          <router-link to="/manager/home">主頁</router-link> |
          <router-link to="/manager/404">副頁</router-link>
        </nav>
        <div style="margin-bottom: 30px;">
          <el-button @click="backward" type="success" plain size="default" >後退</el-button>
          <el-button @click="forward" type="success" plain size="default" >前進</el-button>
        </div>
      </div>
    </div>


  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router/index.js";
import dogImage from '@/assets/dog.jpg'
import foxImage from '@/assets/fox.jpg'
import kapiImage from '@/assets/kapi.jpg'
import duckImage from '@/assets/duck.jpg'
import dog1Image from '@/assets/dog1.jpg'
import flower from '@/assets/flower.jpg'
import rubbit from '@/assets/rubbit.jpg'
//import router from '@/router/index.js'; // 因为在 main.js 中已经通过 app.use(router) 注册了 router 实例，故不需要手动导入

// 預設頭像陣列
// const defaultAvatars = [dogImage, kapiImage, foxImage, duckImage, dog1Image]
const defaultAvatars = [kapiImage]

// 隨機選一張作為預設
const randomAvatar = defaultAvatars[Math.floor(Math.random() * defaultAvatars.length)]

import { useRouter } from 'vue-router'; //useRouter() 会自动找到 Vue 应用注册的 router 实例

const data = reactive({
  user: JSON.parse(localStorage.getItem('login-user')) //将Json字符串 转换成Json对象
})

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('login-user'))
}

const logout = () => {
  localStorage.removeItem('login-user') //清除当前登录用户的缓存数据
  location.href = '/login' //退出登录页面
}

const routers = useRouter(); // ✅ Vue 3 的方式

const backward = () => routers.go(-1);
const forward = () => routers.go(1);
let isHomePage = true; // 初始为首页
const toggleNavigation = () => {
  if (isHomePage) {
    routers.push('/manager/test'); // 跳转到 /test 页面
  } else {
    routers.push('/manager/home'); // 跳转到首页
  }
  isHomePage = !isHomePage; // 切换状态
};
</script>

<style scoped>
.el-menu .is-active {
  background-color: #a1dcc1;
}

.el-sub-menu .is-active{
  background-color: #d9f1e6;
}
</style>