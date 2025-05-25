<template>
  <div>
    <img src="@/assets/404.png" alt="">
    <div style="margin-top: 400px">
      <el-button type="primary" style="width: 90px; height: 40px; font-size: 20px" @click="goHome">返回主頁</el-button>
    </div>
  </div>
</template>
<script setup>
const goHome = () => {
  location.href = '/manager/home'
}
</script>
<style scoped>
 img {
   position: absolute;
   left: 50%;
   top: 1px;
   transform: translateX(-50%);
   max-width: 100%;
   height: auto;
 }
</style>
