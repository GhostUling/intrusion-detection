<template>
  <div class="user-page">
    <div class="user">
      <el-input class="user-input" placeholder="请输入姓名" v-model="name" clearable></el-input>
      <el-input class="user-input" placeholder="请输入电话" v-model="phone" clearable></el-input>
      <el-button class="user-btn" type="primary" @click="search">搜索</el-button>
      <el-button class="user-btn" type="primary" @click="add">添加</el-button>
      <el-table :data="tableData" style="width: 100%" :row-class-name="tableRowClassName">

        <el-table-column prop="id" label="序号" width="180"></el-table-column>
        <el-table-column prop="name" label="姓名" width="180"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="phone" label="电话" width="180"></el-table-column>
        <el-table-column prop="create_time" label="创建日期" width="180"></el-table-column>

        <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button-group>
            <el-button size="mini" type="primary" @click="updateButton(scope.row)">修改</el-button>
            <el-button size="mini" type="danger" @click="deleteButton(scope.row.id)">删除</el-button>
          </el-button-group>
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

    <!-- 添加/修改用户对话框 -->
    <el-dialog class="dialog-edit" :title="editForm.id ? '修改用户' : '添加用户'" :visible.sync="dialogFormVisible" width="500px">
          <el-form :model="editForm" :rules="rules" ref="editForm">
            <el-form-item prop="name" class="user-editInput" label="姓名：" :label-width="formLabelWidth">
                <el-input v-model="editForm.name" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item prop="username" class="user-editInput" label="用户名：" :label-width="formLabelWidth">
                <el-input v-model="editForm.username" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item prop="password" class="user-editInput" label="密码：" :label-width="formLabelWidth">
                <el-input v-model="editForm.password" autocomplete="off" :type="editForm.id ? 'text' : 'password'" 
                  :placeholder="editForm.id ? '留空表示不修改密码' : '请输入密码'"></el-input>
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

export default {
  mounted() {
    this.search()
  },
  data() {
    var validateEmail=(rule,value,callback)=>{
        const reg = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
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
        id: null,
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
      tableData: []
    };
  },
  methods: {
    search(){
      let param1={
        name:this.name,
        phone:this.phone,
        pageNum:this.pageNum,
        pageSize:this.pageSize,
      }
      request.get('/user/selectByUserNameAndPhone', { params: param1 }).then(res => {
        if(res.code=='0'){
          this.tableData=res.data.list
          this.total=res.data.total
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    
    add(){
      this.resetEditForm();
      this.dialogFormVisible = true;
    },
    
    confirmEdit(editForm) {
      this.$refs[editForm].validate((valid) => {
          if(valid){
            // 如果是修改且密码为空，则删除password字段
            let formData = {...this.editForm};
            if (formData.id && !formData.password) {
              delete formData.password;
            }
            
            if(!formData.id){
              // 添加用户
              request.post('user/add', formData).then(res=>{
                if (res.code == '0'){
                  this.$message({message: '添加成功',type: 'success'});
                  this.dialogFormVisible = false;
                  this.search();
                } else {
                  this.$message({message: '添加失败',type: 'error'});
                }
              })
            } else {
              // 修改用户
              request.post('user/update', formData).then(res=>{
                if (res.code == '0'){
                  this.$message({message: '修改成功',type: 'success'});
                  this.dialogFormVisible = false;
                  this.search();
                } else {
                  this.$message({message: '修改失败',type: 'error'});
                }
              })
            }
          } else {
            this.$message.error("请输入正确的用户信息");
          }
      })
    },
    
    updateButton(row){
      this.editForm = {...row};
      this.editForm.password = ''; // 清空密码，修改时允许留空
      this.dialogFormVisible = true;
    },
    
    deleteButton(id){
      this.$confirm('确定删除该条数据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true,
        customClass: 'custom-upload-dialog'  // 添加自定义类名
      }).then(() => {
        request.delete('/user/del/'+id).then(res=>{
          if(res.code=='0'){
              this.$message.success("删除成功");
              this.search();
          } else {
              this.$message.error("删除失败");
          }
        });
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    
    resetEditForm() {
      this.editForm = {
        id: null,
        name: '',
        username: '',
        password: '',
        email: '',
        phone: '',
      };
      // 重置表单验证状态
      if (this.$refs.editForm) {
        this.$refs.editForm.resetFields();
      }
    },
    
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex === 1) {
        return "warning-row";
      } else if (rowIndex === 3) {
        return "success-row";
      }
      return "";
    },
    
    handleSizeChange(val) {
      this.pageSize = val;
      this.search();
    },
    
    handleCurrentChange(val){
      this.pageNum = val;
      this.search();
    },
  },
};
</script>

<style scoped>
.user-page {
  padding: 20px;
}

.user-input {
  width: 10%;
  margin-right: 10px;
}

.user-btn {
  width: 80px;
  margin-right: 5px;
}

.block {
  margin-top: 20px;
  text-align: left;
}

/* 修复 dialog 内部输入间距（去掉嵌套语法，兼容普通 CSS） */
.dialog-edit .user-editInput {
  margin-bottom: 20px;
}

/* 把 el-dialog 的关闭按钮定位到右上角，使用深度选择器兼容 scoped */
.dialog-edit ::v-deep .el-dialog__headerbtn,
.dialog-edit ::v-deep .el-dialog__close {
  position: absolute;
  right: 12px;
  top: 12px;
  transform: none;
  z-index: 10;
}

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

</style>