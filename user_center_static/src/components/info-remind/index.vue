<template>
    <div class="info-remind">
        <pageTitle class="htitle" title="提醒设置"></pageTitle>
        <div>
            <el-form :model="infoForm" class="from-ruleForm">
                <el-row :gutter="24">
                    <el-col :span="6.5">
                        <el-form-item label="办文到期日期">
                            <el-date-picker
                                    v-model="infoForm.expireDate"
                                    placeholder="选择日期"
                                    clearable
                                    type="date"
                                    value-format="yyyy-MM-dd"
                                    :picker-options="pickerOptions"
                            >
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item label="提前提醒天数" class="col-day">
                            <el-input class="date-num" v-model="infoForm.afterDay" auto-complete="on" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" class="point-col">
                        <el-form-item label="提醒方式">
                            <el-checkbox-group v-model="infoForm.msgType">
                                <el-checkbox v-for="item of msgTypeList" :key="item.value" :label="item.value"
                                             name="type">{{item.name}}
                                </el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </div>
    </div>
</template>

<script>
    import pageTitle from "../page-title";

    export default {
        name: 'infoRemindCom',
        props: {
            infoForm: {
                expireDate: "",
                afterDay: "",
                msgType: []
            },
            msgTypeList: {
                type: Array,
                default: () => []
            },
            isAll: {
                type: Boolean,
                default: false
            }
        },
        components: {
            pageTitle,
        },
        watch: {
            msgTypeList: {
                handler(val) {
                    if (!val.length || this.infoForm.msgType.length) return;
                    
                    this.isAll ? val.map(item => this.infoForm.msgType.push(item.value)) : this.infoForm.msgType.push(val[0].value)

                },
                deep: true
            },
            infoForm: {
                handler(val) {
                    this.$emit("selectedInfo", val);
                },
                deep: true
            }
        },
        data() {
            return {
                pickerOptions: this.dateConfig()
            };
        },
    };
</script>

<style lang="scss" scoped>
    .info-remind {
        /deep/ .from-ruleForm {
            padding-top: 0;
        }

        .col-day {
            /deep/ .el-input__inner {
                width: 0.7rem;
                margin-right: .1rem;
            }

            /deep/ .el-select {
                .el-input__inner {
                    width: 0.59rem;
                    margin-right: 0;
                }
            }
        }

        /deep/ .el-checkbox {
            width: .9rem;
        }

        /deep/ .el-form-item {
            margin-bottom: 0;
        }

        .point-col {
            /deep/ .el-form-item__content {
                width: 100%;
                line-height: .28rem;
            }
        }
        .date-num /deep/.el-input__suffix {
            right: 8px;
        }

        @media screen and (max-width: 1501px) {
            .col-day {
                /deep/ .el-input__inner {
                    width: 54px;
                    margin-right: 10px;
                }

                /deep/ .el-select {
                    .el-input__inner {
                        width: 59px;
                    }
                }
            }
        }
    }
</style>
