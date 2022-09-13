<template>
    <div class="date-select">
        <el-radio-group v-model="radioSelect" @change="onRadioChange" size="mini">
            <el-radio v-for="date in radioList" :label="date.value" :key="date.label" border>
                {{ date.label }}
            </el-radio>
        </el-radio-group>
        <el-date-picker
                v-model="dateSelect"
                @change="onDateChange"
                size="mini"
                :value-format="dateFormat"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :unlink-panels="unlink"
        >
        </el-date-picker>
        <el-button v-if="showButton" size="mini" type="primary" @click="onConfirm">
            查询
        </el-button>
        <el-button v-if="showButton" size="mini" type="info" @click="resetDate">
            重置
        </el-button>
    </div>
</template>

<script>
    import {dateHash} from "@/utils/date.js";

    export default {
        name: 'dateFastSelectCom',
        props: {
            showButton: {
                type: Boolean,
                default: () => false,
            },
            radioList: {
                type: Array,
                default: () => [
                    {value: "day", label: "今天"},
                    {value: "week", label: "本周"},
                    {value: "month", label: "本月"},
                    {value: "quarter", label: "本季"},
                    {value: "year", label: "本年"},
                ],
            },
            dateFormat: {
                type: String,
                default: () => "yyyy-MM-dd"
            },
            value: {
                default: '',
            },
            unlink:{
              type: Boolean,
              default: () => false,
            }
        },
        data() {
            return {
                radioSelect: '',
                dateSelect: [],
            };
        },
        watch: {
            value: {
                handler(val) {
                   if (Array.isArray(val) && val.length == 2) {
                       this.dateSelect = [...val]
                   }else {
                       let has = this.radioList.some(v => v.value === val);
                        if (has) {
                            this.radioSelect = val;
                            this.onRadioChange();
                        }
                   }
                },
                deep: true,
                immediate: true
            }
        },
        methods: {
            onRadioSelect(type) {
                this.radioSelect = type;
                this.onRadioChange();
            },
            onConfirm() {
                const date = this.dateSelect;
                let start, end;
                if (!Array.isArray(date) || date.length == 0) {
                    start = end = '';
                }else {
                    start = date[0];
                    end = date[1];
                }
                this.$emit('update:strat', start)
                this.$emit('update:end', end)
            },

            onRadioChange() {
                this.dateSelect = dateHash[this.radioSelect];
                this.onConfirm();
            },
            onDateChange() {
                this.radioSelect = "";
                this.onConfirm();
            },
            resetDate() {
                this.radioSelect = '';
                this.dateSelect = [];
                this.$emit("confirm", []);
            },
        },
    };
</script>

<style lang="scss" scoped>
    .date-select {
        display: flex;

        /deep/ .el-button {
            height: 28px;
        }

        /deep/ .el-radio-group {
            margin-right: 10px;

            .el-radio {
                margin-right: 10px;
                margin-left: 0;
                margin-top: 0;
                height: 30PX;
                padding: 0 15px;
                line-height: 28PX;
            }

            .el-radio__input {
                display: none;
            }

            .el-radio__label {
                padding-left: 0;
            }
        }

        /deep/ .el-range-input,
        /deep/ .el-input__inner {
            margin-right: 10px;
        }

        /deep/ .el-radio__input.is-checked + .el-radio__label {
            color: #409eff;
        }
    }

    @media screen and (min-width: 1501px) {
        .date-select {
            /deep/ .el-radio-group {
                .el-radio {
                    height: 30px;
                    line-height: 28px;
                }
            }
        }
    }
</style>
