<template>
    <div class="sys-parameter-list">
        <div class="sys-parameter-left">
            <div class="aside-con">
                <div class="hd">
                    <h2>部门</h2>
                </div>
                <loading-component :loading="treeLoding" class="bd">
                    <fold-tree
                        label="cname"
                        ref="zzTree"
                        :strictly="true"
                        :treeList="treeListData"
                        :highlight="true"
                        @clickNode="handleClickNode"
                    ></fold-tree>
                </loading-component>
            </div>
        </div>
        <div class="sys-parameter-right">
            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane v-for="(item, i) in sysParameterListHash" :name="item['package']" :key="i">
                    <div class="tabs-label" slot="label">
                        <svg-icon :iconClass="item['icon']" />
                        <span> {{ item["name"] }}</span>
                    </div>
                </el-tab-pane>
                <component :org-id="orgId" :is="activeName" :ref="activeName"></component>
            </el-tabs>
            <footer>
                <el-button type="primary" :loading="saveLoading" size="mini" @click="onSave">
                    保存
                </el-button>
                <el-button :disabled="saveLoading" @click="onBack" size="mini">
                    返回
                </el-button>
            </footer>
        </div>
    </div>
</template>

<script>
import packages from "./packages";
import FoldTree from "@/components/fold-tree";
import { getLocalStorage } from "@/utils/auth";
import { sysParameterListHash, setTypeHash } from "./config";

export default {
    name: "sysParameterList",
    data() {
        return {
            orgId: getLocalStorage("userInfo").orgId,
            saveLoading: false,
            treeLoding: false,
            activeName: "SystemParameters",
            sysParameterList: [],
            treeListData: [],
            listLoading: false,
            sysParameterListHash,
        };
    },
    components: { FoldTree, ...packages },
    mounted() {
        this.getDeptTree();
    },
    methods: {
        handleClick() {},
        currentView(activeName) {
            return packages[activeName] ? activeName : "Empty";
        },
        onBack() {
            this.$router.go(-1);
        },
        handleClickNode(data) {
            this.orgId = data.id;
        },
        async getDeptTree() {
            this.treeLoding = true;
            try {
                let res = await this.$http.getUcenterOrgTree({ compType: "10027-30" });
                if (res.code == 0) {
                    let organizationList = this.$formatTree(
                        res.data,
                        "listPerson",
                        true,
                        "tree-filebox",
                        "tree-file",
                        "",
                        false,
                        true
                    );

                    organizationList.forEach((item) => {
                        item.icon = "tree-filebox";
                    });
                    this.treeListData = organizationList;
                }
            } catch (error) {}
            this.treeLoding = false;
        },
        async onSave() {
            this.saveLoading = true;
            try {
                const { activeName } = this;
                let idDelIn = "";
                const item = this.$refs[activeName];
                const formData = item.getForm();
                if (item.getDelete) {
                    idDelIn = item.getDelete();
                }
                const { message, code } = await this.$http.sysParameterSave({
                    orgId: this.orgId,
                    idDelIn,
                    setType: setTypeHash[activeName],
                    parameters: JSON.stringify(formData),
                });

                if (code === 0) {
                    this.$showSuccess(message);
                } else {
                    this.$message.error(message);
                }
            } catch (error) {
                this.$message.error(message);
                console.error(error);
            }
            this.saveLoading = false;
        },
    },
};
</script>

<style lang="scss" scoped>
.sys-parameter-list {
    height: 100%;
    padding: 15px 0;
    display: flex;
    justify-content: space-between;
}
.sys-parameter-left {
    width: 280px;
    height: 100%;
    margin-right: 10px;
}
.sys-parameter-right {
    height: 100%;
    width: calc(100% - 300px);
    position: relative;
    display: flex;
    flex-direction: column;
    /deep/.el-tabs {
        height: calc(100% - 40px);
    }
    .el-tabs__item.is-active {
        .tabs-label {
            font-weight: 700;
            color: #409eff;
        }
    }
    /deep/.el-tabs__content {
        height: calc(100% - 55px);
        overflow: auto;
    }
    .tabs-label {
        color: #666;
        font-size: 14px;
        padding: 0 6px 0 2px;
        svg {
            font-size: 16px;
        }
    }
    > footer {
        height: 40px;
        display: flex;
        justify-content: flex-end;
        align-items: center;
        padding-right: 10px;
    }
}
</style>
