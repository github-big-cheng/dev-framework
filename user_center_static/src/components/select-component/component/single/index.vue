<template>
    <div class="personal-single" v-loading="singleLoading"
        element-loading-text="努力加载中"
        element-loading-spinner="el-icon-loading">
        <div class="dv-organization">
            <fold-tree
                :style="{visibility: singleLoading ? 'hidden' : ''}"
                label="cname"
                labelTwo="name"
                ref="zzTree"
                v-show="!showSearchZz"
                selectType="name"
                :treeList="organizationList"
                :dfCheckedKeys="dfCheckedKeys"
                @clickNode="handleClickNode"
                @nodeExpand="formatDisabled"
                @dbSelected="selectedZzClick"
            ></fold-tree>
            <el-input
                v-model.trim="organizationValue"
                clearable
                class="search-ipt"
                ref="touchFocus"
                placeholder="请输入关键字搜索"
                @input="filterData"
            >
                <el-button
                    slot="append"
                    icon="el-icon-alisearch"
                ></el-button>
            </el-input>
            <div class="s-list" v-show="showSearchZz">
                <div
                    v-for="(item, index) of searchOrganizationList"
                    :key="item.id"
                    :class="zzIndex == index ? 's-item is-selected ' : 's-item'"
                    :style="{ height: renderPerson ? '48px' : '30px' }"
                    @click="handleSelectedZz(index, item)"
                    @dblclick="selectedZzClick(item)"
                >
                    <div v-if="!renderPerson" class="noRenderP">
                        <div class="item-user">
                            <i
                                :class="firstIcon"
                                :style="{
                                    color: item.isDisabled ? '#ccc' : '#E5E5E5',
                                }"
                            ></i>
                        </div>
                        <div class="item-desc">
                            <div
                                class="i-name"
                                :style="{
                                    color: item.isDisabled ? '#ccc' : '#333',
                                }"
                                v-html="item.personName2 || item[keyName]"
                            ></div>
                        </div>
                    </div>
                    <div v-else class="renderPerson">
                        <div class="item-user">
                            <span
                                v-if="renderPerson && item.imgPath"
                                :style="{
                                    color: item.isDisabled ? '#ccc' : '#E5E5E5',
                                }"
                                ><img
                                    class="tuser-avatar"
                                    :src="url + '/file' + item.imgPath"
                            /></span>
                            <span
                                v-if="renderPerson && !item.imgPath"
                                class="el-icon-aliuser default-avatar"
                                :style="{
                                    color: item.isDisabled ? '#ccc' : '#E5E5E5',
                                }"
                            ></span>
                        </div>
                        <div class="item-desc">
                            <div
                                class="i-name"
                                :style="{
                                    color: item.isDisabled ? '#ccc' : '#333',
                                }"
                                v-html="item.personName2 || item[keyName]"
                            ></div>
                            <div
                                class="i-dept"
                                :style="{
                                    color: item.isDisabled ? '#ccc' : '#999',
                                }"
                                v-html="item.defDeptName2 || item[keyName2]"
                            >
                               
                            </div>
                        </div>
                    </div>

                    <i
                        class="el-icon-alipitchon"
                        v-show="zzIndex == index"
                    ></i>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Api from "@/api/api";
import foldTree from "@/components/fold-tree";
import lodash from 'lodash'
const URL = window.location.origin;
export default {
    components: {
        foldTree,
    },
    props: {
        select: {
            type: Object,
            default: () => {
                return {};
            },
        },
        apiurl: {
            type: String,
            default: () => "",
        },
        params: {
            type: Object,
            default: () => {},
        },
        type: {
            type: String,
            default: () => "",
        },
        firstIcon: {
            type: String,
            default: () => "tree-filebox",
        },
        secondIcon: {
            type: String,
            default: () => "tree-file",
        },
        thirdIcon: {
            type: String,
            default: () => "el-icon-alihead",
        },
        dialogVisible: {
            type: Boolean,
            default: () => false,
        },
        renderType: {
            type: String,
            default: "",
        },
        searchValue: {
            type: String,
            default: "",
        },
        isActived: {
            type: Boolean,
            default: false,
        },
        idsScoped: {
            type: Array,
            default: () => [],
        },
    },
    data() {
        return {
            tabName: "organization",
            organizationList: [],
            organizationValue: "",
            showSearch: false,
            allOrganizationList: [],
            searchOrganizationList: [],
            selectedPerson: {},
            zzIndex: null,
            renderPerson:
                this.renderType == "partyPerson" ||
                this.renderType == "deptPerson",
            url: URL,
            singleLoading: true,
        };
    },
    watch: {
        dialogVisible(val) {
            if (val) {
                this.getOrganizationList();
            }
        },
        organizationValue(val) {
            if (!val) {
                this.showSearch = false;
                if (!this.idsScoped.length) {
                    this.searchOrganizationList = [];
                    this.$emit('update:hasSerach', false);
                }
            }
        },
        selectedPerson(list) {
            this.$emit("selected", { key: [this.type], value: list });
        },
        isActived(val) {
            if (val && this.searchValue.trim()) {
                this.organizationValue = this.searchValue;
                this.filterData();
            } else {
                this.organizationValue = "";
                this.showSearch = false;
            }
        },
    },
    created() {
        this.getOrganizationList();
    },
    mounted(){
        this.$nextTick(() => {
            this.$refs['touchFocus'].focus();
        });
    },
    computed: {
        showSearchZz() {
            return this.showSearch || this.idsScoped.length && !this.singleLoading
        },
        dfCheckedKeys() {
            return this.select && this.select.id
                ? [Number(this.select.id)]
                : [];
        },
        keyName() {
            return this.renderPerson ? "name" : "cname";
        },
        keyName2() {
            return this.renderPerson ? 'orgName' : 'cname';
        },
    },
    methods: {
        selectedZzClick(opt) {
            this.selectedPerson = opt;
            this.$emit("dbSelected", { key: [this.type], value: opt });
        },
        handleSelectedZz(idx, opt) {
            this.zzIndex = idx;
            this.selectedPerson = opt;
        },
        handleClickNode(opt) {
            this.selectedPerson = opt;
        },
        filterData() {
            this.searchOrganizationList = this.allOrganizationList.filter(
                (item) => {
                    item.personName2 = this.transName(
                        this.organizationValue,
                        item[this.keyName]
                    );
                    item.defDeptName2 = this.transName(this.organizationValue, item[this.keyName2]);
                    return (
                        item[this.keyName].indexOf(this.organizationValue) != -1
                    );
                }
            );
            this.showSearch = true;
            this.$emit("update:searchValue", this.organizationValue);
        },
        transName(query, name) {
            if (name == null) return '';
            let reg = new RegExp(query, "g");
            
            name = name.replace(
                query,
                `<label style="color: #fa8c16 !important;">${query}</label>`
            );
            return `<span>${name}<span>`;
        },
        getOrganizationList() {
            let params = Object.assign((this.params || {}), {
                personIdQueryIn: this.idsScoped.length ? this.idsScoped.join() : '',
                isMain: 1
            })
            Api[this.apiurl](params).then((res) => {
                if (res.code == 0) {
                    /**
                     * 判断是否是菜单管理
                     */
                    if (this.apiurl === 'getUcenterobjfunc') {
                        let data = JSON.stringify(res.data).replace(/subFunction/g, 'children');
                        data = data.replace(/name/g, 'cname')
                        res.data = JSON.parse(data);
                    }
                    /**
                     * 判断是否选人
                     */
                    let organizationList = this.renderPerson
                        ? this.$formatTree(
                              res.data,
                              "listPerson",
                              true,
                              this.firstIcon,
                              this.secondIcon,
                              "",
                              true,
                              'children', this.idsScoped
                          )
                        : this.$formatTree(
                              res.data,
                              "children",
                              false,
                              this.firstIcon,
                              this.secondIcon,
                              false, false,
                              'children', this.idsScoped
                          );

                    organizationList.forEach((item) => {
                        item.icon = this.firstIcon;
                    });
                    this.organizationList = organizationList;
                    this.allOrganizationList = !this.renderPerson
                        ? this.$filterOrgAll(organizationList)
                        : this.$filterAll(organizationList);
                    if (this.idsScoped.length) {
                        this.searchOrganizationList = lodash.cloneDeep(this.allOrganizationList)
                    }
                    if (this.select && this.select.id) {
                        const selectedIndex = this.allOrganizationList.findIndex(
                            (item) => item.id == this.select.id
                        );
                        if (selectedIndex > -1) {
                            const selected = this.allOrganizationList[
                                selectedIndex
                            ];
                            this.zzIndex = selectedIndex;
                            this.selectedPerson = selected;
                            this.$emit("selected", {
                                key: [this.type],
                                value: this.selectedPerson,
                            });
                        }
                    }
                    this.formatDisabled();
                }
                setTimeout(() => {this.singleLoading = false}, 300)
            }).catch(() => setTimeout(() => {this.singleLoading = false}, 500));
            
        },
        formatDisabled() {
            setTimeout(() => {
                let disableds = document.getElementsByClassName("is-disabled");
                disableds.forEach((item) => {
                    item.parentNode.style.background = "#fff";
                    item.parentNode.style.color = "#ccc";
                });
            }, 0);
        },
    },
};
</script>

<style lang="scss" scoped>
  @import "@/styles/select-com.scss";
</style>
