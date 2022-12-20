<template>
  <el-container class="home_container">
    <el-header class="home_header">
      <div class="home_title">拼夕夕-商家</div>
      <div class="home_userinfoContainer">
        <el-dropdown>
                  <span class="el-dropdown-link home_userinfo">
                    {{admin.name}}<i class="el-icon-arrow-down el-icon--right home_userinfo"></i>
                  </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>

      <el-aside class="home_aside" width="200px">

        <el-menu router>
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-s-platform"></i>商品模块</template>
            <el-menu-item index="/goodsAdd" :class="$route.path=='/GoodsAdd?id='+id?'is-active':''">
              <i class="el-icon-folder-add"></i>添加商品
            </el-menu-item>
            <el-menu-item index="/goodsManager" :class="$route.path=='/GoodsManager?id='+id?'is-active':''">
              <i class="el-icon-document-copy"></i>商品管理
            </el-menu-item>
          </el-submenu>

        </el-menu>

      </el-aside>

      <el-container>
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-text="this.$router.currentRoute.name"></el-breadcrumb-item>
          </el-breadcrumb>
            <router-view></router-view>
        </el-main>
        <el-footer class="home_footer">电子商务平台</el-footer>
      </el-container>

    </el-container>

  </el-container>
</template>
<script>
export default{
  methods: {
    logout(){
      let _this = this;
      this.$confirm('注销登录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        localStorage.removeItem('shop')
        _this.$router.replace({path: '/login'})
      })
    }
  },
  data(){
    return {
      admin:'',
      id:''
    }
  },
  created() {
    const id = this.$route.query.id;
    let admin = JSON.parse(window.localStorage.getItem('shop'))
    this.admin = admin
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
