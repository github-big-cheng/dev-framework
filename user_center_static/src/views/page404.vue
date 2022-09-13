<template>
    <div class="page404">
        <div class="svg-404">
            <svg class="icon svg-icon" aria-hidden="true">
                <use xlink:href="#icon-404"></use>
            </svg>
        </div>
        <p>您访问的页面找不到了，去首页看看吧</p>
        <div class="btn-bar">
            <el-button type="primary" class="cancel-btn" @click="goBack($route)">返回上一页</el-button>
            <el-button class="refresh-btn" @click="handleHomeClick">返回首页</el-button>
        </div>
    </div>
</template>

<script>
// import '@/assets/css/iconfont/iconfont.js';
export default {
    name:"page404",
    data(){
        return {
            homeRouter:''
        }
    },
    methods:{
        handleHomeClick() {
            // 关闭当前tab
            this.$store.dispatch('tagsView/delView', this.$router);
            this.$router.push({
                name: "home",
            });

        },
    }
}
</script>


<style lang="scss" scoped>
  @import "@/styles/page404.scss";
</style>
