<template>
    <div class="main-content-wrap inner-maincon view-wrap" ref="look_view">
        <div class="form-box">
            <form-com
                ref="ruleFormBox"
                :config="baseFormConfigs"
                :isformBtn="true"
                :formBtn="formBtns"
                columnNum="row-col1"
                @submit="submit"
            >
                <template #orderNo>
                    <el-input @input="handlerOrderNo"  ref="orderNo" type="text" v-model="orderNo"></el-input>
                        <a @click="showTableList('dept')" href="javascript:void(0)">
                        <i class="iconfont el-icon-ordernum" title="详情">&#xe619;</i></a>
                </template>
            </form-com>
        </div>
        <Dialog
                iconfont="el-icon-alipeople-tit"
                width="40%"
                title="详情"
                :dialogVisible="deptDialogVisible"
                :showTrueBtn=false
                @cancelClick="deptDialogVisible = false">
            <Table
                    :tableTit="tableTit"
                    :isOperation="false"
                    :iSelection="false"
                    :tableData="tableData"
                    :tbLoading="tbLoading"
                    :height="height"
                    :pageNo="pageNo"
                    :showSet="false"
                    :pageSize="pageSize"
            >
            </Table>
            <div ref="footer">
                <Pagination
                    :total="total"
                    :defaultPage="this.pageNo"
                    :pageSize="pageSize"
                    @changePageSize="changePageSize"
                    @changeCurrentPage="changeCurrentPage"
                    v-show="tableData.length > 0 && !tbLoading"/>
            </div>
        </Dialog>
    </div>
</template>

<script>
    import selectComponent from "@/components/select-component";
    import Dialog from "@/components/dialog";
    import Table from "@/components/table";
    import Pagination from "@/components/pagination";
    import formCom from '@/components/form-com'

    export default {
        name: "deptAdjustmentEdit",
        components: {
            selectComponent,
            Dialog,
            Table,
            Pagination,
            formCom
        },
        data() {
            return {
                personId: "",
                deptDialogVisible: false,
                tableTit: [
                    {
                        colKey: "personName",
                        prop: "personName",
                        label: "人员",
                    },
                    {
                        colKey: "orderNo",
                        prop: "orderNo",
                        label: "顺序号",
                    },
                ],
                tableData: [],
                height: 500,
                total: null,
                pageNo: 1,
                pageSize: 10,
                tbLoading: false,
                deptTableData: [],
                partyTableData: [],
                isTestingInput: false,
                getTableType: '',
                formConfigs: [
                    {
                        type: 'input',
                        disabled: true,
                        prop: 'personName',
                        value: '',
                        label: '人员名称',
                    },
                    {
                        type: "selectCom",
                        label: "部门名称",
                        prop: "deptId",
                        prop2: "deptName",
                        value: "",
                        names: "",
                        title: "部门名称",
                        tabList: ["dept"],
                        rules: {
                            require: true,
                        },
                    },
                    {
                        type: 'slot',
                        label: '排序',
                        prop: "orderNo",
                        value: '',
                        rules: {
                            require: true
                        },
                        slotName: 'orderNo',
                        class: 'input-ordernum'
                    },
                ],
                formBtns: [
                    {
                        btnText: "取消",
                        handlerType: "cancelClick",
                    },
                    {
                        type: "primary",
                        btnLoading: false,
                        btnText: "保存",
                        handlerType: "submitForm",
                    },
                ],
                baseFormConfigs: [],
                orderNo: '',
                deptId: ''
            };
        },
        created() {
            let {params} = this.$route;
            this.personId = params.id;
            if (this.personId != null && this.personId != "") {
                this.getPersonData();
            }
        },
        methods: {
            submit({ handlerType }) {
                this[handlerType]();
            },
            handlerOrderNo(val) {
                this.$refs.ruleFormBox.ruleForm.orderNo = val
            },
            getPersonData() {
                this.$http.getUcenterDeptpersonView({personId: this.personId}).then((res) => {
                    if (res.code == 0) {
                         let {data} = res;
                         this.orderNo = data.orderNo;
                       this.formConfigs.forEach(item => {
                            if (item.type == "selectCom") {
                                item.value = data[item.prop];
                                item.names = data[item.prop2];
                            }else {
                                item.value = data[item.prop];
                            }
                            this.baseFormConfigs.push(item)
                        })
                    }
                })
            },
            showTableList(type) {
                this.pageNo = 1;
                this.pageSize = 10;
                this.deptId = this.$refs.ruleFormBox.ruleForm.deptId;
                if (this.deptId == '' || this.deptId == null) {
                    this.$showWarning("请选择部门名称！");
                    return;
                }
                
                this.getTableType = type;
                this.tableData =[];
                this.deptDialogVisible = true;
                this.getTableList(type);
            },
            getTableList(type) {
                let searchForm = {};
                
                searchForm.deptId = this.deptId
                searchForm.pageNo = this.pageNo;
                searchForm.pageSize = this.pageSize;
                this.tbLoading = true;
                this.$http.getDeptAdjustmentList(searchForm).then((res) => {
                    if (res.code == 0) {
                        if (type == "dept") {
                            this.deptTableData = [];
                            let deptOrderNo = 0;
                            res.data.list.forEach((item) => {
                                this.deptTableData.push({
                                    personId: item.personId,
                                    personName: item.personName,
                                    orderNo: item.orderNo,
                                });
                            });
                            this.tableData = this.deptTableData;
                            this.total = res.data.total;
                        } else {
                            this.partyTableData = [];
                            let partyOrderNo = 0;
                            res.data.list.forEach((item) => {
                                this.partyTableData.push({
                                    personId: item.personId,
                                    personName: item.personName,
                                    orderNo: item.orderNo,
                                });
                            });
                            this.tableData = this.partyTableData;
                            this.total = res.data.total;
                        }

                    }
                    this.tbLoading = false;
                });
            },
            changePageSize({pageSize}) {
                this.pageNo = 1;
                this.pageSize = pageSize;
                this.getTableList(this.getTableType);
            },
            changeCurrentPage({currentPage}) {
                this.pageNo = currentPage;
                this.getTableList(this.getTableType);
            },
            async submitForm() {
                const {status, data} = await this.$refs.ruleFormBox.getFormAndValidate();
                
                if (!status) {
                    this.$refs[data[0].field].focus();
                    return;
                }

                this.formBtns[1].btnLoading = true;
                this.$http.getUcenterDeptpersonEdit({...data, personId: this.$route.params.id})
                    .then((res) => {
                        if (res.code == 0) {
                            this.$showSuccess(res.message);
                            this.goBack(this.$route, true);
                        }
                        this.formBtns[1].btnLoading = false;
                    })
                    .catch((err) => {
                        this.formBtns[1].btnLoading= false;
                    });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .input-ordernum {
        a {
            position: absolute;
            right: 0;
            top: 0;
            padding-right: 10px;
            height: 30px;
            line-height: 30px;
            color: #ccc;    
            display: flex;
            align-items: center;
            i {
                font-size: 16px;
            }
        }
    }
    /deep/.el-form>.el-form-item:nth-child(3) .lineBlock {
        position: relative;
    }
</style>
