import Vue from 'vue'
import VueRouter from 'vue-router'

import login from "../views/login";
import Register from "@/views/Register";

import Shop from "../views/Shop/Shop";
import HelloShop from "@/views/Shop/HelloShop";
import ShopUpdate from "@/views/Shop/ShopUpdate";
import ShopDefault from "@/views/Shop/ShopDefault";

import User from "@/views/User/User";
import HelloUser from "@/views/User/HelloUser";
import UserUpdate from "@/views/User/UserUpdate";
import UserDefault from "@/views/User/UserDefault";
import Browse from "@/views/User/Browse";
import Purchase from "@/views/User/Purchase";
import Cart from "@/views/User/Cart";

import GoodsAdd from "@/views/Goods/GoodsAdd";
import GoodsManager from "@/views/Goods/GoodsManager";
import GoodsUpdateShop from "@/views/Goods/GoodsUpdateShop";
import GoodsUpdateDefault from "@/views/Goods/GoodsUpdateDefault";
import GoodsDefault from "@/views/Goods/GoodsDefault";

Vue.use(VueRouter)

const routes = [
  {
    path: '/shop',
    name: '商家',
    component: Shop,
    redirect:'/helloShop',
    children:[
      {
        path: '/goodsAdd',
        name: '商品添加模块',
        component: GoodsAdd
      },
      {
        path: '/goodsManager',
        name: '商品管理模块',
        component: GoodsManager
      },
      {
        path: '/helloShop',
        name: '欢迎界面',
        component: HelloShop,
      },
      {
        path: '/goodsUpdateShop',
        name: '商家商品修改模块',
        component: GoodsUpdateShop
      },
    ]
  },
  {
    path:'/shopDefault',
    name:'商家后台',
    component: ShopDefault,
  },
  {
    path: '/shopUpdate',
    name: '商家修改模块',
    component: ShopUpdate
  },
  {
    path:'/goodsDefault',
    name:'商品后台',
    component: GoodsDefault,
  },
  {
    path: '/goodsUpdateDefault',
    name: '后台商品修改模块',
    component: GoodsUpdateDefault
  },



  {
    path:'/register',
    name:'注册',
    component: Register,
  },
  {
    path:'/login',
    name:'登陆',
    component: login
  },



  {
    path: '/user',
    name: '用户',
    component: User,
    redirect: '/helloUser',
    children: [
      {
        path: '/helloUser',
        name: '欢迎界面',
        component: HelloUser,
      },
      {
        path: '/browse',
        name: '商品浏览',
        component: Browse,
      },
      {
        path: '/cart',
        name: '购物车',
        component: Cart,
      },
      {
        path: '/purchase',
        name: '商品购买',
        component: Purchase,
      },
    ]
  },
  {
    path:'/userDefault',
    name:'用户后台',
    component: UserDefault,
  },
  {
    path: '/userUpdate',
    name: '用户修改模块',
    component: UserUpdate
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
