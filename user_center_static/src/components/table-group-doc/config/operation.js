/*
 * @Author: your name
 * @Date: 2021-08-13 16:50:22
 * @LastEditTime: 2021-08-16 18:19:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \nwxt_oa\src\components\popover-search-form\config\operation.js
 */
export default{
    props: {
        btnConfigs: {
            type: Array,
            default: () => [],
        },
    },
    methods: {
        handlerType(type, item) {
          if (type === "refresh") {
            this.reloadTableList();
            return;
        } else if (type === "edit") {
            const length = this.clickSelectionList.length;
            if (length === 0) {
                return this.$showWarning("请选择一条数据");
            } else if (length > 1) {
                return this.$showWarning("只能选择一条数据");
            }
        } else if (type === "delete" || type === "publish" || type === "cancel") {
            const length = this.clickSelectionList.length;
            if (!length) {
                return this.$showWarning("至少选择一条数据");
            }
        }
            this.$emit("handlerType", type, item);
          },
    }
}