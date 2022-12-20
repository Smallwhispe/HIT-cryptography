<template>
  <div class="login-container">
<!--    填写的表单-->
    <el-form :model="ruleForm" :rules="rules"
             status-icon
             ref="ruleForm"
             label-position="left"
             label-width="0px"
             class="demo-ruleForm login-page">
      <h3 class="title">拼夕夕</h3>
<!--      填写的项目用item去封装-->
<!--      prop即为非功校验方法-->
      <el-form-item prop="username">
        <el-input type="text"
                  v-model="ruleForm.username"
                  placeholder="用户名"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password"
                  v-model="ruleForm.password"
                  placeholder="密码,请输入数字字母组合"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-radio v-model="type" label="shop">商家</el-radio>
        <el-radio style="position: relative;left: 80px" v-model="type" label="user">用户</el-radio>
      </el-form-item>
      <el-form-item style="width:100%;" index="1">
        <el-button type="primary" style="width:100%;" @click="handleSubmit" :loading="logining">登录</el-button>
      </el-form-item>
      <el-form-item style="width:100%;" index="2">
        <el-button type="primary" style="width:100%;" @click="register" :loading="registering">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import JSEncrypt from 'jsencrypt';
import AES from "@/AES";

export default {
  name: "Login",
  data(){
    return{
      logining: false,
      registering: false,
      ruleForm: {
        username: 'admin',
        password: '123456'
      },
      loginMessage: {
        username: "",
        password: "",
        key: "",
        // hash: "",
      },
      type: 'shop',
      rules: {
        // username和password本身就是校验规则
        // required:必填与否
        // message:触发校验后显示的文字
        // trigger:触发校验条件  blur:光标离开(失去焦点)
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}]
      }
    }
  },
  methods: {
    // readFile (filePath){
    //   // 创建一个新的xhr对象
    //   let xhr = null, okStatus = document.location.protocol === 'file' ? 0 : 200
    //   xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP')
    //   xhr.open('GET', filePath, false)
    //   xhr.overrideMimeType('text/html;charset=utf-8')
    //   xhr.send(null)
    //   return xhr.status === okStatus ? xhr.responseText : null
    // },
    register(){
      this.$router.push('/register')
    },
    handleSubmit(){
      this.$refs.ruleForm.validate((valid) => {
        if(valid){
          const key = AES.generatekey(16);

          //如果是对象/数组的话，需要先JSON.stringify转换成字符串
          // 不传key值，就默认使用上述定义好的key值

          this.loginMessage.username = AES.encrypt(this.ruleForm.username, key);
          this.loginMessage.password = AES.encrypt(this.ruleForm.password, key);

          const rsa_pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEv7pIdcexHyTCWA7U6llaU0CKHZRanjqkNE8AsPQjyQv66SRUPaiUqui0kwC/iIE79cYUaYkuzk/EJBhs/MkMuefDKBfRl0ZSWNrt0c5bA5/moF6rVK644+fSdn82GsVoeQvjDG78BYYJlW8NUTxcRupksDN8GB1whhhHF2qmbQIDAQAB";

          let encryptor = new JSEncrypt();
          // 设置公钥
          encryptor.setPublicKey(rsa_pub);
          this.loginMessage.key = encryptor.encrypt(key);

          // const fs = require('fs');
          // let signPrivateKey;
          // fs.readFile('./common/ca_private_key.txt', function (err, data) {
          //   //当文件读取失败时，可以获取到err的值，输出错误信息
          //   if (err) throw err
          //   //当文件读取成功时，可以获取到data的值，输出响应的内容
          //   signPrivateKey = data.toString();
          // })
          //
          //
          // const sig = new jsrsasign.KJUR.crypto.Signature({
          //   "alg": "SHA256withRSA",
          //   "prov": "cryptojs/jsrsa",
          //   "prvkeypem": signPrivateKey
          // });
          //
          // // 初始化
          //
          // sig.init(signPrivateKey)
          // // 传入待加密字符串
          // sig.updateString(this.loginMessage.username+this.loginMessage.password+this.loginMessage.key);
          // // 生成密文
          // this.loginMessage.hash = jsrsasign.hextob64(sig.sign());


          this.logining = true
          let _this = this
          if(_this.type == 'user'){
            axios.get('http://172.24.218.142:8181/user/login', {params:_this.loginMessage}).then(function (resp) {
              _this.logining = false
              if(resp.data.code == -1){
                _this.$alert('用户名不存在', '提示', {
                  confirmButtonText: '确定'
                })
              }
              if(resp.data.code == -2){
                _this.$alert('密码错误', '提示', {
                  confirmButtonText: '确定'
                })
              }
              if(resp.data.code == 0){
                //跳转到Shop

                //展示当前登录用户信息
                localStorage.setItem('user', JSON.stringify(resp.data.data));
                _this.$router.replace({path: '/user'})
              }
            })
          }
          if(_this.type == 'shop'){
            axios.get('http://172.24.218.142:8181/shop/login', {params:_this.loginMessage}).then(function (resp) {
              _this.logining = false
              if(resp.data.code == -1){
                _this.$alert('用户名不存在', '提示', {
                  confirmButtonText: '确定'
                })
              }
              if(resp.data.code == -2){
                _this.$alert('密码错误', '提示', {
                  confirmButtonText: '确定'
                })
              }
              if(resp.data.code == 0){
                //跳转到Shop
                //展示当前登录用户信息
                localStorage.setItem('shop', JSON.stringify(resp.data.data));
                //当前路由跳转
                _this.$router.replace({path: '/shop'})
              }
            })
          }
        }
      })
    }
  }
};
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100%;
}
.login-page {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
label.el-checkbox.rememberme {
  margin: 0px 0px 15px;
  text-align: left;
}
</style>