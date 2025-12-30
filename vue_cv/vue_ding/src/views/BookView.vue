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
    this.uploadAndLoad()
  },
  
  methods: {
    // 上传并加载数据
    async uploadAndLoad() {
      try {
        // 1. 先执行上传
        await this.uploadAll()
        // 2. 上传完成后查询图片数据
        await this.search()
      } catch (error) {
        console.error('上传或加载失败:', error)
        this.$message.error('图片加载失败')
      }
    },
    
    // 调用上传接口
    uploadAll() {
      return new Promise((resolve, reject) => {
        request.get("/files/uploadAll", { params: { userid: this.params.userid } })
          .then(res => {
            if (res.code === '0') {
              this.$message.success(res.msg || '上传成功')
              resolve(res)
            } else {
              this.$message.error(res.msg || '上传失败')
              reject(res)
            }
          })
          .catch(error => {
            this.$message.error('上传请求失败')
            reject(error)
          })
      })
    },
    
    // 查询图片数据
    search() {
      return new Promise((resolve, reject) => {
        request.get("/Image/userid", { 
          params: { 
            userid: this.params.userid 
          } 
        }).then(res => {
          if (res.code === '0') {
            this.tableData = res.data || []
            resolve(res)
          } else {
            this.$message.error(res.msg)
            reject(res)
          }
        }).catch(error => {
          this.$message.error('获取图片失败')
          reject(error)
        })
      })
    },
  },
}
</script>

<style scoped>
/* 样式保持不变 */
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