<template>
  <div>
    <div>
      <!-- @keyup.enter.native 为当前元素绑定回车确认事件 -->
      <el-input style="width: 200px;margin-right: 10px;" @keyup.enter.native="search()" v-model="params.name"
        placeholder="请输入图书名称"></el-input>
      <el-input style="width: 200px;margin-right: 10px;" @keyup.enter.native="search()" v-model="params.author"
        placeholder="请输入图书作者"></el-input>
      <el-button type="warning" @click="search()">查询</el-button>
      <el-button type="info" @click="clearInput()">清空</el-button>
      <el-button type="primary" @click="alertAddDialog()">添加</el-button>
    </div>
    

    <div class="about">
      <!-- 引入elementUI的表格组件 -->
      <el-table :data="tableData" style="width: 100%" :row-class-name="tableRowClassName">
        <el-table-column prop="id" label="序号" width="60">
        </el-table-column>
        <el-table-column prop="name" label="图书名称" width="180">
        </el-table-column>
        <el-table-column prop="price" label="价格" width="180">
        </el-table-column>
        <el-table-column prop="author" label="作者" width="180">
        </el-table-column>
        <el-table-column prop="press" label="出版社" width="180">
        </el-table-column>
        <el-table-column label="图书封面" width="180">
          <template v-slot="scope">
            <el-image style="width: 100px; height: 100px" 
              :src="'http://localhost:8080/api/files/' + scope.row.img" 
              :preview-src-list="['http://localhost:8080/api/files/' + scope.row.img]">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
              <el-button slot="reference" type="danger"  style="margin-left: 10px;">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="block">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="params.pageNum"
        :page-sizes="[5, 10, 15, 20]" :page-size="params.pageSize" layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
    <div>
      <el-dialog title="请户输入信息" :visible.sync="dialogFormVisible" width="40%">
        <el-form :model="form">
          <el-form-item label="名称:" label-width="15%">
            <el-input v-model="form.name" autocomplete="off" style="width: 90%;"></el-input>
          </el-form-item>

          <el-form-item label="作者:" label-width="15%">
            <el-input v-model="form.author" autocomplete="off" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="价格:" label-width="15%">
            <el-input v-model="form.price" autocomplete="off" style="width: 90%;"></el-input>
          </el-form-item>
          <el-form-item label="出版社:" label-width="15%">
            <el-input v-model="form.press" autocomplete="off" style="width: 90%;"></el-input>
          </el-form-item>
          <!--上传文件-->
          <el-form-item label="图书封面:" label-width="15%">
            <el-upload  action="http://localhost:8080/api/files/upload" :on-success="successUpload">
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="add()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import request from '@/utils/request'
export default {
  data() {
    return {
      /* 定义一个数组用于存放表格的数据 */
      tableData: [],
      params: {
        name: '',
        author: '',
        pageNum: 1,
        pageSize: 5,
      },
      total: 0,
      dialogFormVisible: false,
      form: {
        name: '',
        author: '',
        id:'',
        price: '',
        press: '',
        img: '',
      },
    }
  },
  // 在页面加载时执行
  created() {
    this.search()
  },
  methods: {
    successUpload(res){
      this.form.img = res.data
    },
    del(id){
      request.delete("/admin/del/"+id).then(res =>{
        if(res.code==='0'){
          this.$message({
            type: 'success',
            message: '删除成功'
          }),
          this.search()
        }
        else{
          this.$message.error('删除失败')
        }
      })
    },
    load() {
      //调用封装好的request访问后端controller中的接口
      request.get("/admin/getAdminUser").then(res => {
        if (res.code === '0') {
          this.tableData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    //用户输入信息点击查询按钮调用方法
    search() {
      request.get("/Image/userid", { params: this.params }).then(res => {
        if (res.code === '0') {
          this.tableData = res.data
          this.total = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    //清空用户输入
    clearInput() {
      this.params.name = '',
        this.params.author = ''
    },
    //弹出添加表单弹窗
    alertAddDialog() {
      this.form = {}
      this.dialogFormVisible = true
    },
    edit(obj) {
      this.form = obj
      this.dialogFormVisible = true
    },
    //确认添加用户
    add() {
      request.post('/book/add', this.form).then(res => {
        if (res.code === '0') {
          this.$message({
            message: '操作成功',
            type: 'success'
          });
          this.dialogFormVisible = false
          this.search()
        }
        else {
          this.$message.error(res.msg);
        }
      })
    },

    handleSizeChange(pageSize) {
      this.params.pageSize = pageSize
      this.search()
    },
    handleCurrentChange(val) {
      this.params.pageNum = val
      this.search()
    },
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex === 1) {
        return 'warning-row';
      } else if (rowIndex === 3) {
        return 'success-row';
      }
      return '';
    }
  },
}
</script>
<style scoped>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

.block {
  margin-top: 15px;
  text-align: right;
}
</style>