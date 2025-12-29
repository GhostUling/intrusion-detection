import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import UserView from '../views/UserView.vue'
import LoginView from '../views/LoginView.vue'
import LayoutView from '../views/LayoutView.vue'
import BookView from '../views/BookView.vue'
import BmlView from '../views/BmlView.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/',
    //name: 'layout',
    component: LayoutView,
    children:[
          {
            path: '',
            name: 'home',
            component: HomeView
          },
          {
            path: '/user',
            name: 'user',
            component: UserView
          },
          {
            path: '/book',
            name: 'book',
            component: BookView
          },
          {
            path: '/bml',
            name: 'bml',
            component: BmlView
          }
        ]
  }
]



const router = new VueRouter({
  routes
})

//定义路由守卫，如果没有登录，则不允许用户访问
router.beforeEach((to,from,next) => {
  if(to.path === '/login'){
    next();
  }
  if(to.path === '/register'){
    next();
  }
  //从浏览器中获取当前登陆用户信息
  const user = localStorage.getItem("user");
  //如果访问的路径不是登录或注册，那么就要对用户的登录状态进行判断
  if(!user && to.path !='/login' && to.path != '/register'){
    return next("/login");
  }
  next();
})    

export default router
