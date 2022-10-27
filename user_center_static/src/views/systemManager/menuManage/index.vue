<!--
 * @Author: your name
 * @Date: 2021-06-02 11:11:07
 * @LastEditTime: 2021-08-06 17:30:39
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\views\systemManager\menuManage\index.vue
-->
<template>
    <div class="main-wrapper menuManage">
        <div class="search-box">
            <div class="asearch-form input-w260">
                <el-form :inline="true" :model="searchForm" @submit.native.prevent>
                    <el-form-item label="">
                        <el-input
                                v-model="searchForm.nameQueryLike"
                                clearable
                                :focus="true"
                                class="input-search"
                                placeholder="请输入菜单名称"
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
        <operation-com
                @handlerType="operationHandler"
                :btnConfigs="btnConfigs"
        ></operation-com>

        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane
                    v-for="(item, index) in projectList"
                    :label="item.name"
                    :name="item.name"
                    :key="item.id"
            ></el-tab-pane>
        </el-tabs>

        <el-row
                :gutter="24"
                class="asideormain"
                :class="isAsideCollapse ? '' : 'aside-packup'"
        >
            <el-col
                    :span="6"
                    class="asidewrap"
                    :width="isAsideCollapse ? '20px' : '290px'"
            >
                <div class="aside-con">
                    <div class="hd" @click="isAsideCollapse = !isAsideCollapse">
                        <h2 v-show="isAsideCollapse">菜单</h2>
                        <span class="hd-right">
              <i class="el-icon-d-arrow-right"></i>
            </span>
                    </div>
                    <div class="bd">
                        <fold-tree
                                label="name"
                                ref="zzTree"
                                :treeLoading="treeLoading"
                                :strictly="true"
                                :treeList="treeListData"
                                :highlight="true"
                                :dfCheckedKeys="defaultIds"
                                childrenName="subFunction"
                                @clickNode="handleClickNode"
                        ></fold-tree>
                    </div>
                </div>
            </el-col>
            <el-col :span="18" class="mainwrap-has-aside">
                <table-com
                        :tableTit="tableTit"
                        :tableData="tableData"
                        :tbLoading="tbLoading || treeLoading"
                        :height="height"
                        :pageNo="searchForm.pageNo"
                        :pageSize="searchForm.pageSize"
                        :showSet="true"
                        :selectableFun="handelSelectTable"
                        :addRowClassName="setRowClassName"
                        @clickSelection="handleClickSelection"
                        @dbClick="dbEditClick"
                        @lookClick="handleViewClick"
                        @setTableClick="showChangeTable = true"
                        @sortClick="handleSetSort"
                        :hasChangeTable="true"
                        listCode="ucenter_function_list"
                        @updateTable="getConfigList"
                ></table-com>
                <pagination
                        :total="total"
                        :defaultPage="searchForm.pageNo"
                        @changePageSize="changePageSize"
                        @changeCurrentPage="changeCurrentPage"
                        v-show="tableData.length && !tbLoading"
                ></pagination>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import searchForm from '@/components/search-form'
    import CustomTime from '@/components/custom-time'
    import pageTitle from '@/components/page-title'
    import foldTree from '@/components/fold-tree'
    import TableCom from '@/components/table'
    import Pagination from '@/components/pagination'
    import changeTable from '@/components/change-table'
    import operationCom from '@/components/operation'

    export default {
        name: 'menuManage',
        components: {
            searchForm,
            CustomTime,
            pageTitle,
            foldTree,
            TableCom,
            Pagination,
            changeTable,
            operationCom,
        },
        data() {
            return {
                isAsideCollapse: true,
                searchForm: {
                    pageNo: 1,
                    pageSize: 10,
                },
                opTimeType: '',
                opTime: [],
                height: null,
                isCollapse: true,
                isCleanSearch: false,
                isShowAscurrVal: false,
                selectionList: [],
                btnConfigs: [
                    {
                        type: 'add',
                        text: '新增',
                        icon: 'el-icon-aliadd',
                        handlerType: 'handleAddClick',
                        has: 'ucenter_function_add',
                    },
                    {
                        type: 'modify',
                        text: '修改',
                        icon: 'el-icon-alimodify',
                        handlerType: 'handleEditClick',
                        has: 'ucenter_function_edit',
                    },
                    {
                        type: 'remove',
                        text: '删除',
                        icon: 'el-icon-aliback',
                        code: 'ucenter_function_delete',
                        handlerType: 'handleDeleteClick',
                    },
                    {
                        type: 'refresh',
                        text: '刷新',
                        icon: 'el-icon-alirefresh',
                        code: 'ucenter_function_view',
                        handlerType: 'getTableList',
                    },
                ],
                total: 0,
                tbLoading: false,
                titList: [
                    {
                        colKey: 'parent',
                        prop: 'parentName',
                        label: '上级菜单',
                    },
                    {
                        colKey: 'name',
                        prop: 'name',
                        label: '名称',
                    },
                    {
                        colKey: 'code',
                        prop: 'code',
                        label: '代码',
                    },
                    {
                        colKey: 'action',
                        prop: 'action',
                        label: '请求地址',
                    },
                    {
                        colKey: 'imgPath',
                        prop: 'imgPath',
                        label: '图标路径',
                    },
                    {
                        colKey: 'orderNo',
                        prop: 'orderNo',
                        label: '排序号',
                    },
                ],
                tableTit: [],
                tableData: [],
                showChangeTable: false,
                seTableList: [],
                activeName: "",
                activeIndex: 0,
                treeListData: [],
                defaultIds: [],
                selectNode: null,
                treeLoading: false,
                projectList: []
            }
        },
        watch: {
            isCollapse() {
                setTimeout(() => {this.getTbHeight()}, 0)
            },
            async 'searchForm.nameQueryLike'(val) {
                if (val.trim() == '') {
                    let {total, list} = await this.getMenuListChild(this.defaultIds[0])

                    list.forEach((item) => item.parentName = this.selectNode.name)

                    this.total = total
                    this.tableData = []
                    this.tableData.push(...list)
                    this.tbLoading = false
                }
            },
        },
        created() {
            this.projectCombox();
            this.getConfigList()
        },
        mounted() {
            this.getTbHeight()
        },
        methods: {
            handleClick(tab) {
                this.activeIndex = tab.index;
                this.getMenuList(true)
            },
            getConfigList() {
                this.$getPageList('ucenter_function_list', this.titList).then((data) => {
                    this.tableTit = data
                })
            },
            async projectCombox() {
                const {code, data} = await this.$http.projectCombox({});
                if (code == 0) {
                    this.projectList = data.list;
                    if (this.projectList && this.projectList.length) {
                        this.activeName = this.projectList[0].name;
                    }
                    console.log(this.activeName);
                    await this.getMenuList(true);
                }
            },
            async getMenuList(init = false) {
                this.treeLoading = true
                try {
                    let {data} = await this.$http.getMenuList({
                        projectId: this.projectList[this.activeIndex].id,
                    });
                    if (!data || !data.length) {
                        this.$route.meta.noLoading = true
                        return ;
                    }

                    let menuList = this.$formatTree(
                        data,
                        'subFunction',
                        false,
                        'tree-filebox',
                        'tree-file',
                        '',
                        false,
                        true,
                        'subFunction'
                    )

                    this.treeListData = this.formatMenuList(menuList)
                    if (init) {
                        this.treeListData.length && await this.handleClickNode(this.treeListData[0], init)
                        if (this.treeListData && this.treeListData.length) {
                            this.defaultIds = [this.treeListData[0].id]
                        }
                    }
                } catch (e) {
                    console.log(e);
                } finally {
                    this.treeLoading = false;
                }
            },
            formatMenuList(menuList) {
                let isArr = typeof menuList.length !== 'undefined'
                menuList = isArr ? menuList : [menuList]
                for (let v of menuList) {
                    let tag = true
                    const menuBox = []
                    if (v.subFunction && v.subFunction.length > 0) {
                        for (let v2 of v.subFunction) {
                            if (v2.subFunction && v2.subFunction.length > 0) {
                                v2 = this.formatMenuList(v2)
                            }
                            if (v2.type != 4) {
                                tag = false
                            } else {
                                menuBox.push(v2)
                            }
                        }
                    }
                    v.expandTag = tag
                    v.menuBox = menuBox
                }
                return isArr ? menuList : menuList[0]
            },
            async getMenuListChild(id) {
                this.tbLoading = true

                let params = {
                    ...this.searchForm,
                    projectId: this.projectList[this.activeIndex].id,
                    // parentId: id,
                    idWithChildrenQueryEq: id
                };
                const {code, data} = await this.$http.getMenuListChild(params);
                if (code == 0) {
                    return data;
                }

                return {
                    total: 0,
                    list: []
                };
            },
            operationHandler(type) {
                this[type]()
            },
            /**
             * foldtree
             *
             * @param node
             * @param init
             * @returns {Promise<void>}
             */
            async handleClickNode(node = this.selectNode, init) {
                let {total, list} = await this.getMenuListChild(node.id)
                this.selectNode = node
                this.tbLoading = false

                this.total = total

                list.forEach((item) => item.parentName = node.name)

                this.tableData = []
                this.tableData.push(...list)

                if (init) {
                    this.$route.meta.noLoading = true
                }
            },
            //table
            getTableList() {
                this.tbLoading = true
                this.handleClickNode()
            },
            getTbHeight() {
                setTimeout(async () => {
                    try {
                        this.height = await this.$formatTableHeight()
                    } catch (e) {}
                }, 0)
            },
            handelSelectTable(item) {

            },
            setRowClassName(row) {
                if (row.isReview == '0') {
                    //未读
                    return 'unread-row'
                }
                return ''
            },
            //多选复选框
            handleClickSelection(item) {
                this.selectionList = item
            },
            // operation
            // 添加
            handleAddClick() {
                this.$router.push({
                    name: 'menuManageAdd',
                    params: {noCache: true},
                })
            },
            //编辑
            handleEditClick() {
                if (this.selectionList.length == 0) {
                    this.$showWarning('请选择一条数据')
                } else if (this.selectionList.length > 1) {
                    this.$showWarning('只能选择一条数据')
                } else {
                    this.$router.push({
                        name: 'menuManageEdit',
                        params: {noCache: true, id: this.selectionList[0].id},
                    })
                }
            },
            //双击
            dbEditClick({id}) {
                if (this.$filterBtnShow(['ucenter_dept_edit'])) {
                    this.$router.push({
                        name: 'deptEdit',
                        params: {noCache: true, id},
                    })
                } else {
                    if (this.$filterBtnShow(['ucenter_dept_view'])) {
                        this.$router.push({
                            name: 'tableView',
                            params: {id},
                        })
                    }
                }
            },
            //查看
            handleViewClick({id}) {
                this.$router.push({
                    name: 'deptView',
                    params: {id},
                })
            },
            //删除
            handleDeleteClick() {
                if (this.selectionList.length == 0) {
                    this.$showWarning('请选择要删除的数据')
                    return
                }
                this.$confirm('此操作会删除该菜单, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                }).then(() => {
                    let ids = '',
                        idsArr = []
                    this.selectionList.forEach((item) => {
                        idsArr.push(item.id)
                    })
                    ids = idsArr.join(',')
                    let params = {
                        idQueryIn: ids,
                    }

                    this.$http.ucenterFunctionDel(params).then((res) => {
                        if (res.code == 0) {
                            this.$showSuccess('删除成功！')
                            this.getTableList()
                        }
                    })
                }).catch(() => {
                })
            },
            //分页操作
            changePageSize({pageSize}) {
                this.searchForm.pageSize = pageSize
                this.getTableList()
            },
            changeCurrentPage({currentPage}) {
                this.searchForm.pageNo = currentPage
                this.getTableList()
            },
            reloadTableList() {
                this.changeCurrentPage({currentPage: 1})
            },
            // 排序
            handleSetSort({tableTit, orderBy}) {
                this.tableTit = tableTit
                this.searchForm.orderBy = orderBy
            },
            // 配置表格
            handleTrueClick(list) {
                this.$setTrueTable(list).then((res) => {
                    this.updateTable(res)
                })
            },
            handleDefaultClick() {
                this.$setDefaultTable('ucenter_person_list').then((res) => {
                    this.updateTable(res)
                })
            },
            updateTable(res) {
                this.$showSuccess(res.message)
                this.showChangeTable = false
            },
        },
    }
</script>

<style lang="scss" scoped>
    .menuManage {
        height: 100%;

        /deep/ .el-tabs__header {
            margin: -10px 18px 0;
        }

        .asideormain {
            height: calc(100% - 80px);
            padding-top: 12px;
        }

        .aside-con .bd {
            height: calc(100% - 30px);
        }
    }

    .main {
        padding: 0 10px;
        height: calc(100% - 114px);
    }
</style>
