<!--
 * @Author: your name
 * @Date: 2021-03-27 17:57:59
 * @LastEditTime: 2021-07-02 19:19:10
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\views\layout\components\AppMain.vue
-->
<template>
    <div :class="loading ? 'app-container _loading' : 'app-container'" >
        <div style="height: 100%" v-loading="loading" element-loading-text="数据加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(255, 255, 255, 1)">
            <transition name="fade-transform" mode="out-in">
            <keep-alive :include="cachedViews">
                <router-view :class="{'visibility': loading}" :key="key"/>
            </keep-alive>
        </transition>
        </div>
        
    </div>
</template>

<script>
    export default {
        name: "appMain",
        computed: {
            cachedViews() {
                return this.$store.state.tagsView.cachedViews;
            },
            key() {
                return this.$route.path;
            },
            loading() {
                return !this.$route.meta.noLoading && !this.$route.meta.hasOwnProperty('noWait');
            }
        }
    };
</script>
