<template>
    <div class="tree-box">
        <el-tree
            v-if="visible"
            ref="tree"
            :data="treeList"
            node-key="id"
            :show-checkbox="showCheckbox"
            :props="props"
            class="tree-list-box"
            :default-expand-all="defaultExpandAll"
            :highlight-current="highlight"
            :expand-on-click-node="false"
            :default-checked-keys="dfCheckedKeys"
            :default-expanded-keys="defaultExpandedKeys"
            :current-node-key="currentNodeKey"
            :check-strictly="strictly"
            accordion
            @node-click="handleNodeClick"
            @check-change="getChecked"
            @node-expand="handleNodeExpand"
        >
            <span
                class="custom-tree-node"
                slot-scope="{ node, data }"
                @contextmenu.prevent="mouseclick(data)"
                @dblclick.stop="handleDblClick(data)"
            >
                <svg-icon v-if="!data[labelTwo]" class="t-icon" :iconClass="data.icon" />
                <svg-icon
                    v-if="data[labelTwo] && !data.hasOwnProperty('personImg')"
                    class="t-icon"
                    :iconClass="data.icon"
                />
                <span v-if="data[labelTwo] && data.personImg">
                    <span v-if="!data.personImg.filePath" class="el-icon-aliuser default-avatar"></span>
                    <img v-else class="tuser-avatar" :src="url + data.personImg.filePath" />
                </span>

                <!-- <svg-icon v-if="!data[labelTwo] || data.icon" class="t-icon" :iconClass="data.icon" />
                <span v-if="data[labelTwo] && !data.icon && data.imgPath"><img class="tuser-avatar" :src="url + '/file' + data.imgPath"/></span> -->
                <!-- <span v-if="data[labelTwo] && !data.icon && !data.imgPath" class="el-icon-aliuser default-avatar"></span> -->
                <span class="tree-label" :title="node.label"
                    >{{ node.label }}<i v-if="data.count && data.count > 0">({{ data.count }})</i></span
                >
                <span v-show="node.data.isoperation" v-if="showEdit">
                    <el-button type="text" size="mini" @click="(e) => append(e, data)">
                        添加
                    </el-button>
                    <el-button type="text" size="mini" @click="(e) => remove(e, node, data)">
                        删除
                    </el-button>
                </span>
            </span>
            <!-- 无数据 S-->
            <!-- <template slot="empty">
                <EmptyComponent
                    class="nodata-tips"
                    size="80px"
                    color="#ccc"
                    v-if="!tbLoading"
                />
            </template> -->
        </el-tree>
    </div>
</template>

<script>
// import EmptyComponent from "@/components/empty";
import { requestUrl } from "@/api/api";

let id = 10000;
export default {
    name: "foldTreeCom",
    components: {
        // EmptyComponent
    },
    props: {
        treeList: {
            type: Array,
            default: () => [],
        },
        showCheckbox: {
            type: Boolean,
            default: () => false,
        },
        showEdit: {
            type: Boolean,
            default: () => false,
        },
        defaultExpandAll: {
            type: Boolean,
            default: () => false,
        },
        label: {
            type: String,
            default: () => "label",
        },
        labelTwo: {
            type: String,
            default: () => "label",
        },
        baseNodeKey: {
            type: String,
            default: () => "id",
        },
        highlight: {
            type: Boolean,
            default: () => true,
        },
        dfCheckedKeys: {
            type: Array,
            default: () => [],
        },
        disabledData: {
            type: Array,
            default: () => [],
        },
        strictly: {
            type: Boolean,
            default: () => false,
        },
        selectType: {
            type: String,
            default: () => "",
        },
        keys: {
            type: String,
            default: "",
        },
        childrenName: {
            type: String,
            default: "children",
        },
    },
    data() {
        return {
            props: {
                label: (data, node) => {
                    return data[this.label] ? data[this.label] : data[this.labelTwo];
                },
                children: this.childrenName,
                disabled: (data, node) => {
                    return this.disabledData.indexOf(data.id) != -1;
                },
            },
            url: "",
            selecteds: [],
            menuIds: "",
            expandedKey: [123456789],
            currentNodeKey: this.dfCheckedKeys.length == 0 ? "" : this.dfCheckedKeys[0],
            visible: true,
        };
    },
    computed: {
        defaultExpandedKeys() {
            const defaultExpandedKeys =
                this.dfCheckedKeys.length == 0
                    ? this.expandedKey
                    : this.dfCheckedKeys.reduce((sum, v) => {
                          sum.push(v);
                          if (v == v) {
                              sum.push(v);
                          }
                          return sum;
                      }, []);
            this.currentNodeKey = this.dfCheckedKeys.length == 0 ? "" : this.dfCheckedKeys[0];
            return defaultExpandedKeys;
        },
    },
    watch: {
        menuIds(val) {
            this.selecteds = val.split(",");
        },
        defaultExpandedKeys: {
            handler(val, oldVal) {
                if (JSON.stringify(val) !== JSON.stringify(oldVal)) {
                    this.refreshRoot();
                }
            },
            deep: true,
            immediate: true,
        },
        treeList: {
            handler(list, oldList) {
                if (list && list.length) {
                    if (
                        this.expandedKey.length == 0 ||
                        (this.expandedKey.length === 1 && this.expandedKey.includes(123456789))
                    ) {
                        const expandedKey =
                            // list[0] &&
                            // list[0].children &&
                            // list[0].children[0] &&
                            // list[0].children[0]['id']
                            //     ? list[0].children[0]['id']
                            //     :
                            list[0].id;
                        this.expandedKey.push(expandedKey);
                    }
                }
            },
            deep: true,
            immediate: true,
        },
    },
    created() {
        this.url = requestUrl + "/file/";
    },
    methods: {
        refreshRoot() {
            this.visible = false;
            this.$nextTick(() => {
                this.visible = true;
            });
        },
        handleDblClick(data) {
            this.$emit("dbSelected", data);
        },
        append(e, data) {
            e.stopPropagation();
            const newChild = {
                id: id++,
                label: "testtest",
                isoperation: false,
                children: [],
            };
            if (!data.children) {
                this.$set(data, "children", []);
            }
            data.children.push(newChild);
        },

        remove(e, node, data) {
            e.stopPropagation();
            const parent = node.parent;
            const children = parent.data.children || parent.data;
            const index = children.findIndex((d) => d.id === data.id);
            children.splice(index, 1);
        },
        change(e, node, data) {
            e.stopPropagation();
        },
        mouseclick(data) {
            if (this.showEdit) {
                this.formatTrees(this.treeList, data.id);
            }
        },
        formatTrees(treeList, selectid) {
            treeList.forEach((item) => {
                if (item.id == selectid) {
                    item.isoperation = !item.isoperation;
                } else {
                    item.isoperation = false;
                }
                if (item.children) {
                    this.formatTrees(item.children, selectid);
                }
            });
            return treeList;
        },
        handleNodeClick(node) {
            const Node = Object.assign({}, node);
            // Node.id = Node.id === 'null' ? null : Node.id;
            this.$emit("clickNode", Node);
        },
        getChecked(data, status, data2) {
            let menuIds;
            if (!this.strictly) {
                menuIds = this.getCheckedKeys(this.treeList, this.$refs.tree.getCheckedKeys(), "id").join(",");
            } else {
                menuIds = this.$refs.tree.getCheckedKeys().join(",");
            }
            this.menuIds = menuIds;
            this.$emit("getChecked", { menuIds }, data, status);
        },
        getCheckedKeys(data, keys, key) {
            var res = [];
            recursion(data, false);
            return res;
            function recursion(arr, isChild) {
                var aCheck = [];
                for (var i = 0; i < arr.length; i++) {
                    var obj = arr[i];
                    aCheck[i] = false;
                    if (obj.children) {
                        aCheck[i] = recursion(obj.children, true) ? true : aCheck[i];
                        if (aCheck[i]) {
                            res.push(obj[key]);
                        }
                    }
                    for (var j = 0; j < keys.length; j++) {
                        if (obj[key] == keys[j]) {
                            aCheck[i] = true;
                            if (res.indexOf(obj[key]) == -1) {
                                res.push(obj[key]);
                            }
                            break;
                        }
                    }
                }
                if (isChild) {
                    return aCheck.indexOf(true) != -1;
                }
            }
        },
        handleSetCurrentKey(val) {
            this.$refs.tree.setCurrentKey(val);
        },
        handleClearSelectTree() {
            this.$refs.tree.setCheckedKeys([]);
        },
        setCheckedNodes(val) {
            this.$refs.tree.setCheckedKeys(val);
        },
        handleNodeExpand() {
            this.$emit("nodeExpand");
        },
    },
};
</script>
<style lang="scss" scoped>
@import "@/styles/fold-tree.scss";
</style>
