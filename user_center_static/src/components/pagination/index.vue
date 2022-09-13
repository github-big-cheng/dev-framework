<!--
 * @Author: your name
 * @Date: 2021-06-10 17:08:23
 * @LastEditTime: 2021-08-04 18:22:12
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\components\pagination\index.vue
-->
<template>
    <pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="defaultPage"
        :page-sizes="pageNums"
        :page-size="pageSize"
        :layout="pageLayout"
        :total="total"
    >
    </pagination>
</template>

<script>
import Pagination from './pagination';
export default {
    name: 'paginationCom',
    props: {
        pageNums: {
            type: Array,
            default: () => [10, 20, 50, 100]
        },
        pageSize: {
            type: Number,
            default: () => 10
        },
        total: {
            type: Number,
            default: () => null
        },
        pageLayout: {
            type: String,
            default: () => 'total, sizes, prev, jumper, next'
        },
        defaultPage: {
            type: Number,
            default: () => 1
        }
    },
    components: { Pagination },
    data() {
        return {};
    },
    mounted() {
        this.$emit('mounted', true);
    },
    methods: {
        handleSizeChange(pageSize) {
            this.$emit('changePageSize', { pageSize });
        },
        handleCurrentChange(currentPage) {
            this.$emit('changeCurrentPage', { currentPage });
        }
    }
};
</script>

<style lang="scss" scoped>
  @import "@/styles/pagination.scss";
</style>