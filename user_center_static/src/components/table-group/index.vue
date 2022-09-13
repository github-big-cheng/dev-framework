<template>
    <div class="table-group">
        <!-- 搜索 -->
        <template v-if="!searchPopoverState && searchList && searchConfig.length">
            <div class="search-form-box" v-show="isCollapse">
                <search-form :class="searchLable !== '' ? `search-form-label${searchLable}` : ''" ref="searchForm" :form-data="searchFormData" :config="searchConfig">
                    <div class="search-btn" slot="button">
                        <el-button type="primary" @click="onSearch">查询</el-button>
                        <el-button @click="onEmpty">清空</el-button>
                        <span v-if="searchList.easy" @click="packUp" class="pack-up">
                            收起
                            <i class="el-icon-arrow-up"></i>
                        </span>
                    </div>
                </search-form>
            </div>
        </template>

        <h2 v-if="headTitle" class="head-title">
            {{ headTitle }}
        </h2>

        <!-- 功能按钮 -->
        <div class="operating-button-box">
            <OperationCom :btn-configs="btnConfigs" @handlerType="handlerType"/>
            <div class="operating-button-search" v-if="!isCollapse && searchList && searchList.easy">
                <el-input
                        clearable
                        :focus="true"
                        v-model="easySearchForm[searchList.easy.prop]"
                        class="input-search"
                        :placeholder="searchList.easy.placeholder"
                        @keyup.enter.native="reloadTableList"
                >
                    <el-button slot="append" @click="reloadTableList" icon="el-icon-alisearch"></el-button>
                </el-input>
                <a v-if="!searchPopoverState && searchConfig.length"
                   :class="formDataState ? 'ascurr-value' : ''"
                   @click="moreSwarchButton"
                >高级搜索</a>
                <template v-if="searchPopoverState && searchList && searchConfig.length">
                    <searchPopover width="auto" @resetList="onEmpty" @searchList="onSearch">
                        <search-form
                                ref="searchForm"
                                class="searchForm"
                                :form-data="searchFormData"
                                :config="searchConfig"
                        ></search-form>
                        <!-- <span v-html="searchPopoverStyle"></span> -->
                    </searchPopover>
                </template>
            </div>
            <slot v-if="rightInfo"></slot>
        </div>

        <!-- 表格 -->
        <table-com
                ref="table"
                fixedHeight
                :table-tit="tableTit"
                :table-data="tableData"
                :tb-loading="tableLoading"
                :show-set="showSet"
                :hasLook="hasLook"
                :pageNo="pageInfo.pageNo"
                :currentRowKey="currentRowKey"
                :pageSize="pageInfo.pageSize"
                :addRowClassName="addRowClassName"
                :addColumnClassName="addColClassName"
                :showSummary="showSummary"
                :tableSumFmt="tableSumFmt"
                :iSelection="iSelection"
                :isTooltip="isTooltip"
                @clickSelection="clickSelection"
                @dbClick="dbTableClick"
                @lookClick="lookClick"
                @setTableClick="setTableClick"
                @sortClick="handleSetSort"
                v-bind="$attrs['table-porps']"
        >
            <template v-for="item in scopedSlots" :slot="item" slot-scope="{ scope }">
                <slot :name="item" :scope="scope"></slot>
            </template>
        </table-com>

        <!-- 分页 shi-->
        <div ref="footer" v-if="isFooter">
            <Pagination
                    :total="pageInfo.total"
                    :defaultPage="pageInfo.pageNo"
                    @changePageSize="changePageSize"
                    @changeCurrentPage="changeCurrentPage"
                    v-show="tableData.length > 0"
            />
        </div>

        <!-- 设置表头 -->
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
    import lodash from "lodash";
    import props from "./mixins/props";

    export default {
        components: {
            TableCom: () => import(/* webpackChunkName: "components/table" */ "@/components/table/index.vue"),
            Pagination: () => import(/* webpackChunkName: "components/pagination" */ "@/components/pagination"),
            OperationCom: () => import(/* webpackChunkName: "components/operation" */ "@/components/operation"),
            ChangeTable: () => import(/* webpackChunkName: "components/change-table" */ "@/components/change-table"),
            SearchForm: () => import(/* webpackChunkName: "components/search-form" */ "@/components/search-form/index.vue"),
            SearchPopover: () => import(/* webpackChunkName: "components/search-popover" */ "@/components/search-popover"),
        },

        /* 隐藏props table-props 给table props */
        props,

        data() {
            return {
                isCollapse: false,
                tableLoading: false,
                formDataState: false,
                requestState: true,
                showChangeTable: false,
                scopedSlots: [],
                tableData: [],
                tableAllData: [],
                seTableList: [],
                easySearchForm: {}, // 简易搜索form
                searchConfig: [],
                searchFormData: {},
                clickSelectionList: [],
                tableTit: [],
                pageInfo: {
                    pageNo: 1,
                    pageSize: 10,
                },
            };
        },

        watch: {
            searchList() {
                this.searchMassge();
            },
            searchParams: {
                handler(data) {
                    if (data) {
                        this.searchFormData = data;
                    }
                },
                deep: true,
                immediate: true,
            },
        },

        created() {
            this.init();
            if (this.requestInitState) {
                this.requestTableData();
            }
        },
        methods: {
            async init() {
                await this.searchMassge();
                this.tableTit = this.tableTitle;
                // if (this.requestInitState) {
                //     this.requestTableData();
                // }
                if (this.showSet) {
                    this.getConfigList();
                }
                this.scopedSlots = Object.keys(this.$scopedSlots);
            },

            searchMassge() {
                const data = this.searchList;
                let list = Array.isArray(data) ? data : data.list;
                if (!list) {
                    return (this.searchConfig = []);
                }
                this.isCollapse = Array.isArray(data);
                const newList = lodash.cloneDeep(list);
                newList.push({
                    type: "slot",
                    label: "",
                    slotName: "button",
                });
                this.searchConfig = newList;
            },

            setTableClick() {
                this.showChangeTable = true;
            },

            handlerType(type, item) {
                if (type === "refresh") {
                    this.reloadTableList();
                    return;
                } else if (type === "edit" || type === "save") {
                    const length = this.clickSelectionList.length;
                    if (length === 0) {
                        return this.$showWarning("请选择一条数据");
                    } else if (length > 1) {
                        return this.$showWarning("只能选择一条数据");
                    }
                } else if (
                    type === "delete" ||
                    type === "publish" ||
                    type === "cancel" ||
                    type === "delay" ||
                    item.selectRequired
                ) {
                    const length = this.clickSelectionList.length;
                    if (!length) {
                        return this.$showWarning("至少选择一条数据");
                    }
                }
                this.$emit("handlerType", type, item);
            },

            onEmpty() {
                this.$refs.searchForm.clearFrom();
                this.searchFormData = {};
                this.$emit("init", true);
                if (this.requestInitState) {
                    this.reloadTableList();
                }
            },

            onSearch() {
                const formData = this.$refs.searchForm.getForm();

                this.searchFormData = Object.assign(this.searchFormData, formData);
                this.$nextTick(() => {
                    if (!this.requestInitState) {
                        this.$emit("search", this.searchFormData);
                    } else {
                        this.searchList.searchDataFilter ? this.baseRequest() : this.reloadTableList(formData);
                    }
                });
            },

            /* 点击高级搜索 */
            moreSwarchButton() {
                // this.easySearchForm[this.searchList.easy.prop] = "";
                this.isCollapse = true;
                this.$refs.table.gainHeight();
            },

            /* 点击收起 */
            packUp() {
                const formData = this.$refs.searchForm.getForm();
                const formDataState = Object.values(formData).some((i) => (Array.isArray(i) ? i.length : i));
                this.searchFormData = formData;
                this.formDataState = formDataState;
                this.isCollapse = false;
                this.$refs.table.gainHeight();
            },

            async requestTableData(sendData = {}) {
                if (!this.requestState) return;
                this.requestState = false;
                let {pageInfo, searchFormData, easySearchForm, tableUrl, tableFilterData, requestParams, requestType} = this;

                // 表单数据
                const formData = this.isCollapse ? sendData : easySearchForm;
                const {pageNo, pageSize} = pageInfo;

                this.tableLoading = true;
                const params = {
                    pageNo,
                    pageSize,
                    ...formData,
                    ...sendData,
                    ...requestParams,
                };

                try {
                    const res = await this.$http[tableUrl](params, requestType);
                    if (res.code == 0) {
                        const {data} = res;
                        const {list, total} = data;
                        this.tableAllData = data;
                        this.tableData = (tableFilterData && tableFilterData(list)) || list;
                        this.pageInfo.total = total;
                    }
                } catch (e) {
                    console.error('requestTableData:e',e);
                }

                this.$forceUpdate;
                this.requestState = true;
                this.tableLoading = false;
            },

            async baseRequest(sendData = {}) {
                if (!this.requestState) return;
                this.requestState = false;
                const formData = this.$refs.searchForm.getForm();
                const {pageInfo, tableUrl, tableFilterData, requestParams, requestType} = this;

                this.tableLoading = true;
                const params = {
                    ...pageInfo,
                    ...formData,
                    ...sendData,
                    ...requestParams,
                };
                try {
                    const res = await this.$http[tableUrl](params, requestType);
                    if (res.code == 0) {
                        const {
                            data: {list, total},
                        } = res;
                        this.tableData = (tableFilterData && tableFilterData(list)) || list;

                        this.pageInfo.total = total;
                    }
                } catch (e) {
                    console.error('baseRequest:e', e);
                }

                this.$forceUpdate;
                this.requestState = true;
                this.tableLoading = false;
            },

            getConfigList() {
                const {tableTitle, tableTitleCode} = this;
                this.$getPageList(tableTitleCode, tableTitle).then((data) => {
                    this.tableTit = data;
                });

                this.$getPageFullList(tableTitleCode).then((data) => {
                    this.seTableList = data;
                });
            },

            //排序
            handleSetSort({tableTit, orderBy}) {
                this.tableTit = tableTit;
                this.searchForm.orderBy = orderBy;
                this.reloadTableList();
            },

            /* 查看 */
            lookClick(data) {
                this.$emit("lookClick", data);
            },

            /* 表格双击 */
            dbTableClick(data) {
                this.$emit("dbTableClick", data);
            },

            /* 多选复选框 */
            clickSelection(data) {
                this.clickSelectionList = data;
                this.$emit("clickSelection", data);
            },

            //分页操作
            changePageSize({pageSize}) {
                this.pageInfo.pageSize = pageSize;
                this.requestTableData();
            },
            changeCurrentPage({currentPage}, sendData = {}) {
                this.pageInfo.pageNo = currentPage;
                this.requestTableData(sendData);
            },

            //配置表格
            handleTrueClick(list) {
                this.$setTrueTable(list).then((res) => {
                    this.updateTable(res);
                });
            },

            reloadTableList(sendData = {}) {
                if (sendData instanceof PointerEvent || sendData instanceof KeyboardEvent) {
                    // 排除按钮点击事件
                    sendData = {};
                }
                this.changeCurrentPage({currentPage: 1}, sendData);
            },

            handleDefaultClick() {
                this.$setDefaultTable(this.tableTitleCode).then((res) => {
                    this.updateTable(res);
                });
            },

            updateTable(res) {
                this.$showSuccess(res.message);
                this.getConfigList();
                this.showChangeTable = false;
            },
            tableSumFmt(param) {
                return this.tableSumFilter(param, this.tableAllData)
            }
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/styles/mixin.scss";

    .table-group {
        padding-top: 15Px;

        .input-search {
            margin-right: 5px;
        }

        .operating-button-box {
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
            padding-right: 20px;
        }

        .operating-button-search {
            > .el-input {
                min-width: 220Px;/*no*/
                width: 220px;
            }

            > a {
                margin-left: 4px;
            }

            /deep/ .el-input-group__append {
                text-align: center;
            }
        }

        .ascurr-value {
            color: $cBlue;
        }

        .head-title {
            padding-top: 10px;
            font-size: 20px;
            text-align: center;
        }
    }
</style>
