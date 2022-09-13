<template>
  <div
    class="loading-box"
    v-loading="loading"
    :element-loading-text="loadingText"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(255, 255, 255, .8)"
  >
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "LoadingComponent",
  props: {
    loading: {
      type: Boolean,
      default: () => false,
    },
    loadingText: {
      type: String,
      default: () => "",
    },
  },
};
</script>

<style lang="scss" scoped>
.loading-box {
  height: 100%;
  width: 100%;
}
</style>