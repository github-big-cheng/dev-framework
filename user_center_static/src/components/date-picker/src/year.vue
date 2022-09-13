<template>
    <div class="year-picker">
        <el-date-picker
            v-model="year1"
            type="year"
            :pickerOptions="startPickerOptions"
            value-format="yyyy"
            format="yyyy"
            @change="startYearChange"
            placeholder="开始时间"
        >
        </el-date-picker
        ><span class="to">至</span
        ><el-date-picker
            v-model="year2"
            value-format="yyyy"
            :pickerOptions="endPickerOptions"
            type="year"
            format="yyyy"
            @change="endYearChange"
            placeholder="截止时间"
        >
        </el-date-picker>
    </div>
</template>
<script>
export default {
    props: {
        value: {
            type: Array,
            default: () => [],
        },
        startYear: {
            type: String,
            default: () => "",
        },
        endYear: {
            type: String,
            default: () => "",
        },
    },
    data() {
        return {
            year1: "",
            year2: "",
            startPickerOptions: {
                disabledDate: (time) => {
                    const { year2 } = this;
                    return year2 ? time.getTime() > new Date(year2).getTime() : false;
                },
            },
            endPickerOptions: {
                disabledDate: (time) => {
                    const { year1 } = this;
                    return year1 ? time.getTime() < new Date(year1).getTime() : false;
                },
            },
        };
    },
    watch: {
        value: {
            handler(date) {
                const [year1, year2] = date;
                this.year1 = year1;
                this.year2 = year2;
            },
            immediate: true,
        },
        startYear(date) {
            this.year1 = date;
        },
        endYear(date) {
            this.year2 = date;
        },
    },

    methods: {
        startYearChange() {
            const { year1, year2 } = this;
            this.$emit("update:startYear", year1);
            this.$emit("input", [year1, year2]);
        },

        endYearChange() {
            const { year1, year2 } = this;
            this.$emit("update:endYear", year2);
            this.$emit("input", [year1, year2]);
        },
    },
};
</script>

<style lang="scss" scoped>
.year-picker {
    /deep/.el-date-editor {
        width: 169px;
        min-width: 160px;
        .el-input__inner {
            width: 169px;
            min-width: 160px;
        }
    }
    .to {
        padding: 0 5px;
    }
}
</style>
