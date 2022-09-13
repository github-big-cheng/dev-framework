/*
 * @Author: your name
 * @Date: 2021-08-12 11:24:37
 * @LastEditTime: 2021-08-13 10:58:28
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\views\systemConfigure\flowManager\flowModel\config\table.js
 */
export default {
    data () {
        return {
            tableRequestApi: 'getFlowModelList',

            tableTitleCode: 'flow_model_list',

            tableTitle: [
                {
                    colKey: "category",
                    prop: "category",
                    label: "流程分类",
                    minWidth: 45,
                },
                {
                    colKey: "key",
                    prop: "key",
                    label: "模型标识",
                    minWidth: 45
                },
                {
                    colKey: "name",
                    prop: "name",
                    label: "模型名称",/*  */
                    minWidth: 45
                },
                {
                    colKey: "revision",
                    prop: "version",
                    label: "版本号",
                    minWidth: 45
                },
                {
                    colKey: "createTime",
                    prop: "createTime",
                    label: "创建时间",
                    minWidth: 45
                },
                {
                    colKey: "lastUpdateTime",
                    prop: "lastUpdateTime",
                    label: "最后更新时间",
                    minWidth: 45
                },

            ]
        }
    },
    methods: {
        clickSelection(data) {
            this.clickSelectionList = data;
        },
    }
}