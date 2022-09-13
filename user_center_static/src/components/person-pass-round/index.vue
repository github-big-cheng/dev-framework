<!--
 * @Author: your name
 * @Date: 2021-03-16 10:32:17
 * @LastEditTime: 2021-08-25 16:44:38
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\views\docManager\docQuery\component\receiveLibrary\receiveDialog.vue
-->
<template>
    <div class="main-content-wrap doc-query-container" ref="vbox" v-if="showDialog">
        <!--
          <div ref="pagetitle">
            <pageTitle :title="$route.meta.title"></pageTitle>
          </div>
        -->
        <Dialog
                iconfont="el-icon-alipeople-tit"
                :dialogVisible="showDialog"
                title="阅读流转"
                width="60%"
                @trueClick="handleTrueClick"
                @cancelClick="handleCancelClick">
            <el-tabs v-model="activeName" @tab-click="handleTabClick">
                <el-tab-pane label="按部门" name="10027-10">
                    <el-row :gutter="24">
                        <el-col :span="9" class="col-zz">
                            <div class="tab-tree">
                                <fold-tree
                                        label="cname"
                                        labelTwo="name"
                                        ref="departchildTree"
                                        node-key="id"
                                        selectType="name"
                                        :treeList="departTreeList"
                                        @clickNode="handleClickNode"
                                ></fold-tree>
                            </div>
                        </el-col>
                        <el-col :span="15" class="col-zz">
                            <checkedPerson
                                type="10027-10"
                                :activeName="activeName"
                                :defaultIds="personIds ? personIds.split(',') : []"
                                :isGet="isGet"
                                :selectTreeNodeId="selectDeptTreeNodeId"
                                :showDialog="showDialog"
                                v-if="showDialog"
                                @getIds="getIds"></checkedPerson>
                        </el-col>
                    </el-row>
                </el-tab-pane>
            </el-tabs>
        </Dialog>
    </div>
</template>
<script>
    import Dialog from "@/components/dialog";
    import foldTree from "@/components/fold-tree";
    import checkedPerson from '@/components/checked-person';

    export default {
        name: 'personPassRoundCom',
        props: ['showDialog', 'id', 'personIds'],
        data() {
            return {
                activeName: '10027-10',
                isGet: false,
                // foldTree业务
                departTreeList: [],

                assigneeList: '',

                selectDeptTreeNodeId: null,//当前选中的tree节点id
                firstIcon: 'tree-filebox',
                secondIcon: 'tree-file'
            }
        },
        components: {
            Dialog,
            foldTree,
            checkedPerson
        },
        created() {
            this.getDeptTree()
        },
        // mounted(){
        //     console.log(this.personIds,'personIds')
        // },
        methods: {
            getIds(ids) {
                ids && (this.assigneeList += ids)
            },
            handleClickNode(opt) {
                if (!opt || opt == {}) return;
                this.selectDeptTreeNodeId = opt.id;
            },
            handleTrueClick() {

                this.isGet = true;
                this.assigneeList = '';
                setTimeout(() => {
                    this.isGet = false;
                    if (this.assigneeList == '') {
                        this.$showWarning('流转人员不能为空');
                        return;
                    }
                    this.assigneeList = this.assigneeList.substring(0, this.assigneeList.length - 1);
                    this.$emit('trueClick', this.assigneeList);
                }, 0);

            },
            handleCancelClick() {
                this.$emit('cancelClick')
            },
            handleTabClick({name}) {
            },
            getDeptTree() {
                this.$http.getUcenterOrgTree({}).then((res) => {
                    if (res.code == 0) {
                        this.departTreeList = this.$formatTree(
                            res.data,
                            "children",
                            false,
                            this.firstIcon,
                            this.secondIcon,
                            false, false,
                            'children', this.idsScoped
                        );
                    }
                });
            },
        },
    }
</script>

<style lang="scss" scoped>

    .col-zz {
        box-sizing: border-box;
        padding: 11px !important;
        border: 1px solid #eee;
        height: 400px;
        overflow: auto;
    }

    @media screen and (max-height: 720px) {
        .col-zz {
            height: 350px;
        }
    }
</style>
