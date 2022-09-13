<template>
    <table-group
            ref="tableGroup"
            :search-list="searchList"
            :btn-configs="btnConfigs"
            :table-title="tableTitle"
            :table-url="tableUrl"
            :has-look="hasLook"
            :table-title-code="tableTitleCode"
            @lookClick="lookClick"
            @dbTableClick="dbTableClick"
            @handlerType="operationHandler"
            @clickSelection="clickSelection"
    >
    </table-group>
</template>

<script>
    import TableGroup from "@/components/table-group/index.vue";
    import {btnConfigs, searchList, tableTitle} from "./config/index";

    export default {
        name: "flowDefineList",
        components: {
            TableGroup,
        },
        data() {
            return {
                searchList,
                btnConfigs,
                tableTitle,
                clickSelectionList: [],
                tableUrl: "flowDefineList",
                tableTitleCode: "flow_define_list",
                hasLook: false,
            };
        },
        created() {
            this.hasLook = this.$filterBtnShow(["oa_flow_define_edit", "oa_flow_define_view"], true);
        },
        methods: {
            operationHandler(type, item) {
                this?.[type]?.(item);
            },
            dbTableClick(data) {
                this.hasLook[0] ? this.save(data, true) : this.lookClick(data);
            },

            clickSelection(data) {
                this.clickSelectionList = data;
            },

            add() {
                this.$router.push({
                    name: "flowDefineAdd",
                    params: {type: "add"},
                });
            },

            save(data, db) {
                const {id, status} = db ? data : this.clickSelectionList[0];
                this.$router.push({
                    name: "flowDefineEdit",
                    params: {type: "save", id},
                });
            },

            delete(item) {
                this.deletePublishCancel(item);
            },

            lookClick({id}) {
                this.$router.push({
                    name: "flowDefineView",
                    params: {id},
                });
            },

            async deletePublishCancel(item) {
                const {text} = item;
                const len = this.clickSelectionList.length;
                await this.$confirm(`您确定要${text}这${len}条数据吗？`, "提示", {type: "warning"}).then(()=>{
                    this.apiFun(item);
                }).catch(()=>{});
            },
            async apiFun(item){
                const {text, url} = item;
                item.loading = true;
                try {
                    const {code} = await this.$http[url]({
                        idQueryIn: this.clickSelectionList.map((i) => i.id).join(","),
                    });
                    if (code === 0) {
                        this.$showSuccess(text + "成功");
                        this.$refs.tableGroup.requestTableData();
                    }
                } catch (e) {console.log('apiFun:e', e)}
                item.loading = false;
            }
        },
    };
</script>
