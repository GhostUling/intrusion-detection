<template>
  <div class="image-gallery">
    <div class="image-container">
      <!-- 使用flex布局实现每排5张图片 -->
      <div v-for="item in tableData" :key="item.id" class="image-item">
        <el-image 
          class="image-preview"
          :src="getImageUrl(item.img)" 
          :preview-src-list="previewList"
          fit="cover"
          :alt="item.name || '图片'"
        >
          <!-- 加载失败的占位图 -->
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
            <div class="error-text">加载失败</div>
          </div>
          <!-- 加载中的占位 -->
          <div slot="placeholder" class="image-slot">
            <i class="el-icon-loading"></i>
            <div class="loading-text">加载中...</div>
          </div>
        </el-image>
        <!-- 图片名称 -->
        <div v-if="item.name" class="image-name">{{ item.name }}</div>
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
      loading: false
    }
  },
  
  computed: {
    // 计算所有图片的预览URL列表
    previewList() {
      return this.tableData
        .filter(item => item.img)
        .map(item => this.getImageUrl(item.img))
    }
  },
  
  // 在页面加载时执行
  created() {
    this.initPage()
  },
  
  methods: {
    // 初始化页面
    async initPage() {
      try {
        this.loading = true
        
        // 可选：先执行上传
        try {
          await this.uploadAll()
        } catch (uploadError) {
          console.warn('上传失败，继续加载现有图片:', uploadError)
          // 上传失败不影响加载现有图片
        }
        
        // 加载图片数据
        await this.loadImages()
        
      } catch (error) {
        console.error('页面初始化失败:', error)
        this.$message.error('图片加载失败')
      } finally {
        this.loading = false
      }
    },
    
    // 调用上传接口
    uploadAll() {
      return new Promise((resolve, reject) => {
        request.get("/files/uploadAll")
          .then(res => {
            if (res.code === '0') {
              this.$message.success(res.msg || '图片上传成功')
              resolve(res)
            } else {
              this.$message.warning(res.msg || '上传失败，使用现有图片')
              reject(res)
            }
          })
          .catch(error => {
            console.error('上传请求失败:', error)
            this.$message.warning('上传请求失败，使用现有图片')
            reject(error)
          })
      })
    },
    
    // 加载图片数据
    async loadImages() {
      try {
        // 检查是否有用户ID
        if (!this.params.userid) {
          console.warn('未找到用户ID，无法加载用户图片')
          this.$message.info('请先登录以查看图片')
          return
        }
        
        const res = await request.get("/Image/userid", { 
          params: { 
            userid: this.params.userid 
          } 
        })
        
        if (res.code === '0' && res.data) {
          this.tableData = res.data
          if (this.tableData.length === 0) {
            this.$message.info('暂无图片，请先上传')
          }
        } else {
          this.$message.error(res.msg || '获取图片失败')
        }
      } catch (error) {
        console.error('加载图片失败:', error)
        this.$message.error('加载图片失败，请检查网络连接')
      }
    },
    
    // 获取图片URL（处理文件名编码）
    getImageUrl(filename) {
      if (!filename) return ''
      
      // 对文件名进行URL编码，处理特殊字符
      const encodedFilename = encodeURIComponent(filename)
      
      // 构建正确的API路径
      return `http://localhost:8080/api/files/${encodedFilename}`
    },
    
    // 手动刷新图片
    refreshImages() {
      this.loadImages()
    }
  },
}
</script>

<style scoped>
.image-gallery {
  padding: 20px;
  min-height: 400px;
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
  display: flex;
  flex-direction: column;
  align-items: center;
}

.image-preview {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background-color: #f5f7fa;
}

.image-preview:hover {
  transform: scale(1.03);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.image-slot {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

.image-slot i {
  margin-bottom: 8px;
}

.error-text, .loading-text {
  font-size: 12px;
  margin-top: 4px;
}

.image-name {
  margin-top: 8px;
  font-size: 12px;
  color: #666;
  text-align: center;
  word-break: break-word;
  width: 100%;
  padding: 0 4px;
}

/* 加载状态 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

/* 空状态提示 */
.empty-state {
  width: 100%;
  text-align: center;
  padding: 40px 20px;
  color: #999;
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