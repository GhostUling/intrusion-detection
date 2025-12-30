<template>
  <div class="image-gallery">
    <!-- 添加一个顶部工具栏 -->
    <div class="toolbar">
      <el-button 
        type="primary" 
        size="small" 
        @click="initPage"
        :loading="loading"
        icon="el-icon-refresh"
      >
        刷新
      </el-button>
      <el-button 
        type="success" 
        size="small" 
        @click="uploadAll"
        :loading="uploading"
        icon="el-icon-upload"
      >
        上传图片
      </el-button>
      <span v-if="tableData.length > 0" class="image-count">
        共 {{ tableData.length }} 张图片
      </span>
    </div>
    
    <!-- 空状态提示 -->
    <div v-if="tableData.length === 0 && !loading" class="empty-state">
      <el-empty description="暂无图片">
        <el-button type="primary" @click="uploadAll">上传图片</el-button>
      </el-empty>
    </div>
    
    <!-- 图片容器 -->
    <div v-else class="image-container">
      <div v-for="item in tableData" :key="item.id" class="image-item">
        <el-image 
          class="image-preview"
          :src="getImageUrl(item.img)"
          :preview-src-list="previewList"
          fit="cover"
          :alt="item.name || '图片'"
          lazy
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
    
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-overlay">
      <el-card shadow="never" class="loading-card">
        <div class="loading-content">
          <i class="el-icon-loading loading-icon"></i>
          <p>加载中...</p>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'BookView',
  
  data() {
    // 从localStorage获取用户信息
    let userInfo = {}
    try {
      const userStr = localStorage.getItem("user") || localStorage.getItem("userInfo")
      if (userStr) {
        userInfo = JSON.parse(userStr)
      }
    } catch (e) {
      console.error('解析用户信息失败:', e)
    }
    
    const userId = userInfo.id !== undefined && userInfo.id !== null ? userInfo.id : null
    
    return {
      tableData: [], // 图片数据
      params: {
        userid: userId, // 用户ID用于查询图片
      },
      loading: false,
      uploading: false,
      hasError: false,
      errorMessage: ''
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
  
  mounted() {
    // 监听localStorage变化，以便用户登录/注销时刷新页面
    window.addEventListener('storage', this.handleStorageChange)
  },
  
  beforeDestroy() {
    window.removeEventListener('storage', this.handleStorageChange)
  },
  
  methods: {
    // 处理localStorage变化
    handleStorageChange(e) {
      if (e.key === 'user' || e.key === 'userInfo') {
        this.refreshUserInfo()
      }
    },
    
    // 刷新用户信息
    refreshUserInfo() {
      try {
        const userStr = localStorage.getItem("user") || localStorage.getItem("userInfo")
        if (userStr) {
          const userInfo = JSON.parse(userStr)
          // 保留 id=0 的 admin 情况，使用 nullish 合并以避免将 0 误转为 null
          this.params.userid = (userInfo.id ?? userInfo.userId) ?? null
          // 刷新图片数据
          this.loadImages()
        } else {
          this.params.userid = null
          this.tableData = []
        }
      } catch (e) {
        console.error('刷新用户信息失败:', e)
      }
    },
    
    // 初始化页面
    async initPage() {
      try {
        this.loading = true
        this.hasError = false
        
        // 检查用户登录状态（支持普通用户和admin账号）
        if (this.params.userid === null || this.params.userid === undefined) {
          this.$message.warning('请先登录以查看图片')
          // 这里可以添加跳转到登录页的逻辑
          this.loading = false
          return
        }
        
        // 加载图片数据
        await this.loadImages()
        
      } catch (error) {
        console.error('页面初始化失败:', error)
        this.hasError = true
        this.errorMessage = '页面加载失败: ' + (error.message || '未知错误')
        this.$message.error('页面加载失败')
      } finally {
        this.loading = false
      }
    },
    
    // 调用上传接口
    async uploadAll() {
      if (this.params.userid === null || this.params.userid === undefined) {
        this.$message.warning('请先登录再上传图片')
        return
      }
      
      this.uploading = true
      try {
        // 显示确认对话框
        await this.$confirm('确定要从共享文件夹上传所有图片吗？', '确认上传', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        // 执行上传
        const res = await request.get("/files/uploadAll", { 
          params: { 
            userid: this.params.userid 
          } 
        })
        
        if (res.code === '0' || res.code === 200) {
          this.$message.success(res.msg || '图片上传成功')
          // 上传成功后刷新图片列表
          await this.loadImages()
        } else {
          this.$message.error(res.msg || '上传失败')
        }
      } catch (error) {
        console.error('上传失败:', error)
        // 如果是用户取消，不显示错误
        if (error !== 'cancel' && error !== 'close') {
          this.$message.error('上传失败: ' + (error.message || '未知错误'))
        }
      } finally {
        this.uploading = false
      }
    },
    
    // 加载图片数据
    async loadImages() {
      try {
        // 检查是否有用户ID（支持普通用户和admin账号）
        if (this.params.userid === null || this.params.userid === undefined) {
          console.warn('未找到用户ID')
          this.tableData = []
          return
        }
        
        // 管理员（id=0）应查看所有图片，调用 /Image/selectAll；其它用户按 userid 查询
        let res;
        if (this.params.userid === 0) {
          res = await request.get('/Image/selectAll');
        } else {
          res = await request.get('/Image/userid', { params: { userid: this.params.userid } });
        }
        
        console.log('图片数据响应:', res) // 调试用
        
        if (res.code === '0' || res.code === 200) {
          // 兼容后端可能返回的多种结构：直接数组、{ list: [] }、{ data: [] } 等
          let list = [];
          if (Array.isArray(res.data)) {
            list = res.data;
          } else if (res.data) {
            if (Array.isArray(res.data.list)) {
              list = res.data.list;
            } else if (Array.isArray(res.data.rows)) {
              list = res.data.rows;
            } else if (Array.isArray(res.data.data)) {
              list = res.data.data;
            }
          }
          this.tableData = list || []
          // 将图片数量写入 localStorage，供 HomeView 使用
          try {
            const cnt = Array.isArray(this.tableData) ? this.tableData.length : 0;
            localStorage.setItem('book_image_count', String(cnt));
            // 触发全局自定义事件（在同一窗口中监听）
            window.dispatchEvent(new CustomEvent('bookImageCountUpdated', { detail: { count: cnt } }));
          } catch (e) {
            console.warn('无法写入 book_image_count 到 localStorage', e);
          }
          if (this.tableData.length === 0) {
            console.log('暂无图片数据')
          } else {
            console.log('加载到', this.tableData.length, '张图片')
            // 调试：查看第一张图片的信息
            if (this.tableData.length > 0) {
              console.log('第一张图片:', this.tableData[0])
              console.log('图片URL:', this.getImageUrl(this.tableData[0].img))
            }
          }
        } else {
          this.$message.error(res.msg || '获取图片失败')
          this.tableData = []
        }
      } catch (error) {
        console.error('加载图片失败:', error)
        this.$message.error('加载图片失败: ' + (error.message || '请检查网络连接'))
        this.tableData = []
      }
    },
    
    // 获取图片URL（重要修改：不要编码整个文件名）
    getImageUrl(filename) {
      if (!filename) return ''
      
      console.log('原始文件名:', filename) // 调试用
      
      // 重要修改：不要对整个文件名进行编码
      // 后端期望的是原始文件名格式，例如：1701234567890+example.jpg
      // 直接拼接URL，只对可能有问题的情况进行适当处理
      
      // 清理文件名中的多余空格
      const cleanFilename = filename.trim()
      
      // 构建完整的可访问URL，保证预览和直接加载都一致
      // 如果需要改为相对路径或使用代理，请调整为对应的域名或baseURL
      const host = 'http://localhost:8080'
      const apiPrefix = '/api'
      // 对文件名进行编码，避免空格或特殊字符导致的404
      const encoded = encodeURIComponent(cleanFilename)
      const url = `${host}${apiPrefix}/files/${encoded}`
      console.log('生成的URL:', url) // 调试用
      return url
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
  position: relative;
}

.toolbar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.image-count {
  margin-left: auto;
  color: #666;
  font-size: 14px;
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
  object-fit: cover; /* 确保图片填充整个容器 */
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
  max-height: 32px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
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

.loading-card {
  border: none;
  background: transparent;
}

.loading-content {
  text-align: center;
}

.loading-icon {
  font-size: 40px;
  color: #409EFF;
  margin-bottom: 10px;
}

/* 空状态提示 */
.empty-state {
  margin-top: 50px;
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
  
  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .image-count {
    margin-left: 0;
    text-align: center;
  }
}
</style>