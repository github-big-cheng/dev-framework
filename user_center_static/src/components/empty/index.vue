<!--
 * @Author: your name
 * @Date: 2021-08-03 17:28:29
 * @LastEditTime: 2021-08-05 11:28:10
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \nwxt_oa\src\components\empty\index.vue
-->
<template>
    <div class="empty-data">
        <svg class="icon svg-icon" aria-hidden="true">
            <use xlink:href="#icon-nodata"></use>
        </svg>
        <p>{{ text }}</p>
    </div>
</template>

<script>
export default {
    name: "emptyCom",
    props: {
        text: {
            type: String,
            default: () => "暂无数据",
        },
    },
};
</script>

<style lang="scss" scoped>
.empty-data {
    text-align: center;
    font-size: 12px;
    color: #999;
    line-height: initial;

    .svg-icon {
        max-width: 40px;
        max-height: 50px;
    }

    p {
        margin: 0 0 0 -10px;
    }
}
</style>
