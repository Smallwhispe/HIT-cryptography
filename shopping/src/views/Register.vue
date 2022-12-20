<template>
  <el-container class="home_container">
    <el-header class="home_header">
      <div class="home_title">拼夕夕-身份注册</div>
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
          <div style="margin-top: 60px;margin-left:35%;width: 30%;height: 500px;border: 0px solid red;" >
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
              <el-form-item label="身份">
                <div style="width: 220px;height: 30px;">
                  <template>
                    <el-radio v-model="identity" label="商家">商家</el-radio>
                    <el-radio v-model="identity" label="用户">用户</el-radio>
                  </template>
                </div>
              </el-form-item>
              <el-form-item label="账户" prop="account">
                <el-input v-model.number="ruleForm.account"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
          <router-view></router-view>
        </el-main>
        <el-footer class="home_footer">电子商务平台</el-footer>
      </el-container>

    </el-container>

  </el-container>
</template>
<script>

import JSEncrypt from 'jsencrypt';
import AES from "@/AES";

export default{
  data() {
    return {
      category:null,
      identity:'商家',
      ruleForm: {
        id:1,
        username: '',
        password: '',
        name: '',
        gender: '男',
        account: ''
      },
      registerMessage: {
        id:'',
        username: '',
        password: '',
        name: '',
        gender: '男',
        account: '',
        key: "",
        // hash:"",
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
          this.registering = true
          const key = AES.generatekey(16);

          //如果是对象/数组的话，需要先JSON.stringify转换成字符串
          // 不传key值，就默认使用上述定义好的key值

          this.registerMessage.id = this.ruleForm.id;
          this.registerMessage.username = AES.encrypt(this.ruleForm.username, key);
          this.registerMessage.password = AES.encrypt(this.ruleForm.password, key);
          this.registerMessage.name = this.ruleForm.name;
          this.registerMessage.gender = this.ruleForm.gender;
          this.registerMessage.account = this.ruleForm.account;
          const rsa_pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEv7pIdcexHyTCWA7U6llaU0CKHZRanjqkNE8AsPQjyQv66SRUPaiUqui0kwC/iIE79cYUaYkuzk/EJBhs/MkMuefDKBfRl0ZSWNrt0c5bA5/moF6rVK644+fSdn82GsVoeQvjDG78BYYJlW8NUTxcRupksDN8GB1whhhHF2qmbQIDAQAB";

          let encryptor = new JSEncrypt();
          // 设置公钥
          encryptor.setPublicKey(rsa_pub);
          this.registerMessage.key = encryptor.encrypt(key);

          if(_this.identity == '用户'){
            axios.post('http://172.24.218.142:8181/user/save',_this.registerMessage).then(function (resp) {
              if(resp.data.code == 0){
                _this.$alert(_this.ruleForm.username+'添加成功', '', {
                  confirmButtonText: '确定',
                  callback: action => {
                    _this.$router.push('/login')
                  }
                });
              }
            })
          }else if(_this.identity == '商家'){
            axios.post('http://172.24.218.142:8181/shop/save',_this.registerMessage).then(function (resp) {
              if(resp.data.code == 0){
                _this.$alert(_this.ruleForm.username+'添加成功', '', {
                  confirmButtonText: '确定',
                  callback: action => {
                    _this.$router.push('/login')
                  }
                });
              }
            })
          }
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  },
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
