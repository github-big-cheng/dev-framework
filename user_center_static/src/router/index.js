/*
 * @Author: your name
 * @Date: 2021-03-27 17:57:59
 * @LastEditTime: 2021-08-26 18:01:13
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\router\index.js
 */
import Vue from 'vue'
import Router from 'vue-router'

//const Layout = () => import("@/views/layout/layout.vue")

Vue.use(Router)
const originalReplace = Router.prototype.replace;
Router.prototype.replace = function replace(location) {
  return originalReplace.call(this, location).catch(err => err);
};
const routerPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(error => error)
}

export const constantRouterMap = [
  { path: '/login', name: 'login', meta: { noWait: true } , component: () => import("@/views/login/index.vue") },
  { path: "/404", name: "page404",  meta: { noWait: true } , component: () => import("@/views/page404.vue"), hidden: true },
  // { path: "/downloadCenter", name: "downloadCenter",  meta: { title: "下载中心",　tagTitle:"下载中心",　noWait: true } , component: () => import("@/views/downloadCenter/index.vue")},
  { path: '/home',name: 'home', meta: { noWait: true, title: '首页', noCache: true }, component: () => import("@/views/homePage")},
  { path: '/redirectLogin', name: 'redirectLogin', meta: { noWait: true } , component: () => import("@/views/login/redirectLogin.vue") },

  { path: '/', redirect: '/home', meta: { noWait: true } }
]

const createRouter = () => new Router({
  // mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
const router = createRouter();
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher;
}
export default router;


