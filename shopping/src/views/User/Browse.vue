<template>
  <div style="margin-top: 60px;margin-left:80px;border: 0px solid red;" >
    <el-form style="margin-left: -40px" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="字段：" prop="key">
        <el-select v-model="ruleForm.key" style="width: 160px;float: left" placeholder="请选择字段">
          <el-option label="商品名称" value="name"></el-option>
          <el-option label="商家名称" value="shop"></el-option>
        </el-select>
      </el-form-item>
      <div style="border: 0px solid red;width: 400px;height: 60px;position: relative;top: -64px;left: 270px">
        <el-form-item label="值：">
          <el-input v-model="ruleForm.value" placeholder="请输入关键字" style="width: 160px;"></el-input>
          <el-button type="primary" icon="el-icon-search" style="position: relative;left: 10px;" @click="submitForm('ruleForm')">搜索</el-button>
        </el-form-item>
      </div>
    </el-form>

    <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%;position: relative;top:-30px">
      <el-table-column
          fixed
          prop="id"
          label="ID"
          width="130">
      </el-table-column>
      <el-table-column
          prop="name"
          label="商品名称"
          width="130">
      </el-table-column>
      <el-table-column
          prop="price"
          label="价格"
          width="130">
      </el-table-column>
      <el-table-column
          prop="style"
          label="款式"
          width="130">
      </el-table-column>
      <el-table-column
          prop="shop"
          label="商家名称"
          width="130">
      </el-table-column>
      <el-table-column
          prop="remain"
          label="存货"
          width="130">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="danger"
              @click="edit(scope.row)">购买</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top: 20px;float: right"
                   background
                   layout="prev, pager, next"
                   :page-size="pageSize"
                   :total="total"
                   :current-page.sync="currentPage"
                   @current-change="page">
    </el-pagination>
  </div>

</template>

<script>
export default {
  data() {
    return {
      tableData:null,
      currentPage: 1,
      pageSize: 3,
      total: '',
      key: '',
      value: '',
      ruleForm: {
        key: '',
        value: '',
        page: '',
        size: 3
      },
      rules: {
        key: [
          { required: true, message: '请选择字段', trigger: 'change' }
        ]
      }
    }
  },
  methods:{
    submitForm(formName) {
      const _this = this
      //让翻页复原
      _this.currentPage = 1
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this
          _this.ruleForm.page = _this.currentPage
          axios.get('http://172.24.218.142:8181/goods/search',{params:_this.ruleForm}).then(function (resp) {
            _this.tableData = resp.data.data.data
            _this.total = resp.data.data.total
          })
        }
      });
    },
    page(currentPage){
      const _this = this
      if(_this.ruleForm.value == ''){
        axios.get('http://172.24.218.142:8181/goods/list/'+currentPage+'/'+_this.pageSize).then(function (resp) {
          _this.tableData = resp.data.data.data
          _this.total = resp.data.data.total
        })
      } else {
        _this.ruleForm.page = _this.currentPage
        axios.get('http://172.24.218.142:8181/goods/search',{params:_this.ruleForm}).then(function (resp) {
          _this.tableData = resp.data.data.data
          _this.total = resp.data.data.total
        })
      }
    },
    edit(row){
      const _this = this
      axios.get('http://172.24.218.142:8181/goods/findById/'+row.id).then(function (resp) {
        if (resp.data.data.remain > 0)
          _this.$router.push('/purchase?id='+row.id)
        else
          _this.$alert(resp.data.data.name+'无存货', {
            confirmButtonText: '确定',
            callback: action => {
              _this.$router.push('/Browse')
            }
          })
      })
    },
  },
  created() {
    const _this = this
    axios.get('http://172.24.218.142:8181/goods/list/1/'+_this.pageSize).then(function (resp) {
      _this.tableData = resp.data.data.data
      _this.total = resp.data.data.total
    })
  }
}
</script>