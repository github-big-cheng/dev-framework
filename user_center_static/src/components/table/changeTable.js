/*
 * @Author: your name
 * @Date: 2021-07-30 17:32:39
 * @LastEditTime: 2021-08-20 17:53:23
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\components\table\changeTableConfig.js
 */
export default {
    props: {
        hasChangeTable: {
            type: Boolean,
            default: false
        },
        listCode: {
            type: String,
            default: ''
        }
    },
    data () {
        return {
            changeTable: null,
            showChangeTable: false,
            seTableList: [],
            clickMap: new Map()
        }
    },
    watch: {
        hasChangeTable: {
            handler(val) {
                if (!val) return;
                this.getPageFullList()
            },
            immediate: true
        }
    },
    created () {
        this.changeTable = () => import("@/components/change-table")
    },
    methods: {
        getPageFullList() {
            this.$getPageFullList(this.listCode).then((data) => {
                this.seTableList = data;
            });
        },
        // 重置表头弹窗-确定
        handleTrueClick(list) {
            this.$setTrueTable(list).then((res) => {
                if (res.code == 0) {
                    this.$showSuccess(res.message);
                    this.showChangeTable = false;
                    this.getPageFullList()
                    this.$emit('updateTable')
                }else {
                    this.$showWarning(res.message);
                }
            });
        },
        // 恢复默认设置
        handleDefaultClick(res) {
            if (res.code == 0) {
                this.$showSuccess(res.message);
                this.showChangeTable = false;
                this.emit('updateTable')
            }else {
                this.$showWarning(res.message);
            }
        },
        async handleSetClick() {
            if (this.hasChangeTable) {
                this.showChangeTable = true;
            }else {
                this.$emit("setTableClick");
            }
            
        },

    }
}