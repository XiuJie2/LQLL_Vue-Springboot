<template>
  <div >
    <div class="card" style="margin-bottom: 5px; ">
      <el-card style="border-radius: 10px; margin-bottom: 5px;">
        <el-input style="width: 240px;  margin-right: 5px;" v-model="data.name" placeholder="請輸入名稱查詢" prefix-icon="Search" @keyup.enter="load" >
        </el-input>

        <el-button type="primary" @click="load">查 詢</el-button>
        <el-button type="warning" @click="reset">重 置</el-button>
      </el-card>

      <el-card style="border-radius: 10px; margin-bottom: 5px;">
        <el-button type="primary" @click="handleAdd">新 增</el-button>
        <el-button type="danger" @click="delBatch">批量删除</el-button>
        <el-upload
            style="display: inline-block; margin: 0 12px"
            action="https://black.ntubbirc.ggff.net/api/admin/import"
            :show-file-list="false"
            :on-success="importSuccess"
            :data="uploadData"
        >
          <el-button type="info">導 入</el-button>
        </el-upload>

        <el-upload
            style="display: inline-block;"
            :data="uploadData"
        >
          <el-button type="success" @click="exportData">導 出</el-button>
        </el-upload>
      </el-card>

      <el-card style="margin-bottom: 5px; ">
        <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="30" />
          <el-table-column prop="name" label="名稱"  />
          <el-table-column prop="username" label="賬號"  />
          <el-table-column label="頭像" >
            <template #default="scope">
              <img v-if="scope.row.avatar" :src="scope.row.avatar" alt="" style="display: block; width: 40px; height: 40px; border-radius: 50%" />
            </template>
          </el-table-column>
<!--          <el-table-column prop="no" label="工号" />-->
<!--          <el-table-column prop="departmentId" label="部门" align="center"/>-->
          <el-table-column label="操作" align="center" width="100">
            <template #default="scope">
<!--              <el-button link type="primary">编辑</el-button>-->
<!--              <el-button link type="danger">删除</el-button>-->
              <el-button @click="handleUpdate(scope.row)" type="primary" :icon="Edit" circle></el-button>
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

    <el-dialog title="管理員信息" v-model="data.formVisible" width="500" destory-on-close>
      <el-form ref="formRef" :rules="data.rule" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
        <el-form-item label="名稱" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="請輸入名稱"/>
        </el-form-item>
        <el-form-item label="賬號" prop="username">
          <el-input v-model="data.form.username" autocomplete="off" placeholder="請輸入賬號"/>
        </el-form-item>
        <el-form-item label="頭像">
          <div style="display: flex; align-items: center; gap: 10px;">
            <el-upload
                action="https://black.ntubbirc.ggff.net/api/files/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :data="uploadData"
            >
              <el-button type="primary">上傳頭像</el-button>
            </el-upload>
            <img
                v-if="data.form.avatar"
                :src="data.form.avatar"
                style="width: 40px; height: 40px; border-radius: 50%"
            />
          </div>
        </el-form-item>
<!--        <el-form-item label="工号" prop="no">-->
<!--          <el-input v-model="data.form.no" autocomplete="off" placeholder="请输入工号"/>-->
<!--        </el-form-item>-->
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { Edit, Delete} from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data = reactive({
  name: null,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  ids: {},
  rule: {
    username: [
      {required: true, message: '請輸入賬號', trigger: 'blur'}
    ],
    name: [
      {required: true, message: '請輸入名稱', trigger: 'blur'}
    ],
    no: [
      {required: true, message: '請輸入工號', trigger: 'blur'}
    ]
  }
})

const formRef = ref()

const handleAvatarSuccess = (res) => {
  data.form.avatar = res.data.url
}

const load = () => {
  request.get('/admin/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name :data.name
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
load()

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row)) //深拷贝一个新的对象 用于编辑 这样就不会影响行对象
  data.formVisible = true
}

const user = JSON.parse(localStorage.getItem("login-user"));

// 上传时的额外参数
const uploadData = {
  username: user.username,
  name: user.name
};

const importSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success('批量導入數據成功')
    load()
  }else {
    ElMessage.error(res.msg)
  }
}
const exportData = () => {
  //导出数据 是通过流的形式下载 Excel  打开流的链接 浏览器会自动下载文件
  window.open('https://black.ntubbirc.ggff.net/api/admin/export')
  request.get('/admin/export/info')
}


const reset = () => {
  data.name = null
  load()
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const add = () => {
  request.post('/admin/add', data.form).then(res => { //新增的对象没有id参数
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load() //新增成功重新加载数据
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => { //修改是用 put
  request.put('/admin/update', data.form).then(res => { //编辑的对象包含id
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load() //新增成功重新加载数据
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('刪除數據後無法恢復，您確認刪除嗎？', '刪除確認', { type: 'warning'}).then(() => {
    request.delete('/admin/deleteById/' +id).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load() //删除后重新加载数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const handleSelectionChange = (row) => {
  //从选中的行数组中取出所有行的id组成一个新的数组
  data.ids = row.map(row => row.id)
  console.log(data.ids)
}

const delBatch = () => {
  if (data.ids.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('刪除數據後無法恢復，您確認刪除嗎？', '刪除確認', { type: 'warning'}).then(() => {
    request.delete('/admin/deleteBatch', {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load() //删除后重新加载数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}
</script>

<style scoped>


</style>