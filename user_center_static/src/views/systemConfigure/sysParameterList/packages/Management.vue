<template>
  <loading-component class="management-component" :loading="loading">
    <FormComponent
      ref="blockList"
      :config="blockList"
      :form-inline="false"
      size="mini"
      label-width="200px"
    />
    <FormComponent
      ref="inlineList"
      :config="inlineList"
      size="mini"
      label-width="200px"
      :form-inline="true"
    />
    <FormComponent
      ref="textareaList"
      :config="textareaList"
      label-width="200px"
      :form-inline="false"
    />
  </loading-component>
</template>

<script>
import { formHash } from "../config";
import { FormComponent } from "../components";
import { getLocalStorage } from "@/utils/auth";
const typeHash = {
  2: true,
  3: true,
  4: true,
};

export default {
  name: "Management",
  props: {
    orgId: String | Number,
  },
  components: {
    FormComponent,
  },
  data() {
    return {
      formData: {},
      textareaList: [],
      loading: false,
      inlineList: [],
      configList: [],
      blockList: [],
    };
  },

  mounted() {
    this.filterList();
  },

  watch: {
    orgId() {
      this.formData = {};
      this.textareaList = [];
      this.inlineList = [];
      this.configList = [];
      this.blockList = [];
      this.filterList();
    },
  },

  methods: {
    getForm() {
      // const formData = this.$refs.formComponent.getForm();
      // const formData = Object.values(this.$refs).map((i) => i.getForm())
      let formData = {};
      Object.values(this.$refs).forEach((i) => {
        formData = { ...formData, ...i.getForm() };
      });
      return this.configList.map((i) => {
        const value = formData[i.id];
        return { ...i, value: Array.isArray(value) ? value.join(",") : value };
      });
    },

    async filterList() {
      try {
        this.loading = true;
        const { data } = await this.$http.sysParameterCombox({
          orgId: this.orgId,
          setType: "2",
          keies: "",
        });
        const configList = await Promise.all(
          data.map(async (i) => {
            const returnData = {
              ...i,
              ...formHash[i.disType],
              label: i.descript,
              prop: i.id + "",
              value: i.value,
            };
            if (i.disType === 3) {
              returnData.value = i?.value?.split(",") || [];
            }
            if (+i.disType === 7 && i.customAttr) {
              returnData.children = i.customAttr.split(";").map((i) => {
                const splitData = i.split(":");
                return { value: splitData[0], name: splitData[1] };
              });
            } else if (typeHash[i.disType] && i.altValue) {
              const reqData = await this.$http.getUcenterCodeCombox({
                type: i.altValue,
              });
              returnData.children = reqData.data.map(({ name, value }) => ({
                name,
                value,
              }));
            }
            return returnData;
          })
        );
        const inlineType = [4, 3, 7];
        configList.forEach((i) => {
          if (+i.disType === 8) {
            this.textareaList.push(i);
          } else if (inlineType.includes(+i.disType)) {
            this.blockList.push(i);
          } else {
            this.inlineList.push(i);
          }
          this.configList = configList;
        });
      } catch (error) {
        console.error(error);
      }
      this.loading = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.management-component {
  padding: 0 10px;
  /deep/.el-textarea {
    width: 600px;
  }
}
</style>