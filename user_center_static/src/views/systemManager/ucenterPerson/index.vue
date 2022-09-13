<!--
 * @Author: your name
 * @Date: 2021-05-10 16:39:24
 * @LastEditTime: 2021-08-09 11:25:36
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\views\components\dialog.vue
-->
<template>
    <el-row :gutter="24" class="asideormain" :class="isAsideCollapse ? '' : 'aside-packup'">
        <el-col :span="6" class="asidewrap" :width="isAsideCollapse ? '20px' : '290px'">
            <div class="aside-con">
                <div class="hd" @click="isAsideCollapse = !isAsideCollapse">
                    <h2 v-show="isAsideCollapse">部门</h2>
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
                <div class="asearch-form">
                    <el-form :inline="true" :model="searchForm" @submit.native.prevent>
                        <el-form-item label="" v-show="isCollapse">
                            <el-input
                                    v-model="searchForm.nameOrAccountQueryLike"
                                    clearable
                                    :focus="true"
                                    class="input-search"
                                    placeholder="请输入姓名或账号"
                                    @keyup.enter.native="reloadTableList"
                            >
                                <el-button slot="append" icon="el-icon-alisearch" @click="reloadTableList"></el-button>
                            </el-input>
                            <span class="link-asearch" v-show="isCollapse" @click="isCollapse = false">
                                <a :class="this.isShowAscurrVal ? 'ascurrValue' : ''" href="javascript:void(0)"
                                >高级搜索</a
                                >
                            </span>
                        </el-form-item>
                    </el-form>
                </div>

                <search-form v-show="!isCollapse" ref="searchFormWrap" :config="searchFormConfig">
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
                    :addRowClassName="setRowClassName"
                    :hasLook="$filterBtnShow(['ucenter_person_edit', 'ucenter_person_view'])"
                    @clickSelection="handleClickSelection"
                    @dbClick="dbEditClick"
                    @lookClick="handleViewClickFunc"
                    @setTableClick="showChangeTable = true"
                    @sortClick="handleSetSort"
            >
                <template slot-scope="{ scope }" slot="account">
                    <div class="accout-status">
                        <span>{{ scope.row.account }}</span>
                        <i v-if="scope.row.accountStatus == 2" class="el-icon-alifreeze" title="已锁定"></i>
                        <i v-if="scope.row.accountStatus == 3" class="el-icon-alilogoff" title="已注销"></i>
                    </div>
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
        </el-col>
    </el-row>
</template>

<script>
    import searchForm from "@/components/search-form";
    import foldTree from "@/components/fold-tree";
    import TableCom from "@/components/table";
    import Pagination from "@/components/pagination";
    import changeTable from "@/components/change-table";
    import operationCom from "@/components/operation";
    import {btnConfigs, searchFormConfig, titList} from './config'

    export default {
        name: "personList",
        components: {
            searchForm,
            foldTree,
            TableCom,
            Pagination,
            changeTable,
            operationCom,
        },
        data() {
            return {
                //search
                sexList: [],
                marriageList: [],
                titleList: [],
                educationList: [],
                // drawer:false,
                // direction: 'rtl',
                isAsideCollapse: false,
                searchForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: "",
                    nameOrAccountQueryLike: "",
                    pathIds: "",
                },
                treeListData: [],
                height: null,
                isCollapse: true,
                isCleanSearch: false,
                isShowAscurrVal: false,
                selectionList: [],
                btnConfigs,
                searchFormConfig,
                total: 0,
                tbLoading: false,
                titList,
                tableTit: [],
                tableData: [{}],
                showChangeTable: false,
                seTableList: [],
            };
        },
        watch: {
            isCollapse() {
                setTimeout(() => {
                    this.getTbHeight();
                }, 0);
            },
            "searchForm.nameOrAccountQueryLike"(val) {
                if (val.trim() === "") {
                    this.reloadTableList();
                }
            },
        },
        created() {
            this.getDeptTree();
            this.getTableList();
            this.getConfigList();
            this.getComboxList("10001-10004");
            this.getRoleCombox();
            this.getPositionCombox();
        },
        mounted() {
            this.getTbHeight();
        },
        methods: {
            setSearchFormConfig(index, data, attr = "children") {
                this.searchFormConfig[index][attr] = data;
            },
            getComboxList(type) {
                this.$http.getUcenterCodeCombox({type}).then((res) => {
                    const {code, data} = res;
                    if (code == 0) {
                        switch (type) {
                            case "10001-10004": //性别
                                this.setSearchFormConfig(1, data);
                                break;
                            default:
                                return;
                        }
                    }
                });
            },
            //职称
            getPositionCombox() {
                this.$http.getPositionCombox().then((res) => {
                    const {
                        code,
                        data: {list},
                    } = res;
                    if (code == 0) {
                        this.setSearchFormConfig(4, list);
                    }
                });
            },
            //角色
            getRoleCombox() {
                this.$http.getUcenterRoleCombox().then((res) => {
                    const {
                        code,
                        data: {list},
                    } = res;
                    if (code == 0) {
                        this.setSearchFormConfig(5, list);
                    }
                });
            },
            //
            operationHandler(type) {
                this[type]();
            },
            clearSearch() {
                this.$refs.searchFormWrap.clearFrom();
            },
            closeSearch() {
                this.isCollapse = true;
                this.isShowAscurrVal = this.$refs.searchFormWrap.hasFromValue();
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

                    organizationList?.forEach((item) => {
                        item.icon = "tree-filebox";
                    });
                    this.treeListData = organizationList;
                }
            },
            handleClickNode({id, pathIds}) {
                this.searchForm.pathIds = pathIds;
                this.reloadTableList();
            },
            //table
            getTableList() {
                this.tbLoading = true;
                const searchFormVal = this.$refs["searchFormWrap"];
                const searchFormData = searchFormVal ? this.$refs["searchFormWrap"].getForm() : {};
                const sendFormData = {...searchFormData, ...this.searchForm};

                this.$http
                    .getUcenterPersonList(sendFormData)
                    .then((res) => {
                        if (res.code == 0) {
                            this.tableData = res.data.list;
                            this.tableData?.forEach((item) => {
                                if (item.roleList != null && item.roleList.length) {
                                    let roleIds = "",
                                        roleArr = [];
                                    item.roleList?.forEach((roleitem) => {
                                        roleArr.push(roleitem.name);
                                    });
                                    roleIds = roleArr.join(",");
                                }
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
                this.$getPageList("ucenter_person_list", this.titList).then((data) => {
                    this.tableTit = data;
                });
                this.$getPageFullList("ucenter_person_list").then((data) => {
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
            //多选复选框
            handleClickSelection(item) {
                this.selectionList = item;
            },
            //operation
            //添加
            handleAddClick() {
                let params = {
                    noCache: true,
                };
                this.$router.push({
                    name: "personAdd",
                    params: {type: 'add', noCache: true},
                });
            },
            //查看函数
            handleViewClickFunc({id}) {
                if (!this.$filterBtnShow(["ucenter_person_view"])) {
                    this.$showWarning("对不起，您没有查看的权限");
                    return false;
                }
                this.$router.push({
                    name: "personView",
                    params: {noCache: true, id},
                });
                return true;
            },
            //编辑函数
            handleEditClickFunc({id}) {
                if (!this.$filterBtnShow(["ucenter_person_edit"])) {
                    this.$showWarning("对不起，您没有修改的权限");
                    return false;
                }
                this.$router.push({
                    name: "personEdit",
                    params: {type: 'edit', noCache: true, id},
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
                this.$confirm("此操作会删除该人员, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                })
                    .then(() => {
                        let idQueryIn = "",
                            idQueryInArr = [];
                        this.selectionList?.forEach((item) => {
                            idQueryInArr.push(item.id);
                        });
                        idQueryIn = idQueryInArr.join(",");
                        this.$http.getUcenterPersonDelete({idQueryIn}).then((res) => {
                            if (res.code == 0) {
                                this.$showSuccess("删除成功！");
                                this.getTableList();
                            }
                        });
                    })
                    .catch(() => {
                    });
            },
            //锁定
            handleLockClick() {
                if (this.selectionList.length == 0) {
                    this.$showWarning("请选择数据");
                    return;
                }
                let accounts = "",
                    accountsArr = [];
                let isError = false;
                this.selectionList?.forEach((item) => {
                    if (!item.account || item.account == "") {
                        this.$showWarning("请选择已分配账号的数据");
                        isError = true;
                        return;
                    }
                    if (item.accountStatus == "2") {
                        this.$showWarning("请选择账号未锁定的数据");
                        isError = true;
                        return;
                    }
                    if (item.accountStatus == "3") {
                        this.$showWarning("请选择账号未注销的数据");
                        isError = true;
                        return;
                    }
                    accountsArr.push(item.account);
                });
                if (!isError) {
                    accounts = accountsArr.join(",");
                    this.$confirm("账号将要被锁定, 请确认是否继续？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    }).then(() => {
                        this.$http.getUcenterPersonLock({accounts}).then((res) => {
                            if (res.code == 0) {
                                this.$showSuccess("锁定成功！");
                                this.getTableList();
                            }
                        });
                    }).catch(() => {});
                }
            },
            //解锁
            handleUnLockClick() {
                if (this.selectionList.length == 0) {
                    this.$showWarning("请选择数据");
                    return;
                }
                let accounts = "",
                    accountsArr = [];
                let isError = false;
                this.selectionList?.forEach((item) => {
                    if (!item.account || item.account == "") {
                        this.$showWarning("请选择已分配账号的数据");
                        isError = true;
                        return;
                    }
                    if (item.accountStatus == "1") {
                        this.$showWarning("请选择已锁定账号的数据");
                        isError = true;
                        return;
                    }
                    if (item.accountStatus == "3") {
                        this.$showWarning("请选择未注销账号的数据");
                        isError = true;
                        return;
                    }
                    accountsArr.push(item.account);
                });
                if (!isError) {
                    accounts = accountsArr.join(",");
                    this.$http
                        .getUcenterPersonUnlock({accounts})
                        .then((res) => {
                            if (res.code == 0) {
                                this.$showSuccess("解锁成功！");
                                this.getTableList();
                            }
                        })
                        .catch(() => {
                        });
                }
            },
            //注销
            handleDestroyClick() {
                if (this.selectionList.length == 0) {
                    this.$showWarning("请选择数据");
                    return;
                }
                let accounts = "",
                    accountsArr = [];
                let isError = false;
                this.selectionList?.forEach((item) => {
                    if (!item.account) {
                        this.$showWarning("请选择已分配账号的数据");
                        isError = true;
                        return;
                    }
                    if (item.accountStatus == "3") {
                        this.$showWarning("请选择未注销账号的数据");
                        isError = true;
                        return;
                    }
                    accountsArr.push(item.account);
                });
                if (!isError) {
                    accounts = accountsArr.join(",");
                    this.$confirm("账号将要被注销, 请确认是否继续？", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    })
                        .then(() => {
                            this.$http.getUcenterPersonDestroy({accounts}).then((res) => {
                                if (res.code == 0) {
                                    this.$showSuccess("注销成功！");
                                    this.getTableList();
                                }
                            });
                        })
                        .catch(() => {
                        });
                }
            },
            //重置密码
            handleResetPwdClick() {
                if (this.selectionList.length == 0) {
                    this.$showWarning("请选择一条数据");
                    return;
                }
                if (this.selectionList.length > 1) {
                    this.$showWarning("只能选择一条数据");
                    return;
                }
                let {account, accountStatus} = this.selectionList[0];
                if (!account) {
                    this.$showError("当前记录未分配账号，请刷新后重试");
                    return;
                }
                if (accountStatus != "1") {
                    this.$showError("当前记录账号状态异常，请刷新后重试");
                    return;
                }
                this.$confirm("账号" + account + "的密码将要被重置，请确认是否继续？", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                }).then(() => {
                    this.$http
                        .getUcenterPersonResetPwd({account})
                        .then((res) => {
                            if (res.code == 0) {
                                this.$showSuccess("密码重置成功！");
                                this.getTableList();
                            }
                        })
                        .catch(() => {
                        });
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
                this.changeCurrentPage({currentPage:1});
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
            },
        },
    };
</script>
