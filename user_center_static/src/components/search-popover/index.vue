<!--
 * @Author: your name
 * @Date: 2021-03-28 11:04:46
 * @LastEditTime: 2021-08-16 16:50:47
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\components\searchPopover\index.vue
-->
<template>
  <el-popover
    placement="bottom"
    :width="width"
    trigger="manual"
    class="search-float"
    v-model="visible"
    popper-class="search-popover search-box"
  >
    <div class="popover-box">
      <slot></slot>
      <div class="form-button">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="resetClick">重置</el-button>
        <el-button type="primary" @click="searchClick">搜索</el-button>
      </div>
    </div>
    <i
      :class="['el-icon-aliasearch', iconActive ? 'active' : '']"
      
      @click="moreSwarchButton"
      slot="reference"
    ></i>
  </el-popover>
</template>

<script>
export default {
  name: 'searchPopoverCom',
  props: {
    width: {
      type: [Number, String],
      default: () => 510,
    },
    calss: {
      type: String,
    },
    iconActive: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      visible: false,
    };
  },
  watch: {
    visible(val) {
      !val && this.$emit('popoverStatus')
    },
    // iconActive(val) {
    //   console.log(val)
    // }
  },
  methods: {
    searchClick() {
      this.visible = false;
      this.$emit("searchList");
    },
    resetClick() {
      this.$emit("resetList");
    },
    //点击高级搜索
    moreSwarchButton(){
      this.visible = !this.visible;
      this.$emit("popoverShowBtn");
    }
  },
};
</script>

<style lang="scss" scoped>
@import "@/styles/search-form.scss";
</style>
