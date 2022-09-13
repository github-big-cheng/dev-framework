<template>
    <div class="main-wrapper">
        <div :class="!isCollapse ? 'search-box' : 'asearch-box'">
            <div class="asearch-form">
                <el-form :inline="true" :model="searchForm" @submit.native.prevent>
                    <el-form-item label="" v-show="isCollapse">
                        <el-input
                            v-model="searchForm.codeOrNameQueryLike"
                            clearable
                            :focus="true"
                            class="input-search"
                            placeholder="请输入名称或代码"
                            @keyup.enter.native="reloadTableList"
                        >
                            <el-button
                                slot="append"
                                icon="el-icon-alisearch"
                                @click="reloadTableList"
                            ></el-button>
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
            :selectableFun="handelSelecTable"
            :hasLook="$filterBtnShow(['sys_code_edit', 'sys_code_view'])"
            @clickSelection="handleClickSelection"
            @dbClick="dbEditClick"
            @lookClick="handleViewClickFunc"
            @setTableClick="showChangeTable = true"
            @sortClick="handleSetSort"
        >
            <template slot-scope="{scope}" slot="isSysSlot">
                <span v-if="scope.row.isSys == 1">系统</span>
                <span v-if="scope.row.isSys == 0">用户</span>
            </template>
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
import searchForm from '@/components/search-form'
import operationCom from '@/components/operation'

export default {
    name: "codeList",
    components: {
        tableCom,
        Pagination,
        changeTable,
        searchForm,
        operationCom
    },
    data() {
        return {
            height: null,
            showChangeTable: false,
            isShowAscurrVal: false,
            searchForm: {
                codeOrNameQueryLike: "",
                type: "",
                pageNo: 1,
                pageSize: 10,
                orderBy: "",
                excludePCC: 1,
                isType: 1,
                typeQueryNotIn: '10001,10001-10037,10001-10033,10001-10034,10001-10035',
            },
            searchFormConfig:[
                {
                    type: 'input',
                    label: '代码',
                    prop: 'codeQueryLike',
                },
                {
                    type: 'input',
                    label: '名称',
                    prop: 'nameQueryLike',
                },
                {
                    type: 'input',
                    label: '简称',
                    prop: 'snameQueryLike',
                },
                {
                    type: 'select',
                    label: '系统/用户',
                    prop: 'isSys',
                    children: [
                        {
                            value: "1",
                            name: "系统"
                        },
                        {
                            value: "0",
                            name: "用户"
                        }
                    ],
                },
                {
                    type: 'slot',
                    label: '',
                    slotName: 'slot-botton',
                }
            ],
            btnConfigs: [
                {
                    type: 'add',
                    text: '新增',
                    icon: 'el-icon-aliadd',
                    has : 'sys_code_add',
                    handlerType: 'handleAddClick'
                },
                {
                    type: 'modify',
                    text: '修改',
                    icon: 'el-icon-alimodify',
                    has : 'sys_code_edit',
                    handlerType: 'handleEditClick'
                },
                {
                    type: 'remove',
                    text: '删除',
                    icon: 'el-icon-aliback',
                    has: "sys_code_delete",
                    handlerType: 'handleDeleteClick'
                },
                {
                    type: 'refresh',
                    text: '刷新',
                    icon: 'el-icon-alirefresh',
                    handlerType: 'getTableList'
                }
            ],
            tableTit: [],
            titList: [
                {
                    colKey: "name",
                    prop: "name",
                    label: "名称",
                    orderBy: "name",
                    asc: "",
                    width: null,
                    type: "click",
                    isOperation: true,
                },
                {
                    colKey: "code",
                    prop: "code",
                    label: "代码",
                    width: null,
                    orderBy: "code",
                    asc: "",
                },
                {
                    colKey: "type",
                    prop: "typeValue",
                    label: "类别",
                    width: null,
                },
                {
                    colKey: "value",
                    prop: "value",
                    label: "代码值",
                    width: null,
                },
                {
                    colKey: "sname",
                    prop: "sname",
                    label: "简称",
                    minWidth: "60",
                },
                {
                    colKey: "isSystem",
                    prop: "isSys",
                    label: "系统/用户",
                    minWidth: "60",
                    type: "isSlot",
                    slotName: "isSysSlot"
                },
            ],
            tableData: [],
            selectionList: [],
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
        "searchForm.codeOrNameQueryLike"(val) {
            if (val.trim() === "") {
                this.reloadTableList();
            }
        }
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
            this.isShowAscurrVal = this.$refs.searchFormWrap.hasFromValue()
        },
        clearSearch() {
            this.$refs.searchFormWrap.clearFrom();
        },
        operationHandler(type) {
            this[type]()
        },
        //table
        getConfigList() {
            this.tableTit = this.titList;
            this.$getPageList("sys_code_list", this.titList).then((data) => {
                this.tableTit = data;
            });
            this.$getPageFullList("sys_code_list").then((data) => {
                this.seTableList = data;
            });
        },
        getTableList() {
            this.tbLoading = true;
            const searchFormVal = this.$refs['searchFormWrap']
            const searchFormData = searchFormVal ? this.$refs['searchFormWrap'].getForm() : {};
            const sendFormData = {...this.searchForm, ...searchFormData}

            this.$http.getUcenterCodeList(sendFormData).then((res) => {
                const {code, data:{list, total}} = res;
                if (code == 0) {
                    list.forEach(item => item.id = item.value)
                    this.tableData = list;
                    this.total = total;
                    this.tbLoading = false;
                }
                this.closeLoading(this.$route);
            })
            .catch(() => this.closeLoading(this.$route));
        },
        getTbHeight() {
            setTimeout(async () => {
                this.height = await this.$formatTableHeight();
            }, 0);
        },
        handelSelecTable(item) {
        },
        handleClickSelection(item) {
            this.selectionList = item;
        },
        //查看函数
        handleViewClickFunc({ value }) {
            if (!this.$filterBtnShow(["sys_code_view"])) {
                this.$showWarning("对不起，您没有查看的权限");
                return false;
            }
            this.$router.push({
                name: "codeView",
                params: { noCache: true, value },
            });
            return true;
        },
        //编辑函数
        handleEditClickFunc(item) {
            if (!this.$filterBtnShow(["sys_code_edit"])) {
                this.$showWarning("对不起，您没有修改的权限");
                return false;
            } else if (item.isSys == "1") {
                this.$showWarning("只能修改用户代码");
                return;
            }
            const value = item.value;
            this.$router.push({
                name: "codeEdit",
                params: { noCache: true, value },
            });
            return true;
        },
        //编辑
        handleEditClick() {
            if (this.selectionList.length == 0) {
                this.$showWarning("请选择一条数据");
            } else if (this.selectionList.length > 1) {
                this.$showWarning("只能选择一条数据");
            } else if (this.selectionList[0].isSys == "1") {
                this.$showWarning("只能修改用户代码");
                return;
            } else {
                this.handleEditClickFunc({ value: this.selectionList[0].value });
            }
        },
        //双击
        dbEditClick(item) {
            this.handleEditClickFunc( item ) || this.handleViewClickFunc( item );
        },
        handleAddClick() {
            this.$router.push({
                name: "codeAdd",
                params: { noCache: true },
            });
        },
        handleDeleteClick() {
            if (this.selectionList.length == 0) {
                this.$showWarning("请选择要删除的数据");
                return;
            }
            this.$confirm("此操作会删除该代码, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    let values,
                        valuesArr = [];
                    this.selectionList.forEach((item) => {
                        valuesArr.push(item.value);
                    });
                    values = valuesArr.join(",");
                    this.$http.getUcenterCodeDelete({ values }).then((res) => {
                        if (res.code == 0) {
                            this.$showSuccess("删除成功！");
                            this.reloadTableList();
                        }
                    });
                })
                .catch(() => {});
        },
        //分页操作
        changePageSize({pageSize}) {
            this.searchForm.pageSize = pageSize;
            this.getTableList();
        },
        changeCurrentPage({currentPage}) {
            this.searchForm.pageNo = currentPage;
            this.getTableList();
        },
        reloadTableList() {
            this.changeCurrentPage({currentPage: 1});
        },
        //排序
        handleSetSort({tableTit, orderBy}) {
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
            this.$setDefaultTable("sys_code_list").then((res) => {
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
