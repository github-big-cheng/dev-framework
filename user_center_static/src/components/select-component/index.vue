<!--
 * @Author: your name
 * @Date: 2021-03-05 17:54:59
 * @LastEditTime: 2021-08-26 10:52:07
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\components\selectPerson\index.vue
-->
<!--
 * @Author: your name
 * @Date: 2021-03-24 17:16:32
 * @LastEditTime: 2021-03-26 23:56:05
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\components\selectPerson\index2.vue
-->
<template>
    <div :class="selectPersonStyle">
        <el-select
            v-model="value"
            :filterable="!checkAll"
            reserve-keyword
            remote
            :disabled="isDisable"
            :multiple="isMulti"
            placeholder=""
            :remote-method="remoteMethod"
            @focus="focus"
            @change="change"
            @clear="clear"
            :clearable="isMulti ? false : true"
            :key="spKey"
            :loading="loading"
            size="small"
        >
            <el-option
                v-for="item in options"
                :key="item.id"
                :label="item[keyLike1]"
                :value="item.id"
            >
                <span
                    style="float: left; font-size: 13px"
                    v-html="item.personName2 || item[keyLike1]"
                ></span>
                <span
                    style="
                        margin-left: 10px;
                        color: #8492a6;
                        font-size: 13px;
                    "
                    >{{ item[keyLike2] }}</span
                >
            </el-option>
        </el-select>
        <div class="right-box" >
            <i
                v-if="enableSelectedAll !== undefined && enableSelectedAll !== ''"
                :style="{
                    color: '#ccc',
                    'font-size': '14px',
                    'margin-right': ' 5px',
                    cursor: checkAll ? 'not-allowed' : 'pointer',
                }"
                :class="
                    renderPerson
                        ? 'el-icon-alimanypeople'
                        : 'tree-org'
                "
                @click.stop="selectAll"
            ></i>
            <i
                class="el-icon-alisearch"
                :style="{ cursor: checkAll ? 'not-allowed' : 'pointer' }"
                @click.stop="showPersonChoice"
            ></i>
        </div>

        <component
            v-if="showChoice"
            :is="choiceCom"
            :title="title"
            :tabList="tabList"
            :defaultIds="defaultIds"
            :showComponent="showChoice"
            :isDisable="isDisable"
            :idsScoped="idsScoped"
            :params="params"
            @trueClick="handleChoiceClick"
            @cancelClick="showChoice = false"
        >
        </component>
    </div>
</template>

<script>
import lodash from "lodash";

export default {
    name: 'selectComponent',
    props: {
        ids: {
            default: "",
        },
        highlight: {
            type: String,
            default: "#FA8C16", //输入框匹配高亮字体颜色
        },
        title: {
            type: String,
            default: "选择人员",
        },
        isMulti: {
            type: Boolean,
            default: false, //是否多选模式
        },
        isDisable: {
            type: Boolean,
            default: false, //是否不可选
        },
        names: {
            type: String,
            default: "",
        },
        tabList: {
            type: Array,
            default: () => {
                return ["deptPerson", "partyPerson"];
            },
        },
        enableSelectedAll: {
            default: undefined,
        },
        idsScoped: {
            type: Array,
            default: () => []
        },
        params: {
            type: Object,
            default: () => {}
        },
        searchRequestApi: {
            type: Object,
            default: () => {
                return {
                    apiUrl: '',
                    params: {},
                }
            }
        }
    },
    data() {
        return {
            choiceCom: () =>
                import(
                    `@/components/select-component/${
                        this.isMulti ? "choice" : "single"
                    }`
                ),
            options: [],
            spKey: 0,
            value: this.isMulti ? [] : '',
            list: [],
            loading: false,
            curIds: [],
            delIndex: null,
            checkAll: false,
            showChoice: false,
            keyLike1: null,
            keyLike2: null,
            keyName1: null,
            keyName2: null,
            baseUrl: null,
            defaultData: [],
            activeTab: "",
            defaultIds: "",
            renderPerson: true,
            typeConfig: null,

        };
    },
    watch: {
        isMulti: {
            handler(val) {
                this.choiceCom = () =>
                import(
                    `@/components/select-component/${val ? "choice" : "single"}`
                );
                this.clear();
                ++this.spKey;
                this.$emit("update:ids", "");
                this.$emit("update:names", "");
            },
            immediate: false
        },
        enableSelectedAll: {
            handler(val) {
                if (val == 1) {
                    let num = this.ids.split(",").length;
                    this.value = [`所有人(${num})`];
                    this.checkAll = true;
                }
            },
            immediate: true
        },
        ids: {
            handler(val, oldValue) {

                if (val == null) { // 不能使用 !value
                    return;
                }

                this.init();

                let mirror = Array.isArray(this.value)
                    ? this.value.join()
                    : this.value;
                if (this.isMulti ? !val.length : !val) return;
                let checkVal = val == null ? "" : val;
                if (checkVal == mirror || this.enableSelectedAll == "1") return;
                typeof val != "string" && (val = val + "");
                let list, names;

                list = val ? val.split(",") : val.split("");
                names = this.names
                    ? this.names.split(",")
                    : this.names.split("");

                this.defaultData = [];
                list.forEach((item, i) => {
                    let data = this.defaultData.filter(
                        (items) => items.id == item
                    );
                    !data.length &&
                        this.defaultData.push({
                            id: Number(item),
                            [this.keyLike1]: names[i],
                        });
                });
                this.options = lodash.cloneDeep(this.defaultData);
                let ids = [];
                if (this.options.length) {
                    this.options.forEach((item) => ids.push(item.id));
                }
                this.value = this.isMulti
                    ? ids
                    : ids.join()
                    ? Number(ids.join())
                    : "";
                this.setValue();
            },
            immediate: true,
        },
    },
    computed:{
        selectPersonStyle(){
            return this.isMulti ? 'select-person _multiple' : 'select-person'
        }
    },
    methods: {
        init() {
            if (this.baseUrl) return;

            function setCommonAttr(_this) {
                _this.baseUrl = "getUcenterOrgCombox";
                _this.keyLike1 = "cname";
                _this.keyLike2 = "linker";
                _this.keyName = "cNameOrCodeOrSnameOrLinkerQueryLike";

                _this.renderPerson = false;
            }

            function setCommonAttr2(_this) {
                _this.baseUrl = "getUcenterOrgListOrPerson";
                _this.keyName = "nameQueryLike";
                _this.keyLike1 = "name";
                _this.keyLike2 = "orgName";
            }

            function setCommonAttr3(_this) {
                _this.baseUrl = "partyOrgCombox";
                _this.keyLike1 = "cname";
                _this.keyLike2 = "linker";
                _this.keyName = "nameQueryLike";

                _this.renderPerson = false;
            }
            switch (this.tabList[0]) {
                case "deptPerson":
                    setCommonAttr2(this);
                    break;
                case "party":
                    setCommonAttr3(this);
                    break;
                default:
                    setCommonAttr(this);
                    break;
            }
        },
        getConfig(data) {
            this.tabConfig = lodash.cloneDeep(data);
        },
        clear() {
            this.value = this.isMulti ? [] : "";
            this.options = [];
            this.setValue();
        },
        setValue() {
            let ids = Array.isArray(this.value)
                ? this.value.join()
                : this.value + "";
            let names = [];
            this.defaultData.forEach((item) => {
                this.options.forEach((items) => {
                    if (item.id == items.id) {
                        item[this.keyLike1] = items[this.keyLike1];
                    }
                });
            });
            if (Array.isArray(this.value)) {
                this.value.forEach((item) => {
                    let data = this.defaultData.filter(
                        (items) => item == items.id
                    );
                    data.length && names.push(data[0][this.keyLike1]);
                });
                //  this.defaultData.forEach(item => names.push(item[this.keyLike1]))
                names = names.join();
            } else {
                let data = this.defaultData.filter(
                    (item) => item.id == Number(this.value)
                );
                names = data.length ? data[0][this.keyLike1] : "";
            }
            this.defaultIds = ids;
            this.$emit("update:ids", ids);
            this.$emit("update:names", names);
            this.$emit("change",  this.defaultData);
        },
        async selectAll() {
            if (this.checkAll || this.isDisable) return;
            this.checkAll = !this.checkAll;
            let params = {
                pageNo: 1,
                pageSize: 100000,
                ...this.typeConfig,
            };
            if (this.idsScoped.length) {
                params.personIdQueryIn = this.idsScoped.join()
            }
            let res = await this.getPerson(params);
            if (!res.code == 0) {
                this.checkAll = false;
                return;
            }
            let value = [],
                names = [];
            if (this.checkAll) {
                const data = res.data.list || res.data;
                data.forEach((item) => {
                    value.push(item.id);
                    names.push(item[this.keyLike1]);
                });
                this.value = ["所有人"];
                this.$emit("update:enableSelectedAll", "1");
                this.$emit("update:ids", value.join());
                this.$emit("update:names", names.join());

                // this.options = res.data.list;
                // this.value = value;
                // this.setValue();
                // this.defaultData = lodash.cloneDeep(res.data.list);
            } else {
                this.options = [];
                this.value = value;
                this.setValue();
                this.defaultData = [];
            }
        },
        handleChoiceClick(list) {
            this.showChoice = false;
            this.options = this.isMulti
                ? list
                : Array.isArray(list)
                ? list
                : [list];
            this.defaultData = this.isMulti
                ? lodash.cloneDeep(list)
                : Array.isArray(list)
                ? list
                : [list];
            let ids = [];
            if (!this.options.length) {
                this.value = this.isMulti ? [] : "";
                this.setValue();
                return;
            }

            this.options.forEach((item) => ids.push(item.id));
            this.value = this.isMulti ? ids : ids[0];
            this.setValue();
        },
        focus(e) {
            if (e && !e.target.value) {
                this.options = [];
            }
        },
        checkName(name) {
            return `<span><label style="color: ${this.highlight}">哈哈</label>哈<span>`;
        },
        showPersonChoice() {
            if (this.checkAll || this.isDisable) return;
            let id = [];
            !this.isMulti ? id.push(this.value) : (id = this.value);
            this.defaultIds = id.join();
            this.showChoice = true;
        },
        async remoteMethod(query) {
            if (query !== "") {
                this.loading = true;
                let params = {
                    pageNo: 1,
                    pageSize: 20, // 人数不宜太多
                    [this.keyName]: query,
                    ...this.typeConfig,
                    ...this.params
                };
                if (this.idsScoped.length) {
                    params.personIdQueryIn = this.idsScoped.join()
                }
                // Object.assign(params, this.tabConfig[this.activeTab].params);
                let res = await this.getPerson(params);
                let data = res.data.list || res.data;
                data = this.filterIdsScoped(data);
                this.loading = false;
                if (res.code == 0) {
                    data.forEach((items) => {
                        
                        let name = items[this.keyLike1];
                        
                        let reg = new RegExp(query, "g");
                        name = name.replace(
                            reg,
                            `<label style="color: ${this.highlight} !important;font-weight: initial;">${query}</label>`
                        );
                        items.personName2 = `<span>${name}<span>`;
                    });
                    this.options = data;
                }
            } else {
                this.options = [];
            }
        },
        filterIdsScoped(data) {
            
            if (!this.idsScoped.length) return data;
            let baseData = lodash.cloneDeep(data)
            baseData = baseData.filter(item => {
                return this.idsScoped.some(items => items == item.id)
            })
            return baseData;
        },
        change(item) {
            /**
             * 是否在全选状态
             */
            
            if (this.checkAll && !this.value.length) {
                this.checkAll = false;
                this.$emit("update:enableSelectedAll", "0");
                this.$emit("update:ids", "");
                this.$emit("update:names", "");
                return;
            }
            if (!Array.isArray(item)) {
                this.defaultData = this.options.filter(
                    (items) => items.id == item
                );
              
            } else {
                this.options.forEach((items) => {
                    this.value.includes(items.id) &&
                        !this.defaultData.some((item) => items.id == item.id) &&
                        this.defaultData.push(items);
                });
            }
            this.setValue();
        },
        getPerson(params = {}) {
            return new Promise((resolve, reject) => {
                Object.assign(params, this.searchRequestApi?.params || {})

                this.$http[this.searchRequestApi?.apiUrl || this.baseUrl](params)
                    .then((res) => {
                        resolve(res);
                    })
                    .catch((err) => {
                        reject(err);
                    });
            });
        },
    },
};
</script>

<style lang="scss" scoped>
    @import "@/styles/select-com.scss";
</style>
