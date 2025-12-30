const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    host: 'localhost',
    port: 9000, // 端口
    
    // 添加代理配置
    proxy: {
      // 静态资源文件代理（图片文件）
      '/files': {
        target: 'http://localhost:8080', // 后端静态资源地址
        changeOrigin: true,
        secure: false,
        // 后端控制器在后端统一添加了 /api 前缀（见 WebConfig），
        // 因此前端请求 /files 需要重写为 /api/files 转发到后端。
        pathRewrite: {
          '^/files': '/api/files'
        }
      }
    }
  }
})