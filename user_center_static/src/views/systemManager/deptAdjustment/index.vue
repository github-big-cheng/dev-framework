<!--
 * @Author: your name
 * @Date: 2021-05-10 16:39:24
 * @LastEditTime: 2021-08-13 14:52:41
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\views\components\dialog.vue
-->
<template>
    <el-row :gutter="24" class="asideormain" :class="isAsideCollapse ? '': 'aside-packup'">
        <el-col :span="6" class="asidewrap" :width="isAsideCollapse?'20px':'290px'">
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
                                v-model="searchForm.personNameQueryLike"
                                clearable
                                :focus="true"
                                class="input-search"
                                placeholder="请输入人员名称"
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
                :hasLook="$filterBtnShow(['ucenter_org_edit', 'ucenter_org_view'])"
                @clickSelection="handleClickSelection"
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
        </el-col>
    </el-row>
</template>

<script>
import foldTree from '@/components/fold-tree'
import TableCom from '@/components/table'
import Pagination from "@/components/pagination";
import changeTable from "@/components/change-table";
import operationCom from '@/components/operation'

export default {
    name: 'deptAdjustment',
    components: {
        foldTree,
        TableCom,
        Pagination,
        changeTable,
        operationCom,
    },
    data () {
        return {
            // drawer:false,
            // direction: 'rtl',
            isAsideCollapse:true,
            searchForm:{
                pageNo:1,
                pageSize:10,
                orderBy:"",
                personNameQueryLike:"",
                pathIds: null,
            },
            deptId:"",
            deptName:"",
            treeListData: [],
            height:null,
            selectionList:[],
            btnConfigs: [
                {
                    type: 'add',
                    text: '调整',
                    icon: 'el-icon-aliadd',
                    has: "ucenter_dept_person_add",
                    handlerType: 'handleAddClick'
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
                    colKey: "deptName",
                    prop: "deptName",
                    label: "部门名称",
                    width: null,
                    orderBy: "cname",
                    asc: "",
                },
                {
                    colKey: "personName",
                    prop: "personName",
                    label: "人员",
                    width: null,
                    orderBy: "personName",
                    asc: "",
                },
                {
                    colKey: "posName",
                    prop: "posName",
                    label: "职位名称",
                    width: null,
                },
                {
                    colKey: "orderNo",
                    prop: "orderNo",
                    label: "顺序号",
                    minWidth: 40,
                },
                {
                    colKey: 'orgName',
                    prop: 'orgName',
                    label: '机关（单位）',
                    width: null,
                },
            ],
            tableTit:[],
            tableData:[],
            showChangeTable:false,
            seTableList:[],
            
        }
    },
    watch: {
        'searchForm.personNameQueryLike'(val) {
            if (val.trim() === '') {
                this.getTableList();
            }
        }
    },
    created(){
        this.getDeptTree();
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
        // foldtree
        async getDeptTree() {
            let res = await this.$http.getUcenterOrgTree();
            if (res.code == 0) {
                let organizationList = this.$formatTree(
                    res.data,
                    "listPerson",
                    true,
                    'tree-filebox',
                    'tree-file',
                    "",
                    false,
                    true,
                )

                organizationList.forEach((item) => {
                    item.icon = 'tree-filebox';
                });
                this.treeListData = organizationList;
            }
        },
        handleClickNode({id, cname, pathIds}) {
            this.deptId = id;
            this.deptName = cname;
            this.searchForm.pathIds = pathIds;
            this.reloadTableList();
        },
        //table
        getTableList() {
            this.tbLoading = true;
            this.$http.getDeptAdjustmentList(this.searchForm).then((res) => {
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
            this.$getPageList("ucenter_dept_person_list", this.titList).then((data) => {
                this.tableTit = data;
            });
            this.$getPageFullList("ucenter_dept_person_list").then((data) => {
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
            let params = {
                noCache: true,
                id: this.searchForm.id,
            };
            if (this.searchForm.hasOwnProperty('id')) {
                delete params.id;
            }
            this.$router.push({
                name: "deptAdjustmentAdd",
                params: {noCache: true, deptId:this.deptId, deptName:this.deptName},
            });
        },
        // //查看函数
        // handleViewClickFunc({ id }) {
        //     if (!this.$filterBtnShow(["ucenter_dept_person_view"])) {
        //         this.$showWarning("对不起，您没有查看的权限");
        //         return false;
        //     }
        //     this.$router.push({
        //         name: "deptView",
        //         params: { noCache: true, id },
        //     });
        //     return true;
        // },
        //删除
        handleDeleteClick() {
            if (this.selectionList.length == 0) {
                this.$showWarning("请选择要删除的数据");
                return;
            }
            this.$confirm("此操作会删除该人员, 是否继续?", "提示", {
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
                this.$http.getUcenterOrgDelete({idQueryIn, compType: '10027-10'}).then((res) => {
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
            this.$setDefaultTable("ucenter_dept_list").then((res) => {
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
