<!--
 * @Author: your name
 * @Date: 2021-05-19 14:12:31
 * @LastEditTime: 2021-08-18 10:59:38
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\components\select-table\index.vue
-->
<template>
    <div class="select-table">
        <el-dropdown trigger="click" ref="carNoSelect" @visible-change="isDown=!isDown">
            <el-input
                @input="search"
                placeholder=""
                v-model="values"
                filterable
            ></el-input>
            <i :style="{transform: isDown ? 'rotate(180deg)' : 'rotate(0deg)'}" class="el-icon-arrow-down downbox"></i>
            <el-dropdown-menu
                slot="dropdown"
                :append-to-body="false"
                ref="carNoDropDown"
                class="car-table-drop"
            >
                <table-com
                    :tableTit="tableTit"
                    :isOperation="false"
                    :iSelection="false"
                    :showSet="false"
                    :isNums="false"
                    :tableData="copyData"
                    :tbLoading="tbLoading"
                    @handleRowClicked="handleRowClicked"
                >

                    <template slot-scope="{scope}" slot="dateRange">
                        {{ scope.row.startDate | formatText }} 至 {{ scope.row.endDate | formatText}}
                    </template>
                    <template slot-scope="{scope}" slot="status">
                       <el-tag :type="statusColor[scope.row.status]">
                            {{ scope.row.statusName }}
                        </el-tag>
                    </template>
                </table-com>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
import tableCom from "@/components/table";
import lodash from 'lodash';
export default {
    name: 'selectTableCom',
    components: {
        tableCom,
    },
    props: {
        tableData: {
            type: Array,
            default: () => []
        },
        value: {
            type: String,
            default: ''
        },
        slots: {
            type: Array,
            default: () => []
        },
        tableTit: {
            type: Array,
            default: () => []
        },
        tbLoading: {
            type: Boolean,
            default: false
        },
        statusColor: {
            type: Object,
            default: () => {
                return {
                    "23006-10": "cgreen", // 空闲
                    "23006-20": "corange", // 使用中
                    "23006-30": "cred", // 维修
                    "23006-40": "cbrown", // 保养
                }
            }
        },
        filterAttr: {
            type: Array,
            default: () => []
        },
    },
    data() {
        return {
            ruleForm: {
                carNo: ''
            },
            isDown: false,
            copyData: [],
            values: this.value
            
        };
    },
    watch: {
        tableData: {
            handler(val) {
                this.copyData = lodash.cloneDeep(val)
            },
            deep: true,
            immediate: true
        },
        value(newValue){
            this.values = newValue
        }
    },
    methods: {
        handleRowClicked(data) {
            this.$refs.carNoSelect.hide()
            this.$emit('handleRowClicked', data)
        },
        search() {
            let data = [];
            this.tableData.forEach(item => {
                let flag = this.filterAttr.some((filter) => item[filter]?.indexOf(this.values) > -1);
                if (flag) {
                    data.push(item);
                }
            })
            this.copyData = data;
        }
    },
};
</script>

<style lang="scss" scoped>
/deep/.el-dropdown{
    height: 30px;
}
    /deep/.el-dropdown-menu {
        padding: 0px;
        margin-top: 10px;
        // left: 0 !important;
    }
    /deep/.popper__arrow {
        display: none;
    }
    .downbox {
        position: absolute;
        right: 10px;
        top: 0;
        height: 30px;
        min-height: 30Px;/*no*/
        display: flex;
        align-items: center;
        color: #DCDFE6;
        transition: all .3s;
    }
</style>
