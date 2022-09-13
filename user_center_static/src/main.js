/*
 * @Author: your name
 * @Date: 2021-03-05 17:54:59
 * @LastEditTime: 2021-08-27 18:29:12
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\main.js
 */
import Vue from 'vue'
import App from "./App.vue";
import router from "./router/index";
import store from "./store/index";
import api from "./api/api";

import "./permission";
import '@/icons'


import "@/styles/reset.css";
import "normalize.css/normalize.css";
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

import CommonFunc from './utils/common'
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import * as filters from "./filters/index";
import mixin from './utils/mixin.js';
import Print from './utils/print.js';
import Rem from './utils/rem.js';
import has from "@/directive/hasPermission";
import "@/directive/focus";
import "@/directive/dialogDrag";
// markdown样式
import 'github-markdown-css'
// 代码高亮
import 'highlight.js/styles/github.css'

import "@/styles/index.scss";
import "./assets/icons/iconfont.css";

/* loading组件 */
import LoadingComponent from './components/loading'
import { setLocalStorage } from "@/utils/auth";

//vxeTable
import './plugins/vxetable'
//Chrome 增加了新的事件捕获机制
import 'default-passive-events'

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
//    const { mockXHR } = require('../mock')
//    mockXHR()
}

//将公共方法挂载在原型下
for (let key in CommonFunc) {
    if (Vue.prototype.hasOwnProperty('$' + key)) {
        console.warn(`${key}属性覆盖`);
        break;
    }

    Object.defineProperty(Vue.prototype, '$' + key, {
        value: CommonFunc[key],
        writable: false
    })
}

Vue.config.productionTip = false;

Vue.use(ElementUI);
Vue.component('LoadingComponent', LoadingComponent)

router.beforeEach((to, from, next) => {
    if (to.meta.menuId !== store.getters.menuId && to.meta.asyncRoute) {
        store.dispatch('tagsView/delAllViews').then(({ visitedViews }) => {
            store.commit('SET_MENUID', to.meta.menuId)
            setLocalStorage('menuId', to.meta.menuId)
            let menu = store.getters.menuListMap.get(to.meta.menuId);
            store.commit('SET_MENULIST', menu)
            next()
        })
    }

    if (to.name == 'viewFile') {
        document.getElementById('app').style.visibility = 'hidden'
    }
    if (!to.matched || to.matched.length == 0) {
        next({ name: "page404" });
        return;
    }

    // 设置来源路由
    if (to.meta && !to.meta.from) {
        to.meta.from = from.name;
        to.meta.fromParams = from.params;
        to.meta.fromQuery = from.query;
    }

    // 设置是否缓存
    if (to.params.noCache) {
        to.meta.noCache = true;
        store.dispatch('tagsView/delCachedView', to);
    }
    store.commit('CLOSE_LOADING')
    // 如name已打开且path不同，更新访问列表
    let hasOpenOther = store.getters.visitedViews.some(view => view.name == to.name && view.path != to.path);
    if (hasOpenOther) {
        store.dispatch('tagsView/updateVisitedView', to);
    }

    next();
})
router.afterEach((to, from) => {
    if (to.params.noCache) {
        setTimeout(() => {
            to.meta.noCache = false;
            store.dispatch('tagsView/addView', to);
        });
    }

    if (to.name == 'viewFile') {
        setTimeout(() => {
            document.getElementById('app').style.visibility = 'visible'
        }, 1000)
    }

})

Object.defineProperty(Vue.prototype, '$http', {
    value: api,
    writable: false
})

Object.keys(filters).forEach((key) => {
    Vue.filter(key, filters[key]);
});

Vue.mixin(mixin)// VUE全局mixin
Vue.use(Print)
Vue.use(Rem)
new Vue({
    router,
    store,
    has,
    render: (h) => h(App),
}).$mount("#app");
