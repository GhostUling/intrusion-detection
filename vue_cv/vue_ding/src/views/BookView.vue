<template>
  <div class="image-gallery">
    <div class="image-container">
      <!-- 使用flex布局实现每排5张图片 -->
      <div v-for="item in tableData" :key="item.id" class="image-item">
        <el-image 
          class="image-preview"
          :src="'http://localhost:8080/api/files/' + item.img" 
          :preview-src-list="previewList"
          fit="cover"
          :alt="item.name"
        >
          <!-- 加载失败的占位图 -->
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  data() {
    // 从localStorage获取用户信息
    const userInfo = JSON.parse(localStorage.getItem("user") || localStorage.getItem("userInfo") || "{}")
    const userId = userInfo.id || null
    
    return {
      tableData: [], // 图片数据
      params: {
        userid: userId, // 用户ID用于查询图片
      },
    }
  },
  
  computed: {
    // 计算所有图片的预览URL列表
    previewList() {
      return this.tableData
        .filter(item => item.img)
        .map(item => 'http://localhost:8080/api/files/' + item.img)
    }
  },
  
  // 在页面加载时执行
  created() {
    this.search()
  },
  
  methods: {
    // 查询图片数据（保留原方法）
    search() {
      request.get("/Image/userid", { 
        params: { 
          userid: this.params.userid 
        } 
      }).then(res => {
        if (res.code === '0') {
          this.tableData = res.data || []
        } else {
          this.$message.error(res.msg)
        }
      })
    },
  },
}
</script>

<style scoped>
.image-gallery {
  padding: 20px;
}

.image-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: flex-start;
}

.image-item {
  flex: 0 0 calc(20% - 13px); /* 每排5张，考虑间隔 */
  max-width: calc(20% - 13px);
  aspect-ratio: 1; /* 保持正方形 */
  box-sizing: border-box;
}

.image-preview {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.image-preview:hover {
  transform: scale(1.03);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.image-slot {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .image-item {
    flex: 0 0 calc(25% - 12px); /* 4列 */
    max-width: calc(25% - 12px);
  }
}

@media (max-width: 900px) {
  .image-item {
    flex: 0 0 calc(33.33% - 11px); /* 3列 */
    max-width: calc(33.33% - 11px);
  }
}

@media (max-width: 600px) {
  .image-item {
    flex: 0 0 calc(50% - 8px); /* 2列 */
    max-width: calc(50% - 8px);
  }
}

@media (max-width: 400px) {
  .image-item {
    flex: 0 0 100%; /* 1列 */
    max-width: 100%;
  }
}
</style>