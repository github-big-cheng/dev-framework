<template>
    <div>
        <TableGroup
            ref="tableGroup"
            :search-list="searchList"
            :btn-configs="btnConfigs"
            :table-title="tableTitle"
            :isTooltip="false"
            :iSelection="false"
            :table-url="tableUrl"
            :table-title-code="tableTitleCode"
        >
            <template slot-scope="{ scope }" slot="status">
                <el-tag :type="scopeRowStatusColor[scope.row.status]">
                    {{ scope.row.statusName }}
                </el-tag>
            </template>
        </TableGroup>
    </div>
</template>

<script>
import TableGroup from "@/components/table-group";
import { searchList, btnConfigs, tableTitle, scopeRowStatusColor } from "./config";
export default {
    name: "flowInstance",
    components: {
        TableGroup,
    },
    data() {
        return {
            tableUrl: "flowInstanceList",
            tableTitleCode: "flow_instance_list",
            searchList,
            btnConfigs,
            tableTitle,
            scopeRowStatusColor,
            hasLook: false,
        };
    },
};
</script>
