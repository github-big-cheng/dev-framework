/*
 * @Author: your name
 * @Date: 2021-08-12 13:38:54
 * @LastEditTime: 2021-08-13 13:21:45
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\views\systemConfigure\flowManager\flowModel\config\operation.js
 */
import {requestUrl} from '@/api/api'

export default {
    data() {
        return {
            btnConfigs: [
                {
                    type: 'add',
                    text: '新增',
                    icon: 'el-icon-aliadd',
                    has: "flow_model_add",
                    handlerType: 'addFlowModel',
                },
                {
                    type: 'edit',
                    text: '编辑',
                    icon: 'el-icon-alimodify',
                    has: 'flow_model_save',
                    handlerType: 'editFlowModel',
                },
                {
                    text: '删除',
                    icon: 'el-icon-aliback',
                    has: 'flow_model_delete',
                    handlerType: 'delFlowModel',
                },
                {
                    text: '在线设计',
                    icon: 'el-icon-aliadd',
                    has: "flow_model_edit",
                    handlerType: 'goModeler',
                },
                {
                    text: '部署',
                    icon: 'el-icon-alisubmit',
                    has: "flow_model_deploy",
                    handlerType: 'deployFlowModel',
                },
                {
                    text: '刷新',
                    icon: 'el-icon-alirefresh',
                    handlerType: 'refresh'
                }
            ]
        }
    },
    methods: {
        addFlowModel() {
            this.showComponent = true;
        },
        editFlowModel() {
            if (!this.checkSelect()) return;

            this.editForm = [];
            let dialogForm = JSON.parse(JSON.stringify(this.dialogForm))
            dialogForm.forEach(item => {
                if (item.prop == 'description') {
                    item.value = this.clickSelectionList[0].metaInfo.description;
                } else {
                    item.value = this.clickSelectionList[0][item.prop]
                }
                this.editForm.push(item)
            })
            this.showComponent = true;
        },
        delFlowModel() {
            if (!this.checkSelect()) return;

            this.$confirm("此操作会删除该数据, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.$http.delFlowModel({modelId: this.clickSelectionList[0].id}).then((res) => {
                        if (res.code == 0) {
                            this.$showSuccess("删除成功！");
                            this.$refs.tableGroup.reloadTableList();
                        } else {
                            this.$showError("删除失败！");
                        }
                    });
                })
                .catch(() => {
                });

        },
        goModeler() {
            if (!this.checkSelect()) return;
            const url = requestUrl + '/activiti/modeler.html?modelId=' + this.clickSelectionList[0].id;
            window.open(url);
        },
        deployFlowModel() {
            if (!this.checkSelect()) return;

            this.$http.deployFlowModel({modelId: this.clickSelectionList[0].id}).then(res => {
                if (res.code == 0) {
                    this.$showSuccess(`部署成功！流程定义id：${res.data}`);
                    this.$refs.tableGroup.reloadTableList();
                } else {
                    this.$showError("部署失败！");
                }
            })
        },
        checkSelect() {
            if (this.clickSelectionList.length == 0) {
                this.$showWarning("请选择一条数据");
                return false;
            } else if (this.clickSelectionList.length > 1) {
                this.$showWarning("只能选择一条数据");
                return false;
            }
            return true;
        },
        operationHandler(type, item) {
            this.isClcikEdit = item.type === 'edit';
            this[type](item);
        },
    }
}
