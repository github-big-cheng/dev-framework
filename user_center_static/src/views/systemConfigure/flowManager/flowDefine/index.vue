<!--
 * @Author: your name
 * @Date: 2021-08-06 11:23:33
 * @LastEditTime: 2021-08-09 14:45:33
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\views\systemConfigure\flowManager\flowDefine\index.vue
-->
<template>
    <div class="mainwrap-has-tab">
        <el-tabs v-model="activeName" @tab-click="handleTabClick">
            <el-tab-pane :label="item.label" :name="item.name" v-for="(item, index) in defineTabNav" :key="index">
                <component
                    :is= "currTabComponent"
                ></component>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
  import { getLocalStorage } from '@/utils/auth'
  export default {
    name: "flowDefine",
    components: {
        Define: () => import(/* webpackChunkName: 'systemConfigure/flowManager/flowDefine' */ "./define"),
        History: () => import(/* webpackChunkName: 'systemConfigure/flowManager/flowDefine' */ "./history"),
    },
    data() {
      return {
        activeName: 'first',
        defineTabNav:[
            {
                label: "流程定义",
                name: "first",
                path: "Define",
                code: 'flow_define_manager'
            },
            {
                label: "历史流程定义",
                name: "second",
                path: "History",
                code: 'flow_define_history'
            }
        ],
        currTabComponent: "Define",
      };
    },
    created() {
      let userInfo = getLocalStorage("userInfo");
      
      this.defineTabNav = this.defineTabNav.filter(item => userInfo && new Set(userInfo.authCodes).has(item.code))
    },
    methods: {
      handleTabClick(tab) {
        this.defineTabNav.forEach(item => {
            if(item.name == tab.name){
                return this.currTabComponent = item.path;
            }
        })
      }
    }
  };
</script>

<style lang="scss" scoped>

</style>