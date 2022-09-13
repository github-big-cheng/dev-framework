<template>
    <div class="connect-wrapper">
        <div class="infoconfig-tree">
            <!-- 人员搜索 -->
            <div class="inner-searchbox">
                <el-input
                        v-model.trim="organizationValue"
                        clearable
                        ref="input"
                        class="search-ipt"
                        placeholder="请输入关键字搜索"
                        @input="filterData"
                >
                    <el-button
                            class="search-btn"
                            slot="append"
                            icon="el-icon-alisearch"
                    ></el-button>
                </el-input>
            </div>
            <!-- 搜索结果数据展示 -->
            <div class="s-list" v-show="showSearchZz">
                <div v-for="(item, index) of searchOrganizationList"
                        :key="item.id"
                        :class="item.isSelected ? 's-item is-selected ' : 's-item'"
                        @click.stop="handleSelectedZz(item, index)"
                >
                    <div class="i-name">
                        {{ item[key] }}
                    </div>
                </div>
            </div>
            <fold-tree
                    v-show="!showSearchZz"
                    showCheckbox
                    ref="zzTree"
                    :label="isGetUcenterOrgTree ? 'cname' : 'name'"
                    :labelTwo="isGetUcenterOrgTree ? 'name' : ''"
                    :strictly="true"
                    :treeList="treeListData"
                    :dfCheckedKeys="dfCheckedKeysZz"
                    @dbSelected="handlerDbSelect"
                    @getChecked="handleClickNode"
            ></fold-tree>
        </div>
        <div class="connect-button">
            <el-button class="add-btn" :class="deptChecked ? 'active' : ''" @click="addData">添加<i
                    class="el-icon-aliarrright2"></i></el-button>
        </div>
        <div class="infoconfig-grid">
            <table-com
                    :tableTit="tableTit"
                    :tableData="tableData"
                    :tbLoading="tbLoading"
                    border
                    :showSet="false"
                    :iSelection="false"
                    :height="500"
            >
                <template slot-scope="{ scope }" slot="orgNameBox">
                    <span>{{ scope.row[isGetUcenterOrgTree ? "cname" : "name"] }}</span>
                </template>
                <template slot-scope="{ scope }" slot="mainOrgBox">
                  <span>
                    <el-radio @change="changeRadio(scope.$index, scope.row)"
                            label="1"
                            v-model="tableDataFrom[scope.$index].isMain"
                        >
                      {{ null }}
                    </el-radio>
                  </span>
                </template>
                <template slot-scope="{ scope }" slot="cadreBox">
                  <span>
                    <el-checkbox
                            v-model="tableDataFrom[scope.$index].isMainPerson"
                    ></el-checkbox>
                  </span>
                </template>
                <template slot-scope="{ scope }" slot="posBox">
                    <el-select v-model="tableDataFrom[scope.$index].posId" filterable clearable placeholder="" >
                        <el-option
                                v-for="item in posList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </template>
                <template slot-scope="{ scope }" slot="posLevBox">
                    <el-select v-model="tableDataFrom[scope.$index].posLev" filterable clearable placeholder="" >
                        <el-option
                                v-for="item in posLevList"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </template>
                <template slot-scope="{ scope }" slot="operateBox">
                    <i class="el-icon-alitrash"
                        title="删除"
                        @click="handleDelete(scope)"
                    ></i>
                </template>
            </table-com>
        </div>
    </div>
</template>

<script>
    import foldTree from "@/components/fold-tree";
    import tableCom from "@/components/table";
    import lodash from "lodash";

    export default {
        name: 'infoConfigCom',
        components: {
            foldTree,
            tableCom,
        },
        props: {
            tableTit: {
                type: Array,
                default: () => [],
            },
            baseFrom: {
                type: Object,
                default: () => {
                },
            },
            url: {
                type: String,
                default: "getUcenterOrgTree",
            },
            defaultFrom: {
                type: Array,
                default: () => [],
            },
            defaultOrgIds: {
                type: Array,
                default: () => [],
            },
        },
        data() {
            return {
                tbLoading: false,
                tableData: [],
                checkedAll: false,
                organizationValue: "",
                showSearchZz: false,
                treeListData: [],
                allOrganizationList: [],
                searchOrganizationList: [],
                listMap: new Map(),
                checkedListMap: new Map(),
                tableDataFrom: [],
                dfCheckedKeysZz: [],
                activeIndex: null,
                baseFromInfo: lodash.cloneDeep(this.baseFrom),
                deptChecked: false,

                posList: [],
                posLevList: []
            };
        },
        mounted() {
            if (this.isGetUcenterOrgTree) {
                this.getDpetTree();
                this.getPosList();
                this.getPosLevList();
            } else {
                this.getRoleTree();
            }
        },
        watch: {
            organizationValue(val) {
                if (!val) {
                    this.showSearchZz = false;
                }
            },
            defaultFrom: {
                handler(val) {
                    val.length && this.deptTreeInit()
                },
                deep: true,
            },
            defaultOrgIds: {
                handler(val) {
                    val.length && this.roleTreeInit()
                },
                deep: true,
            },
            tableDataFrom: {
                handler(val) {
                    if (this.isGetUcenterOrgTree && val.length === 1) {
                        val[0].isMain = "1";
                    }
                },
                deep: true
            }
        },
        computed: {
            isGetUcenterOrgTree() {
                return this.url == "getUcenterOrgTree";
            },
            key() {
                return this.isGetUcenterOrgTree ? "cname" : "name";
            },
        },
        methods: {
            deptTreeInit() {
                this.defaultFrom.forEach(item => {
                    let data = this.listMap.get(item.deptId);
                    if (data) {
                        this.checkedListMap.set(item.deptId, data)
                        this.dfCheckedKeysZz.push(item.deptId)
                        this.tableDataFrom.push({
                            deptId: item.deptId,
                            isMain: item.isMain == 1 ? '1' : '0',
                            isMainPerson: item.isMainPerson == 1 ? true : false
                        });
                        this.tableData.push(data)
                    }
                })
            },
            roleTreeInit() {
                this.defaultOrgIds.forEach(item => {
                    let data = this.listMap.get(item);
                    if (data) {
                        this.checkedListMap.set(item, data)
                        this.dfCheckedKeysZz.push(item)
                        this.baseFromInfo.deptId = item;
                        this.tableData.push(data)
                    }
                })
            },
            changeRadio(index) {
                this.tableDataFrom.forEach((item, i) => {
                    item.isMain = i === index ? "1" : "0";
                });
                this.$forceUpdate();
            },
            filterData() {
                this.checkedAll = false;
                this.searchOrganizationList = this.allOrganizationList.filter((item) => {
                    return (
                        (item && item[this.key].indexOf(this.organizationValue) != -1) ||
                        (item[this.key] &&
                            item[this.key].indexOf(this.organizationValue) != -1)
                    );
                });
                this.showSearchZz = true;
            },
            handleSelectedZz(item, index) {
                item.isSelected = !item.isSelected;
            },
            // foldtree
            async getDpetTree() {
                let res = await this.$http.getUcenterOrgTree();
                if (res.code == 0) {
                    let organizationList = this.$formatTree(
                        res.data,
                        "listPerson",
                        true,
                        "tree-file",
                        "el-icon-alihead",
                        "",
                        false,
                        true
                    );

                    organizationList.forEach((item) => {
                        item.icon = "tree-filebox";
                    });
                    this.treeListData = organizationList;

                    this.allOrganizationList = this.$filterOrgAll(organizationList);

                    this.allOrganizationList.forEach((item, i) => {
                        this.$set(this.allOrganizationList[i], "isSelected", false);
                        this.listMap.set(item.id, item);
                    });
                    this.deptTreeInit()
                }
            },
            async getRoleTree() {
                try {
                    let res;
                    res = await this.$http.getUcenterRoleCombox({type : "person"});
                    this.treeListData = res.data.list;
                    this.allOrganizationList = res.data.list;
                    this.treeListData.forEach((item, i) => {
                        item.icon = "tree-file";
                        this.$set(this.treeListData[i], "isSelected", false);
                        this.listMap.set(item.id, item);
                    });
                    this.roleTreeInit()
                } catch (e) {
                    console.log("getMenuList:e", e);
                }
            },

            async getPosList() {
                let {code, data} = await this.$http.getPositionCombox({pageSize: 99999});
                if (code == 0) {
                    this.posList = data.list;
                };
            },
            async getPosLevList() {
                let {code, data} = await this.$http.getUcenterCodeCombox({type: '10001-10080'})
                if (code == 0) {
                    this.posLevList = data.list;
                };
            },

            handlerDbSelect(data) {
                if (this.tableData.indexOf(data) < 0) {
                    this.tableData.push(data);
                }
                if (this.tableDataFrom.indexOf(data) < 0) {
                    data.deptId = data.id;
                    this.tableDataFrom.push(data);
                }
                if (this.dfCheckedKeysZz.indexOf(data) < 0) {
                    this.dfCheckedKeysZz.push(data.id)
                }
            },

            handleClickNode({menuIds}, data, status) {
                let item = this.listMap.get(data.id);
                item && status
                    ? this.checkedListMap.set(item.id, item)
                    : this.checkedListMap.delete(item.id);
                item.isSelected = !item.isSelected;
                this.deptChecked = item.isSelected;
            },
            addData() {
                // 优先搜索框
                if (this.showSearchZz) {
                    let data = this.allOrganizationList.filter(
                        (item) => item.isSelected == true
                    );

                    data.forEach((item) => {
                        this.checkedListMap.set(item.id, item);
                    });
                }
                this.tableData = Array.from(this.checkedListMap.values());
                this.tableDataFrom = [];
                this.dfCheckedKeysZz = [];
                this.tableData.forEach((item) => {
                    this.baseFromInfo.deptId = item.id;
                    this.tableDataFrom.push(lodash.cloneDeep(this.baseFromInfo));
                    this.dfCheckedKeysZz.push(item.id);
                });
            },
            getForm() {
                let tableDataFrom = lodash.cloneDeep(this.tableDataFrom)
                tableDataFrom.forEach(item => {
                    item.isMainPerson = item.isMainPerson ? '1' : '0'
                })
                return {ucenterPersonOrgs: tableDataFrom, roleIds: this.dfCheckedKeysZz.join()};
            },
            handleDelete(scope) {
                let item = this.checkedListMap.get(scope.row.id);
                item.isSelected = false;
                this.tableData.splice(scope.$index, 1);
                this.tableDataFrom.splice(scope.$index, 1);
                this.checkedListMap.delete(scope.row.id);

                this.dfCheckedKeysZz = this.dfCheckedKeysZz.filter((item) => {
                    return item != scope.row.id;
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/styles/info-config.scss";
</style>
