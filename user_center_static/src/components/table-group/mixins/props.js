
/*
 * @Author: your name
 * @Date: 2021-08-13 16:50:22
 * @LastEditTime: 2022-03-08 09:35:11
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \nwxt_oa\src\components\popover-search-form\config\operation.js
 */

import { String } from "core-js";

/* 隐藏props tabel-props 给table props */
export default {
    searchList: {
        /**
         * 有高级搜索为Object 无高级搜索Array
         * @Object { list:[] 高级搜索list , easy:{ label value} }
         */
        type: Object | Array,
        deep: true,
    },
    /* 搜索默认值 */
    searchParams: {
        type: Object,
        deep: true,
    },
    /* 是否初始化请求列表 */
    requestInitState: {
        type: Boolean,
        default: () => true,
    },

    btnConfigs: {
        type: Array,
        default: () => [],
    },
    tableTitle: {
        type: Array,
    },
    tableUrl: {
        type: String,
    },
    tableTitleCode: {
        type: String,
        require: true,
    },
    showSet:{
        type: Boolean,
        default: () => true,
    },
    iSelection: {
        //是否显示筛选框
        type: Boolean,
        default: () => true,
    },
    isTooltip: {
        //当内容过长被隐藏时显示tooltip
        type: Boolean,
        default: () => true,
    },
    /**
     * 处理tabledata数据
     * (tableData)=>返回处理的数据
     */
    tableFilterData: {
        type: Function,
    },
    hasLook: {
        type: Boolean | Array,
        default: false
    },
    rightInfo: {
        type: Boolean,
        default: false,
    },
    headTitle: {
        type: String,
        default: () => "",
    },
    requestType: {
        type: String,
        default: () => "",
    },
    requestParams: {
        type: Object,
        default: () => {},
    },
    searchPopoverState: {
        type: Boolean,
        default: () => false,
    },
    addRowClassName: {
        //添加行样式
        type: Function,
        default: () => {
            return true;
        },
    },
    addColClassName:{
        //添加列样式
        type: Function,
        default: () => {
            return true;
        },
    },
    isFooter: {
        type: Boolean,
        default: () => true,
    },
    showSummary:{
        type: Boolean,
        default: () => false,
    },
    tableSumFilter:{
        type: Function, 
    },
    //search　label字数配置
    searchLable:{
        type: String,
        default: () => ''
    },
    currentRowKey: {
        default: 'id'
    }

}