<template>
    <div class="main-wrapper">
        <div :class="!isCollapse ? 'search-box' : 'asearch-box'">
            <div class="asearch-form">
                <el-form :inline="true" :model="searchForm" @submit.native.prevent>
                    <el-form-item label="" v-show="isCollapse">
                        <el-input
                            v-model="searchForm.targetServerQueryLike"
                            clearable
                            :focus="true"
                            class="input-search"
                            placeholder="请输入服务实例名称"
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
            <template slot="operationSlot" slot-scope="{scope}">
                <el-button @click="showDetail(scope.row)" type="text" size="small">查看</el-button>
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

        <dialog-com
                title="操作详情"
                iconfont="el-icon-alinote-tit"
                :dialogVisible="dialogVisible"
                @trueClick="dialogHandleTrueClick"
                @cancelClick="dialogHandleTrueClick"
                :showCancelBtn="false"
            >
            <view-com :viewConfigs="detailConfigs" :showBack="false"></view-com>
        </dialog-com>
    </div>
</template>

<script>
import tableCom from "@/components/table";
import Pagination from "@/components/pagination";
import changeTable from "@/components/change-table";
import searchForm from '@/components/search-form'
import operationCom from '@/components/operation'
import DialogCom from "@/components/dialog/index";
import viewCom from '@/components/view-com';
import {jsonHighlight} from '@/filters/index';

export default {
    name: "operationLogList",
    components: {
        DialogCom,
        tableCom,
        Pagination,
        changeTable,
        searchForm,
        operationCom,
        viewCom
    },
    data() {
        return {
            height: null,
            showChangeTable: false,
            isShowAscurrVal: false,
            searchForm: {
                targetServerQueryLike: "",
                pageNo: 1,
                pageSize: 10,
                orderBy: "",
            },
            searchFormConfig:[
                {
                    type: 'input',
                    label: '实例名称',
                    prop: 'targetServerQueryLike',
                },
                {
                    type: 'customTime',
                    label: '时间',
                    attr: ['requestTimeQueryGe', 'requestTimeQueryLe']
                },
                {
                    type: 'slot',
                    label: '',
                    slotName: 'slot-botton',
                }
            ],
            btnConfigs: [
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
                    colKey: "targetServer",
                    prop: "targetServer",
                    label: "访问实例",
                    width: 200,
                },
                {
                    colKey: "requestPath",
                    prop: "requestPath",
                    label: "访问资源",
                    width: null,
                },
                {
                    colKey: "ip",
                    prop: "ip",
                    label: "IP地址",
                    width: 180,
                },
                {
                    colKey: "userName",
                    prop: "userName",
                    label: "操作人",
                    width: 100,
                },
                {
                    colKey: "opTime",
                    prop: "requestTime",
                    label: "操作时间",
                    width: 180,
                },
                {
                    colKey: "operation",
                    label: "操作",
                    align: 'center',
                    type: "isSlot",
                    slotName: "operationSlot",
                    width: 80,
                }
            ],
            tableData: [],
            seTableList: [],
            total: null,
            tbLoading: true,
            isCollapse: true,

            // 查看详情
            detailConfigs: [],
            dialogVisible: false,
            row: {},
        };
    },
    watch: {
        isCollapse() {
            this.getTbHeight();
        },
        "searchForm.targetServerQueryLike"(val) {
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
            this.$getPageList("sys_operation_log_list", this.titList).then((data) => {
                this.tableTit = data;
            });
            this.$getPageFullList("sys_operation_log_list").then((data) => {
                this.seTableList = data;
            });
        },
        getTableList() {
            this.tbLoading = true;
            const searchFormVal = this.$refs['searchFormWrap']
            const searchFormData = searchFormVal ? this.$refs['searchFormWrap'].getForm() : {};
            const sendFormData = {...this.searchForm, ...searchFormData}

            this.$http.getSysOperationLogList(sendFormData).then((res) => {
                const {code, data:{list, total}} = res;
                if (code == 0) {
                    list.forEach(item => item.id = item.value)
                    this.tableData = list;
                    this.total = total;
                    this.tbLoading = false;
                }
                this.closeLoading(this.$route);
                this.getTbHeight()
            })
            .catch(() => this.closeLoading(this.$route));
        },
        getTbHeight() {
            setTimeout(async () => {
                this.height = await this.$formatTableHeight();
            }, 0);
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
            this.$setDefaultTable("ucenter_operationin_log_list").then((res) => {
                this.updateTable(res);
            });
        },
        updateTable(res) {
            this.$showSuccess(res.message);
            this.getConfigList();
            this.showChangeTable = false;
        },

        initViewConfig() {
            this.detailConfigs = [
                {
                    label:"协议类型",
                    content: this.row.schema,
                },
                {
                    label:"请求方式",
                    content: this.row.requestMethod,
                },
                {
                    label:"请求时间",
                    content: this.row.requestTime,
                },
                {
                    label:"请求内容",
                    content: this.row.requestBody,
                },
                {
                    label:"响应时间",
                    content: this.row.responseTime,
                },
                {
                    label:"响应内容",
                    content: this.row.responseData,
                },
                {
                    label:"耗时（ms）",
                    content: this.row.executeTime,
                },
                {
                    label:"令牌",
                    content: this.row.token,
                }
            ]
        },
        showDetail(row) {
            this.row = row || {};
            this.dialogVisible = true;
            this.initViewConfig();
        },
        dialogHandleTrueClick() {
            this.row = {};
            this.dialogVisible = false;
        }
    },
};
</script>
