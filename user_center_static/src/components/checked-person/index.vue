<!--
 * @Author: your name
 * @Date: 2021-03-16 16:54:15
 * @LastEditTime: 2021-08-23 13:59:14
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\components\checkedPerson\index.vue
-->
<template>
    <div>
        <div v-if="departPersonList.length">
            <div v-for="(item, i) in departPersonList" :key="item+i">
                <div class="allSelectBox" style="position: relative">
                    <el-checkbox
                            class="allselect"
                            v-model="checkedAllMap[item.id]"
                            :indeterminate="indeterminateMap[item.id]"
                            @change="(val) => handleCheckAllChange(val, item.id)"
                    >{{ item.cname }}
                    </el-checkbox>
                    <span class="curNum">
                        {{"（"}}<strong>{{checkedPersonMap[item.id] ? checkedPersonMap[item.id].length : '0'}}</strong>{{ " / " + item.listPerson.length + "）"}}
                    </span>
                </div>
                <el-checkbox-group
                        class="selectChild"
                        v-if="item.listPerson"
                        v-model="checkedPersonMap[item.id]"
                        @change="handleCheckedCitiesChange(item.id)"
                >
                    <el-checkbox
                            v-for="person in item.listPerson"
                            :disabled="checkDefaultIds(person.id)"
                            :label="person.id"
                            :key="person.deptId + '_' + person.id"
                    >{{ person.name }}
                    </el-checkbox>
                </el-checkbox-group>
            </div>
        </div>
    </div>
</template>

<script>
    import lodash from "lodash";

    export default {
        name: 'checkedPersonCom',
        props: {
            type: {
                type: String,
                default: () => "",
            },
            isGet: {
                type: Boolean,
                default: () => false,
            },
            activeName: {
                type: String,
                default: () => "",
            },
            defaultIds: {
                type: Array,
                default: () => [],
            },
            selectTreeNodeId: {
                default: "",
            },
            showDialog: {
                default: false,
            }
        },
        data() {
            return {
                basicDataList: [], // 基础数据
                departPersonList: [], // 显示数据
                deptPersonMap: {}, // 按部门所有人员数组
                checkedAllMap: {}, // 部门全选按钮状态
                checkedPersonMap: {}, // 按部门被选中的人员数组
                indeterminateMap: {}, // 全选
            };
        },
        created() {
            this.getDeptPersonTree();
        },
        methods: {
            getDeptPersonTree() {
                this.$http.getUcenterOrgTreePerson().then((res) => {
                    const {code, data} = res;
                    if (code === 0) {
                        this.basicDataList = lodash.cloneDeep(data); // 原数据

                        const arr = [];
                        this.formatData(arr, this.filterData(data));
                        this.departPersonList = arr;
                    }
                });
            },

            formatData(arr, list) {
                if (!list || !list.length) {
                    return;
                }

                list.forEach(item => {
                    // init
                    this.$set(this.checkedAllMap, item.id, false);
                    this.$set(this.checkedPersonMap, item.id, []); // 初始化选中数据
                    this.$set(this.deptPersonMap, item.id, []); // 初始化部门下所有人员数据
                    this.$set(this.indeterminateMap, item.id, false);

                    let {id, cname, listPerson} = item;

                    // 处理人员
                    let persons = [];
                    if (listPerson && listPerson.length) {
                        listPerson.forEach(person => {
                            let {id, deptId, name} = person;
                            persons.push({id, deptId, name});
                            this.deptPersonMap[item.id].push(id);
                        });
                    }

                    // 当前部门及人员
                    arr.push({id, cname, listPerson: persons});

                    // 递归
                    this.formatData(arr, item.children);
                });
            },


            /**
             * 部门全选/全不选
             *
             * @param status
             * @param deptId
             */
            handleCheckAllChange(status, deptId) {
                let item = status ? this.deptPersonMap[deptId] : []
                this.$set(this.checkedPersonMap, deptId, item)
                this.$set(this.indeterminateMap, deptId, false);
            },
            /**
             * 明细选择变化
             *
             * @param deptId
             */
            handleCheckedCitiesChange(deptId) {
                let checkedCount = this.checkedPersonMap[deptId].length;
                let totalCount = this.deptPersonMap[deptId].length;
                this.$set(this.checkedAllMap, deptId, checkedCount == totalCount);
                this.$set(this.indeterminateMap, deptId, checkedCount > 0 && checkedCount < totalCount);
            },

            filterData(data) {
                if(!data || !data.length) {
                    return [];
                }
                if (!this.selectTreeNodeId) {
                    return data;
                }

                for (let i in data) {
                    if (data[i].id == this.selectTreeNodeId) {
                        return [data[i]];
                    }
                    let arr =this.filterData(data[i].children);
                    if (arr && arr.length) {
                        return arr;
                    }
                }

                return [];
            },
            checkDefaultIds(id) {
                const set = new Set(this.defaultIds);
                return set.has(String(id));
            },

        },
        watch: {
            isGet(val) {
                const checkedMap = lodash.cloneDeep(this.checkedPersonMap);
                let arr = [];
                for (let ck in checkedMap) {
                    let selectedArr = checkedMap[ck];
                    if (selectedArr && selectedArr) {
                        arr = arr.concat(selectedArr);
                    }
                }
                this.$emit("getIds", arr.join() + ",");
            },
            selectTreeNodeId(val) {
                if (!val) {
                    return;
                }

                const data = lodash.cloneDeep(this.basicDataList);
                const arr = [];
                this.formatData(arr, this.filterData(data));
                this.departPersonList = arr;
            }
        },
    };
</script>

<style lang="scss" scoped>
@import "@/styles/mixin.scss";
    .allselect {
        width: 100%;
        background: $cGrayf1;
        border-radius: 2px;
        padding: 6px 5px;
    }

    .selectChild {
        padding: 5px;

        .el-checkbox {
            width: 25%;
            margin: 0 0 5px 0;
        }

        /deep/ .el-checkbox__input.is-disabled .el-checkbox__inner::after {
            transform: rotate(45deg) scaleY(1);
        }
    }

    .allSelectBox {
        position: relative;

        .curNum {
            position: absolute;
            right: 5px;
            top: 50%;
            transform: translateY(-50%);
            color: cGray9;
            font-size: 14px;
            strong{
                color: $cBlue;
            }
        }

        .el-checkbox__input {
            vertical-align: -3px;
        }
    }
</style>
