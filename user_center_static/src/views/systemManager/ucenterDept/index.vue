<!--
 * @Author: your name
 * @Date: 2021-05-10 16:39:24
 * @LastEditTime: 2021-08-13 14:52:17
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\views\components\dialog.vue
-->
<template>
    <el-row :gutter="24" class="asideormain" :class="isAsideCollapse ? '' : 'aside-packup'">
        <el-col :span="6" class="asidewrap" :width="isAsideCollapse ? '20px' : '290px'">
            <div class="aside-con">
                <div class="hd" @click="isAsideCollapse = !isAsideCollapse">
                    <h2 v-show="isAsideCollapse">部门组织结构</h2>
                    <span class="hd-right">
                        <i class="el-icon-d-arrow-right"></i>
                    </span>
                </div>
                <div class="bd">
                    <fold-tree
                            label="cname"
                            ref="zzTree"
                            :strictly="true"
                            :treeList="treeListData"
                            :highlight="true"
                            @clickNode="handleClickNode"
                    ></fold-tree>
                </div>
            </div>
        </el-col>
        <el-col :span="18" class="mainwrap-has-aside">
            <div class="search-box">
                <div class="asearch-form input-w260">
                    <el-form :inline="true" :model="searchForm" @submit.native.prevent>
                        <el-form-item label="">
                            <el-input
                                    v-model="searchForm.cnameQueryLike"
                                    clearable
                                    :focus="true"
                                    class="input-search"
                                    placeholder="请输入名称"
                                    @keyup.enter.native="reloadTableList"
                            >
                                <el-button slot="append" icon="el-icon-alisearch" @click="reloadTableList"></el-button>
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
                    :hasLook="$filterBtnShow(['ucenter_org_edit', 'ucenter_org_view'])"
                    @clickSelection="handleClickSelection"
                    @dbClick="dbEditClick"
                    @lookClick="handleViewClickFunc"
                    @setTableClick="showChangeTable = true"
                    @sortClick="handleSetSort"
            ></table-com>
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

            <!-- <el-drawer
                title="我是标题"
                :visible.sync="drawer"
                :direction="direction"
                :before-close="handleCloseDrawer">
                <span>我来啦!</span>
            </el-drawer> -->
        </el-col>
    </el-row>
</template>

<script>
    import foldTree from "@/components/fold-tree";
    import TableCom from "@/components/table";
    import Pagination from "@/components/pagination";
    import changeTable from "@/components/change-table";
    import operationCom from "@/components/operation";
    import {btnConfigs, titList} from "./config";

    export default {
        name: "ucenterDept",
        components: {
            foldTree,
            TableCom,
            Pagination,
            changeTable,
            operationCom,
        },
        data() {
            return {
                // drawer:false,
                // direction: 'rtl',
                isAsideCollapse: true,
                searchForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: "",
                    personNameQueryLike: "",
                    pathIdsQueryLike: "",
                },
                treeListData: [],
                height: null,
                selectionList: [],
                btnConfigs,
                total: 0,
                tbLoading: false,
                titList,
                tableTit: [],
                tableData: [{}],
                showChangeTable: false,
                seTableList: [],
                parentId:"",
                parentValue:""
            };
        },
        watch: {
            "searchForm.personNameQueryLike"(val) {
                if (val.trim() === "") {
                    this.getTableList();
                }
            },
        },
        created() {
            this.getDeptTree();
            this.getTableList();
            this.getConfigList();
        },
        mounted() {
            this.getTbHeight();
        },
        methods: {
            operationHandler(type) {
                this[type]();
            },
            // foldtree
            async getDeptTree() {
                let res = await this.$http.getUcenterOrgTree();
                if (res.code == 0) {
                    let organizationList = this.$formatTree(
                        res.data,
                        "listPerson",
                        true,
                        "tree-filebox",
                        "tree-file",
                        "",
                        false,
                        true
                    );

                    organizationList.forEach((item) => {
                        item.icon = "tree-filebox";
                    });
                    this.treeListData = organizationList;
                }
            },
            handleClickNode({pathIds, parentId, parentValue}) {
                this.parentId = parentId;
                this.parentValue = parentValue;
                this.searchForm.pathIdsQueryLike = pathIds;
                this.reloadTableList();
            },
            //table
            getTableList() {
                this.tbLoading = true;
                this.$http
                    .getUcenterOrgList(this.searchForm)
                    .then((res) => {
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
                    } catch (e) {
                    }
                }, 0);
            },
            getConfigList() {
                this.$getPageList("ucenter_org_list", this.titList).then((data) => {
                    this.tableTit = data;
                });
                this.$getPageFullList("ucenter_org_list").then((data) => {
                    this.seTableList = data;
                });
            },
            handelSelecTable(item) {
            },
            setRowClassName(row) {
                if (row.isReview == "0") {
                    //未读
                    return "unread-row";
                }
                return "";
            },
            //点击行
            // handleRowClick({row}){
            //     this.drawer= true;
            //
            // },
            // handleCloseDrawer(done){
            //     done();
            // },
            //多选复选框
            handleClickSelection(item) {
                this.selectionList = item;
            },
            //operation
            //添加
            handleAddClick() {
                if(this.pathIds) {

                }
                let params = {
                    noCache: true,
                    id: this.searchForm.id,
                };
                if (this.searchForm.hasOwnProperty("id")) {
                    delete params.id;
                }
                this.$router.push({
                    name: "deptAdd",
                    params: {noCache: true, parentId:this.parentId, parentValue:this.parentValue},
                });
            },
            //查看函数
            handleViewClickFunc({id}) {
                if (!this.$filterBtnShow(["ucenter_org_view"])) {
                    this.$showWarning("对不起，您没有查看的权限");
                    return false;
                }
                this.$router.push({
                    name: "deptView",
                    params: {noCache: true, id},
                });
                return true;
            },
            //编辑函数
            handleEditClickFunc({id}) {
                if (!this.$filterBtnShow(["ucenter_org_edit"])) {
                    this.$showWarning("对不起，您没有修改的权限");
                    return false;
                }
                this.$router.push({
                    name: "deptEdit",
                    params: {noCache: true, id},
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
                    this.handleEditClickFunc({id: this.selectionList[0].id});
                }
            },
            //双击
            dbEditClick(item) {
                this.handleEditClickFunc(item) || this.handleViewClickFunc(item);
            },
            //删除
            handleDeleteClick() {
                if (this.selectionList.length == 0) {
                    this.$showWarning("请选择要删除的数据");
                    return;
                }
                this.$confirm("此操作会删除该数据, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        let idQueryIn = "",
                            idQueryInArr = [];
                        this.selectionList.forEach((item) => {
                            idQueryInArr.push(item.id);
                        });
                        idQueryIn = idQueryInArr.join(",");
                        this.$http.getUcenterOrgDelete({idQueryIn}).then((res) => {
                            if (res.code == 0) {
                                this.$showSuccess("删除成功！");
                                this.getTableList();
                                this.getDeptTree();
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
                this.$setDefaultTable("ucenter_org_list").then((res) => {
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
