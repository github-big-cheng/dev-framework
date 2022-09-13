<!--
 * @Author: your name
 * @Date: 2021-08-06 11:23:33
 * @LastEditTime: 2021-08-06 14:16:26
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\views\systemConfigure\logManager\index.vue
-->
<template>
    <div class="mainwrap-has-tab">
        <el-tabs v-model="activeName" @tab-click="handleTabClick">
            <el-tab-pane :label="item.label" :name="item.name" v-for="(item, index) in logTabNav" :key="index">
                <component
                    :is= "currentTabComponent"
                ></component>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
  import { getLocalStorage } from '@/utils/auth'
  export default {
    name: "logList",
    components: {
        securityLog: () => import(/* webpackChunkName: 'logManager/securityLog' */ "./component/securityLog.vue"),
        operationLog: () => import(/* webpackChunkName: 'logManager/operationLog' */ "./component/operationLog.vue"),
    },
    data() {
      return {
        activeName: 'first',
        logTabNav:[
            {
                label: "登录日志",
                name: "first",
                path: "securityLog",
                code: 'ucenter_login_log_list'
            },
            {
                label: "操作日志",
                name: "second",
                path: "operationLog",
                code: 'sys_operation_log_list'
            }
        ],
        currentTabComponent: "securityLog",
      };
    },
    created() {
      let userInfo = getLocalStorage("userInfo");
      
      this.logTabNav = this.logTabNav.filter(item => userInfo && new Set(userInfo.authCodes).has(item.code))
    },
    methods: {
      handleTabClick(tab) {
        this.logTabNav.forEach(item => {
            if(item.name == tab.name){
                return this.currentTabComponent = item.path;
            }
        })
      }
    }
  };
</script>