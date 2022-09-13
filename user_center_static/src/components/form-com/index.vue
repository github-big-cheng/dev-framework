<!--
 * @Author: your name
 * @Date: 2021-05-13 10:04:27
 * @LastEditTime: 2022-03-01 15:52:39
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\components\search-form\index.vue
-->
<template>
    <div>
        <div class="rule-form" :class="columnNum">
            <el-form :inline="formInline" ref="formCom" :model="ruleForm" :rules="rules">
                <template v-for="(item, i) in config">
                    <el-form-item :key="i" :label="item.label" :prop="item.prop" :class="item.class" v-if="item.show !== false">
                        <div v-if="item.type == 'line'" />
                        <div v-else-if="item.type == 'text'" class="form-text">
                            {{ ruleForm[item.prop2] || ruleForm[item.prop] || item.value || "-" }}
                            <span class="unit-text" v-if="item.endText && (ruleForm[item.prop2] || ruleForm[item.prop] || item.value)" :class="item.endText.class">{{ item.endText.text }}</span>
                        </div>
                        <el-tooltip
                            v-else
                            :tabindex="i + 1"
                            class="item"
                            :popper-class="getPopperClass(item)"
                            :manual="!rules[item.prop].warn"
                            effect="pink"
                            :content="rules[item.prop].warn"
                            placement="right"
                        >
                            <div :class="['lineBlock', item.endText ? 'formitem-unit' : '']">
                                <el-input
                                    :ref="item.prop"
                                    v-if="item.type == 'input'"
                                    :type="item.typeName || 'text'"
                                    v-model="ruleForm[item.prop]"
                                    :placeholder="item.placeholder"
                                    :maxlength="item.noMax ? '' : (item.maxlength || 100)"
                                    :clearable="item.hasOwnProperty('clearable') ? item.clearable : true"
                                    v-focus="item.focus"
                                    :disabled="item.disabled"
                                    :show-word-limit="showWordLimit(item)"
                                    @input="input(item)"
                                    @change="
                                        (val) => {
                                            inputChange(val, item.prop, item);
                                        }
                                    "
                                />

                                <el-input
                                    v-if="item.type == 'password'"
                                    type="text"
                                    v-model="ruleForm[item.prop]"
                                    :placeholder="item.placeholder"
                                    clearable
                                    class="no-autofill-pwd"
                                    :disabled="item.disabled"
                                    @change="
                                        (val) => {
                                            handlePwdChange(val, item.prop);
                                        }
                                    "
                                />
                                <YearPicker
                                    v-else-if="item.type === 'yearPicker'"
                                    v-model="ruleForm[item.prop]"
                                    :startYear.sync="item.startYear"
                                    :endYear.sync="item.endYear"
                                />

                                <SelectBese
                                    v-else-if="item.type === 'select'"
                                    v-model="ruleForm[item.prop]"
                                    :select-key="item.selectedKey"
                                    :children="item.children"
                                    :propName.sync="ruleForm[item.prop2]"
                                    :select-props="item.selectProps"
                                    :url="item.url"
                                    :params="item.params"
                                    :search-key="item.searchKey"
                                    :normalizer="item.normalizer"
                                    :disabled="item.disabled"
                                    @change="(value, selectItem) => item.changeCB && item.changeCB(value, selectItem)"
                                />
                                <!-- <el-select
                  clearable
                  v-else-if="item.type === 'select'"
                  v-model="ruleForm[item.prop]"
                  :disabled="item.disabled"
                  :filterable="
                    item.hasOwnProperty('filterable') ? item.filterable : true
                  "
                  @clear="clearSelect(item)"
                  @change="
                    changeSelect(item, ruleForm[item.prop], item.children)
                  "
                  placeholder=""
                >
                  :placeholder="`请选择${item.placeholder || item.label}`"
                  <el-option
                    :disabled="item.disabled"
                    v-for="option in item.children"
                    :key="option.value"
                    :value="
                      item.baseSelectValue
                        ? option[item.baseSelectValue]
                        : option.value
                    "
                    :label="
                      item.baseSelectName
                        ? option[item.baseSelectName]
                        : option.name
                    "
                  />
                </el-select> -->

                                <el-radio-group :disabled="item.disabled" v-model="ruleForm[item.prop]" v-else-if="item.type === 'radio'">
                                    <el-radio
                                        v-for="option in item.children"
                                        :key="option.value"
                                        :label="option.value"
                                        @change="
                                            (val) => {
                                                radioChange(val, item.prop, item);
                                            }
                                        "
                                        >{{ option.name }}</el-radio
                                    >
                                </el-radio-group>

                                <el-checkbox-group
                                    :disabled="item.disabled"
                                    v-model="ruleForm[item.prop]"
                                    v-else-if="item.type == 'checkbox'"
                                >
                                    <el-checkbox v-for="option in item.children" :key="option.value" :label="option.value" name="type">{{
                                        option.name
                                    }}</el-checkbox>
                                </el-checkbox-group>

                                <el-date-picker
                                    :disabled="item.disabled"
                                    v-else-if="item.type == 'datePicker'"
                                    v-model="ruleForm[item.prop]"
                                    type="daterange"
                                    :value-format="item.format || 'yyyy-MM-dd'"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    :picker-options="pickerOptions"
                                    clearable
                                    :unlink-panels="item.unlink ? item.unlink : false"
                                    @change="(date) => dateChange(date, item)"
                                ></el-date-picker>

                                <el-date-picker
                                    :disabled="item.disabled"
                                    :picker-options="pickerOptions"
                                    v-else-if="item.type == 'date'"
                                    v-model="ruleForm[item.prop]"
                                    clearable
                                    :type="item.dateType || 'date'"
                                    :value-format="item.format || 'yyyy-MM-dd'"
                                >
                                </el-date-picker>

                                <el-date-picker
                                    v-else-if="item.type === 'datetime'"
                                    :disabled="item.disabled"
                                    type="datetime"
                                    :picker-options="pickerOptions"
                                    v-model="ruleForm[item.prop]"
                                    clearable
                                    :value-format="item.format || 'yyyy-MM-dd HH:mm:ss'"
                                >
                                </el-date-picker>

                                <el-date-picker
                                    v-else-if="item.type === 'datetimeRange'"
                                    v-model="ruleForm[item.prop]"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    :format="item.format || 'yyyy-MM-dd HH:mm'"
                                    :value-format="item.format || 'yyyy-MM-dd HH:mm'"
                                    :default-time="item.hasOwnProperty('defaultTime') ? item.defaultTime : ['08:00:00', '08:00:00']"
                                    @change="(date) => dateChange(date, item)"
                                >
                                </el-date-picker>

                                <template v-else-if="item.type === 'selectCom'">
                                    <component
                                        :isDisable="item.disabled"
                                        :is="selectCom"
                                        :title="item.title"
                                        :tabList="item.tabList"
                                        :isMulti="item.isMulti || false"
                                        :ids.sync="ruleForm[item.prop]"
                                        :names.sync="ruleForm[item.prop2]"
                                        :key="i"
                                        :params="item.params || {}"
                                        :searchRequestApi="item.searchRequestApi"
                                        :enableSelectedAll.sync="ruleForm[item.prop3]"
                                        @change="(data) => item.changeCB && item.changeCB(data)"
                                        @update="selectComUpdate(item, ruleForm[item.prop])"
                                    ></component>
                                </template>

                                <template v-else-if="item.type === 'treeSelect'">
                                    <component
                                        :is="treeSelect"
                                        @input="treeSelectInput(item.prop)"
                                        v-model="ruleForm[item.prop]"
                                        :options="item.children"
                                        :placeholder="item.placeholder || ''"
                                        :disabled="item.disabled"
                                        noOptionsText="暂无数据"
                                        noResultsText="无相关数据"
                                        :default-expand-level="1"
                                        :normalizer="(node) => normalizer(node, item.normalizer)"
                                    >
                                    </component>
                                </template>

                                <template v-else-if="item.type === 'slot'">
                                    <slot :name="item.slotName" :scope="item" :rule-form="ruleForm"></slot>
                                </template>

                                <template v-if="item.nextCom">
                                    <slot :name="item.slotName"></slot>
                                </template>

                                <el-cascader
                                    v-else-if="item.type == 'cascader'"
                                    :placeholder="item.placeholder || ' '"
                                    v-model="ruleForm[item.prop]"
                                    :options="item.children"
                                    filterable
                                    :props="{ checkStrictly: true }"
                                    clearable
                                ></el-cascader>

                                <UploadFiles
                                    v-else-if="item.type == 'uploadFile'"
                                    v-model="ruleForm[item.prop]"
                                    :up-url="item.url"
                                    :type="item.fileType"
                                    @editFile="(data) => editFile(item, data)"
                                />

                                <el-switch
                                    v-else-if="item.type == 'switch'"
                                    v-model="ruleForm[item.prop]"
                                    @change="(val) => switchChange(val, item)"
                                    :active-value="item.activeValue"
                                    :active-text="item.activeText"
                                    :inactive-value="item.inactiveValue"
                                    :inactive-text="item.inactiveText"
                                ></el-switch>

                                <template v-else-if ="item.type == 'yearQuarter'">
                                    <el-date-picker
                                        :disabled="item.disabled"
                                        :picker-options="pickerOptions"
                                        v-model="ruleForm[item.prop]"
                                        clearable
                                        :type="item.yearType || 'year'"
                                        :value-format="item.format || 'yyyy'"
                                        @change="(value, selectItem) => item.changeCB && item.changeCB(value, selectItem)"
                                    >
                                    </el-date-picker>
                                    <SelectBese
                                        v-model="ruleForm[item.prop2]"
                                        :children="item.children2"
                                        :select-props="item.selectProps2"
                                        :url="item.url2"
                                        :params="item.params2"
                                        :search-key="item.searchKey2"
                                        :normalizer="item.normalizer2"
                                        :disabled="item.disabled"
                                        @change="(value, selectItem) => item.changeCB2 && item.changeCB2(value, selectItem)"
                                    />
                                    <SelectBese
                                        v-if="ruleForm[item.prop2] === item.showValue3"
                                        v-model="ruleForm[item.prop3]"
                                        :children="item.children3"
                                        :url="item.url3"
                                        :params="item.params3"
                                        :normalizer="item.normalizer3"
                                        :disabled="item.disabled"
                                        @change="(value, selectItem) => item.changeCB3 && item.changeCB3(value, selectItem)"
                                    />
                                </template>

                                <span class="unit-text" v-if="item.endText" :class="item.endText.class">{{ item.endText.text }}</span>
                                
                                <tinymce-editor v-if="item.type == 'editor'" v-model="ruleForm[item.prop]"></tinymce-editor>
                            </div>
                        </el-tooltip>
                    </el-form-item>
                </template>
            </el-form>
            <span v-html="tooltipStyle"></span>
        </div>
        <div class="form-button" v-if="isformBtn">
            <el-button
                :class="item.class"
                :type="item.type"
                v-for="(item, index) in formBtn"
                :key="index"
                @click="handlerClick(item, formBtn)"
                :disabled="item.disabled"
                :loading="item.btnLoading"
                >{{ item.btnText }}</el-button
            >
        </div>
        <slot v-else name="workFlow"></slot>
    </div>
</template>

<script>
import rulesMap from "./rules";
import DatePicker from "@/components/date-picker";
import { setLocalStorage } from "@/utils/auth";
export default {
    name: "formCom",
    components: {
        DatePicker,
        SelectBese: () => import('@/components/select-component/base.vue'),
        UploadFiles: () => import('@/components/upload-files'),
        YearPicker: () => import("@/components/date-picker/src/year.vue"),
        TinymceEditor: () => import("@/components/tinymce-editor")
    },

    props: {
        config: {
            type: Array,
            default: () => [],
        },
        allRow: {
            type: Number,
            require: true,
        },
        allCol: {
            type: Array,
            require: true,
        },
        isformBtn: {
            type: Boolean,
            default: () => false,
        },
        formBtn: {
            type: Array,
            default: () => [],
        },
        btnLoading: {
            type: Boolean,
            default: () => false,
        },
        formInline: {
            type: Boolean,
            default: () => true,
        },
        columnNum: {
            type: String,
            default: () => "row-col2",
        },
        formData: {
            type: Object,
            default: () => ({}),
        },
    },
    data() {
        return {
            selectCom: () => import("@/components/select-component"),
            treeSelect: () => import("@riophae/vue-treeselect"),
            tooltip: true,
            ruleForm: {},
            pickerOptions: this.$dateConfig(),
            currentFuzzyTime: "all",
            temDate: [],
            other: {},
            baseConfig: [],
            rules: {},
        };
    },
    watch: {
        temDate: {
            handler(newVal) {
                if (newVal && newVal.length > 0) {
                    this.currentFuzzyTime = null;
                }
            },
            deep: true,
        },
        formData: {
            handler(data) {
                if (data) {
                    this.formDataFilter();
                }
            },
            deep: true,
        },
        config: {
            handler(newVal, val) {
                if (!newVal.length) return;
                const curVal = newVal ? newVal : val;

                curVal.forEach((item) => {
                    if (item.type == "selectCom") {
                        !this.ruleForm.hasOwnProperty(item.prop2) && this.$set(this.ruleForm, item.prop2, item.names);
                        if(item.hasOwnProperty('prop3') && !this.ruleForm.hasOwnProperty(item.prop3)) {
                            this.$set(this.ruleForm, item.prop3, item.isAll);
                        }
                    }
                    if (item.type == 'datetimeRange') {
                        this.$set(this.ruleForm, item.attr[0], item.value[0])
                        this.$set(this.ruleForm, item.attr[1], item.value[1])
                    }
                    !this.ruleForm.hasOwnProperty(item.prop) && this.$set(this.ruleForm, item.prop, item.value);

                    this.setRules(item);
                });
            },
            deep: true,
            immediate: true,
        },
    },
    created() {
        if (this.formData && Object.keys(this.formData).length) {
            this.formDataFilter();
        }
    },
    computed: {
        tooltipStyle() {
            return `<style>
                .el-tooltip__popper {
                    background: #F56C6C;
                    line-height: 1;
                    color: #fff;
                }
                .el-tooltip__popper[x-placement^=right] .popper__arrow::after {
                    border-right-color:#F56C6C;
                }
                .el-tooltip__popper[x-placement^=right] .popper__arrow {
                    border-right-color: #F56C6C;
                }
                .hideTooltip {
                    display: none !important;
                }
                .el-tooltip__popper[x-placement^=left] .popper__arrow {
                    border-left-color: #F56C6C !important;
                }
                .el-tooltip__popper[x-placement^=right], .el-tooltip__popper[x-placement^=left] .popper__arrow::after {   
                    border-left-color: #F56C6C !important;
                }
            </style>`;
        },
    },
    methods: {
        inputChange(val, prop, item) {
            item.changeCB && item.changeCB(val, item);
            this.$emit("getInputChange", val, prop, item);
        },
        radioChange(val, prop, item) {
            item.changeCB && item.changeCB(val, item);
            this.$emit("getRadioChange", val, prop, item);
        },
        switchChange(val, item) {
            item.changeCB && item.changeCB(val)
            this.$emit("getSwitchChange", val);
        },
        treeSelectInput(prop) {
            this.$refs.formCom.validateField(prop);
        },
        formDataFilter() {
            const { formData } = this;
            if (formData.hasOwnProperty("id")) {
                this.ruleForm["id"] = formData.id;
            }
            this.config.forEach((item) => {
                const { prop, attr, show, prop1, prop2, prop3 } = item;
                if (formData.hasOwnProperty(prop) && !item.hasOwnProperty(show) || show === true) {
                    if (prop) {
                        this.ruleForm[prop] = formData[prop];
                    }
                    if (attr && attr.length) {
                        attr.forEach((i) => (this.ruleForm[i] = formData[i]));
                    }
                    if (prop1) {
                        this.ruleForm[prop1] = formData[prop1];
                    }
                    if (prop2) {
                        this.ruleForm[prop2] = formData[prop2];
                    }
                    if (prop3) {
                        this.ruleForm[prop3] = formData[prop3];
                    }
                }
            });
        },
        normalizer(node, normalizer) {
            if (normalizer) {
                return {
                    id: node[normalizer.value] || "id",
                    label: node[normalizer.label] || "name",
                    children: node?.children?.length && node?.children,
                };
            } else {
                return node;
            }
        },
        showWordLimit(item) {
            return item.hasOwnProperty("showWordLimit") ? item.showWordLimit : item.typeName == "textarea" ? true : false;
        },
        input(item) {
            //执行回调函数
            item.hasOwnProperty("changeCB") && !item.hasOwnProperty("isNumber") && item.changeCB(this.ruleForm[item.prop]);
            //去除空格
            item.typeName != "textarea" && (this.ruleForm[item.prop] = this.ruleForm[item.prop].trim());
            //判断数值类型
            if (!item.hasOwnProperty("isNumber")) return;
            //默认带出小数点后三位
            let numberFixed3 = item.hasOwnProperty("numberFixed3") ? this.ruleForm[item.prop].toFixed(3) : this.ruleForm[item.prop];

            let floatPoint = item.hasOwnProperty("floatPoint") ? item.floatPoint : 2;

            let RegExpNum = new RegExp("^\\d+(\\.\\d{0," + floatPoint + "})?$");
            let repalceReg = new RegExp("^\\D*(\\d*(?:\\.\\d{0," + floatPoint + "})?).*$");

            if (!RegExpNum.test(this.ruleForm[item.prop])) {
                this.ruleForm[item.prop] = this.ruleForm[item.prop].replace(repalceReg, "$1");
            }
            //执行回调函数
            item.hasOwnProperty("changeCB") && item.changeCB(this.ruleForm[item.prop]);
        },
        changeSelect(value, item) {
            item.changeCB && item.changeCB(value, item);
        },
        clearSelect(value) {
            value.hasOwnProperty("clearCB") && value.clearCB();
        },
        setFormValue(attr, value) {
            this.ruleForm[attr] = value;
        },
        getPopperClass(item) {
            return this.rules[item.prop].warn == null ? "hideTooltip" : "";
        },
        handleFuzzyTime(items) {
            this.currentFuzzyTime = items.type;
            this.temDate = [];
        },
        editFile(item, file){
            let attachments = this.ruleForm[item.prop] || [];
            attachments?.forEach(attachment => {
                if (attachment.filePath === file.filePath) {
                    attachment.fileName = file.fileName;
                }
            })
        },
        clearFrom() {
            this.$refs.formCom.resetFields();
            Object.keys(this.rules).forEach((item) => {
                this.rules[item].warn = null;
            });
        },
        getClass(value, type) {
            let hasValue = this.ruleForm.hasOwnProperty(value);
            if (!hasValue) {
                this.$set(this.ruleForm, value, "all");
            }
            return this.ruleForm[value] == type;
        },
        getFormAndValidate() {
            return new Promise((resolve) => {
                this.$refs.formCom.validate(async (valid, object) => {
                    await this.checkNumber();
                    if (valid)
                    
                        resolve({
                            status: true,
                            data: { ...this.ruleForm },
                        });
                    else {
                        let warn = Array.from(Object.values(object))[0];
                        this.setFocus(warn[0].field);
                        this.$showWarning(warn[0].message);
                        resolve({
                            status: false,
                            data: warn,
                        });
                    }
                });
            });
        },
        async getForm() {
            await this.checkNumber();
            return { ...this.ruleForm };
        },
        checkNumber() {
            for (let key of Object.keys(this.rules)) {
                let item = this.rules[key];
                if (item.isNumber && this.ruleForm.hasOwnProperty(key)) {
                    typeof this.ruleForm[key] == "string" && this.ruleForm[key] != "" && (this.ruleForm[key] = Number(this.ruleForm[key]));
                }
            }
        },
        setRules(item) {
            let rulesMaps = rulesMap(this.rules);
            !item.hasOwnProperty("rules") && (item.rules = {});
            (!this.rules.hasOwnProperty(item.prop) || item.isSetReuire) &&
                this.$set(this.rules, item.prop, {
                    required: item.rules.hasOwnProperty("require") ? item.rules.require : false,
                    message: item.rules.message || `${item.label}不能为空`,
                    trigger: item.rules.trigger || ["blur", "change"],
                    comType: item.type,
                    type: item.rules.type,
                    isNumber: item.isNumber,
                    validator: item.rules.validator || rulesMaps.get(item.checkType || "other"),
                    warn: null,
                });
        },
        setFocus(prop) {
            let item = this.rules[prop];
            if (!item) return;
            let obj = this.$refs[prop];
            if (item.comType == "input" && obj) {
                let input;
                input = Array.from(obj) ? obj[0] : obj;
                input.focus();
            }
        },
        handlePwdChange(val, prop) {
            if (prop === "newPwd") {
                setLocalStorage("newpassword", val);
            }
        },
        handlerClick(item, formBtn) {
            this.$emit("submit", item, formBtn);
        },
        selectComUpdate(item, value) {
            item.cb && item.cb(item, value);
        },
        //日期范围
        dateChange(date, item) {
            if (!item.attr) return;
            const [start, end] = date;
            this.ruleForm[item.attr[0]] = start;
            this.ruleForm[item.attr[1]] = end;
            item.hasOwnProperty("changeCB") && item.changeCB(start, end);
        },
    },
};
</script>

<style scoped lang="scss">
@import "@/styles/rule-form.scss";
</style>
