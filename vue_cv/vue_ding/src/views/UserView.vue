<template>
  <!-- 添加一个最外层的div -->
  <div class="user-page">
    <div class="user">
      <el-input class="user-input" placeholder="请输入姓名" v-model="name" clearable></el-input>
      <el-input class="user-input" placeholder="请输入电话·" v-model="phone" clearable></el-input>
      <el-button class="user-btn" type="primary" @click="search">搜索</el-button>
      <el-button class="user-btn" type="primary" @click="add">添加</el-button>
      <el-table :data="tableData" style="width: 100%" :row-class-name="tableRowClassName">

        <el-table-column prop="id" label="序号" width="180"></el-table-column>
        <el-table-column prop="name" label="姓名" width="180"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="create_time" label="创建日期" width="180"></el-table-column>

        <el-table-column label="操作">
          <!-- 这里也需要修复操作列 -->
          <template slot-scope="scope">
            <el-button style="margin-right: 10px;" type="primary" @click="updateButton(scope.row)">修改</el-button>
            <el-popconfirm title="确定删除该条数据吗？" @confirm="deleteButton(scope.row.id)">
              <el-button slot="reference" type="danger">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 分页组件 -->
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5,10,20,50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper" 
        :total="total">
      </el-pagination>
    </div>

    <!-- 添加按钮弹出输入框 -->
    <el-dialog class="dialog-edit" title="添加用户" :visible.sync="dialogFormVisible" width="500px">
          <el-form :model="editForm" :rules="rules" ref="editForm">
            <el-form-item prop="name" class="user-editInput" label="姓名：" :label-width="formLabelWidth">
                <el-input v-model="editForm.name" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item prop="username" class="user-editInput" label="用户名：" :label-width="formLabelWidth">
                <el-input v-model="editForm.username" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item prop="password" class="user-editInput" label="密码：" :label-width="formLabelWidth">
                <el-input v-model="editForm.password" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item prop="email" class="user-editInput" label="邮箱：" :label-width="formLabelWidth">
                <el-input v-model="editForm.email" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item prop="phone" class="user-editInput" label="电话：" :label-width="formLabelWidth">
                <el-input v-model="editForm.phone" autocomplete="off"></el-input>
            </el-form-item>
            

          </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button class="user-btn" @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" class="user-btn" @click="confirmEdit('editForm')">确 定</el-button>
            </div>
        </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
//import { search } from 'core-js/fn/symbol';
export default {
  // 修正1：将 mounted:{} 改为正确的生命周期钩子
  mounted() {
    // 这里可以添加组件挂载后的初始化逻辑
    this.search()
  },
  data() {

    var validateEmail=(rule,value,callback)=>{
        //需要通过正则表达式校验邮箱的格式
        //定义正则表达式
        const reg = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        //使用正则表达式校验输入的值,返回值为bool类型,如果满足则为true,否则为false
        let flag = reg.test(value)
        if (value === '') {
            callback(new Error('邮箱不能为空'));
        } else if (!flag) {
            callback(new Error('请输入正确的邮箱'));
        } else {
            callback();
        }
    };
    var validatePhone=(rule,value,callback)=>{
        //定义校验手机号的正则表达式
            const regTel = /^1[3-9]\d{9}$/;
            let flag = regTel.test(value);
            if (value === '') {
                callback(new Error('手机号不能为空'));
            } else if (!flag) {
                callback(new Error('请输入正确的手机号'))
            } else {
                callback()
            }
    };
    return {

      rules:{
         name:[
            {required:true,message:"姓名不能为空"}
         ],
         password:[
            {required:true,message:"密码不能为空"}
         ],
         username:[
            {required:true,message:"用户名不能为空"}
         ],
         email:[
            {required:true,message:"邮箱不能为空"},
            {validator:validateEmail,trigger:'blur'}
         ],
         phone:[
            {required:true,message:"电话不能为空"},
            {validator:validatePhone,trigger:'blur'}
         ],
      },
      dialogFormVisible: false,
      editForm: {
        name: '',
        username: '',
        password: '',
        email: '',
        phone: '',
      },
      formLabelWidth: '80px',
      name:'',
      phone:'',
      pageNum:1,
      pageSize:5,
      total:'',
      /* 定义一个数组用于存放表格的数据 */
      tableData: []
    };
  },
  methods: {
    /*查询请求方法*/
    search(){
      //alert("点击了查询按钮");
      //发送查询请求访问后端
      let param1={
        name:this.name,
        phone:this.phone,
        pageNum:this.pageNum,
        pageSize:this.pageSize,
      }
      request.get('/user/selectByUserNameAndPhone', { params: param1 }).then(res => {
        //console.log(res);
        if(res.code=='0'){
          this.tableData=res.data.list
          this.total=res.data.total
        }
      })
    },
    /*点击添加,弹窗方法*/
    add(){
      this.dialogFormVisible = true;
    },
    /*确认添加方法*/
    confirmEdit(editForm) {
      this.$refs[editForm].validate((valid) => {
          if(valid){
            if(this.editForm.id==null){
              request.post('user/add',this.editForm).then(res=>{
                if (res.code == '0'){
                  this.$message({message: '添加成功',type: 'success'});
                  this.dialogFormVisible = false;
                  this.search() // 添加成功后刷新表格数据
                }
                else
                {
                  this.$message({message: '添加失败',type: 'error'});
                }
              })
            }else{
              request.post('user/update',this.editForm).then(res=>{
                if (res.code == '0'){
                  this.$message({message: '添加成功',type: 'success'});
                  this.dialogFormVisible = false;
                  this.search() // 添加成功后刷新表格数据
                }
                else
                {
                  this.$message({message: '添加失败',type: 'error'});
                }
              })
            }
          }else{
            this.$message.error("请输入正确的用户信息")
          }
      })


    },
    /*修改方法*/
    updateButton(row){
      //console.log("点击了修改按钮");
      this.editForm=row;
      this.dialogFormVisible = true;
    },
    /*删除方法*/
    deleteButton(id){
      //console.log("点击了删除按钮");
      request.delete('/user/del/'+id).then(res=>{
        if(res.code=='0'){
            this.$message.success("删除成功")
            this.search()
        }
      })
    },
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
      //console.log(`每页 ${val} 条`);
      this.pageSize=val
      this.search()
    },
    handleCurrentChange(val){
      //console.log(`当前页 ${val}`);
      this.pageNum=val
      this.search()
    },
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
