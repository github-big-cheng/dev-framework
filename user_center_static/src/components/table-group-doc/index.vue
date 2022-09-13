<!--
 * @Author: your name
 * @Date: 2021-08-13 16:37:29
 * @LastEditTime: 2021-08-25 15:56:20
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \nwxt_oa\src\components\popover-search-form\index.vue
-->
<template>
    <div class="table-group">
        <div class="operating-button-box">
            <OperationCom :btn-configs="btnConfigs" @handlerType="handlerType" />
            
            <div class="operating-button-search" v-if="searchList && searchList.easy">
                <el-input
                    clearable
                    :focus="true"
                    v-model="searchFormData[searchList.easy.prop]"
                    class="input-search"
                    :placeholder="searchList.easy.placeholder"
                    @keyup.enter.native="reloadTableList"
                >
                    <el-button slot="append" @click="reloadTableList" icon="el-icon-alisearch"></el-button>
                </el-input>
               

                <searchPopover
                    width="auto"
                    ref="searchPopover"
                    :iconActive="formDataState"
                    @popoverShowBtn="popoverShowBtn"
                    @popoverStatus="popoverStatus"
                    @resetList="onEmpty"
                    @searchList="onCearch"
                >
                    <search-form ref="searchForm" class="searchForm" :config="searchConfig" ></search-form>
                    <!-- <span v-html="searchPopoverStyle"></span> -->
                </searchPopover>
            </div>
            <slot v-if="rightInfo"></slot>
        </div>

        <!-- 表格 -->
        <table-com
            ref="table"
            fixedHeight
            :table-tit="tableTit"
            :table-data="tableData"
            :tb-loading="tableLoading"
            :hasLook="hasLook"
            :hasChangeTable="true"
            :listCode="tableTitleCode"
            @updateTable="getConfigList"
            @clickSelection="clickSelection"
            @dbClick="dbTableClick"
            @lookClick="lookClick"
            @sortClick="handleSetSort"
            v-bind="$attrs['table-porps']"
        >
            <template v-for="item in scopedSlots" :slot="item" slot-scope="{ scope }">
                <slot :name="item" :scope="scope"></slot>
            </template>
        </table-com>

        <!-- 分页 shi-->
        <div ref="footer">
            <Pagination
                :total="pageInfo.total"
                :defaultPage="pageInfo.pageNo"
                @changePageSize="changePageSize"
                @changeCurrentPage="changeCurrentPage"
                v-show="tableData.length > 0"
            />
        </div>
    </div>
</template>

<script>
import operation from './config/operation'
import table from './config/table'
import searchPopover from './config/searchPopover'

export default {
    mixins: [ operation, table, searchPopover ],
    components: {
        OperationCom: () => import("@/components/operation"),
        tableCom: () => import("@/components/table"),
        searchPopover: () => import("@/components/search-popover"),
        searchForm: () => import("@/components/search-form"),
        Pagination: () => import("@/components/pagination"),
    },
    
    data() {
        return {
            clickSelectionList: [],
            scopedSlots: []
        };
    },
    mounted () {
        this.requestTableData()
        this.scopedSlots = Object.keys(this.$scopedSlots);
    },
};
</script>
<style lang="scss" scoped>
@import "@/styles/mixin.scss";
.table-group {
    padding-top: 15Px;
    .operating-button-box {
        display: flex;
        align-items: flex-start;
        justify-content: space-between;
        padding-right: 20px;
    }
    .operating-button-search {
        > .el-input {
            width: auto;
        }
        > a {
            margin-left: 4px;
        }
        /deep/.el-input-group__append{
            text-align: center;
        }
    }
    .ascurr-value {
        color: $cBlue;
    }
    .head-title {
        padding-top: 10px;
        font-size: 20px;
        text-align: center;
    }

    /deep/.icon-asearch{
        margin-left: 5px;
        cursor: pointer;
        &:hover{
            color: $cBlue;
        }
    }
    /deep/.search-form  {
         width: 50% !important;
         background: red;
    }
}
</style>
