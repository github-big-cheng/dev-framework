<template>
    <div class="main-wrapper">
        <div :class="!isCollapse ? 'search-box' : 'asearch-box'">
            <div class="asearch-form">
                <el-form :inline="true" :model="searchForm" @submit.native.prevent>
                    <el-form-item label="" v-show="isCollapse">
                        <el-input
                            v-model="searchForm.nameQueryLike"
                            clearable
                            :focus="true"
                            class="input-search"
                            placeholder="请输入名称"
                            @keyup.enter.native="reloadTableList"
                        >
                            <el-button
                                slot="append"
                                icon="el-icon-alisearch"
                                @click="reloadTableList"
                            ></el-button>
                        </el-input>
                    </el-form-item>
                </el-form>
            </div>
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
            @clickSelection="handleClickSelection"
            @dbClick="handleEditClick"
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
import operationCom from '@/components/operation'

export default {
    name: "applyList",
    components: {
        tableCom,
        Pagination,
        changeTable,
        operationCom
    },
    data() {
        return {
            height: null,
            showChangeTable: false,
            isShowAscurrVal: false,
            searchForm: {
                nameQueryLike: "",
                type: "",
                pageNo: 1,
                pageSize: 10,
                orderBy: "",
            },
            searchFormConfig:[
                {
                    type: 'input',
                    label: '代码',
                    value: 'codeQueryLike',
                },
                {
                    type: 'input',
                    label: '名称',
                    value: 'nameQueryLike',
                },
                {
                    type: 'input',
                    label: '简称',
                    value: 'snameQueryLike',
                },
                {
                    type: 'select',
                    label: '系统/用户',
                    value: 'isSys',
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
                    has : 'sys_project_add',
                    handlerType: 'handleAddClick'
                },
                {
                    type: 'modify',
                    text: '修改',
                    icon: 'el-icon-alimodify',
                    has : 'sys_project_save',
                    handlerType: 'handleEditClick'
                },
                {
                    type: 'remove',
                    text: '删除',
                    icon: 'el-icon-aliback',
                    has: "sys_project_delete",
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
                },
                {
                    colKey: "code",
                    prop: "code",
                    label: "代码",
                    width: null,
                    orderBy: "code",
                    asc: "",
                },
                // {
                //     colKey: "inPath",
                //     prop: "inPath",
                //     label: "内网地址",
                //     width: null,
                // },
                // {
                //     colKey: "outPath",
                //     prop: "outPath",
                //     label: "外网地址",
                //     width: null,
                // },
                {
                    colKey: "keyValue",
                    prop: "keyValue",
                    label: "key值",
                    minWidth: "40",
                },
                {
                    colKey: "orderNo",
                    prop: "orderNo",
                    label: "排序",
                    minWidth: "30",
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
        "searchForm.nameQueryLike"(val) {
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
        operationHandler(type) {
            this[type]()
        },
        //table
        getConfigList() {
            this.tableTit = this.titList;
            this.$getPageList("sys_project_list", this.titList).then((data) => {
                this.tableTit = data;
            });
            this.$getPageFullList("sys_project_list").then((data) => {
                this.seTableList = data;
            });
        },
        getTableList() {
            this.tbLoading = true;
            const searchFormVal = this.$refs['searchFormWrap']
            const searchFormData = searchFormVal ? this.$refs['searchFormWrap'].getForm() : {};
            const sendFormData = {...this.searchForm, ...searchFormData}

            this.$http.getUcenterProjectList(sendFormData).then((res) => {
                const {code, data:{list, total}} = res;
                if (code == 0) {
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
        //编辑
        handleEditClick() {
            if (this.selectionList.length == 0) {
                this.$showWarning("请选择一条数据");
            } else if (this.selectionList.length > 1) {
                this.$showWarning("只能选择一条数据");
            } else {
                this.$router.push({
                    name: "applyEdit",
                    params: { noCache: true, id: this.selectionList[0].id },
                });
            }
        },
        handleAddClick() {
            this.$router.push({
                name: "applyAdd",
                params: { noCache: true },
            });
        },
        handleDeleteClick() {
            if (this.selectionList.length == 0) {
                this.$showWarning("请选择要删除的数据");
                return;
            }
            this.$confirm("此操作会删除该应用, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    let idQueryIn,
                        idQueryInArr = [];
                    this.selectionList.forEach((item) => {
                        idQueryInArr.push(item.id);
                    });
                    idQueryIn = idQueryInArr.join(",");
                    this.$http.getUcenterProjectDelete({ idQueryIn }).then((res) => {
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
            this.$setDefaultTable("sys_project_list").then((res) => {
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
