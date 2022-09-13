<template>
    <div class="card-table-box">
        <header>
            <span>{{ title }}</span>
            <el-button v-if="!addState" icon="el-icon-plus" type="text" @click="add">新增</el-button>
        </header>
        <TableCom
            v-if="tableData.length"
            :table-tit="tableTitle | tableTitleFilter"
            :table-data="tableData"
            :showSet="false"
            :iSelection="false"
            :isOperation="!addState"
            fixed="right"
            :isNums="false"
        >
            <template slot-scope="{ row }" v-if="!addState">
                <div class="table-icon-operate">
                    <el-button type="text" icon="el-icon-edit" @click="edit(row)"></el-button>
                    <el-button type="text" icon="el-icon-delete" @click="del(row)"></el-button>
                </div>
            </template>
        </TableCom>
    </div>
</template>
<script>
import TableCom from "@/components/table/index.vue";
export default {
    name: "CardTable",
    components: { TableCom },
    props: {
        title: {
            type: String,
        },
        tableTitle: {
            type: Array,
        },
        tableData: {
            type: Array,
        },
        addState: {
            type: Boolean,
        },
    },
    filters: {
        tableTitleFilter(data) {
            return data.map((i) => ({ ...i, prop: i.prop2 ? i.prop2 : i.prop }));
        },
    },
    methods: {
        add() {
            this.$emit("add");
        },
        edit(row) {
            this.$emit("edit", row);
        },
        del(row) {
            this.$emit("del", row);
        },
    },
};
</script>

<style lang="scss" scoped>
@import "@/styles/mixin.scss";

.card-table-box {
    border: 1px solid #ccc;
    margin-top: 8px;
    header {
        height: 35px;
        color: $cBlue;
        line-height: 35px;
        font-size: 13px;
        padding: 0 8px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        > span {
            font-size: 14px;
            font-weight: 600;
        }
    }
    /deep/.el-icon-delete {
        color: $cRed;
    }

    .table-list {
        min-height: 30px;
    }
}
</style>
