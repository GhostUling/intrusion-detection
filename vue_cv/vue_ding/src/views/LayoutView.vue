<template>
  <div class="app">
    <el-container>
      <el-header style="background-color: #545c64;">

        <img src="@/assets/logo.png" style="width: 60px; height: 60px; position:absolute;left:10px;top:10px;">
        <!--   font-size: 18px    字体大小 -->
        <span style=" position:absolute;left:80px;top:30px;font-size: 18px;">入侵检测管理系统</span>

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
        <el-aside style="background-color:#545c64; width:200px;min-height: 100vh; ">
          <el-menu :default-active="$route.path" router class="el-menu-vertical-demo" @open="handleOpen"
            @close="handleClose" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
            <el-menu-item index="/">
              <i class="el-icon-menu"></i>
              <span slot="title">系统首页</span>
            </el-menu-item>

            <el-menu-item index="/user" v-if="user.type != 'role_student'">
              <i class="el-icon-s-custom"></i>
              <span slot="title">用户信息</span>
            </el-menu-item>
            
            <el-submenu index="3">
              <template slot="title">
                <i class="el-icon-picture"></i>
                <span>图片管理</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="/bml">图片文件</el-menu-item>
                <el-menu-item index="/book">图书管理</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
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

<style lang="less">
.myHeader {
  background-color: #2b2b2b;
}
</style>