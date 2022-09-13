<!--
 * @Author: your name
 * @Date: 2021-05-10 16:39:24
 * @LastEditTime: 2021-08-06 11:26:28
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\views\components\dialog.vue
-->
<template>
    <div class="main-wrapper">
        <div class="search-box">
            <div class="asearch-form input-w260">
                <el-form :inline="true" :model="searchForm" @submit.native.prevent>
                    <el-form-item label="">
                        <el-input
                            v-model="searchForm.codeOrNameLike"
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
            :addRowClassName="setRowClassName"
            :hasLook="$filterBtnShow(['ucenter_role_edit', 'ucenter_role_view'])"
            @clickSelection="handleClickSelection"
            @dbClick="dbEditClick"
            @lookClick="handleViewClickFunc"
            @setTableClick="showChangeTable = true"
            @sortClick="handleSetSort"
        >
            <template slot-scope="{scope}" slot="sysCon">
                 <!-- 0用户、1系统 -->
                <span v-if="scope.row.isSys == 0">用户</span>
                <span v-if="scope.row.isSys == 1">系统</span>
            </template>
        </table-com>
            <!-- @handleRowClicked="handleRowClick" -->

        <pagination
            :total="total"
            :defaultPage="searchForm.pageNo"
            @changePageSize="changePageSize"
            @changeCurrentPage="changeCurrentPage"
            v-show="tableData.length && !tbLoading"
        ></pagination>

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
import TableCom from '@/components/table'
import Pagination from "@/components/pagination";
import changeTable from "@/components/change-table";
import operationCom from '@/components/operation'

export default {
    name: 'ucenterRole',
    components: {
        TableCom,
        Pagination,
        changeTable,
        operationCom,
    },
    data () {
        return {
            searchForm:{
                pageNo:1,
                pageSize:10,
                orderBy:"",
                codeOrNameLike:"",
            },
            height:null,
            selectionList:[],
            btnConfigs: [
                {
                    type: 'add',
                    text: '新增',
                    icon: 'el-icon-aliadd',
                    has: 'ucenter_role_add',
                    handlerType: 'handleAddClick'
                },
                {
                    type: 'modify',
                    text: '修改',
                    icon: 'el-icon-alimodify',
                    has:"ucenter_role_edit",
                    handlerType: 'handleEditClick',
                },
                {
                    type: 'remove',
                    text: '删除',
                    icon: 'el-icon-aliback',
                    has:"ucenter_role_delete",
                    handlerType: 'handleDeleteClick'
                },
                {
                    type: 'refresh',
                    text: '刷新',
                    icon: 'el-icon-alirefresh',
                    handlerType: 'getTableList'
                }
            ],
            total:0,
            tbLoading:false,
            titList: [
                {
                    colKey: "name",
                    prop: "name",
                    label: "名称",
                    width: null,
                    orderBy: "name",
                    asc: "",
                    type: "click",
                    isOperation: true,
                },
                {
                    colKey: "code",
                    prop: "code",
                    label: "代码",
                    minWidth: 40,
                    orderBy: "code",
                    asc: "",
                },
                {
                    colKey: "isSystem",
                    prop: "issys",
                    label: "系统/用户",
                    minWidth: 50,
                    type:"isSlot",
                    slotName:"sysCon"
                },
                {
                    colKey: "createBy",
                    prop: "createByName",
                    label: "创建人",
                    minWidth: 50,
                },
                {
                    colKey: "createTime",
                    prop: "createTime",
                    label: "创建时间",
                    minWidth: 60,
                },
                {
                    colKey: 'orgName',
                    prop: 'orgName',
                    label: '机关（单位）',
                    width: null,
                },
            ],
            tableTit:[],
            tableData:[{}],
            showChangeTable:false,
            seTableList:[],
        }
    },
    created(){
        this.getTableList();
        this.getConfigList();
    },
    mounted(){
        this.getTbHeight();
    },
    methods: {
        operationHandler(type) {
            this[type]()
        },
        //table
        getTableList() {
            this.tbLoading = true;
            this.$http.getUcenterRoleList(this.searchForm).then((res) => {
                if (res.code == 0) {
                    this.tableData = res.data.list.map((item) => {
                        return item;
                    });
                   
                    this.total = res.data.total;
                    this.tbLoading = false;
                }
                this.closeLoading(this.$route);
                })
            .catch((err) => this.closeLoading(this.$route));
        },
        getTbHeight() {
            setTimeout(async () => {
                try {
                    this.height = await this.$formatTableHeight();
                } catch (e) {}
            }, 0);
        },
        getConfigList() {
            this.$getPageList("ucenter_role_list", this.titList).then((data) => {
                this.tableTit = data;
            });
            this.$getPageFullList("ucenter_role_list").then((data) => {
                this.seTableList = data;
            });
        },
        handelSelecTable(item) {
            
        },
        setRowClassName(row) {
            if (row.isReview == "0") { //未读
                return 'unread-row';
            }
            return '';
        },
        //多选复选框
        handleClickSelection(item) {
            this.selectionList = item;
        },
        //operation
        //添加
        handleAddClick() {
            this.$router.push({
                name: "roleAdd",
                params: {noCache: true},
            });
        },
        //查看函数
        handleViewClickFunc({ id }) {
            if (!this.$filterBtnShow(["ucenter_role_view"])) {
                this.$showWarning("对不起，您没有查看的权限");
                return false;
            }
            this.$router.push({
                name: "roleView",
                params: { noCache: true, id },
            });
            return true;
        },
        //编辑函数
        handleEditClickFunc({ id, isSys }) {
            if (!this.$filterBtnShow(["ucenter_role_edit"])) {
                this.$showWarning("对不起，您没有修改的权限");
                return false;
            }
            if (isSys == 1) {
                this.$showWarning("系统初始化角色不允许修改！");
                return false;
            }


            this.$router.push({
                name: "roleEdit",
                params: { noCache: true, id },
            });
            return true;
        },
        //编辑
        handleEditClick() {
            if (this.selectionList.length == 0) {
                this.$showWarning("请选择一条数据");
            } else if (this.selectionList.length > 1) {
                this.$showWarning("只能选择一条数据");
            } else {
                this.handleEditClickFunc(this.selectionList[0]);
            }
        },
        //双击
        dbEditClick(item) {
            this.handleEditClickFunc( item ) || this.handleViewClickFunc( item );
        },
        //删除
        handleDeleteClick() {
            if (this.selectionList.length == 0) {
                this.$showWarning("请选择要删除的数据");
                return;
            }

            let flag = true;
            let idsArr = [];
            this.selectionList.forEach((item) => {
                if (item.isSys == 1) {
                    flag = false;
                    return false;
                }
                idsArr.push(item.id);
            });
            if (!flag) {
                this.$showWarning("系统初始化角色不允许删除！");
                return;
            }
            let ids = idsArr.join(",");

            this.$confirm("此操作会删除该角色, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
            .then(() => {
                this.$http.getUcenterRoleDelete({idQueryIn:ids}).then((res) => {
                    if (res.code == 0) {
                        this.$showSuccess("删除成功！");
                        this.getTableList();
                    }
                });
            })
            .catch(() => {
            });
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
        //配置表格
        handleTrueClick(list) {
            this.$setTrueTable(list).then((res) => {
                this.updateTable(res);
            });
        },
        handleDefaultClick() {
            this.$setDefaultTable("ucenter_person_list").then((res) => {
                this.updateTable(res);
            });
        },
        updateTable(res) {
            this.$showSuccess(res.message);
            this.getConfigList();
            this.showChangeTable = false;
        }
    }
}
</script>
