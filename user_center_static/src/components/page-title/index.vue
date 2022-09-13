<!--
 * @Author: your name
 * @Date: 2021-06-15 14:27:13
 * @LastEditTime: 2021-08-05 11:29:01
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \gssp_fe\src\components\page-title\index.vue
-->
<template>
    <div
        :class="[
            'title-box',
            isFirst ? 'title-first' : '',
            isTitleBg ? 'title-bgcolor' : '',
            alignType ? alignType : 'left',
        ]"
    >
        <span class="title-text">
            <template>
                <i class="title-icon" :class="titleIcon" v-if="titleIcon"></i>
                <i class="title-line" v-else-if="isTitleLine"></i>
            </template>{{ title }}
            <el-badge v-if="countNum" class="mark" :value="countNum"/>
        </span>
        <slot name="pageRight"></slot>
    </div>
</template>

<script>
export default {
    name: "pageTitleCom",
    props: {
        title: {
            type: String,
            default: () => "首页",
        },
        countNum: {
            type: Number,
            default: () => 0,
        },
        isFirst: {
            type: Boolean,
            default: () => false,
        },
        isTitleLine: {
            type: Boolean,
            default: () => true,
        },
        titleIcon: {
            type: String,
            default: () => "",
        },
        isTitleBg: {
            type: Boolean,
            default: () => false,
        },
        alignType: {
            //标题位置
            type: String,
            default: () => "left",
        },
    },
};
</script>

<style lang="scss" scoped>
@import "@/styles/page-title.scss";
</style>
