<template>
  <div>
    <div class="card" style="margin-bottom: 5px; ">
      <el-card style="border-radius: 10px; margin-bottom: 5px;">
        <el-input style="width: 240px;  margin-right: 5px;" v-model="data.name" placeholder="請輸入操作人查詢" prefix-icon="Search" @keyup.enter="load" ></el-input>
        <el-input style="width: 240px;  margin-right: 5px;" v-model="data.type" placeholder="請輸入操作類型查詢" prefix-icon="Search" @keyup.enter="load" ></el-input>


        <el-button type="primary" @click="load">查 詢</el-button>
        <el-button type="warning" @click="reset">重 置</el-button>
      </el-card>

      <el-card style="border-radius: 10px; margin-bottom: 5px;">
        <el-button type="danger" @click="delBatch">批量删除</el-button>
        <el-upload
            style="display: inline-block; margin: 0 8px"
            action="https://black.ntubbirc.ggff.net/api/log/import"
        >
        </el-upload>
        <el-button type="success" @click="exportData">導 出</el-button>
      </el-card>

      <el-card style="margin-bottom: 5px; ">
        <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="30"></el-table-column>
          <el-table-column prop="time" label="操作時間"/>
          <el-table-column prop="name" label="操作人"/>
          <el-table-column prop="username" label="操作人賬號"/>
          <el-table-column prop="type" label="操作內容"/>
          <el-table-column prop="json" label="詳細信息" show-overflow-tooltip/>
          <el-table-column prop="ip" label="ip地址"/>
          <el-table-column label="操作" align="center" width="100">
            <template #default="scope">
              <!--              <el-button link type="primary">编辑</el-button>-->
              <!--              <el-button link type="danger">删除</el-button>-->
              <el-button @click="del(scope.row.id)" type="danger" :icon="Delete" circle></el-button>
            </template>
          </el-table-column>

        </el-table>

        <el-pagination style="margin-top: 10px"
                       @current-change = "load"
                       @size-change = "load"
                       v-model:current-page="data.pageNum"
                       v-model:page-size="data.pageSize"
                       :page-sizes="[5, 10, 15, 20]"
                       background
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="data.total"
        />
      </el-card>
    </div>


  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { Edit, Delete} from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data = reactive({
  name: null,
  username: null,
  type: null,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  ids: [],
  categoryList: [],
  rule: {
  }
})

const formRef = ref()
const user = JSON.parse(localStorage.getItem("login-user"));
const exportData = () => {
  //导出数据 是通过流的形式下载 Excel  打开流的链接 浏览器会自动下载文件
  window.open('https://black.ntubbirc.ggff.net/api/log/export')
  request.get('/log/export/info')
  load()
}

request.get('/category/selectAll').then(res => {
  data.categoryList = res.data
})

const load = () => {
  request.get('/log/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name :data.name,
      type : data.type
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
load()

const reset = () => {
  data.name = null
  data.username= null
  data.type= null
  load()
}


const del = (id) => {
  ElMessageBox.confirm('輸出數據後無法恢復，您確認刪除嗎？', '刪除確認', { type: 'warning'}).then(() => {
    // request.delete('/log/deleteById/' +id).then(res => {
    //   if (res.code === '200') {
    //     ElMessage.success('操作成功')
    //     load() //删除后重新加载数据
    //   } else {
    //     ElMessage.error(res.msg)
    //   }
    // })
    ElMessage("要銷毀痕跡，先跟開發人員說明情況哦")
  }).catch()
}

const handleSelectionChange = (row) => {
  //从选中的行数组中取出所有行的id组成一个新的数组
  data.ids = row.map(row => row.id)
  console.log(data.ids)
}

const delBatch = () => {
  if (data.ids.length === 0) {
    ElMessage.warning('請選擇數據')
    return
  }
  ElMessageBox.confirm('刪除數據後無法恢復，您確認刪除嗎？', '刪除確認', { type: 'warning'}).then(() => {
    // request.delete('/log/deleteBatch', {data: data.ids}).then(res => {
    //   if (res.code === '200') {
    //     ElMessage.success('操作成功')
    //     load() //删除后重新加载数据
    //   } else {
    //     ElMessage.error(res.msg)
    //   }
    // })
    ElMessage.error("做壞事了嗎？要銷毀痕跡！")
  }).catch()
}
</script>

<style scoped>


</style>