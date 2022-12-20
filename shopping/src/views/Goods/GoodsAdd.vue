<template>
  <div style="margin-top: 30px;margin-left:330px;width: 300px;height: 350px;border: 0px solid red;" >
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="商品名称" prop="name">
        <el-input v-model="ruleForm.name"></el-input>
      </el-form-item>
      <el-form-item label="价格" prop="price">
        <el-input v-model="ruleForm.price"></el-input>
      </el-form-item>
      <el-form-item label="款式" prop="style">
        <el-input v-model="ruleForm.style"></el-input>
      </el-form-item>
      <el-form-item label="商家名称" prop="shop">
        <el-input v-model="ruleForm.shop" readonly></el-input>
      </el-form-item>
      <el-form-item label="商家账户" prop="shopaccount">
        <el-input v-model="ruleForm.shopaccount"></el-input>
      </el-form-item>
      <el-form-item label="存货" prop="remain">
        <el-input v-model="ruleForm.remain"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>

<script>
export default {
  data() {
    return {
      category:null,
      ruleForm: {
        name: '',
        price: '',
        style: '',
        shop: '',
        shopaccount: '',
        remain: ''
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
        shopaccount: [
          { required: true, message: '请输入商家账户', trigger: 'blur' }
        ],
        remain: [
          { required: true, message: '请输入存货', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      const _this = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('http://172.24.218.142:8181/goods/save',_this.ruleForm).then(function (resp) {
            if(resp.data.code == 0){
              _this.$alert(_this.ruleForm.name+'添加成功', '', {
                confirmButtonText: '确定',
                callback: action => {
                  _this.$router.push('/goodsManager')
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
    let item = JSON.parse(window.localStorage.getItem('shop'))
    this.ruleForm.shop = item.name
    this.ruleForm.shopaccount = item.account
  }
}
</script>
