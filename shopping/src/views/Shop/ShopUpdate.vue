<template>
  <el-container class="home_container">
    <el-header class="home_header">
      <div class="home_title">拼夕夕-商家编辑</div>
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
            <div style="margin-top: 60px;margin-left:330px;width: 300px;height: 500px;border: 0px solid red;" >
              <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="ruleForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                  <el-input v-model="ruleForm.password"></el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                  <div style="width: 170px;height: 30px;">
                    <template>
                      <el-radio v-model="ruleForm.gender" label="男">男</el-radio>
                      <el-radio v-model="ruleForm.gender" label="女">女</el-radio>
                    </template>
                  </div>
                </el-form-item>
                <el-form-item label="账户" prop="account">
                  <el-input v-model.number="ruleForm.account"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitForm('ruleForm')">立即修改</el-button>
                  <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>

          <router-view></router-view>
        </el-main>
        <el-footer class="home_footer">电子商务平台</el-footer>
      </el-container>

    </el-container>

  </el-container>
</template>

<script>
export default {
  data() {
    return {
      category:null,
      ruleForm: {
        username: '',
        password: '',
        name: '',
        gender: '男',
        account: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        account: [
          { required: true, message: '请输入账户', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      const _this = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.put('http://172.24.218.142:8181/shop/update',_this.ruleForm).then(function (resp) {
            if(resp.data.code == 0){
              _this.$alert(_this.ruleForm.username+'修改成功', '', {
                confirmButtonText: '确定',
                callback: action => {
                  _this.$router.push('/shopDefault')
                }
              });
            }
          })
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  },
  created() {
    const _this = this
    axios.get('http://172.24.218.142:8181/shop/findById/'+_this.$route.query.id).then(function (resp) {
      _this.ruleForm = resp.data.data
    })
  }
}
</script>
