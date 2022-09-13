<!--
 * @Author: your name
 * @Date: 2021-08-03 17:28:29
 * @LastEditTime: 2021-08-19 16:13:06
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \nwxt_oa\src\components\operation\index.vue
-->
<template>
    <div class="operation-btn">
        <el-button
            v-for="(item, i) in btnConfigs"
            :key="i"
            :icon="item.icon"
            :loading="item.loading"
            v-has="item.has"
            @click="handlerClick(item, i)"
            :type="item.type"
            >{{ item.text }}</el-button
        >
    </div>
</template>

<script>
export default {
    props: {
        btnConfigs: {
            type: Array,
            default: [],
            index: 0
        },
    },
    methods: {
        handlerClick(item, i) {
            const state = item.has ? this.$filterBtnShow([item.has]) : true;
            if (!state) {
                this.$showWarning(`对不起，您没有${item.text}的权限！`);
                return;
            }
            this.$emit("handlerType", item.handlerType, item);
        },
    },
};
</script>

<style lang="scss" scoped>
@import "@/styles/operation-btn.scss";
</style>
