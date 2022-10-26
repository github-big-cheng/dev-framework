<!--
 * @Author: your name
 * @Date: 2021-03-31 17:47:23
 * @LastEditTime: 2021-08-20 18:34:35
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\components\selectionDialog\choice\component\choice.vue
-->
<template>
    <div class="c1">
        <el-input
            v-model.trim="organizationValue"
            clearable
            class="search-ipt"
            placeholder="请输入关键字搜索"
            ref="touchFocus"
            @input="filterData"
        >
            <el-button slot="append" icon="el-icon-alisearch"></el-button>
        </el-input>
        <div
            class="dv-organization"
            v-loading="choiceLoading"
            element-loading-text="努力加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(255, 255, 255, 1)"
        >
            <fold-tree
                :style="{ visibility: choiceLoading ? 'hidden' : '' }"
                :showCheckbox="true"
                :treeList="organizationList"
                :dfCheckedKeys="dfCheckedKeysZz"
                label="cname"
                labelTwo="name"
                ref="zzTree"
                :strictly="!renderPerson"
                v-show="!showSearchZz"
                @getChecked="handleGetChecked"
                @dbSelected="selectedZzClick"
            />
            <div class="s-list" v-show="showSearchZz">
                <div
                    v-for="(item, index) of searchOrganizationList"
                    :key="item.id"
                    :style="{ height: renderPerson ? '48px' : '30px' }"
                    :class="item.isZzSelected ? 's-item is-selected' : 's-item'"
                    @click="handleTreeSelectedZz(item, index)"
                    @dblclick="handleDbTreeSelected(item, index)"
                >
                    <!-- <div class="item-user">
                        <span v-if="item.imgPath"
                            ><img
                                class="tuser-avatar"
                                :src="URL + '/file' + item.imgPath"
                        /></span>
                        <span
                            v-if="!item.imgPath"
                            class="el-icon-aliuser default-avatar"
                        ></span>
                    </div>
                    <div class="item-desc">
                        <div
                            class="i-name"
                            :style="{
                                color: item.isDisabled ? '#ccc' : '#333',
                            }"
                            v-html="item.personName2"
                        >
                        </div>
                        <div
                            class="i-dept"
                            :style="{
                                color: item.isDisabled ? '#ccc' : '#999',
                            }"
                            v-html="item.defDeptName2"
                        >
                        </div>
                    </div> -->
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
                                    :src="URL + '/file' + item.imgPath"
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
                                v-html="item.personName2 ||  item[keyName]"
                            ></div>
                            <div
                                class="i-dept"
                                :style="{
                                    color: item.isDisabled ? '#ccc' : '#999',
                                }"
                                v-html="item.defDeptName2 || item[keyName2]"
                            ></div>
                        </div>
                    </div>
                    <i
                        class="el-icon-alipitchon"
                        v-show="item.isZzSelected"
                    ></i>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Api from "@/api/api";
import foldTree from "@/components/fold-tree";
import lodash from "lodash";
const URL = window.location.origin;
export default {
    components: {
        foldTree,
    },
    props: {
        dfSelected: {
            default: () => new Map(),
        },
        controlData: {
            default: () => new Map(),
        },
        workingArea: {
            type: Map,
            default: () => new Map(),
        },
        dialogVisible: {
            type: Boolean,
            default: false,
        },
        params: {
            type: Object,
            default: () => {},
        },
        tabName: {
            type: String,
            default: "",
        },
        apiurl: {
            type: String,
            default: "",
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
        isRenderTree: {
            type: Boolean,
            default: false,
        },
        hasSerach: {
            type: Boolean,
            default: false,
        },
        showSearchArea: {
            type: Boolean,
            default: false,
        },
        type: {
            type: Number,
            default: 0,
        },
        searchValue: {
            type: String,
            default: "",
        },
        isActived: {
            type: Boolean,
            default: false,
        },
        renderType: {
            type: String,
            default: "",
        },
        idsScoped: {
            type: Array,
            default: () => [],
        },
    },
    data() {
        return {
            organizationValue: "",
            showSearch: false,
            organizationList: [],
            dfCheckedKeysZz: [],
            searchOrganizationList: [],
            loading: false,
            allOrganizationList: new Map(),
            selectTreeIds: [],
            URL,
            choiceLoading: true,
            renderPerson: this.renderType == "deptPerson",
        };
    },
    watch: {
        isActived(val) {
            if (val && this.searchValue.trim()) {
                this.organizationValue = this.searchValue;
                this.filterData();
            } else {
                this.organizationValue = "";
                this.showSearch = false;
            }
        },
        organizationValue(val) {
            if (!val) {
                this.showSearch = false;
                if (!this.idsScoped.length) {
                    this.searchOrganizationList = [];
                    this.$emit("update:hasSerach", false);
                }
            }
        },
        dialogVisible: {
            handler(val) {
                this.selectedOrganization = 0;
                if (val) {
                    let ids = [];
                    for (let key of this.dfSelected) ids.push(key);
                    this.getOrganizationList(ids.join());
                }
            },
            immediate: true,
        },
        workingArea: {
            handler(map) {},
            deep: true,
        },
        isRenderTree(val) {
            if (!val) return;
            let ids = [];

            let mirrorMap = this.workingArea;

            for (let key of mirrorMap.keys()) {
                ids.push(key + "");
            }
            this.mountedTreeSelected(ids);
            this.dfCheckedKeysZz = ids;

            this.$emit("update:isRenderTree", false);
        },
    },
    mounted() {
        this.$nextTick(() => {
            this.$refs["touchFocus"].focus();
        });
    },
    computed: {
        showSearchZz() {
            return (
                this.showSearch ||
                (this.idsScoped.length && !this.choiceLoading)
            );
        },
        keyName() {
            return this.renderPerson ? "name" : "cname";
        },
        keyName2() {
            return this.renderPerson ? "orgName" : "cname";
        },
    },
    methods: {
        filterData() {
            this.throttle(this.filterSearch, 300)();
        },
        filterSearch() {
            let map = new Map();
            let query = this.organizationValue;
            for (let [key, value] of this.allOrganizationList) {
                if (
                    value[this.keyName].indexOf(query) != -1 ||
                    value[this.keyName2].indexOf(query) != -1
                ) {
                    value.isZzSelected = false;
                    this.controlData.get(key) && (value.isZzSelected = true);
                    value.personName2 = this.transName(
                        query,
                        value[this.keyName]
                    );
                    value.defDeptName2 = this.transName(
                        query,
                        value[this.keyName2]
                    );
                    map.set(key, lodash.cloneDeep(value));
                }
            }
            this.searchOrganizationList = Array.from(map.values());
            this.showSearch = true;
            this.$emit("update:searchValue", this.organizationValue);
        },

        /**
         * tree勾选事件回调
         */
        handleGetChecked({ menuIds }) {
            this.selectTreeIds = menuIds.split(",");
            let workingArea = new Map();

            this.selectTreeIds.forEach((item) => {
                let items = this.allOrganizationList.get(Number(item));
                items && workingArea.set(Number(item), items);
            });
            this.$emit("setWorking", workingArea, this.allOrganizationList);
        },

        /**
         * tree双击事件回调
         */
        selectedZzClick(data) {
            let persons = this.setDbclick(data);
            /**
             * 合并勾选缓存区和当前选中区
             */
            let workingArea = lodash.cloneDeep(this.workingArea);
            if (persons.size) {
                for (let [key, value] of persons) {
                    workingArea.set(key, value);
                }
            }

            for (let key of this.workingArea.keys()) {
                let items = this.allOrganizationList.get(Number(key));

                if (items && items[this.keyName]) {
                    items.isZzSelected = false;
                }
            }
            this.changeSelected(workingArea);
        },

        /**
         * @param ids id集合
         * 获取数据列表
         */
        getOrganizationList(ids) {
            let params = Object.assign(this.params || {}, {
                personIdQueryIn: this.idsScoped.length
                    ? this.idsScoped.join()
                    : "",
                isMain: 1,
            });
            Api[this.apiurl](params)
                .then((res) => {
                    if (res.code != 0) {
                        this.$showWarning("获取数据失败");
                        return;
                    }

                    let list = res.data;
                    this.totalNum =
                        list && list.length > 0 ? list[0].totalNumber : 0;

                    let organizationList = this.renderPerson
                        ? this.$formatTree(
                              res.data,
                              "listPerson",
                              true,
                              this.firstIcon,
                              this.secondIcon,
                              "",
                              true,
                              true,
                              "listPerson",
                              this.idsScoped
                          )
                        : this.$formatTree(
                              res.data,
                              "children",
                              false,
                              this.firstIcon,
                              this.secondIcon,
                              '',
                              false,
                              false,
                              "children",
                              this.idsScoped
                          );

                    this.organizationList = organizationList;
                    let allOrganizationList = !this.renderPerson
                        ? this.$filterOrgAll(organizationList)
                        : this.$filterAll(organizationList);

                    let obj = [];
                    allOrganizationList.forEach((item) => {
                        this.allOrganizationList.set(item.id, item);
                        obj.push(item);
                    });
                    if (this.idsScoped.length) {
                        this.searchOrganizationList =
                            lodash.cloneDeep(allOrganizationList);
                    }
                    this.$emit("chekcIds", {
                        type: this.type,
                        obj,
                    });
                    setTimeout(() => {
                        this.choiceLoading = false;
                    }, 300);
                })
                .catch(() =>
                    setTimeout(() => {
                        this.choiceLoading = false;
                    }, 300)
                );
        },

        /**
         * 工具函数 改变勾选状态
         */
        changeSelected(map) {
            this.$emit("setWorking", map, this.allOrganizationList);
            this.$emit("selected", map, "add");
        },

        /**
         * 工具函数
         */

        mountedTreeSelected(ids) {
            this.$refs.zzTree.setCheckedNodes(ids);
        },
        handleTreeSelectedZz(item, index) {
            let isZzSelected = this.searchOrganizationList[index].isZzSelected;
            this.$set(
                this.searchOrganizationList[index],
                "isZzSelected",
                !isZzSelected
            );

            let workingArea = lodash.cloneDeep(this.workingArea);
            if (workingArea.get(item.id)) return;
            workingArea.set(item.id, lodash.cloneDeep(item));
            this.$emit("setWorking", workingArea, this.allOrganizationList);

            this.searchOrganizationList.some((item) => item.isZzSelected) &&
                this.$emit("update:hasSerach", true);
        },
        handleDbTreeSelected(item, index) {
            let workingArea = lodash.cloneDeep(this.workingArea);

            workingArea.set(item.id, lodash.cloneDeep(item));
            for (let key of this.workingArea.keys()) {
                let items = this.allOrganizationList.get(Number(key));

                if (items && items[this.keyName]) {
                    items.isZzSelected = false;
                }
            }
            this.$set(this.searchOrganizationList[index], "isZzSelected", true);
            this.$emit("setWorking", workingArea, this.allOrganizationList);
            this.$emit("selected", workingArea, "add");
        },
        /**
         * 防抖
         */
        throttle(fn, delay) {
            // last为上一次触发回调的时间, timer是定时器
            let last = 0,
                timer = null;
            // 将throttle处理结果当作函数返回

            return function () {
                // 保留调用时的this上下文
                let context = this;
                // 保留调用时传入的参数
                let args = arguments;
                // 记录本次触发回调的时间
                let now = +new Date();

                // 判断上次触发的时间和本次触发的时间差是否小于时间间隔的阈值
                if (now - last < delay) {
                    // 如果时间间隔小于我们设定的时间间隔阈值，则为本次触发操作设立一个新的定时器
                    clearTimeout(timer);
                    timer = setTimeout(function () {
                        last = now;
                        fn.apply(context, args);
                    }, delay);
                } else {
                    // 如果时间间隔超出了我们设定的时间间隔阈值，那就不等了，无论如何要反馈给用户一次响应
                    last = now;
                    fn.apply(context, args);
                }
            };
        },

        /**
         * 处理双击事件返回的数据扁平化
         */
        setDbclick(data) {
            let persons = new Map();

            /**
             * 判断控件类型
             */
            if (!this.renderPerson) {
                persons.set(data.id, data);
                return persons;
            }

            if (!data.hasOwnProperty("children")) {
                persons.set(data.id, data);
                return persons;
            }
            check(data);

            function check(data) {
                if (!data.hasOwnProperty("children")) {
                    persons.set(data.id, data);
                    return persons;
                }

                data.listPerson.forEach((item) => persons.set(item.id, item));

                data.children.length &&
                    data.children.forEach((item) => {
                        check(item);
                    });
            }

            return persons;
        },

        transArray(map) {
            if (!map.size) return [];

            let arr = [];
            for (let value of map.values()) {
                arr.push(value);
            }
            return arr;
        },
        transMap(array) {
            let map = new Map();
            if (!array.length) {
                return map;
            }

            array.forEach((item) => map.set(item.id, item));

            return map;
        },
        transName(query, name) {
            let reg = new RegExp(query, "g");
            name = name.replace(
                query,
                `<label style="color: #fa8c16 !important;">${query}</label>`
            );
            return `<span>${name}<span>`;
        },
    },
};
</script>

<style lang="scss" scoped>
@import "@/styles/select-com.scss";
</style>
