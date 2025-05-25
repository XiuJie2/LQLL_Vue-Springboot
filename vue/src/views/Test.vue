<template>
  <div>
    <div style="font-size: 20px; font-weight: bold; color: #42b883; text-decoration: underline; font-style: italic; margin-bottom: 20px;">
      欢迎您使用vue-inspector,骄傲的小公主
    </div>

    <div style="font-size: 16px; color: #42b883; margin-bottom: 20px;">
      {{ b }}
      {{ data.c }}
      {{ data.d }}
    </div>

    <div style="margin-bottom: 20px;">
      <input v-model="data.c" />
    </div>

    <div style="margin-bottom: 20px;">
      <span style="color: red" v-if="data.name === '佩奇'">小猪佩奇</span>
      <span style="color: blue" v-if="data.name === '乔治'">小猪乔治</span>
      <span style="color: hotpink" v-else>小猪朵朵</span>
    </div>

    <div style="display: flex; justify-content: space-between; flex-wrap: wrap;">
      <div style="width:200px; height: 200px; background-color: #42b883; line-height: 200px; margin: 30px; font-size: 50px" v-for="item in data.arr">
        {{ item }}
      </div>
    </div>

    <div style="margin-bottom: 20px;">
      <select style="width: 200px; margin-right: 20px;">
        <option value="1">苹果</option>
        <option value="2">香蕉</option>
        <option value="3">橘子</option>
      </select>

      <select style="width: 200px; margin-right: 20px;">
        <option v-for="item in data.fruit">{{ item }}</option>
      </select>

    </div>

    <div style="margin-bottom: 20px;">
      <button @click="click">点我加好运</button>
    </div>

    <!--    display: flex; #弹性分布 -->
    <!--    justify-content: space-between; #让方块均匀分布，收尾对齐 -->
    <!--    flex-wrap: wrap; #当空间不足时自动换行 -->
    <div style="display: flex; justify-content: space-between; flex-wrap: wrap;">
      <div :style="data.box" style="text-align: center;  line-height: 100px; margin-right: 80px; margin-left: 80px; margin-bottom: 30px; font-size: 20px" v-for="item in data.arr">{{ item }}
      </div>
    </div>

    <div style="margin-bottom: 20px;" >
      <img :src="data.img" alt="">
    </div>

    <div style="margin: 20px;">
      <el-button @click="clickData" :style="{ color: 'red'}">成功</el-button>
      <el-button type="primary">失败</el-button>
      <el-button type="success" plain size="large" >Success</el-button>
      <el-button type="info">Info</el-button>
      <el-button type="warning">Warning</el-button>
      <el-button type="danger">Danger</el-button>
    </div>

    <div style="margin: 50px;">
      <el-icon :size="20">
        <Edit/>
      </el-icon>
      <span style="margin-left: 10px; color: #42b753">
        <el-icon :size="20" color="#42b883" style="top: 4px"><View/></el-icon>100
      </span>

      <el-button type="danger" icon="Delete" circle style="margin: 30px"></el-button>

      <el-input v-model="data.d" style="width: 240px" placeholder="Type something" prefix-icon="Search"></el-input>
    </div>

  </div>
</template>

<script setup>
import {reactive, ref, onMounted} from "vue";
import router from "@/router/index.js"
const b = ref("明天您好")
const data = reactive({
  c: 123,
  d: "apple",
  name: '治',
  arr: ["红桃A","黑桃B","梅花C"],
  fruit: ['苹果', '橘子', '香蕉'],
  box: {
    width: '100px',
    height: '100px',
    background: "yellow"
  },
  img: 'https://www.runoob.com/wp-content/uploads/2017/01/vue.png',
  id: router.currentRoute.value.query.id
})
onMounted(() => { console.log("页面加载完成") })

const clickData = () => {
  alert("我喜欢你董小姐")
}

const click = () => {
  alert("好运+1")
}

</script>


