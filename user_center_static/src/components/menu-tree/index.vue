<template>
    <div v-loading="loading.base" class="dv-menu-tree">
        <el-tree
            v-if="visible.base"
            :key="key"
            :data="treeList"
            :show-checkbox="showCheckbox"
            node-key="id"
            :props="props"
            class="menu-tree-box"
            :expand-on-click-node="true"
            :default-checked-keys="dfCheckedKeys"
            :default-expanded-keys="dfCheckedKeys"
            :filter-node-method="filterNode"
            ref="tree"
            :default-expand-all="expandAll"
            :render-after-expand="false"
            @node-click="handleNodeClick"
            @check-change="getChecked"
            @node-expand="handleExpand"
            @check="loadingChecked"
        >
            <span
                :class="[
                    'custom-tree-node',
                    'span_' + data.layer,
                    'custom-tree-node-' + data.type,
                    'custom-tree-node-expand-' + (data.expandTag ? 1 : 0)
                ]"
                slot-scope="{ node, data }"
                v-show="data.type !== '4'"
            >
                <!-- <i class="t-icon" :class="data.icon"></i> -->
                <svg-icon class="t-icon" :iconClass="data.icon" />
                <span class="node-label">
                    {{ node.label }}
                    <el-tag
                        v-if="data.isPhone"
                        size="mini"
                        type="success"
                        class="link"
                    >
                        APP
                    </el-tag>
                </span>
                <span class="handle-menu">
                    <el-checkbox
                        v-for="(item, index) in data.menuBox"
                        :key="index"
                        v-model="item.isSelect"
                        :true-label="1"
                        :false-label="0"
                        @change="handleMenu($event, item)"
                        >{{ item.name }}</el-checkbox
                    >
                </span>
                <!-- <span class="handle-button"></span> -->
            </span>
        </el-tree>
    </div>
</template>

<script>
let id = 10000;
export default {
    name: 'menuTreeCom',
    props: {
        treeList: {
            type: Array,
            default: () => []
        },
        showCheckbox: {
            type: Boolean,
            default: () => false
        },
        expandAll: {
            type: Boolean,
            default: () => false
        },
        label: {
            type: String,
            default: () => 'label'
        },
        labelTwo: {
            type: String,
            default: () => 'label'
        },
        highlight: {
            type: Boolean,
            default: () => false
        },
        dfCheckedKeys: {
            type: Array,
            default: () => []
        },
        height: {
            type: Number,
            default: () => null
        }
    },
    data() {
        return {
            props: {
                label: (data, node) => {
                    return data[this.label]
                        ? data[this.label]
                        : data[this.labelTwo];
                }
            },
            key: 1,
            loading: {
                base: false
            },
            visible: {
                base: true
            }
        };
    },
    watch: {
        treeList() {
            this.operateDom();
            // this.$refs.tree.filter('');
        },
    },
    // mounted() {},
    methods: {
        handleExpandAll(tag) {
            this.expandAll = tag;
        },
        handleMenu(e, item) {
            this.$refs.tree.setChecked(item.id, e ? true : false);
        },
        operateDom() {
            this.$nextTick(() => {
                let dom = document.querySelectorAll('.custom-tree-node-4');
                dom.forEach((item, i) => {
                    item.parentNode.parentNode.style.display = 'none';
                });
                let dom2 = document.querySelectorAll(
                    '.custom-tree-node-expand-1'
                );
                dom2.forEach((item, i) => {
                    item.parentNode.children[0].style.display = 'none';
                });
                let dom3 = document.querySelectorAll(
                    '.custom-tree-node-expand-0'
                );
                for (const v of dom3) {
                    const d = v.parentNode.children[0];
                    if (
                        !d.className.includes('expanded') &&
                        d.className.includes('is-leaf')
                    ) {
                        d.style.display = 'none';
                    }
                }
            });
        },
        filterNode(value, data, node) {
            return data.type !== '4';
        },
        change(e, node, data) {
            e.stopPropagation();
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
            this.$emit('clickNode', node);
        },
        loadingChecked(data, e) {
            this.loading.base = true;
            setTimeout(() => {
                this.loading.base = false;
            }, 0);
        },
        getChecked(data, e) {
            data.isSelect = e ? 1 : 0;
            let menuIds = this.getCheckedKeys(
                this.treeList,
                this.$refs.tree.getCheckedKeys(),
                'id'
            ).join(',');
            this.$emit('getChecked', { menuIds });
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
                        aCheck[i] = recursion(obj.children, true)
                            ? true
                            : aCheck[i];
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
        async setCheckedNodes(val) {
            this.loading.base = true;
            setTimeout(() => {
                this.$refs.tree.setCheckedKeys(val);
                this.loading.base = false;
                // setTimeout(() => {
                //     this.loading.base = false;
                // }, 0);
            }, 50);
        },
        handleExpand() {
            this.$emit('expand');
        }
    }
};
</script>

<style lang="scss" scoped>
@import "@/styles/menu-tree.scss";
</style>
