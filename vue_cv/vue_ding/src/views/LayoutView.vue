<template>
  <div class="app">
    <el-container>
      <el-header style="background: linear-gradient(135deg, #89c2ff 0%, #4a90e2 100%); height: 60px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">

        <img src="@/assets/logo.png" style="width: 40px; height: 40px; position:absolute;left:20px;top:10px;">
        <!--   font-size: 18px    字体大小 -->
        <span style=" position:absolute;left:80px;top:20px;font-size: 18px;color: white;font-weight: bold;">入侵检测管理系统</span>

        <!-- 退出登录组件 -->
        <el-dropdown trigger="click" style="color: white; font-size: 16px; float: right; height:60px; line-height:60px">
          <span class="el-dropdown-link">
            {{ user.name }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <div @click="loginout()">退出登录</div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>


      </el-header>
      <el-container>
        <el-aside style="background: white; width:200px; border-right: 1px solid #f0f0f0; box-shadow: 2px 0 8px rgba(0,0,0,0.05); overflow: hidden;">
          <el-menu :default-active="$route.path" router class="el-menu-vertical-demo" @open="handleOpen"
            @close="handleClose" style="background: white; border-right: none;"
            text-color="#666" active-text-color="#1890ff">
            <el-menu-item index="/">
              <i class="el-icon-s-home"></i>
              <span slot="title">系统首页</span>
            </el-menu-item>

            <el-menu-item index="/user" v-if="user.type != 'role_student'">
              <i class="el-icon-user"></i>
              <span slot="title">用户信息</span>
            </el-menu-item>

            <el-menu-item index="/book" v-if="user.type != 'role_student'">
              <i class="el-icon-picture-outline"></i>
              <span slot="title">图片管理</span>
            </el-menu-item>
            
          </el-menu>
        </el-aside>
        <el-main style="padding: 20px; background-color: white;">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script>
export default {
  data() {
    return {
      //获取到当前登陆用户信息
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  methods: {
    loginout() {
      //清除当前浏览器的登录状态信息
      localStorage.removeItem("user")
      //跳转到登录页
      setTimeout(() => {
        this.$router.push("/login")
      }, 1000)
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<style lang="less" scoped>
/* 菜单激活状态样式 */
::v-deep .el-menu-item {
  color: #666;
  transition: all 0.3s;
}
::v-deep .el-menu-item:hover {
  background-color: #f5f5f5 !important;
  color: #333;
}
::v-deep .el-menu-item.is-active {
  background-color: #f0f7ff !important;
  color: #1890ff !important;
  border-right: 3px solid #1890ff;
  font-weight: bold;
}

/* 子菜单样式 */
::v-deep .el-submenu__title {
  color: #666 !important;
}
::v-deep .el-submenu__title:hover {
  background-color: #f5f5f5 !important;
  color: #333 !important;
}
</style>