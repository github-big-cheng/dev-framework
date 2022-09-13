<template>
    <el-select
        class="select-box"
        clearable
        placeholder=""
        :filter-method="remoteMethod"
        v-model="seleted"
        :filterable="!!searchKey"
        :loading="loading"
        v-bind="$attrs.selectProps"
        :disabled="disabled"
        @change="onChange"
    >
        <el-option v-for="item in options" :key="item.value" :label="item[normalizer.label]" :value="item[normalizer.value]"> </el-option>
    </el-select>
</template>
<script>
import lodash from "lodash";
export default {
    /* selectProps:{} select所有的porps */
    props: {
        children: {
            type: Array,
        },
        url: {
            type: String,
        },
        params: {
            type: Object,
        },
        normalizer: {
            type: Object,
            default: () => ({
                label: "name",
                value: "value",
            }),
        },
        searchKey: {
            type: String,
            default: () => "",
        },
        value: {
            type: Boolean | String | Number,
        },
        disabled:{
            type: Boolean,
            default: () => false,
        }
    },
    data() {
        return {
            options: [],
            loading: false,
            seleted: "",
            changeState: false,
        };
    },
    watch: {
        children: {
            handler(data) {
                this.options = data || [];
            },
            deep: true,
        },
        value: {
            handler(val, oldVal) {
                if (val !== oldVal && val !== undefined && val !== null) {
                    this.multipleCompute();
                } else {
                    this.seleted = ''
                }
            },
            immediate: true,
        },
    },
    mounted() {
        this.options = this.children || [];
        if (this.url && !this.options.length) {
            this.requestList();
        }
    },
    methods: {
        async requestList(searchData) {
            this.loading = true;
            const { url, params, searchKey } = this;
            const newParams = { ...params };
            if (searchKey) {
                newParams[searchKey] = searchData;
            }
            try {
                const { data, code } = await this.$http[url](newParams);
                if (code === 0) {
                    if (Array.isArray(data)) {
                        this.options = data;
                    } else {
                        this.options = data.list;
                    }
                    this.multipleCompute();
                }
            } catch (error) {
                console.error(error);
            }
            this.loading = false;
        },

        remoteMethod: lodash.debounce(function(data) {
            this.requestList(data);
        }, 500),

        onChange(value) {
            this.changeState = true;
            const { multiple } = this.$attrs?.selectProps || {};
            const seletedValue = value ? (multiple ? value.join(",") : value) : "";
            this.$emit("input", seletedValue);
            const { normalizer, options } = this;
            const option = value
                ? options.filter((i) => (multiple ? value.includes(i[normalizer.value] + "") : i[normalizer.value] + "" === value + ""))
                : [];
            if (this.$attrs.propName) {
                const label = option.map((i) => i[normalizer.label]);
                this.$emit("update:propName", label.join(","));
            }
            this.$emit("change", seletedValue, multiple ? option : option[0]);
        },
        multipleCompute() {
            const { multiple } = this.$attrs?.selectProps || {};
            const { value, options, normalizer } = this;
            
            let seleted = value;
            if (multiple) {
                const id = options[0][normalizer.value];
                seleted = value ? value.split(",") : [];
                if (Number(id)) {
                    seleted = seleted.map((i) => Number(i));
                }
            }
            this.seleted = seleted;
        },
    },
};
</script>

<style lang="scss" scoped>
.select-box {
    /deep/.el-tag--small {
        height: 25px;
    }
}
</style>
