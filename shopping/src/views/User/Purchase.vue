<template>
  <div style="margin-top: 30px;margin-left:330px;width: 300px;height: 500px;border: 0px solid red;" >
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="商品名称" prop="name">
        <el-input v-model="ruleForm.name" readonly></el-input>
      </el-form-item>
      <el-form-item label="价格" prop="price">
        <el-input v-model="ruleForm.price" readonly></el-input>
      </el-form-item>
      <el-form-item label="款式" prop="style">
        <el-input v-model="ruleForm.style" readonly></el-input>
      </el-form-item>
      <el-form-item label="商家名称" prop="shop">
        <el-input v-model.number="ruleForm.shop" readonly></el-input>
      </el-form-item>
      <el-form-item label="账户" prop="account">
        <el-input v-model.number="ruleForm.account"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model.number="ruleForm.password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">立即购买</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import AES from "@/AES";
import JSEncrypt from "jsencrypt";
import {sha256} from "js-sha256";
import jsrsasign from "jsrsasign";

export default {
  data() {
    return {
      codekey:'',
      publickey :'',
      category:null,
      ruleForm: {
        name: '',
        price: '',
        style: '',
        shop: '',
        shopaccount: '',
        account: '',
        password: ''
      },
      transferForm: {
        username: 'customer',
        targetname: 'trump1',
        password: '1',
        amount: '10000',
        key: '',
        hash: '',
        orderhash: '',
        signature: '',
      },
      caForm: {
        user_name: 'bank',
        key: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入商品名称', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' }
        ],
        style: [
          { required: true, message: '请输入款式', trigger: 'blur' }
        ],
        shop: [
          { required: true, message: '请输入商家名称', trigger: 'blur' }
        ],
        account: [
          { required: true, message: '请输入账户', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    readFile (filePath){
      // 创建一个新的xhr对象
      let xhr = null, okStatus = document.location.protocol === 'file' ? 0 : 200
      xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP')
      xhr.open('GET', filePath, false)
      xhr.overrideMimeType('text/html;charset=utf-8')
      xhr.send(null)
      return xhr.status === okStatus ? xhr.responseText : null
    },
    submitForm(formName) {
      const key1 = AES.generatekey(16);
      this.caForm.user_name = AES.encrypt(this.caForm.user_name, key1);
      let encryptor1 = new JSEncrypt();
      // 设置公钥
      let rsa_pub1 = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQNi15KedoMtc2lTgoySrqbN6NLM1TMabsqcQbvX5xZtfB0+GTkl9uG2rJhXTliOZJ6TnurLXSGh7asz8V8JDZSyh/dpv0fC6zm6mJuBZpoyMlKca9X3BOCwkgjhWWRkPZ1z3k6Zr4Qv1lMJvYedUAHh3viV+/IPztfBqlRzE3dwIDAQAB';
      encryptor1.setPublicKey(rsa_pub1);
      this.caForm.key = encryptor1.encrypt(key1);

      axios.get('http://172.24.67.216:8181/caCert/GetPubkey',{params : this.caForm}).then((resp) =>{
        this.publickey = AES.decrypt(resp.data,key1);
        console.log(this.publickey)
      });

      const pay = this.ruleForm.account+this.ruleForm.shopaccount+this.ruleForm.password+this.ruleForm.price
      const payhash = sha256(pay)
      const order = this.ruleForm.shop+this.ruleForm.name+this.ruleForm.style
      this.transferForm.orderhash = sha256(order)
      console.log(this.transferForm.orderhash)
      const str = payhash+this.transferForm.orderhash
      this.transferForm.hash = sha256(str)

      const key = AES.generatekey(16);
      this.codekey = key;
      //如果是对象/数组的话，需要先JSON.stringify转换成字符串
      // 不传key值，就默认使用上述定义好的key值

      this.transferForm.username = AES.encrypt(this.ruleForm.account, key);
      this.transferForm.targetname = AES.encrypt(this.ruleForm.shopaccount, key);
      this.transferForm.password = AES.encrypt(this.ruleForm.password, key);
      this.transferForm.amount = AES.encrypt(this.ruleForm.price, key);
      this.transferForm.orderhash = AES.encrypt(this.transferForm.orderhash, key);
      this.transferForm.hash = AES.encrypt(this.transferForm.hash, key);

      const rsa_pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDoHP3cVQ0qxhn69rBSzPrR9Cl67fGHtOj+KYMdQ59BMHENUHoOXButrC3dlMdHBZJS/pfUdosXCb+8CfFcVx+jW2XC2hl1ekUdEaf7hWqrTM6Zm7HjMpd6I+2UJSoabUj8no3uVAZwHaHuCoqDlYVAyndKHqXePk9qQ2R8dpUPsQIDAQAB";
      let encryptor = new JSEncrypt();
      // 设置公钥
      console.log(rsa_pub)
      encryptor.setPublicKey(rsa_pub);
      this.transferForm.key = encryptor.encrypt(key);

      // 获得CA前端的私钥，从私钥文件内容中获取
      const signPrivateKey = this.readFile(JSON.parse(window.localStorage.getItem("user")).account+'_prikey.txt');

      let rsa = new jsrsasign.RSAKey();
      const pk = '-----BEGIN PRIVATE KEY-----' + signPrivateKey + '-----END PRIVATE KEY-----';
      rsa = jsrsasign.KEYUTIL.getKey(pk);

      const sig = new jsrsasign.KJUR.crypto.Signature({
        "alg": "SHA256withRSA",
      });

      sig.init(rsa)
      // 传入待加签的字符串
      sig.updateString(this.transferForm.hash);
      // 生成签名
      this.transferForm.signature = jsrsasign.hextob64(sig.sign());

      const _this = this
      this.$refs[formName].validate((valid) => {
        if (valid) {


          axios.put('http://172.24.107.117:8181/users/e-business',_this.transferForm).then((resp1) =>{
            // console.log(this.transferForm.signature)
            // console.log(this.transferForm.hash)
            // console.log(this.codekey)
            // console.log(resp1.data.code)
            AES.decrypt(resp1.data.code, this.codekey);
            console.log(AES.decrypt(resp1.data.code, this.codekey))
            if (AES.decrypt(resp1.data.code, this.codekey) == '0')
            {


              axios.post('http://172.24.218.142:8181/cart/save',_this.ruleForm).then((resp2) => {
                if(AES.decrypt(resp1.data.code, this.codekey) == 0){


                  _this.$alert(_this.ruleForm.name+'购买成功', '', {
                    confirmButtonText: '确定',
                    callback: action => {
                      _this.$router.push('/Cart')
                    }
                  });


                }
              })


            }
            else _this.$alert(_this.ruleForm.name+'购买失败', '', {
              confirmButtonText: '确定',
              callback: action => {
                _this.$router.push('/Cart')
              }
            });
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
    axios.get('http://172.24.218.142:8181/goods/findById/'+_this.$route.query.id).then(function (resp) {
      _this.ruleForm.name = resp.data.data.name
      _this.ruleForm.price = resp.data.data.price
      _this.ruleForm.style = resp.data.data.style
      _this.ruleForm.shop = resp.data.data.shop
      _this.ruleForm.shopaccount = resp.data.data.shopaccount
      _this.ruleForm.account = JSON.parse(window.localStorage.getItem("user")).account
    })
  }
}
</script>
