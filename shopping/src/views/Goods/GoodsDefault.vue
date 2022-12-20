<template>
  <el-container class="home_container">
    <el-header class="home_header">
      <div class="home_title">拼夕夕-商品后台数据</div>
      <div class="home_userinfoContainer">
      </div>
    </el-header>
    <el-container>
      <el-container>
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-text="this.$router.currentRoute.name"></el-breadcrumb-item>
          </el-breadcrumb>
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
                  width="160">
              </el-table-column>
              <el-table-column
                  prop="name"
                  label="商品名称"
                  width="160">
              </el-table-column>
              <el-table-column
                  prop="price"
                  label="价格"
                  width="160">
              </el-table-column>
              <el-table-column
                  prop="style"
                  label="款式"
                  width="160">
              </el-table-column>
              <el-table-column
                  prop="shop"
                  label="商家名称"
                  width="160">
              </el-table-column>
              <el-table-column
                  prop="shopaccount"
                  label="商家账号"
                  width="160">
              </el-table-column>
              <el-table-column
                  prop="remain"
                  label="存货"
                  width="160">
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                      size="mini"
                      @click="edit(scope.row)">编辑</el-button>
                  <el-button
                      size="mini"
                      type="danger"
                      @click="del(scope.row)">删除</el-button>
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

          <router-view></router-view>
        </el-main>
        <el-footer class="home_footer">电子商务平台</el-footer>
      </el-container>

    </el-container>

  </el-container>
</template>
<script>
export default{
  data() {
    return {
      tableData:null,
      currentPage: 1,
      pageSize: 4,
      total:null,
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
          // 下拉框用change事件，input用改变焦点事件
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
      this.$router.push('/goodsUpdateDefault?id='+row.id)
    },
    del(row) {
      const _this = this
      this.$confirm('确认删除【'+row.name+'】吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete('http://localhost:8181/goods/deleteById/'+row.id).then(function (resp) {
          if(resp.data.code==0){
            _this.$alert('【'+row.name+'】已删除', '', {
              confirmButtonText: '确定',
              callback: action => {
                location.reload()
              }
            });
          }
        });
      });
    }
  },
  created() {
    const _this = this
    axios.get('http://localhost:8181/goods/list/1/'+_this.pageSize).then(function (resp) {
      console.log(resp.data)
      _this.tableData = resp.data.data.data
      _this.total = resp.data.data.total
    })
  }
}
</script>
<style>
.home_container {
  height: 100%;
  position: absolute;
  top: 0px;
  left: 0px;
  width: 100%;
}

.home_header {
  background-color: #2B2B2B;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.home_title {
  color: #C2C2C2;
  font-size: 22px;
  display: inline;
}

.home_userinfo {
  color: #fff;
  cursor: pointer;
}

.home_userinfoContainer {
  display: inline;
  margin-right: 20px;
}

.home_aside {
  background-color: #FFFFFF;
}

.home_footer {
  background-color: #FFFFFF;
  color: #000000;
  font-size: 18px;
  line-height: 60px;
  text-align: center;
}
</style>
