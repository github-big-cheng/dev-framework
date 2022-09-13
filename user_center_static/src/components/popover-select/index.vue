<template>
  <el-popover
    :popper-class="popperClass"
    placement="bottom-start"
    :disabled="false"
    trigger="click"
    @show="popoverShow"
    v-model="popoverState"
  >
    <h3
      slot="reference"
      v-loading="loading"
      :class="['reference', deepColor && 'reference-deep-color']"
    >
      <svg-icon class="enterprise-icon" :iconClass="iconClass" />
      {{
        confirmChecked.length >= options.length
          ? "全部" + title
          : hashLabel[confirmChecked[0]]
      }}
      <span v-if="isMultiple">( {{ confirmChecked.length }} )</span>

      <span v-if="!isDefaultTitle">{{ defaultTitle }}</span>

      <el-icon class="el-icon-caret-bottom"></el-icon>
    </h3>
    <div v-loading="loading" class="popover-select-box-main">
      <div class="main-header">
        <el-checkbox
          :disabled="!isMultiple"
          class="all-checkbox"
          :indeterminate="isIndeterminate"
          v-model="checkAll"
          @change="handleCheckAllChange"
        >
          全选
        </el-checkbox>
        <el-input
          v-if="isSearch"
          class="search-box"
          clearable
          placeholder="请输入仓库信息"
          @keyup.enter.native="handlerWarehouseName"
          @clear="clearWarehouseName"
          v-model="warehouseName"
        >
          <span @click="handlerWarehouseName" class="slot-search" slot="suffix">
            <i class="el-icon-search"></i>
          </span>
        </el-input>
      </div>

      <div
        v-for="({ list, adminisCodeName, compName }, index) in listOptions"
        :key="index"
        class="checkbox-item-box"
      >
        <div class="checkbox-name">{{ adminisCodeName || compName }}</div>
        <el-checkbox-group v-model="checked">
          <el-checkbox
            v-for="item in list"
            size="mini"
            border
            :label="item.id"
            :key="item.id"
             @change="handleCheckedChange(item)"
          >
            {{ item.cname || item.name }}
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </div>
    <footer class="popover-select-box-bottom">
      <el-button type="primary" size="mini" @click="onSave">确定</el-button>
      <el-button size="mini" @click="popoverState = false">取消</el-button>
    </footer>
  </el-popover>
</template> 

<script>
import lodash from "lodash";
export default {
  name: "personPassRoundCom",
  props: {
    list: {
      type: Array,
      default: () => [],
    },
    requestUrl: {
      type: String,
      required: false,
    },
    isSearch: {
      type: Boolean,
      required: false,
    },
    value: {
      type: Array,
      default: () => [],
    },
    deepColor: {
      type: Boolean,
      default: () => false,
    },
    isMultiple: {
      type: Boolean,
      default: true,
    },
    isDefaultTitle: {
      type: Boolean,
      default: true,
    },
    isRequest: {
      type: Boolean,
      default: true,
    },
    title: {
      type: String,
      default: () => "",
    },
    defaultId: {
      default: null,
    },
    default: {
      type: Array,
    },
    iconClass: {
      type: String,
      default: () => "enterprise",
    }
  },
  data() {
    return {
      warehouseName: "",
      isIndeterminate: false,
      checkAll: false,
      popoverState: false,
      loading: false,
      checked: [],
      options: [],
      listOptions: [],
      hashLabel: {},
      confirmChecked: [],
      defaultTitle: "",
      popperClass: "popover-select-box",
      baseListOptions: [],
      selectItem: null,
    };
  },
  created() {
    this.popperClass = this.popperClass + (this.deepColor ? " deep-color" : "");
  },
  watch: {
    list() {
      this.listOptions = this.list;
      this.listInit();
    },
    checked: {
      handler(newVal, oldVal) {
        if (this.isMultiple) return;

        if (newVal.length == 0 && oldVal.length) {
          this.checked = oldVal;
        } else if (newVal.length > 1) {
          this.checked = [newVal[1]];
        }
      },
      deep: true,
    },
    defaultId(val) {
      if (val == null) return;
      this.requestPartionCompany({ companyIdQueryIn: val });
    },
    value(val) {
      this.checked = val || [];
      this.confirmChecked = val;
    },
  },
  mounted() {
    this.checked = this.value || [];

    if (!this.requestUrl) {
      this.listOptions = this.list;
      this.listInit();
    }

    this.isRequest && this.requestPartionCompany();
  },
  methods: {
    handlerWarehouseName() {},
    clearWarehouseName() {},
    listInit() {
      const list =
        this.listOptions?.reduce((a, b) => {
          return [
            ...a,
            ...b.list.map((j, index) => {
              this.hashLabel[j.id] = j.cname || j.name;
              if (index == 0) {
                this.selectItem = j;
              }
              return j.id;
            }),
          ];
        }, []) || this.listOptions;
      this.options = list;
      let checked = this.isMultiple ? list : [list[0]];
      this.baseListOptions = lodash.cloneDeep(this.listOptions);
      if (this?.default?.length) {
        this.checked = this.default;
        this.confirmChecked = this.default;
      } else {
        this.confirmChecked = checked;
        this.$emit("input", checked);
        this.$emit("checkedList", checked, this.selectItem);
        this.isDefaultTitle && (this.defaultTitle = this.hashLabel[checked[0]]);
      }
      this.checkAll = this.isMultiple ? true : false;
    },

    async requestPartionCompany(parmas = {}) {
      if (this.requestUrl) {
        try {
          this.loading = true;
          const { data } = await this.$http[this.requestUrl](parmas);
          this.listOptions = data;
          this.listInit();
        } catch (err) {
          console.error(err);
        }
        this.loading = false;
      }
    },
    popoverShow() {
      this.checked = this.confirmChecked;
      let checkedCount = this.confirmChecked.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.options.length;
    },
    handleCheckAllChange(val) {
      this.checked = val ? this.options : [];
      this.isIndeterminate = false;
    },
    handleCheckedChange(val) {
      this.selectItem = val;
      if (!this.isMultiple) return;
      
      let checkedCount = this.checked.length;
      this.checkAll = checkedCount === this.options.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.options.length;
    },

    onSave() {
      this.confirmChecked = this.checked;
      this.$emit("input", this.checked);
      this.$emit("save", this.checked, this.selectItem);
      this.popoverState = false;
    },
  },
};
</script>

<style lang="scss" lang="scss">
.deep-color.el-popover.popover-select-box {
  background-color: #04152f;
  .checkbox-name,
  .all-checkbox .el-checkbox__label,
  .el-checkbox__label {
    color: #fff;
  }
  .el-checkbox__input.is-checked + .el-checkbox__label {
    color: #409eff;
  }
  .popper__arrow {
    border-bottom-color: #409eff;
  }
  .popper__arrow::after {
    border-bottom-color: #04152f;
  }
}
.el-popover.popover-select-box {
  max-height: 700px;
  min-height: 200px;
  background-color: #fff;
  height: 50%;
  width: 500px;
  border: 1px solid #409eff;
  font-family: MicrosoftYaHei;
  .popper__arrow {
    border-bottom-color: #409eff;
  }
  .checkbox-name,
  .all-checkbox .el-checkbox__label {
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #333333;
    margin-bottom: 7px;
  }
  .checkbox-item-box {
    margin-bottom: 15px;
    .el-checkbox__inner {
      display: none;
    }
    .el-checkbox__input {
      width: 100%;
      position: absolute;
    }
    .el-checkbox.is-bordered + .el-checkbox.is-bordered {
      margin-right: 10px;
      margin-left: 0;
    }
    .el-checkbox.is-bordered {
      overflow: hidden;
      padding: 3px 15px 3px 3px;
      margin-bottom: 5px;
      position: relative;
    }
    .el-checkbox__input.is-checked .el-checkbox__inner::after {
      transform: rotate(0deg) scale(1.2) translate(0px, 8px);
    }
    .el-checkbox__input.is-checked .el-checkbox__inner {
      width: 35px;
      height: 35px;
      border: none;
      display: block;
      position: absolute;
      right: -16px;
      bottom: -45px;
      transform: rotate(45deg);
    }
  }
}
.popover-select-box-bottom {
  position: absolute;
  bottom: 0;
  height: 40px;
  right: 0;
  display: flex;
  width: 100%;
  padding-right: 10px;
  border-top: 1px solid #cccccc;
  justify-content: flex-end;
  align-items: center;
}
.popover-select-box-main {
  height: calc(100% - 30px);
  overflow: auto;
}

.reference {
  display: inline;
  color: #333333;
  font-size: 16px;
  cursor: pointer;

  .enterprise-icon {
    font-size: 18px;
    color: #fa8c16;
  }
}
.main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .search-box {
    padding: 10px;
    width: 184px;
    height: 26px;
    line-height: 26px;
    display: flex;
    align-items: center;
    .el-input__inner {
      background-color: initial;
      border-radius: 12px;
      height: 24px;
    }
    .el-input__suffix {
      right: 15px;
    }
    .slot-search {
      display: inline-block;
      height: 26px;
      line-height: 26px;
      cursor: pointer;
    }
  }
}

.reference-deep-color {
  .enterprise-icon {
    color: #3ad8ff;
  }
  color: #fff;
}
</style>