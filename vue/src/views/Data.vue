<template>
  <div class="dashboard-container">
    <!-- 搜索和操作区域 -->
    <el-card class="search-card" shadow="hover">
      <div class="search-operations">
        <el-input
            v-model="data.name"
            style="width: 240px; margin-right: 10px;"
            placeholder="請輸入產品名稱查詢"
            prefix-icon="Search"
            clearable
        />
        <el-button type="primary" @click="handleSearch">查 詢</el-button>
        <el-button @click="handleReset">重 置</el-button>

        <div class="operations">
          <el-button type="primary" @click="handleAdd">新 增</el-button>
          <el-button type="danger" @click="handleBatchDelete">批量刪除</el-button>
          <el-button type="info" @click="handleImport">導 入</el-button>
          <el-button type="success" @click="handleExport">導 出</el-button>
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
                <div class="stat-title">總銷售額</div>
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
                <div class="stat-title">總訂單量</div>
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
                <div class="stat-title">客戶數量</div>
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
                <div class="stat-title">商品總數</div>
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
            <div class="chart-title">月度銷售額趨勢</div>
          </template>
          <div class="chart-container">
            <v-chart :option="salesChartOption" autoresize />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-title">產品銷售佔比</div>
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
            <div class="chart-title">區域銷售排行</div>
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
        <el-table-column prop="productName" label="產品名稱"  align="center" />
        <el-table-column prop="category" label="類別"  align="center">
          <template #default="{row}">
            <el-tag :type="getCategoryTagType(row.category)">{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="單價(¥)"  align="center" />
        <el-table-column prop="quantity" label="銷量"  align="center" />
        <el-table-column prop="total" label="總金額(¥)" align="center" />
        <el-table-column prop="region" label="銷售區域"  align="center" />
        <el-table-column prop="salesPerson" label="銷售人員" align="center" />
        <el-table-column label="操作" align="center" fixed="right">
          <template #default>
            <el-button size="small" type="primary" plain>編輯</el-button>
            <el-button size="small" type="danger" plain>刪除</el-button>
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
    { id: 1, date: '2024-05-06', productName: '翡翠福鐲', category: '限量款推薦', price: 12800, quantity: 4, total: 51200, region: '台北', salesPerson: '李仕杰' },
    { id: 2, date: '2024-05-05', productName: '冰種翡翠耳墜', category: '熱銷榜單', price: 5600, quantity: 6, total: 33600, region: '高雄', salesPerson: '董小姐' },
    { id: 3, date: '2024-05-04', productName: '陽綠平安扣', category: '高端定制區', price: 4200, quantity: 3, total: 12600, region: '桃園', salesPerson: '王小姐' },
    { id: 4, date: '2024-05-03', productName: '飄花手鐲', category: '經典收藏區', price: 19800, quantity: 2, total: 39600, region: '新竹', salesPerson: '張小姐' },
    { id: 5, date: '2024-05-02', productName: '翡翠白菜擺件', category: '翡翠精品區', price: 8800, quantity: 1, total: 8800, region: '新北', salesPerson: '楊小姐' },
    { id: 6, date: '2024-05-01', productName: '翡翠葉子吊墜', category: '熱銷榜單', price: 3600, quantity: 5, total: 18000, region: '雲林', salesPerson: '陳小姐' },
    { id: 7, date: '2024-04-30', productName: '翡翠觀音', category: '經典收藏區', price: 7200, quantity: 2, total: 14400, region: '台東', salesPerson: '方小姐' },
    { id: 8, date: '2024-04-29', productName: '翡翠貔貅摆件', category: '限量款推薦', price: 9800, quantity: 1, total: 9800, region: '嘉義', salesPerson: '宇小姐' },
    { id: 9, date: '2024-04-28', productName: '福瓜翡翠吊墜', category: '高端定制區', price: 3300, quantity: 4, total: 13200, region: '台南', salesPerson: '黃小姐' },
    { id: 10, date: '2024-04-27', productName: '蛋面翡翠戒指', category: '翡翠精品區', price: 6400, quantity: 3, total: 19200, region: '南投', salesPerson: '胡小姐' },
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
    name: '銷售額 (萬元)',
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
      name: '銷售額',
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
      name: '銷售佔比',
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
        { value: 20, name: '熱銷榜單' },
        { value: 15, name: '高端定制區' },
        { value: 12, name: '經典收藏區' },
        { value: 10, name: '翡翠精品區' },
        { value: 8, name: '限量款推薦' }
      ],
      color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
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
    data: ['台北', '台南', '高雄', '新北', '雲林', '台東', '桃園'],
    axisLine: {
      lineStyle: {
        color: '#999'
      }
    }
  },
  series: [
    {
      name: '銷售額',
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
        formatter: '{c} 萬元'
      }
    }
  ]
});

const getCategoryTagType = (category) => {
  const map = {
    '翡翠精品區': '',
    '高端定制區': 'success',
    '限量款推薦': 'warning',
    '經典收藏區': 'info',
    '熱銷榜單': 'danger'
  };
  return map[category] || '';
};

const handleSearch = () => {
  data.loading = true;
  // 模拟搜索延迟
  setTimeout(() => {
    data.loading = false;
    ElMessage.success('查詢成功');
  }, 800);
};

const handleReset = () => {
  data.name = '';
  ElMessage.info('已重置搜索條件');
};

const handleAdd = () => {
  ElMessage.info('點擊了新增按鈕');
};

const handleBatchDelete = () => {
  ElMessage.warning('點擊了批量刪除按鈕');
};

const handleImport = () => {
  ElMessage.info('點擊了導入按鈕');
};

const handleExport = () => {
  ElMessage.success('點擊了導出按鈕');
};

const handleSizeChange = (val) => {
  data.pageSize = val;
  ElMessage.info(`每頁顯示 ${val} 條`);
};

const handleCurrentChange = (val) => {
  data.pageNum = val;
  ElMessage.info(`當前頁: ${val}`);
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