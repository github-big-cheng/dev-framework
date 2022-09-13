<template>
  <el-checkbox-group
    v-bind="$attrs.props"
    v-model="checkboxValue"
    @change="checkboxChange"
  >
    <el-checkbox
      v-for="option in children"
      :key="option.value"
      :label="option.name"
    >
    </el-checkbox>
  </el-checkbox-group>
</template>

<script>
export default {
  name: "CheckboxComponent",
  props: {
    value: {
      type: Array | String,
      require: true,
      default: () => [],
    },
    children: {
      type: Array,
      require: true,
      default: () => [],
    },
  },
  data() {
    return {
      checkboxValue: [],
    };
  },

  created() {
    this.initializeValue(this.value);
  },

  watch: {
    value(newData) {
      this.initializeValue(newData);
    },
  },

  methods: {
    checkboxChange(newData) {
      this.$emit(
        "input",
        this.children
          .filter((i) => newData.includes(i.name))
          .map((i) => i.value)
          .join(",")
      );
    },
    initializeValue(newData) {
      if (newData!== undefined) {
        let data = Array.isArray(newData) ? newData : newData.split(",");
        this.checkboxValue = this.children
          .filter((i) => data.includes(i.value))
          .map((i) => i.name);
      }
    },
  },
};
</script>
