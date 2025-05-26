<template>
  <div>
    <div class="card" style="margin-bottom: 5px; ">
      <el-card style="border-radius: 10px; margin-bottom: 5px;">
        <el-input style="width: 240px;  margin-right: 5px;" v-model="data.name" placeholder="请输入名称查询" prefix-icon="Search">
        </el-input>

        <el-button type="primary" @click="load">查 询</el-button>
        <el-button type="warning" @click="reset">重 置</el-button>
      </el-card>

      <el-card style="border-radius: 10px; margin-bottom: 5px;">
        <el-button type="primary" @click="handleAdd">新 增</el-button>
        <el-button type="danger" @click="delBatch">批量删除</el-button>
        <el-upload
            style="display: inline-block; margin: 0 12px"
            action="https://black.ntubbirc.ggff.net/api/product/import"
            :show-file-list="false"
            :on-success="importSuccess"
            :data="uploadData"
        >
          <el-button type="info">导 入</el-button>
        </el-upload>
        <el-upload
            style="display: inline-block;"
            :data="uploadData"
        >
          <el-button type="success" @click="exportData">导 出</el-button>
        </el-upload>
      </el-card>

      <el-card style="margin-bottom: 5px; ">
        <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="30"></el-table-column>
          <el-table-column prop="name" label="名称"/>
          <el-table-column prop="categoryName" label="類別"/>
          <el-table-column prop="no" label="編號"/>
          <el-table-column label="圖片">
            <template #default="scope">
              <img v-if="scope.row.image" :src="scope.row.image" alt="" style="display: block; width: 40px; height: 40px; border-radius: 50%" />
            </template>
          </el-table-column>
          <el-table-column prop="price" label="價格"/>
          <el-table-column prop="description" label="介紹" show-overflow-tooltip/>
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

    <el-dialog title="產品信息" v-model="data.formVisible" width="500" destory-on-close>
      <el-form ref="formRef" :rules="data.rule" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入產品名称"/>
        </el-form-item>
        <!--        <el-form-item label="類別" prop="category">-->
        <!--          <el-input v-model="data.form.category" autocomplete="off" placeholder="请選擇產品類別"/>-->
        <!--        </el-form-item>-->
        <el-form-item label="類別" prop="categoryId">
          <el-select style="width: 100%" v-model="data.form.categoryId">
            <el-option v-for="item in data.categoryList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="編號" prop="no">
          <el-input v-model="data.form.no" autocomplete="off" placeholder="请输入產品編號"/>
        </el-form-item>
        <el-form-item label="圖片" prop="image">
          <div style="display: flex; align-items: center; gap: 10px;">
            <el-upload
                action="https://black.ntubbirc.ggff.net/api/files/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :data="uploadData"
            >
              <el-button type="primary">上传圖片</el-button>
            </el-upload>
            <img
                v-if="data.form.image"
                :src="data.form.image"
                style="width: 40px; height: 40px; border-radius: 50%"
            />
          </div>
        </el-form-item>
        <!--        <el-form-item label="性别">-->
        <!--          <el-radio-group v-model="data.form.sex">-->
        <!--            <el-radio value="男" label="男"></el-radio>-->
        <!--            <el-radio value="女" label="女"></el-radio>-->
        <!--          </el-radio-group>-->
        <!--        </el-form-item>-->
        <el-form-item label="價格">
          <el-input-number style="width: 180px" :min="1" v-model="data.form.price" autocomplete="off" placeholder="请输入产品价格" />
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input type="textarea" :rows="2" v-model="data.form.description" autocomplete="off" placeholder="请输入产品介绍"/>
        </el-form-item>
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
  ids: [],
  categoryList: [],
  rule: {
    name: [
      {required: true, message: '请输入產品名稱', trigger: 'blur'}
    ],
    categoryId: [
      {required: true, message: '請選擇產品類別', trigger: 'blur'}
    ],
    no: [
      {required: true, message: '請輸入產品編號', trigger: 'blur'}
    ],
    image: [
      {required: true, message: '請上傳產品圖片', trigger: 'blur'}
    ]
  }
})

const formRef = ref()

const exportData = () => {
  //导出数据 是通过流的形式下载 Excel  打开流的链接 浏览器会自动下载文件
  window.open('https://black.ntubbirc.ggff.net/api/product/export')
  request.get('/product/export/info')
}

const user = JSON.parse(localStorage.getItem("login-user"));

// 上传时的额外参数
const uploadData = {
  username: user.username,
  name: user.name
};

const handleAvatarSuccess = (res) => {
  data.form.image = res.data.url
}

request.get('/category/selectAll').then(res => {
  data.categoryList = res.data
})

const importSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success('批量导入数据成功')
    load()
  }else {
    ElMessage.error(res.msg)
  }
}

const load = () => {
  request.get('/product/selectPage', {
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

const reset = () => {
  data.name = null
  load()
}

const save = () => {
  formRef.value.validate((valid) => {
    const no = Number(data.form.no)  // 將輸入轉為數字
    if (valid && !Number.isInteger(no)) {
      ElMessage.error('編號必須是整數')
      return
    }
    if(valid){
      data.form.id ? update() : add()
    }
  })
}

const add = () => {
  request.post('/product/add', data.form).then(res => { //新增的对象没有id参数
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
  request.put('/product/update', data.form).then(res => { //编辑的对象包含id
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
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗？', '删除确认', { type: 'warning'}).then(() => {
    request.delete('/product/deleteById/' +id).then(res => {
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
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗？', '删除确认', { type: 'warning'}).then(() => {
    request.delete('/product/deleteBatch', {data: data.ids}).then(res => {
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