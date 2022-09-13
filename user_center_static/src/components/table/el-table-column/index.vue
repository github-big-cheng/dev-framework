<!--
 * @Author: your name
 * @Date: 2022-01-18 17:20:05
 * @LastEditTime: 2022-01-18 17:31:04
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \gssp_fe\src\components\table\el-table-column\index.vue
-->
<template>
    <el-table-column :prop="data.prop" :label="data.label" :align="data.alignType" :width="data.width" :min-width="data.minWidth">
        <template v-if="data.children && data.children.length">
            <my-column
                v-for="(item, index) in data.children"
                :key="index"
                :data="item"
                :width="item.width"
                :min-width="item.minWidth"
            ></my-column>
        </template>
    </el-table-column>
</template>

<script>
export default {
    name: "MyColumn",
    props: {
        data: {
            type: Object,
        }
    },
};
</script>
<style scoped>
</style>