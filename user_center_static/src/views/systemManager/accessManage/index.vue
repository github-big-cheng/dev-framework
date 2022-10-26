<template>
    <div class="u-obj-container" ref="vbox">
        <el-row :gutter="24" class="asideormain">
            <el-col :span="6" class="asidewrap">
                <div class="aside-con">
                    <el-tabs v-model="activeName" @tab-click="handleTabClick">
                        <el-tab-pane label="按用户" name="10079-40">
                            <div class="tab-tree">
                                <!-- 人员搜索 -->
                                <el-input
                                        v-model.trim="organizationValue"
                                        ref="input"
                                        class="search-ipt"
                                        placeholder="请输入关键字搜索"
                                        @input="filterData"
                                >
                                    <el-button class="search-btn" slot="append" icon="el-icon-alisearch"></el-button>
                                </el-input>
                                <fold-tree
                                        v-show="!showSearchZz"
                                        :treeList="userTreeList"
                                        :treeLoading="treeLoading"
                                        label="cname"
                                        labelTwo="name"
                                        @clickNode="handleClickNode"
                                        class="tree-wrapper"
                                ></fold-tree>
                                <!-- 人员搜索结果数据展示 -->
                                <div class="s-list" v-show="showSearchZz">
                                    <draggable
                                            v-model="searchOrganizationList"
                                            chosen-class="chosen"
                                            force-fallback="true"
                                            group="people"
                                            animation="1000"
                                            delay="100"
                                    >
                                        <transition-group>
                                            <div
                                                    v-for="(item, index) of searchOrganizationList"
                                                    :key="item.id"
                                                    :class=" activeIndex == index ? 's-item is-selected ' : 's-item'"
                                                    @click.stop="handleSelectedZz(item, index)"
                                            >
                                                <div class="item-user">
                                                  <span v-if="item.name && item.imgPath"
                                                        :style="{ color: item.isDisabled ? '#ccc' : '#E5E5E5', }">
                                                      <img class="tuser-avatar" :src="url + '/file' + item.imgPath"
                                                      /></span>
                                                    <span v-if="item.name && !item.imgPath"
                                                          class=" el-icon-aliuser default-avatar"
                                                          :style="{ color: item.isDisabled ? '#ccc' : '#E5E5E5', }"></span>
                                                </div>
                                                <div class="item-desc">
                                                    <div class="i-name"
                                                         :style="{ color: item.isDisabled ? '#ccc' : '#333', }">
                                                        {{ item.name }}
                                                    </div>
                                                    <div class="i-dept"
                                                         :style="{ color: item.isDisabled ? '#ccc' : '#999', }">
                                                        {{ item.cname }}
                                                    </div>
                                                </div>
                                            </div>
                                        </transition-group>
                                    </draggable>
                                </div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="按角色" name="10079-30">
                            <div class="tab-tree">
                                <fold-tree
                                        label="name"
                                        :treeList="roleTreeList"
                                        @clickNode="handleClickNode"
                                ></fold-tree>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="按部门" name="10079-10">
                            <div class="tab-tree">
                                <fold-tree
                                        :treeList="posTreeList"
                                        label="cname"
                                        @clickNode="handleClickNode"
                                ></fold-tree>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </el-col>
            <el-col :span="18" class="mainwrap-has-aside">
                <el-tabs v-model="tabActiveName" @tab-click="handleClick">
                    <el-tab-pane
                            :label="item.name"
                            :name="i + ''"
                            v-for="(item, i) in projectList"
                            :key="item.id"
                    ></el-tab-pane>
                </el-tabs>
                <operation-com
                        ref="dvheader"
                        @handlerType="operationHandler"
                        :btnConfigs="btnConfigs"
                ></operation-com>
                <div
                        class="access-table"
                        v-loading="menuLoading"
                        element-loading-text="加载中..."
                        element-loading-spinner="el-icon-loading"
                        element-loading-background="rgba(255, 255, 255, 1)"
                >
                    <span class="sline1"></span>
                    <span class="sline2"></span>
                    <div class="tree-header">
                        <div class="t-left">
                            <el-checkbox
                                    v-model="checkedAll"
                                    @change="handleChangeSelected"
                            ></el-checkbox>
                        </div>
                        <div class="t-center">菜单名称</div>
                        <div class="t-center2">操作</div>
                        <!-- <div class="t-right">数据权限分配</div> -->
                    </div>
                    <menu-tree
                            v-loading="loading.menutree"
                            :key="key.menutree"
                            :treeList="treeList"
                            label="name"
                            ref="menutree"
                            :showCheckbox="true"
                            :height="height"
                            :dfCheckedKeys="dfCheckedKeys"
                            :expandAll="expandAll"
                            class="menu-tree"
                            @getChecked="handleGetChecked"
                            @clickNode="handleNodeClick"
                            @expand="handlExpandClick"
                    ></menu-tree>
                </div>
            </el-col>
        </el-row>
        <div class="dv-btn form-button" ref="footer">
            <el-button @click="handleRefreshClick">取消</el-button>
            <el-button
                    type="primary"
                    :loading="isLoading"
                    style="color: #fff; border: 1px solid #409eff"
                    @click="clickSave"
            >{{ isLoading ? '保存中' : '保存' }}
            </el-button>
        </div>
    </div>
</template>

<script>
    import foldTree from '@/components/fold-tree'
    import menuTree from '@/components/menu-tree'
    import operationCom from '@/components/operation'
    import draggable from 'vuedraggable'
    import lodash from 'lodash'

    export default {
        name: 'accessManage',
        components: {
            foldTree,
            menuTree,
            draggable,
            operationCom,
        },
        data() {
            return {
                canFilter: true,

                btnConfigs: [
                    {
                        text: '打开权限树',
                        type: 'add',
                        icon: 'el-icon-aliadd',
                        handlerType: 'handleExpandAll',
                        params: true,
                    },
                    {
                        text: '关闭权限树',
                        icon: 'el-icon-alidelete',
                        handlerType: 'handleExpandAll',
                        type: 'modify',
                        params: false,
                    },
                    {
                        type: 'refresh',
                        text: '刷新',
                        icon: 'el-icon-alirefresh',
                        code: 'ucenter_function_view',
                        handlerType: 'handleClick',
                    },
                ],
                tabActiveName: 0,
                activeName: '10079-40',
                userTreeList: [],
                roleTreeList: [],
                posTreeList: [],
                obj: {
                    '10079-40': {objId: '',},
                    '10079-30': {objId: '',},
                    '10079-10': {objId: '',},
                },
                map: {
                    tab: {'10079-40': '用户', '10079-30': '角色', '10079-10': '部门',},
                },
                ids: '',
                type: '10079-40',
                dfCheckedKeys: [],
                alltreeids: [],
                isLoading: false,
                tableData: [],
                treeList: [],
                projectList: [],
                height: null,
                tHeight: null,
                checkedAll: false,
                menuLoading: false,
                key: {
                    menutree: 1,
                },
                expandAll: false,
                loading: {
                    menutree: false,
                },
                timeout: 0,
                url: '',
                personData: {},
                organizationValue: '',
                showSearchZz: false,
                allOrganizationList: [],
                searchOrganizationList: [],
                activeIndex: -1,
                treeLoading: false,
            }
        },
        watch: {
            ids: {
                handler(val) {
                    let set = new Set(val.split(','));
                    this.checkedAll = !this.alltreeids.some(id => !set.has(id));
                    // console.log(this.checkedAll, set.size, this.alltreeids.length);
                }
            },
            organizationValue(val) {
                if (!val) {
                    this.showSearchZz = false
                }
            },
        },
        created() {
            // this.getMenuList('', "userId");
            this.getProjectList()
            this.getUserList()
            this.getRoleList()
            this.getPosList()
            this.url = window.location.origin
        },
        methods: {
            handleClick() {
                let id = this.obj[this.type].objId;
                this.getMenuList(id)
            },
            //
            operationHandler(type, params) {
                this[type](params)
            },
            filterData() {
                if (this.organizationValue.trim() === '') return;
                this.searchOrganizationList = this.allOrganizationList.filter((item) => item.name && item.name.indexOf(this.organizationValue) != -1)
                this.showSearchZz = true
            },
            clearUserListSelect() {
                let userTree = this.$formatTree(
                    this.personData,
                    'listPerson',
                    true,
                    'tree-filebox',
                    'tree-file',
                    null,
                    null,
                    true
                )
                userTree.forEach((item) => {
                    item.icon = 'tree-filebox'
                })
                ;(this.userTreeList = []), (this.userTreeList = userTree)
            },
            handleSelectedZz(item, index) {
                this.activeIndex = index

                this.obj[this.type].objId = item.id
                this.$refs.menutree.setCheckedNodes([])
                this.checkedAll = false
                this.getMenuList(item.id, 'userId')
            },
            async handleExpandAll(tag) {
                this.loading.menutree = true
                await new Promise((resolve) => {
                    setTimeout(() => {
                        let dom = document.querySelectorAll(
                            '.mainwrap-has-aside .el-tree-node.is-focusable'
                        )

                        dom = Object.values(dom)
                        for (const d of dom) {
                            if (d.className.includes('is-expanded') == tag) {
                                d.click()
                            }
                        }
                        resolve()
                    }, 500)
                })
                this.loading.menutree = false
                // this.key.menutree++;
            },
            formatLeft() {
                setTimeout(() => {
                    let span_2 = document.getElementsByClassName('span_2')
                    if (span_2 && span_2.length) {
                        for (let i = 0; i < span_2.length; i++) {
                            if (span_2[i] && span_2[i].parentNode) {
                                span_2[i].parentNode.getElementsByClassName('el-checkbox')[0].className =
                                    'el-checkbox treelist-layer1'
                            }
                        }
                    }

                    let span_3 = document.getElementsByClassName('span_3')
                    if (span_3 && span_3.length) {
                        for (let i = 0; i < span_3.length; i++) {
                            if (span_3[i] && span_3[i].parentNode) {
                                span_3[i].parentNode.getElementsByClassName('el-checkbox')[0].className =
                                    'el-checkbox treelist-layer2'
                            }
                        }
                    }

                    let span_4 = document.getElementsByClassName('span_4')
                    if (span_4 && span_4.length) {
                        for (let i = 0; i < span_4.length; i++) {
                            if (span_4[i] && span_4[i].parentNode) {
                                span_4[i].parentNode.getElementsByClassName('el-checkbox')[0].className =
                                    'el-checkbox treelist-layer3'
                            }
                        }
                    }
                }, 0)
            },
            formatChecked() {
                setTimeout(() => {
                    let checboxs = document.getElementsByClassName('el-checkbox')
                    if (checboxs && checboxs.length) {
                        for (let i = 0; i < checboxs.length; i++) {
                            checboxs[i].style.background = '#fff'
                        }
                    }

                    let checkeds = document.getElementsByClassName('is-indeterminate')
                    if (checkeds && checkeds.length) {
                        for (let i = 0; i < checboxs.length; i++) {
                            if (checkeds[i] && checkeds[i].parentNode) {
                                checkeds[i].parentNode.style.background = 'rgba(218, 238, 253,1)'
                            }
                        }
                    }
                }, 0)
            },
            handlExpandClick() {
                this.formatLeft()
            },
            async getProjectList() {
                const {code, data} = await this.$http.projectCombox();
                if (code == 0) {
                    this.projectList = data.list;
                }
            },
            getUserList() {
                this.treeLoading = true
                this.$http.getUcenterOrgTreePerson({isMain: 1}).then((res) => {
                    if (res.code == 0) {
                        this.personData = res.data
                        let userTree = this.$formatTree(
                            this.personData,
                            'listPerson',
                            true,
                            'tree-filebox',
                            'tree-file',
                            null,
                            true
                        )
                        userTree.forEach((item) => {
                            item.icon = 'tree-filebox'
                        })
                        this.userTreeList = userTree
                        //搜索从全体中搜索
                        this.allOrganizationList = this.$filterAll(this.userTreeList)
                    }
                    this.treeLoading = false
                    this.$route.meta.noLoading = true
                })
            },
            getRoleList() {
                this.$http.ucenterroleList({pageSize: 9999}).then((res) => {
                    if (res.code == 0) {
                        let {list} = res.data
                        // list.forEach((item) => {
                        //   item.icon = "treefile";
                        // });
                        this.roleTreeList = list
                    }
                })
            },
            getPosList() {
                this.$http.getUcenterOrgTree().then((res) => {
                    if (res.code == 0) {
                        let organizationList = this.$formatTree(
                            res.data,
                            'listPerson',
                            true,
                            'tree-filebox',
                            'tree-file',
                            false,
                            true
                        )

                        organizationList.forEach((item) => {
                            item.icon = 'tree-filebox'
                        })
                        this.posTreeList = organizationList
                    }
                })
            },
            handleClickNode: lodash.debounce(
                function (data) {
                    if (data.children && this.type != '10079-10') return
                    this.menuLoading = true
                    this.$refs.menutree.setCheckedNodes([])
                    this.checkedAll = false
                    //按部门需特殊处理
                    if (this.type == '10079-10') {
                        //处理字符串org
                        let id = Number(data.id.replace('org', ''))

                        this.obj[this.type].objId = id
                        this.getMenuList(id)
                    }

                    if (!data.children && this.type != '10079-10') {
                        let {id} = data
                        this.obj[this.type].objId = id
                        this.getMenuList(id)
                    }
                },
                300,
                {
                    leading: true,
                }
            ),
            async getMenuList(objId) {
                if (!objId) return
                try {
                    this.menuLoading = true
                    let res
                    let menuList
                    res = await this.$http.getUcenterobjfunc({
                        projectId: this.projectList[this.tabActiveName].id,
                        objType: this.activeName,
                        objId,
                    })
                    menuList = this.$formatTree(
                        res.data,
                        'subFunction',
                        false,
                        'tree-filebox',
                        'tree-file'
                    )

                    this.treeList = this.formatMenuList(menuList);

                    this.tableData = menuList
                    this.dfCheckedKeys = this.$filterTreeId(menuList).treeIds
                    this.ids = this.dfCheckedKeys.join(',')
                    let allIds = this.$filterTreeId(menuList).allIds
                    this.alltreeids = this.$filterAllTreeId(menuList)

                    if (allIds.length == this.alltreeids.length) {
                        this.checkedAll = true
                    } else {
                        this.checkedAll = false
                    }

                    this.formatLeft()
                    this.formatChecked()
                    // this.closeLoading(this.$route);
                } catch (e) {
                    // this.closeLoading(this.$route);
                } finally {
                    setTimeout(() => {
                        this.menuLoading = false
                    }, 400)
                }
            },
            formatMenuList(menuList) {
                let isArr = typeof menuList.length !== 'undefined'
                menuList = isArr ? menuList : [menuList]
                for (let v of menuList) {
                    let tag = true
                    const menuBox = []
                    if (v.subFunction && v.subFunction.length > 0) {
                        for (let v2 of v.subFunction) {
                            if (v2.subFunction && v2.subFunction.length > 0) {
                                v2 = this.formatMenuList(v2)
                            }
                            if (v2.type != 4) {
                                tag = false
                                // break;
                            } else {
                                menuBox.push(v2)
                            }
                        }
                    }
                    v.expandTag = tag
                    v.menuBox = menuBox
                }
                return isArr ? menuList : menuList[0]
            },
            clickSave() {
                if (!this.obj[this.type].objId || this.obj[this.type].objId == 0) {
                    this.$showWarning('请指定人员')
                    return
                }
                this.saveMenu(this.ids, this.obj[this.type].objId)
            },
            saveMenu(funcIds, objId) {
                let params = {
                    funcIds,
                    objType: this.activeName,
                    objId,
                    projectId: this.projectList[this.tabActiveName].id,
                }
                this.isLoading = true
                this.$http
                    .functionSave(params)
                    .then((res) => {
                        if (res.code == 0) {
                            this.$showSuccess(res.message)
                        } else {
                            this.$showError(res.message)
                        }
                        this.isLoading = false
                    })
                    .catch((err) => {
                        this.isLoading = false
                    })
            },
            handleGetChecked({menuIds}) {
                this.formatLeft();
                this.ids = menuIds
                this.formatChecked()

                if (
                    !menuIds ||
                    (this.obj[this.type].objId && this.obj[this.type].objId !== '0')
                ) {
                    clearTimeout(this.timeout)
                } else {
                    this.handleGetCheckedTip()
                }
            },
            handleNodeClick(node) {
            },
            handleGetCheckedTip() {
                clearTimeout(this.timeout)
                this.timeout = setTimeout(() => {
                    this.$message.warning(`请先选择${this.map.tab[this.activeName]}`)
                }, 100)
            },
            handleTabClick({name}) {
                this.type = name
                this.$refs.menutree.setCheckedNodes([])
                this.checkedAll = false
                this.getMenuList(this.obj[name].objId, name)
            },
            handleRefreshClick() {
                this.$refs.menutree.setCheckedNodes([])
                this.getMenuList(this.obj[this.type].objId, this.type)
            },
            handleChangeSelected(status) {
                if (status) {
                    this.$refs.menutree.setCheckedNodes(this.alltreeids)
                } else {
                    this.$refs.menutree.setCheckedNodes([])
                }
            },
        },
    }
</script>

<style lang="scss" scoped>
    @import '@/styles/menu-tree.scss';

    .u-obj-container {
        padding: 0 10px;
        height: 100%;

        .asideormain {
            height: calc(100% - 58Px); /*no*/
        }

        .aside-packup .aside-con .hd {
            top: 0;
        }
    }

    @media screen and (min-width: 1501px) {
        .u-obj-container .asideormain {
            height: calc(100% - 58px);
        }
    }
</style>
