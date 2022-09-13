/*
 * @Author: your name
 * @Date: 2021-08-13 16:50:26
 * @LastEditTime: 2021-08-17 19:45:52
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \nwxt_oa\src\components\popover-search-form\config\table.js
 */
export default {
    props: {
        tableTitle: {
            type: Array,
        },
        tableUrl: {
            type: String,
        },
        tableTitleCode: {
            type: String,
            require: true,
        },
        /**
         * 处理tabledata数据
         * (tableData)=>返回处理的数据
         */
        tableFilterData: {
            type: Function,
        },
        hasLook: {
            type: Array,
        },
        rightInfo: {
            type: Boolean,
            default: false,
        },
        headTitle: {
            type: String,
            default: () => "",
        },
        requestParams: {
            type: String | Object,
            default: () => "",
        },
        params: {
            type: Object,
            default: () => {}
        }
    },
    data () {
        return {
            tableLoading: false,
            tableData: [],
            tableTit: [],
            pageInfo: {
                pageNo: 1,
                pageSize: 10,
            },
            
        }
    },
    created () {
        this.tableTit = this.tableTitle;
        this.getConfigList();
    },
    
    methods: {
        async requestTableData() {
            const { pageInfo, searchFormData, tableUrl, tableFilterData, requestParams } = this;
            this.tableLoading = true;
            try {
                const res = await this.$http[tableUrl](
                    {
                        ...pageInfo,
                        ...searchFormData,
                        ...this.params
                    },
                    requestParams
                );
                if (res.code == 0) {
                    const {
                        data: { list, total },
                    } = res;
                    this.tableData = (tableFilterData && tableFilterData(list)) || list;

                    this.pageInfo.total = total;
                }
            } catch (error) {
                console.error(error);
            }

            this.$forceUpdate;
            this.tableLoading = false;
        },

        getConfigList() {
            const { tableTitle, tableTitleCode } = this;
            this.$getPageList(tableTitleCode, tableTitle).then((data) => {
                this.tableTit = data;
            });

           
        },

        //排序
        handleSetSort({ tableTit, orderBy }) {
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
        changePageSize({ pageSize }) {
            this.pageInfo.pageSize = pageSize;
            this.requestTableData();
        },
        changeCurrentPage({ currentPage }) {
            this.pageInfo.pageNo = currentPage;
            this.requestTableData();
        },
        updateTable(res) {
            this.$showSuccess(res.message);
            this.getConfigList();
        },
        reloadTableList() {
            this.changeCurrentPage({ currentPage: 1 });
        },
    }
}