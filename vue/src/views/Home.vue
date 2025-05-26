<template>
  <div class="home">
    <!-- 品牌理念區塊 -->
    <div class="hero">
      <img src="@/assets/brandLogo.png" width="70%" alt="logo" class="logo" />
      <p class="slogan">緬甸礦脈的天然饋贈 · 東方美學的當代詮釋</p>
      <p class="subtitle">精選緬甸八大場口頂級翡翠原石，傳承百年玉雕工藝</p>
    </div>

    <!-- 輪播圖片 -->
    <div class="carousel-container">
      <el-carousel height="500px" indicator-position="outside" arrow="always">
        <el-carousel-item v-for="(item, index) in data.srcList" :key="index">
          <div class="carousel-content">
            <img :src="item.image" alt="" class="carousel-img" />
            <div class="carousel-caption">
              <h3>{{ item.title }}</h3>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 場口特色區塊 -->
    <div class="mine-section">
      <h2 class="section-title">
        <el-icon><Location /></el-icon>
        緬甸頂級場口直採
      </h2>
      <div class="mine-grid">
        <div v-for="mine in data.mines" :key="mine.name" class="mine-card">
          <div class="mine-header">
            <h3>{{ mine.name }}</h3>
            <span class="mine-tag" :style="{backgroundColor: mine.tagColor}">{{ mine.type }}</span>
          </div>
          <div class="mine-character">
            <p><strong>特色：</strong>{{ mine.character }}</p>
            <p><strong>代表作品：</strong>{{ mine.products }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 產品概念區塊 -->

    <el-col>
      <div class="philosophy">
        <h2>自然靈韻 · 匠心傳承</h2>
        <p>
          「綠沁琉璃」嚴選緬甸帕敢、木那等頂級場口原石，每塊翡翠皆歷經億萬年地質孕育。
          我們的玉雕師傅傳承清代宮廷琢玉技藝，以現代設計詮釋東方美學，使每件作品既保有翡翠的自然靈性，又煥發當代藝術光彩。
        </p>
        <p>
          從原石採選到設計雕琢，我們堅持"三不原則"：不染色、不注膠、不酸洗，
          確保每件作品都是最純粹的天然翡翠，經得起時間考驗。
        </p>
      </div>
    </el-col>


    <!-- 品質保證 -->
    <div class="quality-section">
      <h2 class="section-title">
        <el-icon><Medal /></el-icon>
        綠沁琉璃品質承諾
      </h2>
      <div class="quality-features">
        <div v-for="feature in data.qualityFeatures" :key="feature.title" class="feature-card">
          <div class="feature-icon" :style="{color: feature.color}">
            <component :is="feature.icon" />
          </div>
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, markRaw} from 'vue'
import { Location, Medal, Coin, ScaleToOriginal, Check } from '@element-plus/icons-vue'

// 提前标记所有图标组件
const icons = {
  check: markRaw(Check),
  coin: markRaw(Coin),
  medal: markRaw(Medal),
  location: markRaw(Location),
  scale: markRaw(ScaleToOriginal)
}



const data = reactive({
  srcList: [
    {
      image: 'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
      title: '木那場口 · 雪花棉翡翠',
      description: '如初雪般清澈的棉絮分布，冰透水潤'
    },
    {
      image: 'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg',
      title: '帕敢老坑 · 帝王綠手鐲',
      description: '濃郁正陽綠，種老起膠'
    },
    {
      image: 'https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg',
      title: '後江場口 · 黃陽綠戒面',
      description: '鮮艷明快的色彩，種水兼備'
    },
    {
      image: 'https://fuss10.elemecdn.com/3/28/bbf893f792f03a54408b3b7a7ebf0jpeg.jpeg',
      title: '莫西沙 · 玻璃種觀音',
      description: '通透如冰，熒光四溢'
    }
  ],
  mines: [
    {
      name: '帕敢場口',
      type: '老坑',
      character: '種老水足，以黑烏沙皮聞名，多出高色高種翡翠',
      products: '帝王綠手鐲、滿綠蛋面',
      tagColor: '#2e8b57'
    },
    {
      name: '木那場口',
      type: '名坑',
      character: '以雪花棉著稱，種水通透，色陽均勻',
      products: '雪花棉掛件、晴水手鐲',
      tagColor: '#3a5f5f'
    },
    {
      name: '後江場口',
      type: '老坑',
      character: '小個頭水石，皮薄色濃，多出高色小料',
      products: '戒面、耳墜',
      tagColor: '#556b6e'
    },
    {
      name: '莫西沙',
      type: '種水料',
      character: '以灰白皮為主，種老起熒，剛性足',
      products: '無事牌、觀音佛像',
      tagColor: '#6b8e23'
    }
  ],
  qualityFeatures: [
    {
      icon: icons.coin,  // 使用预处理过的组件
      title: '場口溯源',
      description: '每件作品標註緬甸原產場口，確保來源真實',
      color: '#2e8b57'
    },
    {
      icon: icons.scale,
      title: 'A貨保證',
      description: '絕無染色注膠，附權威鑑定證書',
      color: '#3a5f5f'
    },
    {
      icon: icons.check,
      title: '工藝標準',
      description: '傳承宮廷琢玉技法，誤差控制在0.1mm內',
      color: '#6b8e23'
    }
  ]
})
</script>

<style scoped>
.home {
  font-family: 'Noto Serif TC', serif;
  color: #2f3e46;
  background-color: #f7f8f5;
  padding-bottom: 80px;
}

.hero {
  text-align: center;
  padding: 80px 20px 40px;
  background: linear-gradient(to bottom, #edf5f0, #ffffff);
}

.hero h1 {
  font-size: 52px;
  font-weight: bold;
  color: #2e8b57;
  letter-spacing: 2px;
  margin-bottom: 10px;
}

.slogan {
  font-size: 20px;
  color: #3a5f5f;
  margin-top: 15px;
  letter-spacing: 1px;
}

.subtitle {
  font-size: 18px;
  color: #556b6e;
  margin-top: 10px;
}

.carousel-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.carousel-content {
  position: relative;
  height: 100%;
}

.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.carousel-caption {
  position: absolute;
  bottom: 40px;
  left: 40px;
  color: white;
  text-align: left;
  background: rgba(0, 0, 0, 0.6);
  padding: 20px;
  border-radius: 8px;
  max-width: 400px;
}

.carousel-caption h3 {
  font-size: 24px;
  margin-bottom: 10px;
}

.carousel-caption p {
  font-size: 16px;
  line-height: 1.6;
}

.mine-section {
  max-width: 1200px;
  margin: 60px auto;
  padding: 0 20px;
}

.section-title {
  text-align: center;
  font-size: 32px;
  color: #3a5f5f;
  margin-bottom: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.mine-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.mine-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s;
}

.mine-card:hover {
  transform: translateY(-5px);
}

.mine-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.mine-header h3 {
  font-size: 20px;
  color: #2e8b57;
  margin: 0;
}

.mine-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 20px;
  color: white;
}

.mine-character p {
  margin: 8px 0;
  line-height: 1.6;
}

.intro-section {
  max-width: 1200px;
  margin: 80px auto;
  padding: 0 20px;
}

.philosophy h2 {
  font-size: 28px;
  color: #3a5f5f;
  margin-bottom: 20px;
}

.philosophy p {
  font-size: 16px;
  line-height: 1.8;
  color: #495057;
  margin-bottom: 15px;
}

.craft-image img {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.quality-section {
  max-width: 1200px;
  margin: 60px auto;
  padding: 0 20px;
}

.quality-features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-top: 40px;
}

.feature-card {
  background: white;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.feature-icon {
  font-size: 40px;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 20px;
  color: #3a5f5f;
  margin-bottom: 15px;
}

.feature-card p {
  font-size: 15px;
  color: #666;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .hero h1 {
    font-size: 40px;
  }

  .slogan {
    font-size: 18px;
  }

  .carousel-caption {
    left: 20px;
    bottom: 20px;
    max-width: 80%;
  }
}
</style>