<!--
 * @Author: your name
 * @Date: 2021-08-12 09:59:08
 * @LastEditTime: 2021-08-12 18:17:32
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\views\systemConfigure\flowManager\flowModel\index.vue
-->
<template>
    <div>
        <TableGroup
            ref="tableGroup"
            :search-list="searchList"
            :btn-configs="btnConfigs"
            :table-title="tableTitle"
            :table-url="tableRequestApi"
            :table-title-code="tableTitleCode"
            @handlerType="operationHandler"
            @clickSelection="clickSelection"
        />
    </div>
    
</template>

<script>
import TableGroup from "@/components/table-group";
import { tableTitle, searchList, btnConfigs } from './config'
export default {
    name: "flowDefineHistory",
    components: {
        TableGroup,
    },
    data() {
        return {
            tableRequestApi: 'getProcessDefineList',
            tableTitleCode: 'flow_define_history',
            searchList,
            btnConfigs,
            tableTitle,
            clickSelectionList: [],
        };
    },
    methods: {
        clickSelection(data) {
            this.clickSelectionList = data;
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
            this[type](item);
        },
        delFlowModel() {
            if (!this.checkSelect()) return;

            this.$confirm("此操作会删除该数据, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
            .then(() => {
                this.$http.delProcessDefine({processDefineId: this.clickSelectionList[0].processDefineId}).then((res) => {
                    if (res.code == 0) {
                        this.$showSuccess("删除成功！");
                        this.$refs.tableGroup.reloadTableList();
                    }else {
                        this.$showError("删除失败！");
                    }
                });
            })
            .catch(() => {
            });
        },
    }
};
</script>
