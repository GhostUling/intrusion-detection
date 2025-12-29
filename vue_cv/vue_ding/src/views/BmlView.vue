<template>
  <div>
    <div class="about">
      <!-- 展示图片的表格 -->
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="id" label="序号" width="60">
        </el-table-column>
        <el-table-column prop="name" label="图片名称" width="180">
        </el-table-column>
        <el-table-column label="图片预览" width="300">
          <template v-slot="scope">
            <el-image 
              style="width: 150px; height: 150px" 
              :src="'http://localhost:8080/api/files/' + scope.row.img" 
              :preview-src-list="['http://localhost:8080/api/files/' + scope.row.img]"
              fit="contain">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="size" label="文件大小" width="120">
        </el-table-column>
        <el-table-column prop="type" label="文件类型" width="120">
        </el-table-column>
        <el-table-column prop="date" label="上传时间" width="180">
        </el-table-column>
      </el-table>
    </div>
    
    <div class="block">
      <el-pagination 
        @size-change="handleSizeChange" 
        @current-change="handleCurrentChange" 
        :current-page="params.pageNum"
        :page-sizes="[5, 10, 15, 20]" 
        :page-size="params.pageSize" 
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  data() {
    return {
      /* 定义一个数组用于存放图片的数据 */
      tableData: [],
      params: {
        pageNum: 1,
        pageSize: 5,
      },
      total: 0,
    }
  },
  // 在页面加载时执行
  created() {
    this.loadImages()
  },
  methods: {
    // 加载图片列表
    loadImages() {
      request.get("/files/images", { params: this.params }).then(res => {
        if (res.code === '0') {
          this.tableData = res.data.list
          this.total = res.data.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    
    handleSizeChange(pageSize) {
      this.params.pageSize = pageSize
      this.loadImages()
    },
    
    handleCurrentChange(val) {
      this.params.pageNum = val
      this.loadImages()
    }
  },
}
</script>

<style scoped>
.block {
  margin-top: 15px;
  text-align: right;
}

.el-image {
  border: 1px solid #ebeef5;
  border-radius: 4px;
}
</style>