<template>
  <!-- 添加一个最外层的div -->
  <div class="user-page">
    <div class="user">
      <el-input class="user-input" placeholder="请输入姓名" v-model="name" clearable></el-input>
      <el-input class="user-input" placeholder="请输入电话" v-model="phone" clearable></el-input>
      <el-button class="user-btn" type="success">搜索</el-button>
      <el-button type="primary">添加</el-button>
      <el-table :data="tableData" style="width: 100%" :row-class-name="tableRowClassName">
        <el-table-column prop="date" label="日期" width="180"></el-table-column>
        <el-table-column prop="name" label="姓名" width="180"></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>
        <el-table-column label="操作">
          <!-- 这里也需要修复操作列 -->
          <template slot-scope="scope">
            <el-button type="primary">修改</el-button>
            <el-button type="danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage4"
        :page-sizes="[100, 200, 300, 400]"
        :page-size="100"
        layout="total, sizes, prev, pager, next, jumper" 
        :total="400">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  // 修正1：将 mounted:{} 改为正确的生命周期钩子
  mounted() {
    // 这里可以添加组件挂载后的初始化逻辑
  },
  methods: {
    /* 动态定义table表格的样式 */
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex === 1) {
        return "warning-row";
      } else if (rowIndex === 3) {
        return "success-row";
      }
      return "";
    },//对应的方法
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
  },
  data() {
    return {
      /* 定义一个数组用于存放表格的数据 */
      tableData: [
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-01",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
      ],
    };
  },
};
</script>

<style scoped>
/* 修正2：添加 scoped 属性，让样式只在本组件生效 */
.user-view {
  padding: 20px;
}

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
.user-input {
    width: 10%;
    margin-right: 10px;
}

.user-btn {
    width: 80px;
    margin-right: 5px;
}
</style>
