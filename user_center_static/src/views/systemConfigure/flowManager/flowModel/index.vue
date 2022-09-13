<!--
 * @Author: your name
 * @Date: 2021-08-12 09:59:08
 * @LastEditTime: 2021-08-13 13:19:35
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

        <Dialog
            v-if="showComponent"
            :dialogVisible="showComponent"
            class="min-dialog-form"
            :title="title"
            :btnLoading="DLbtnLoading"
            @cancelClick="handleCancelClick"
            @trueClick="handleTrueClick"
        >
            <form-com :config="isClcikEdit ? editForm : dialogForm" ref="dialogForm" columnNum="row-col1" />
        </Dialog>
    </div>
    
</template>

<script>
import TableGroup from "@/components/table-group";
import Dialog from "@/components/dialog";
import formCom from '@/components/form-com'
import operation from './config/operation'
import search from './config/search'
import table from './config/table'
import dialog from './config/dialog'
export default {
    name: "flowModel",
    mixins: [ operation, search, table, dialog ],
    components: {
        TableGroup,
        Dialog,
        formCom
    },
    data() {
        return {
            clickSelectionList: [],
        };
    },
    methods: {
        
    }
};
</script>
