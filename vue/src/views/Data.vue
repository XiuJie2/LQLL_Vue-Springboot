<template>
  <div class="dashboard-container">
    <!-- 搜索和操作区域 -->
    <el-card class="search-card" shadow="hover">
      <div class="search-operations">
        <el-input
            v-model="data.name"
            style="width: 240px; margin-right: 10px;"
            placeholder="请输入产品名称查询"
            prefix-icon="Search"
            clearable
        />
        <el-button type="primary" @click="handleSearch">查 询</el-button>
        <el-button @click="handleReset">重 置</el-button>

        <div class="operations">
          <el-button type="primary" @click="handleAdd">新 增</el-button>
          <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
          <el-button type="info" @click="handleImport">导 入</el-button>
          <el-button type="success" @click="handleExport">导 出</el-button>
        </div>
      </div>
    </el-card>

    <!-- 数据概览卡片 -->
    <div class="data-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-content">
              <div class="stat-icon" style="background-color: #f0f7ff;">
                <el-icon color="#409EFF" :size="24"><Money /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">总销售额</div>
                <div class="stat-value">¥ 1,248,560</div>
                <div class="stat-trend">
                  <span class="up">12.5% <el-icon><Top /></el-icon></span>
                  同比上月
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-content">
              <div class="stat-icon" style="background-color: #f0f9eb;">
                <el-icon color="#67C23A" :size="24"><ShoppingCart /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">总订单量</div>
                <div class="stat-value">8,642</div>
                <div class="stat-trend">
                  <span class="up">8.3% <el-icon><Top /></el-icon></span>
                  同比上月
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-content">
              <div class="stat-icon" style="background-color: #fdf6ec;">
                <el-icon color="#E6A23C" :size="24"><User /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">客户数量</div>
                <div class="stat-value">3,215</div>
                <div class="stat-trend">
                  <span class="up">5.7% <el-icon><Top /></el-icon></span>
                  同比上月
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-content">
              <div class="stat-icon" style="background-color: #fef0f0;">
                <el-icon color="#F56C6C" :size="24"><Goods /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">商品总数</div>
                <div class="stat-value">156</div>
                <div class="stat-trend">
                  <span class="down">2.1% <el-icon><Bottom /></el-icon></span>
                  同比上月
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-title">月度销售额趋势</div>
          </template>
          <div class="chart-container">
            <v-chart :option="salesChartOption" autoresize />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-title">产品销售占比</div>
          </template>
          <div class="chart-container">
            <v-chart :option="pieChartOption" autoresize />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-title">区域销售排行</div>
          </template>
          <div class="chart-container">
            <v-chart :option="barChartOption" autoresize />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card shadow="hover" class="table-card">
      <el-table
          :data="data.tableData"
          stripe
          style="width: 100%"
          v-loading="data.loading"
      >
        <el-table-column prop="id" label="ID" align="center" />
        <el-table-column prop="date" label="日期" align="center" />
        <el-table-column prop="productName" label="产品名称"  align="center" />
        <el-table-column prop="category" label="类别"  align="center">
          <template #default="{row}">
            <el-tag :type="getCategoryTagType(row.category)">{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="单价(¥)"  align="center" />
        <el-table-column prop="quantity" label="销量"  align="center" />
        <el-table-column prop="total" label="总金额(¥)" align="center" />
        <el-table-column prop="region" label="销售区域"  align="center" />
        <el-table-column prop="salesPerson" label="销售人员" align="center" />
        <el-table-column label="操作"align="center" fixed="right">
          <template #default>
            <el-button size="small" type="primary" plain>编辑</el-button>
            <el-button size="small" type="danger" plain>删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="data.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { PieChart, BarChart, LineChart } from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DatasetComponent
} from 'echarts/components';
import VChart from 'vue-echarts';
import {
  Search, Money, ShoppingCart, User, Goods,
  Top, Bottom, Edit, Delete
} from '@element-plus/icons-vue';

use([
  CanvasRenderer,
  PieChart,
  BarChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DatasetComponent
]);

const data = reactive({
  name: '',
  loading: false,
  pageNum: 1,
  pageSize: 10,
  total: 35,
  tableData: [
    { id: 1, date: '2024-05-06', productName: '翡翠福镯', category: '手镯', price: 12800, quantity: 4, total: 51200, region: '华东', salesPerson: '张仕杰' },
    { id: 2, date: '2024-05-05', productName: '冰种翡翠耳坠', category: '耳坠', price: 5600, quantity: 6, total: 33600, region: '华北', salesPerson: '李小明' },
    { id: 3, date: '2024-05-04', productName: '阳绿平安扣', category: '挂饰', price: 4200, quantity: 3, total: 12600, region: '华南', salesPerson: '王美丽' },
    { id: 4, date: '2024-05-03', productName: '老坑飘花手镯', category: '手镯', price: 19800, quantity: 2, total: 39600, region: '华东', salesPerson: '张仕杰' },
    { id: 5, date: '2024-05-02', productName: '翡翠白菜摆件', category: '摆件', price: 8800, quantity: 1, total: 8800, region: '华北', salesPerson: '赵大伟' },
    { id: 6, date: '2024-05-01', productName: '翡翠叶子吊坠', category: '项链', price: 3600, quantity: 5, total: 18000, region: '华南', salesPerson: '陈小芳' },
    { id: 7, date: '2024-04-30', productName: '翡翠观音', category: '挂饰', price: 7200, quantity: 2, total: 14400, region: '华东', salesPerson: '张仕杰' },
    { id: 8, date: '2024-04-29', productName: '翡翠貔貅摆件', category: '摆件', price: 9800, quantity: 1, total: 9800, region: '华北', salesPerson: '李小明' },
    { id: 9, date: '2024-04-28', productName: '福瓜翡翠吊坠', category: '挂饰', price: 3300, quantity: 4, total: 13200, region: '华南', salesPerson: '王美丽' },
    { id: 10, date: '2024-04-27', productName: '蛋面翡翠戒指', category: '戒指', price: 6400, quantity: 3, total: 19200, region: '华东', salesPerson: '张仕杰' },
  ]
});

// 销售趋势图配置
const salesChartOption = reactive({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
    axisLine: {
      lineStyle: {
        color: '#999'
      }
    }
  },
  yAxis: {
    type: 'value',
    name: '销售额 (万元)',
    axisLine: {
      show: true,
      lineStyle: {
        color: '#999'
      }
    },
    splitLine: {
      lineStyle: {
        type: 'dashed'
      }
    }
  },
  series: [
    {
      name: '销售额',
      type: 'line',
      smooth: true,
      data: [42, 38, 56, 78, 65, 89, 120, 145, 138, 156, 178, 195],
      itemStyle: {
        color: '#409EFF'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0,
            color: 'rgba(64, 158, 255, 0.5)'
          }, {
            offset: 1,
            color: 'rgba(64, 158, 255, 0.1)'
          }]
        }
      }
    }
  ]
});

// 产品占比饼图配置
const pieChartOption = reactive({
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    right: 10,
    top: 'center'
  },
  series: [
    {
      name: '销售占比',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: 35, name: '手镯' },
        { value: 20, name: '耳坠' },
        { value: 15, name: '挂饰' },
        { value: 12, name: '摆件' },
        { value: 10, name: '戒指' },
        { value: 8, name: '项链' }
      ],
      color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#B37FEB']
    }
  ]
});

// 区域销售排行柱状图配置
const barChartOption = reactive({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'value',
    axisLine: {
      lineStyle: {
        color: '#999'
      }
    },
    splitLine: {
      lineStyle: {
        type: 'dashed'
      }
    }
  },
  yAxis: {
    type: 'category',
    data: ['华东', '华北', '华南', '华中', '西南', '西北', '东北'],
    axisLine: {
      lineStyle: {
        color: '#999'
      }
    }
  },
  series: [
    {
      name: '销售额',
      type: 'bar',
      data: [125, 110, 95, 85, 75, 60, 50],
      itemStyle: {
        color: function(params) {
          const colorList = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#B37FEB', '#FF85C0'];
          return colorList[params.dataIndex];
        },
        borderRadius: [0, 4, 4, 0]
      },
      label: {
        show: true,
        position: 'right',
        formatter: '{c} 万元'
      }
    }
  ]
});

const getCategoryTagType = (category) => {
  const map = {
    '手镯': '',
    '耳坠': 'success',
    '挂饰': 'warning',
    '摆件': 'danger',
    '戒指': 'info',
    '项链': 'primary'
  };
  return map[category] || '';
};

const handleSearch = () => {
  data.loading = true;
  // 模拟搜索延迟
  setTimeout(() => {
    data.loading = false;
    ElMessage.success('查询成功');
  }, 800);
};

const handleReset = () => {
  data.name = '';
  ElMessage.info('已重置搜索条件');
};

const handleAdd = () => {
  ElMessage.info('点击了新增按钮');
};

const handleBatchDelete = () => {
  ElMessage.warning('点击了批量删除按钮');
};

const handleImport = () => {
  ElMessage.info('点击了导入按钮');
};

const handleExport = () => {
  ElMessage.success('点击了导出按钮');
};

const handleSizeChange = (val) => {
  data.pageSize = val;
  ElMessage.info(`每页显示 ${val} 条`);
};

const handleCurrentChange = (val) => {
  data.pageNum = val;
  ElMessage.info(`当前页: ${val}`);
};
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.search-operations {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.operations {
  margin-left: auto;
  display: flex;
  gap: 10px;
}

.data-overview {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 8px;
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stat-trend {
  font-size: 12px;
  color: #909399;
}

.stat-trend .up {
  color: #67C23A;
}

.stat-trend .down {
  color: #F56C6C;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 8px;
  height: 350px;
}

.chart-title {
  font-size: 16px;
  font-weight: bold;
}

.chart-container {
  width: 100%;
  height: 300px;
}

.table-card {
  border-radius: 8px;
}

.el-pagination {
  padding: 16px 0;
}
</style>