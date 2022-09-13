<!--
 * @Author: your name
 * @Date: 2021-03-27 17:57:59
 * @LastEditTime: 2021-06-03 14:01:20
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\App.vue
-->
<template>
  <div id="app">
    <router-view  v-if="inRoute == '/login'" />
    <router-view  v-else-if="inRoute == '/redirectLogin'" />
    <router-view  v-else-if="inRoute == '/viewFile'" />
    <Layout v-else/> 
  </div>
</template>

<script>
import Layout from "./views/layout/layout";
export default {
  name: "app",
  components: {
    Layout
  },
  data(){
    return{
      inRoute:'',
    }
  },
  watch:{
        $route:{
            handler(newRouter){
              this.inRoute = newRouter.path;
            }
        },
  },
};
</script>

<style>
</style>
