<template>
  <loading-component :loading="loading">
    <FormComponent
      ref="formComponent"
      :formInline="false"
      :config="configList"
      label-width="200px"
    />
  </loading-component>
</template>
<script>
import { formHash } from "../config";
import { FormComponent } from "../components";
import UploadImage from "@/components/upload-single-img";

const typeHash = {
  2: true,
  3: true,
  4: true,
};

export default {
  name: "SystemDisplays",
  props: {
    orgId: String | Number,
  },
  components: {
    FormComponent,
    UploadImage,
  },
  data() {
    return {
      loading: false,
      formData: {},
      configList: [],
    };
  },

  mounted() {
    this.filterList();
  },

  watch: {
    orgId() {
      this.formData = {};
      this.configList = [];
      this.filterList();
    },
    configList: {
      handler(val) {},
      deep: true,
    },
  },

  methods: {
    getForm() {
      const formData = this.$refs.formComponent.getForm();
      return this.configList.map((i) => {
        const value = formData[i.id];
        return { ...i, value: Array.isArray(value) ? value.join(",") : value };
      });
    },

    async filterList() {
      this.loading = true;
      try {
        const { data } = await this.$http.sysParameterCombox({
          orgId: this.orgId,
          setType: "3",
          keies: "",
        });
        const configList = await Promise.all(
          data.map(async (i) => {
            const returnData = {
              ...i,
              ...formHash[i.disType],
              label: i.descript,
              prop: i.id + "",
              value: i.disType === 3 ? [] : i.value,
            };

            if (i.disType === 7 && i.customAttr) {
              returnData.children = i.customAttr.split(";").map((i) => {
                const splitData = i.split(":");
                return { value: splitData[0], name: splitData[1] };
              });
            } else if (typeHash[i.disType] && i.altValue) {
              const reqData = await this.$http.getUcenterCodeCombox({
                type: i.altValue,
              });

              returnData.children = reqData.data.list.map(
                ({ name, value }) => ({
                  name,
                  value,
                })
              );
            }
            return returnData;
          })
        );

        this.configList = configList;
      } catch (error) {}
      this.loading = false;
    },
  },
};
</script>