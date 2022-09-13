<!--
 * @Author: your name
 * @Date: 2021-05-07 10:05:31
 * @LastEditTime: 2021-05-08 11:47:58
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\README.md
-->
# enforcecorps

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

### src目录文件介绍

##### + api 所有请求
        - api.js 集成modules下所有文件
        + modules 
            - index.js 按项目板块创建对应的js

##### + assets  主题 字体 图片等静态资源

##### + components  全局公用组件
        * 组件按功能创建对应的文件夹
        + 组件名 
            -index.js

##### + directive  全局指令
        * 自定义指定按功能创建对应的js文件, 并在main.js中导入挂载
        - dialogDrag.js * 移动弹窗 v-dialogDrag
        - focus.js * 输入框聚焦 v-focus
        - hasPermission.js * 判断权限 v-has

##### + filters  全局 filter
        * 过滤函数按模块创建对应的js文件, 并在main.js中导入挂载

##### + icons  项目所有 svg icons
       

##### + router  路由
        + modules * 路由按模块划分，创建对应的js文件
        - addRouter.js * 集成modules文件夹中所有路由文件并按权限匹配动态插入，并设置路由meta中相关属性
        - index.js * 路由入口文件，包含默认路由（无须验证用户权限）和路由拦截


##### + styles  全局样式
        * 按组件划分对用的scss文件，如果默认加载则需在index.scss中导入

##### + store  全局 store管理
        + modules * 按功能模块划分，创建对应的js文件
        - getter.js 开放state中部分属性接口
        - index.js 状态管理入口文件，集成modules文件夹中所有文件


##### + utils  全局公用方法
        - auth.js * 设置token和用户相关信息存入到localStorage和cookie中，开发存入和删除方法
        - common.js * 全局方法--已经在main.js中集成
        - formatMenu.js * 设置和获取左侧菜单信息
        - mixin.js * 全局混入
        - request.js * ajax请求响应封装，其中集成了全局等待层的处理


##### + views 所有页面
        + layout  * 包含头部，左侧菜单导航，页签，用户设置，主视图区域全局组件
            + components 
                + Settings * 用户自定义设置，显示隐藏页签、换肤等
                + Sidebar * 左侧菜单导航
                + TagsView * 页签
                - AppMain.vue * 主视图区域
                - index.js * 组件入口js
                - Header.vue * 头部
            + mixin * 局部混入
            - index.vue *入口文件
        + login * 登录
            - index.vue
        - page404.vue * 404页面
        - viewFile.vue * 在线查看文档


##### - App.vue 入口页面

##### - main.js 入口文件 加载组件 初始化等

##### - permission.js 权限管理

##### - settings.js 用户自定义设置
        
        