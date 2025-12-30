<template>
  <div class="dashboard">
    <!-- 背景装饰元素 -->
    <div class="bg-circle circle-1"></div>
    <div class="bg-circle circle-2"></div>
    <div class="bg-circle circle-3"></div>
    
    <!-- 标题 -->
    <div class="header">
      <h1 class="title">入侵检测系统</h1>
      <p class="subtitle">数据统计概览</p>
    </div>
    
    <!-- 数据卡片 -->
    <div class="data-cards">
      <div class="card user-card">
        <div class="card-content">
          <div class="card-icon">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="card-stats">
            <div class="card-number">{{ userCount }}</div>
            <div class="card-label">已注册用户</div>
          </div>
        </div>
        <div class="card-footer">系统用户总数</div>
      </div>
      
      <div class="card image-card">
        <div class="card-content">
          <div class="card-icon">
            <i class="el-icon-camera-solid"></i>
          </div>
          <div class="card-stats">
            <div class="card-number">{{ imageCount }}</div>
            <div class="card-label">上传图片</div>
          </div>
        </div>
        <div class="card-footer">检测图片总数</div>
      </div>
    </div>
    
    <!-- 底部装饰 -->
    <div class="footer">
      <div class="stat-line"></div>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";

export default {
  name: "HomeView",
  data() {
    return {
      userCount: 0,
      imageCount: 125,
      loadingUserCount: false
    };
  },
  mounted() {
    this.fetchUserCount();
  },
  methods: {
    async fetchUserCount() {
      this.loadingUserCount = true;
      try {
        const res = await request.get('/user/count');
        // 后端返回的 Result.data 中存放真正的数据（参照后端 Result.success）
        if (res && res.code === '0') {
          this.userCount = Number(res.data) || 0;
        } else {
          console.error('获取用户总数失败', res && res.msg);
        }
      } catch (err) {
        console.error('请求用户总数出错', err);
      } finally {
        this.loadingUserCount = false;
      }
    }
  }
};
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60px 20px;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* 背景装饰 */
.bg-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  z-index: 0;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  right: -100px;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -80px;
  left: -80px;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  right: 10%;
}

/* 标题 */
.header {
  text-align: center;
  margin-bottom: 60px;
  position: relative;
  z-index: 1;
}

.title {
  font-size: 42px;
  font-weight: 600;
  color: white;
  margin-bottom: 12px;
  letter-spacing: 1px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.8);
  letter-spacing: 3px;
}

/* 数据卡片 */
.data-cards {
  display: flex;
  gap: 40px;
  position: relative;
  z-index: 1;
  margin-bottom: 60px;
}

.card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 40px 30px;
  width: 320px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.card:hover {
  transform: translateY(-10px);
  box-shadow: 0 30px 60px rgba(0, 0, 0, 0.2);
}

.card-content {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.card-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 25px;
  font-size: 36px;
  color: white;
}

.user-card .card-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.image-card .card-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-stats {
  flex: 1;
}

.card-number {
  font-size: 64px;
  font-weight: 700;
  color: #333;
  line-height: 1;
  margin-bottom: 8px;
}

.card-label {
  font-size: 18px;
  color: #666;
  font-weight: 500;
}

.card-footer {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  color: #888;
  font-size: 14px;
}

/* 底部装饰 */
.footer {
  position: relative;
  z-index: 1;
  text-align: center;
}

.stat-line {
  width: 200px;
  height: 2px;
  background: rgba(255, 255, 255, 0.3);
  margin: 0 auto 20px;
}

.update-time {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  letter-spacing: 1px;
}

/* 响应式 */
@media (max-width: 768px) {
  .data-cards {
    flex-direction: column;
    gap: 30px;
  }
  
  .card {
    width: 100%;
    max-width: 400px;
  }
  
  .title {
    font-size: 32px;
  }
}
</style>