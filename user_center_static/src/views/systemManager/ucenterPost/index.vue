<template>
    <div class="main-wrapper" ref="vbox">
        <div ref="dvheader">
            <!-- 职称管理 -->
            <div :class="!isCollapse ? 'search-box' : 'asearch-box'">
                <div class="asearch-form">
                    <el-form :inline="true" :model="searchForm" @submit.native.prevent>
                        <el-form-item label="" v-show="isCollapse">
                            <el-input
                                v-model="searchForm.nameOrCodeQueryLike"
                                clearable
                                :focus="true"
                                class="input-search"
                                placeholder="请输入名称或代码"
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
        </div>
        <Table
            :tableTit="tableTit"
            :isOperation="false"
            :tableData="tableData"
            :tbLoading="tbLoading"
            :height="height"
            :pageNo="searchForm.pageNo"
            :pageSize="searchForm.pageSize"
            :hasLook="$filterBtnShow(['ucenter_position_edit', 'ucenter_position_view'])"
            @clickSelection="handleClickSelection"
            @dbClick="dbEditClick"
            @lookClick="handleLookClick"
            @setTableClick="showChangeTable = true"
            @sortClick="handleSetSort"
        >
        </Table>
        <div ref="footer">
            <Pagination
                :total="total"
                :defaultPage="searchForm.pageNo"
                @changePageSize="changePageSize"
                @changeCurrentPage="changeCurrentPage"
                v-show="tableData.length > 0 && !tbLoading"
            />
        </div>
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
import Table from "@/components/table";
import Pagination from "@/components/pagination";
import changeTable from "@/components/change-table";
import searchForm from "@/components/search-form";
import operationCom from "@/components/operation";
import { searchFormConfig, btnConfigs, titList } from "./config";

export default {
    name: "ucenterPost",
    components: {
        Table,
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
                nameOrCodeQueryLike: "",
                type: "",
                pageNo: 1,
                pageSize: 10,
                orderBy: "",
            },
            searchFormConfig,
            btnConfigs,
            titList,
            tableTit: [],
            tableData: [{}],
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
        "searchForm.nameOrCodeQueryLike"(val) {
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
            this.$getPageList("ucenter_position_list", this.titList).then((data) => {
                this.tableTit = data;
            });
            this.$getPageFullList("ucenter_position_list").then((data) => {
                this.seTableList = data;
            });
        },
        getTableList() {
            this.tbLoading = true;
            const searchFormVal = this.$refs["searchFormWrap"];
            const searchFormData = searchFormVal ? this.$refs["searchFormWrap"].getForm() : {};
            const sendFormData = { ...this.searchForm, ...searchFormData };

            this.$http
                .getPositionList(sendFormData)
                .then((res) => {
                    if (res.code == 0) {
                        this.tableData = res.data.list;
                        this.total = res.data.total;
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

        handleClickSelection(val) {
            this.selectionList = val;
        },
        handleLookClick({ id }) {
            this.$router.push({
                name: "postView",
                params: { id: id },
            });
            //缓存数据和页码
            // localStorage.setItem('curPageNo', this.searchForm.pageNo)
        },
        dbEditClick({ id }) {
            if (this.$filterBtnShow(["ucenter_position_edit"])) {
                this.$router.push({
                    name: "postEdit",
                    params: { type: 'edit', noCache: true, id },
                });
            } else {
                if (this.$filterBtnShow(["ucenter_position_view"])) {
                    this.$router.push({
                        name: "postView",
                        params: { id: id },
                    });
                }
            }
        },
        handleAddClick() {
            this.$router.push({
                name: "postAdd",
                params: { type:'add', noCache: true },
            });
        },
        handleEditClick() {
            if (this.selectionList.length == 0) {
                this.$showWarning("请选择一条数据");
                return;
            }
            if (this.selectionList.length > 1) {
                this.$showWarning("只能选择一条数据");
                return;
            }

            this.$router.push({
                name: "postEdit",
                params: { noCache: true, type:'edit', id: this.selectionList[0].id },
            });
        },
        handleDeleteClick() {
            if (this.selectionList.length == 0) {
                this.$showWarning("请选择要删除的数据");
                return;
            }
            this.$confirm("此操作会删除该职称, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    let ids,
                        idsArr = [];
                    this.selectionList.forEach((item) => {
                        idsArr.push(item.id);
                    });
                    ids = idsArr.join(",");
                    this.$http.getPositionDelete({ ids }).then((res) => {
                        if (res.code == 0) {
                            this.$showSuccess("删除成功！");
                            this.reloadTableList();
                        }
                    });
                })
                .catch(() => {});
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
            this.$setDefaultTable("ucenter_job_title").then((res) => {
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

<style lang="scss" scoped></style>
