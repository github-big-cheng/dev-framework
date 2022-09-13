<!--
 * @Author: your name
 * @Date: 2021-05-13 10:04:27
 * @LastEditTime: 2022-03-10 10:55:46
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\components\search-form\index.vue
-->
<template>
  <div class="search-form">
    <el-form :inline="true" ref="searchForm" :model="formSearch">
      <template v-for="(item, index) in config">
        <el-form-item
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :class="item.class"
          v-if="item.show !== false"
        >
          <!-- :label-width="item.labelWidth ? item.labelWidth : 'auto'" -->
          <el-input
            v-if="item.type == 'input'"
            v-model="formSearch[item.prop]"
            :placeholder="item.placeholder"
            clearable
          />
          <SelectBese
            v-else-if="item.type === 'select'"
            v-model="formSearch[item.prop]"
            :children="item.children"
            v-bind="item"
            @change="(value, selectItem) => item.changeCB && item.changeCB(value, selectItem)"
          />
          <YearPicker
              v-else-if="item.type === 'yearPicker'"
              v-model="formSearch[item.prop]"
              :startYear.sync="item.startYear"
              :value-format="item.format || 'yyyy'"
              :endYear.sync="item.endYear"
          />
          <el-date-picker
            v-else-if="item.type == 'datePicker'"
            v-model="formSearch[item.prop]"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            clearable
            :picker-options="pickerOptions"
            :type="item.dateType || 'daterange'"
            :value-format="item.format || 'yyyy-MM-dd'"
            @change="(data) => dateChange(data, item)"
          ></el-date-picker>

          <el-date-picker
            v-else-if="item.type == 'year'"
            v-model="formSearch[item.prop]"
            type="year"
            :value-format="item.format || 'yyyy'"
            placeholder="">
          </el-date-picker>

          <el-cascader
            v-else-if="item.type == 'cascader'"
            :placeholder="item.placeholder || ' '"
            v-model="formSearch[item.prop]"
            :options="item.children"
            :props="{ checkStrictly: true }"
            filterable
            clearable
          ></el-cascader>

          <component
            v-else-if="item.type === 'selectCom'"
            ref="selectCom"
            :is="selectCom"
            :title="item.title"
            :tabList="item.tabList"
            :ids.sync="formSearch[item.prop]"
            :names.sync="item.names"
            :params="item.params || {}"
            @change="(value) => selectComChange(value, item)"
          ></component>

          <component
            v-else-if="item.type === 'customTime'"
            ref="dateCustomTime"
            :is="customTime"
            :options="item.children"
            :isSelect="item.isSelect"
            :strat.sync="formSearch[item.attr[0]]"
            :end.sync="formSearch[item.attr[1]]"
          ></component>

          <template v-else-if="item.type === 'treeSelect'">
            <component
              :is="treeSelect"
              v-model="formSearch[item.prop]"
              :options="item.children"
              @input="inputTreeSelect(item)"
              :normalizer="(node) => normalizer(node, item.normalizer)"
              :placeholder="item.placeholder || ''"
              :default-expand-level="1"
            >
            </component>
          </template>
          <DateFastSelect
            v-else-if="item.type === 'dateFastSelect' && item.attr"
            :strat.sync="formSearch[item.attr[0]]"
            :end.sync="formSearch[item.attr[1]]"
            :value="item.value"
            :unlink="item.unlink"
          />

          <DateFastSelect
            v-else-if="item.type === 'dateFastSelect'"
            v-model="formSearch[item.prop]"
          />

          <div class="date-screen" v-else-if="item.type === 'fuzzyScope'">
            <span
              v-for="(items, index) in item.children"
              :key="index"
              @click="formSearch[item.prop] = item.hasOwnProperty('baseValue') ? items[item.baseValue] : items.value"
              :class="
                getClass(item.prop, item.hasOwnProperty('baseValue') ? items[item.baseValue] : items.value)
                  ? 'select-sp active'
                  : 'select-sp'
              "
            >
              {{ items[item.baseName] || items.label }}
            </span>
          </div>

          <el-radio-group
            :disabled="item.disabled"
            v-model="formSearch[item.prop]"
            v-else-if="item.type === 'radio'"
            @change="(value) => radioChange(value, item)"
          >
            <el-radio
              v-for="option in item.children"
              :key="option.value"
              :label="option.value"
              >{{ option.name }}</el-radio
            >
          </el-radio-group>
         
         <CheckboxComponent
            v-model="formSearch[item.prop]"
            v-else-if="item.type == 'checkbox'"
            :children="item.children"
            :props="item.props"
          />

          <div v-else-if="item.type === 'slot'">
            <slot :name="item.slotName"></slot>
          </div>
        </el-form-item>
      </template>
    </el-form>
  </div>
</template>

<script>
import CheckboxComponent from "./Checkbox.vue";

export default {
  name: 'searchFormCom',
  components: {
    DateFastSelect: () => import("../date-fast-select/index.vue"),
    SelectBese: () => import(`@/components/select-component/base.vue`),
    YearPicker: () => import("@/components/date-picker/src/year.vue"),
    CheckboxComponent,
  },
  props: {
    searchForm: {
      type: Object,
      default: () => {},
    },
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
    formData:{
      type: Object,
    }
  },
  data() {
    return {
      formSearch: {},
      pickerOptions: this.$dateConfig(),
      other: {},
      selectCom: () => import(`@/components/select-component/index`),
      customTime: () => import(`@/components/custom-time/index`),
      treeSelect: () => import("@riophae/vue-treeselect"),
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
    };
  },
  watch:{
    formData: {
      handler(data) {
        if (data) {
          this.formSearch = data;
        }
      },
      deep: true,
      immediate: true,
    },
  },
  methods: {
    inputTreeSelect(item) {
      this.formSearch[item.prop] == "" && (this.formSearch[item.prop] = null);
    },
    selectComChange(value, { change }) {
      change && change(value);
    },
    radioChange(value, { changeCB }) {
      changeCB && changeCB(value, this.formSearch);
    },
    changeDate(item) {
      let key = item.children[0].prop;

      this.formSearch[key] = null;
    },
    openSearch() {
      this.$refs.searchPopover.visible = true;
    },
    clearFrom() {
      for (let key of Object.keys(this.formSearch)) {
        delete this.formSearch[key]
        // this.formSearch[key] = Array.isArray(this.formSearch[key]) ? [] : "";
      }
      let coms = ["selectCom", "dateCustomTime"];
      coms.some((item) => {
        let selectCom = this.$refs[item];
        if (selectCom) {
          Array.isArray(selectCom)
            ? selectCom.some((items) => items.clear())
            : selectCom.clear();
        }
      });

      
    },
    getClass(value, type) {
      let hasValue = this.formSearch.hasOwnProperty(value);
      if (!hasValue) {
        this.$set(this.formSearch, value, '');
      }
      return this.formSearch[value] == type;
    },
    set(item) {
      this.formSearch[item.prop] = items.type;
    },
    hasFromValue() {
      let hasValue = Object.keys(this.formSearch).some((key) => {
        if (Array.isArray(this.formSearch[key])) {
          return this.formSearch[key].length >= 1;
        }
        return this.formSearch[key] && this.formSearch[key] != "";
      });

      return hasValue;
    },
    getForm() {
      let baseData = JSON.parse(JSON.stringify(this.formSearch));
      this.config.forEach(item => {
        if (item.type == 'datePicker') {
          delete baseData[item.prop]
        }
      })

      return { ...baseData };
    },
    //日期范围
    dateChange(date, item) {
      this.$emit("datePickerChange", date);
      if (!item.attr) return;
      const [start, end] = date;
      this.formSearch[item.attr[0]] = start;
      this.formSearch[item.attr[1]] = end;
    },
  },
};
</script>
<style lang="scss" scoped>
@import "@/styles/search-form.scss";
</style>
