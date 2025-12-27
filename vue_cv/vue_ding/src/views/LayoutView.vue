<template>
  <div class="home">
    <el-container>
      <el-header style="background-color: #b3c0d1">
        <img
          src="@/assets/logo.png"
          style="width: 60px; height: 60px; position: relative; top: 7px"
          alt=""
        />
        <!--   font-size: 18px    字体大小 -->
        <span style="position: relative; top: -20px; left: 10px; font-size: 18px"
          >Vue管理系统</span
        >
      </el-header>
      <el-container>
        <el-aside style="background-color: #545c64; width: 200px; min-height: 100vh">
          <el-menu
            :default-active="$route.path"
            router
            class="el-menu-vertical-demo"
            @open="handleOpen"
            @close="handleClose"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
          >
            <el-menu-item index="/">
              <i class="el-icon-menu"></i>
              <span slot="title">系统首页</span>
            </el-menu-item>
            <el-submenu index="2" v-if="user.type != 'role_student'">
              <template slot="title">
                <i class="el-icon-location"></i>
                <span>用户管理</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="1-1">管理员信息</el-menu-item>
                <el-menu-item index="/user">用户信息</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="3">
              <template slot="title">
                <i class="el-icon-setting"></i>
                <span>信息管理</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="">学生信息</el-menu-item>
                <el-menu-item index="">老师信息</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main><router-view /></el-main>
      </el-container>
    </el-container>
  </div>
</template>

//边界线的原因是我们的菜单标签中有一个默认的边框线属性 // !important
:声明当前这个设置是最高优先级,强制性
<style>
.el-menu {
  border-right: none !important;
}
</style>

<script>
export default {
  data() {
    return {
      // 1. 定义user对象（根据你的实际用户类型修改）
      user: {
        type: "role_admin", // 或 'role_student'、'role_teacher'等
      },
    };
  },
  methods: {
    // 2. 定义handleOpen和handleClose方法
    handleOpen(key, keyPath) {
      console.log("菜单展开:", key, keyPath);
      // 这里可以添加菜单展开时的逻辑
    },
    handleClose(key, keyPath) {
      console.log("菜单关闭:", key, keyPath);
      // 这里可以添加菜单关闭时的逻辑
    },
  },
};
</script>
