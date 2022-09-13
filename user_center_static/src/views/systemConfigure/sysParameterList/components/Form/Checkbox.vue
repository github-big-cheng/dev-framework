<template>
  <el-checkbox-group v-model="checkboxValue" @change="checkboxChange">
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
      );
    },
    initializeValue(newData) {
      this.checkboxValue = this.children
        .filter((i) => newData.includes(i.value))
        .map((i) => i.name);
    },
  },
};
</script>
