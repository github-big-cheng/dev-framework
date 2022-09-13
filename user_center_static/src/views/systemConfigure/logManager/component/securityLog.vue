<template>
    <div class="main-wrapper">
        <div :class="!isCollapse ? 'search-box' : 'asearch-box'">
            <div class="asearch-form">
                <el-form :inline="true" :model="searchForm" @submit.native.prevent>
                    <el-form-item label="" v-show="isCollapse">
                        <el-input
                            v-model="searchForm.accountQueryLike"
                            clearable
                            :focus="true"
                            class="input-search"
                            placeholder="请输入账号"
                            @keyup.enter.native="reloadTableList"
                        >
                            <el-button slot="append" icon="el-icon-alisearch" @click="reloadTableList"></el-button>
                        </el-input>
                        <span class="link-asearch" v-show="isCollapse" @click="isCollapse = false">
                            <a :class="this.isShowAscurrVal ? 'ascurrValue' : ''" href="javascript:void(0)">高级搜索</a>
                        </span>
                    </el-form-item>
                </el-form>
            </div>
            <search-form
                v-show="!isCollapse"
                ref="searchFormWrap"
                :config="searchFormConfig"
                class="search-form-label6"
            >
                <div class="search-btn" slot="slot-botton">
                    <el-button type="primary" @click="reloadTableList">查询</el-button>
                    <el-button @click="clearSearch">清空</el-button>
                    <span class="pack-up" @click="closeSearch">收起<i class="el-icon-arrow-up"></i></span>
                </div>
            </search-form>
        </div>

        <operation-com @handlerType="operationHandler" :btnConfigs="btnConfigs"></operation-com>

        <table-com
            :tableTit="tableTit"
            :tableData="tableData"
            :tbLoading="tbLoading"
            :height="height"
            :pageNo="searchForm.pageNo"
            :pageSize="searchForm.pageSize"
            :iSelection="false"
            @setTableClick="showChangeTable = true"
            @sortClick="handleSetSort"
        >
        </table-com>

        <Pagination
            :total="total"
            :defaultPage="searchForm.pageNo"
            @changePageSize="changePageSize"
            @changeCurrentPage="changeCurrentPage"
            v-show="tableData.length > 0 && !tbLoading"
        />

        <changeTable
            :showChangeTable="showChangeTable"
            :seTableList="seTableList"
            @closeDialog="showChangeTable = false"
            @trueClick="handleTrueClick"
            @defaultClick="handleDefaultClick"
        ></changeTable>
    </div>
</template>

<script>
import tableCom from "@/components/table";
import Pagination from "@/components/pagination";
import changeTable from "@/components/change-table";
import searchForm from "@/components/search-form";
import operationCom from "@/components/operation";

export default {
    name: "securityLogList",
    components: {
        tableCom,
        Pagination,
        changeTable,
        searchForm,
        operationCom,
    },
    data() {
        return {
            height: null,
            showChangeTable: false,
            isShowAscurrVal: false,
            searchForm: {
                accountQueryLike: "",
                pageNo: 1,
                pageSize: 10,
                orderBy: "",
            },
            searchFormConfig: [
                {
                    type: "customTime",
                    label: "时间",
                    attr: ["timeQueryGe", "timeQueryLe"],
                },
                {
                    type: "input",
                    label: "类名",
                    prop: "nameQueryLike",
                },
                {
                    type: "slot",
                    label: "",
                    slotName: "slot-botton",
                },
            ],
            btnConfigs: [
                {
                    type: "refresh",
                    text: "刷新",
                    icon: "el-icon-alirefresh",
                    handlerType: "getTableList",
                },
            ],
            tableTit: [],
            titList: [
                {
                    colKey: "opContent",
                    prop: "opContent",
                    label: "内容",
                    width: 500,
                },
                {
                    colKey: "type",
                    prop: "typeName",
                    label: "事件类型",
                    width: 100,
                },
                {
                    colKey: "ip",
                    prop: "ip",
                    label: "IP地址",
                    width: 300,
                },
                {
                    colKey: "time",
                    prop: "time",
                    label: "操作时间",
                    minWidth: 80,
                    orderBy: "time",
                    asc: "",
                },
                {
                    colKey: "orgName",
                    prop: "orgName",
                    label: "机关（单位）",
                    width: null,
                },
            ],
            tableData: [],
            seTableList: [],
            total: null,
            tbLoading: true,
            isCollapse: true,
        };
    },
    watch: {
        isCollapse() {
            this.getTbHeight();
        },
        "searchForm.accountQueryLike"(val) {
            if (val.trim() === "") {
                this.reloadTableList();
            }
        },
    },
    created() {
        this.getTableList();
        this.getConfigList();
    },
    mounted() {
        this.getTbHeight();
    },
    methods: {
        //搜索
        closeSearch() {
            this.isCollapse = true;
            this.isShowAscurrVal = this.$refs.searchFormWrap.hasFromValue();
        },
        clearSearch() {
            this.$refs.searchFormWrap.clearFrom();
        },
        operationHandler(type) {
            this[type]();
        },
        //table
        getConfigList() {
            this.tableTit = this.titList;
            this.$getPageList("ucenter_login_log_list", this.titList).then((data) => {
                this.tableTit = data;
            });
            this.$getPageFullList("ucenter_login_log_list").then((data) => {
                this.seTableList = data;
            });
        },
        getTableList() {
            this.tbLoading = true;
            const searchFormVal = this.$refs["searchFormWrap"];
            const searchFormData = searchFormVal ? this.$refs["searchFormWrap"].getForm() : {};
            const sendFormData = { ...this.searchForm, ...searchFormData };

            this.$http
                .getUcenterLogList(sendFormData)
                .then((res) => {
                    const {
                        code,
                        data: { list, total },
                    } = res;
                    if (code == 0) {
                        list.forEach((item) => (item.id = item.value));
                        this.tableData = list;
                        this.total = total;
                        this.tbLoading = false;
                    }
                    this.closeLoading(this.$route);
                    this.getTbHeight();
                })
                .catch(() => this.closeLoading(this.$route));
        },
        getTbHeight() {
            setTimeout(async () => {
                this.height = await this.$formatTableHeight();
            }, 0);
        },
        //分页操作
        changePageSize({ pageSize }) {
            this.searchForm.pageSize = pageSize;
            this.getTableList();
        },
        changeCurrentPage({ currentPage }) {
            this.searchForm.pageNo = currentPage;
            this.getTableList();
        },
        reloadTableList() {
            this.changeCurrentPage({currentPage: 1});
        },
        //排序
        handleSetSort({ tableTit, orderBy }) {
            this.tableTit = tableTit;
            this.searchForm.orderBy = orderBy;
            this.reloadTableList();
        },
        //配置
        handleTrueClick(list) {
            this.$setTrueTable(list).then((res) => {
                this.updateTable(res);
            });
        },
        handleDefaultClick() {
            this.$setDefaultTable("ucenter_login_log_list").then((res) => {
                this.updateTable(res);
            });
        },
        updateTable(res) {
            this.$showSuccess(res.message);
            this.getConfigList();
            this.showChangeTable = false;
        },
    },
};
</script>
