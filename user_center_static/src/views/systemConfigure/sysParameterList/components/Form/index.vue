<template>
  <div class="form-component">
    <el-form
      :inline="formInline"
      ref="formCom"
      :label-width="labelWidth"
      :size="size"
      :model="ruleForm"
      :rules="rules"
      :label-position="labelPosition"
    >
      <el-form-item
        v-for="(item, i) in config"
        :key="i"
        :prop="item.prop"
        :class="item.class"
        :label="item.label"
      >
        <el-tooltip
          class="item"
          :popper-class="getPopperClass(item)"
          :manual="!rules[item.prop].warn"
          effect="pink"
          :content="rules[item.prop].warn"
          placement="right"
        >
          <el-input
            v-if="item.type == 'input'"
            :type="item.typeName || 'text'"
            v-model="ruleForm[item.prop]"
            :placeholder="item.placeholder"
            :maxlength="item.maxlength"
            clearable
          />

          <el-input
            v-if="item.type == 'password'"
            type="text"
            v-model="ruleForm[item.prop]"
            :placeholder="item.placeholder"
            clearable
            class="no-autofill-pwd"
            @change="
              (val) => {
                handlePwdChange(val, item.prop);
              }
            "
          />

          <el-select
            clearable
            v-else-if="item.type === 'select'"
            v-model="ruleForm[item.prop]"
            :filterable="item.filterable || false"
            :placeholder="item.placeholder"
          >
            <el-option
              v-for="option in item.children"
              :key="option.value"
              :value="
                item.baseSelectValue
                  ? option[item.baseSelectValue]
                  : option.value
              "
              :label="option.name"
            />
          </el-select>

          <el-radio-group
            v-model="ruleForm[item.prop]"
            v-else-if="item.type === 'radio'"
          >
            <el-radio
              v-for="option in item.children"
              :key="option.value"
              :label="option.value"
            >
              {{ option.name }}
            </el-radio>
          </el-radio-group>

          <CheckboxComponent
            v-model="ruleForm[item.prop]"
            v-else-if="item.type == 'checkbox'"
            :children="item.children"
          />
          <date-picker
            v-else-if="item.type == 'datePicker'"
            v-model="ruleForm[item.prop]"
            type="daterange"
            :value-format="item.format || 'yyyy-MM-dd'"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
            clearable
          ></date-picker>

          <date-picker
            :picker-options="pickerOptions"
            v-else-if="item.type == 'date'"
            v-model="ruleForm[item.prop]"
            clearable
            type="date"
            :value-format="item.format || 'yyyy-MM-dd'"
          >
          </date-picker>

          <select-component
            v-else-if="item.type === 'selectCom'"
            :title="item.title"
            :tabList="item.tabList"
            :isMulti="item.isMulti || false"
            :ids.sync="ruleForm[item.prop]"
            :names.sync="ruleForm[item.prop2]"
            :key="i"
            :enableSelectedAll.sync="item.enableSelectedAll"
          ></select-component>

          <upload-image
            v-model="ruleForm[item.prop]"
            v-else-if="item.type === 'uploadLogo'"
            :actionUrl="item.actionUrl"
            :type="item.uploadType"
          ></upload-image>

          <Upload
            v-model="ruleForm[item.prop]"
            v-else-if="item.type === 'upload'"
            :actionUrl="item.actionUrl"
            :type="item.uploadType"
          />
          <div v-else-if="item.type === 'slot'">
            <slot :name="item.slotName"></slot>
          </div>
        </el-tooltip>
      </el-form-item>
    </el-form>
    <span v-html="tooltipStyle"></span>
    <div class="form-button" v-if="isformBtn">
      <el-button
        :class="item.class"
        :type="item.type"
        v-for="(item, index) in formBtn"
        :key="index"
        @click="handlerClick(item)"
        :btnLoading="item.btnLoading"
        >{{ item.btnText }}</el-button
      >
    </div>
    <slot v-else name="workFlow"></slot>
  </div>
</template>

<script>
import rulesMap from "./rules";
import SelectComponent from "@/components/select-component/index";
import DatePicker from "@/components/date-picker";
import UploadImage from "@/components/upload-single-img";
import Upload from "./Upload";
import { setLocalStorage } from "@/utils/auth";
import CheckboxComponent from "./Checkbox.vue";
export default {
  name: "FormCompoment",
  components: {
    SelectComponent,
    DatePicker,
    UploadImage,
    Upload,
    CheckboxComponent,
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
    labelWidth: {
      type: String,
      default: () => "'50px",
    },
    labelPosition: {
      type: String,
      default: () => "right",
    },
    size: {
      type: String,
      default: () => "small",
    },
  },
  data() {
    return {
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
    config: {
      handler(val) {
        if (!val.length) return;
        val.forEach((item) => {
          if (item.type == "selectCom") {
            this.$set(this.ruleForm, item.prop2, item.names);
          }
          this.$set(this.ruleForm, item.prop, item.value);

          this.setRules(item);
        });
      },
      deep: true,
      immediate: true,
    },
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
    change(value, v) {
      
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
    getForm() {
      return { ...this.ruleForm };
    },
    setRules(item) {
      let rulesMaps = rulesMap(this.rules);
      !item.hasOwnProperty("rules") && (item.rules = {});
      this.$set(this.rules, item.prop, {
        required: item.rules.hasOwnProperty("require")
          ? item.rules.require
          : false,
        message: item.rules.message || `${item.label}不能为空`,
        trigger: item.rules.trigger || ["blur", "change"],
        type: item.rules.type,
        validator:
          item.rules.validator || rulesMaps.get(item.checkType || "other"),
        warn: null,
      });
    },
    handlePwdChange(val, prop) {
      if (prop === "newPwd") {
        setLocalStorage("newpassword", val);
      }
    },
    handlerClick(item) {
      this.$parent[item.handlerType]();
    },
  },
};
</script>

<style lang="scss" scoped>
.form-component {
  /deep/.el-form .el-form-item__label {
    color: #555;
    font-weight: 400;
  }
  /deep/.el-checkbox-group {
    display: inline-block;
  }
  /deep/.uploader-single-img {
    margin: 0;
  }
  /deep/.el-form {
    .el-form-item__label {
      font-size: 12px;
    }
    .el-radio__label {
      font-size: 12px;
    }
    .el-form-item--mini{
      &.el-form-item {
        margin-bottom: 10px;
      }
      .el-form-item__label,.el-form-item__content{
        line-height: 30px;
      }
    }
  }
}
</style>