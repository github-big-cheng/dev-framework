<!--
 * @Author: your name
 * @Date: 2021-03-05 17:54:59
 * @LastEditTime: 2021-08-06 10:48:45
 * @FilePath: \zfzd_oa\src\components\AddCirculation\index.vue
 * 
-->
<template>
    <div class="add-circulation">
        <pageTitle :title="title" class="htitle" v-if="type == 1 || type ==3"></pageTitle>
        <el-form :model="circuForm" ref="circuForm" class="from-ruleForm from-send">
            <el-row :gutter="24">
                <slot name="before"></slot>
            </el-row>
            <el-row :gutter="24" v-if="!isReject">
                <el-col :span="20" class="col-hg">
                    <el-form-item :label="childTitle1">
                        <el-radio-group v-model="circuForm.resource">
                            <el-radio v-for="(item, i) in taskList" :key="i" :label="i + 1">{{item.stepName}}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="24" v-if="isReject && clickType != 'disagree'">
                <el-col :span="20">
                    <el-form-item label="驳回方式" class="col-hg" >
                        <el-radio-group v-model="taskDefineKey">
                            <el-radio label="">驳回</el-radio>
                            <el-radio label="submit">驳回至发起人</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="24" v-if="!end && !isReject">
                <el-col :span="18" class="col-hg auditor">
                    <el-form-item :label="childTitle2">
                        <selectComponent
                            :title="childTitle2"
                            :tabList="tabList"
                            :isMulti="curMulti == 2"
                            :names.sync="selectComNames"
                            :ids.sync="selectComIds"
                            :idsScoped="fixedPersonID"
                            :isDisable="curIsFixed == 1"
                        ></selectComponent>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="24" v-if="!isReject">
                <el-col :span="24" class="col-hg">
                    <el-form-item label="提醒方式">
                        <el-checkbox-group v-model="circuForm.remindType">
                            <el-checkbox v-for="item of msgTypeList" :key="item.value" :label="item.value"
                                         text-color="red" name="type">{{item.name}}
                            </el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="24" v-if="type == 2">
                <el-col :span="24" class="col-hg">
                    <el-form-item label="审批意见" prop="comment" class="col-hg">
                        <el-input
                                v-focus="isReject"
                                :maxlength="200"
                                type="textarea"
                                v-model="type2Val"
                                :rows="5"
                                :style="{ width, }"
                                show-word-limit
                                :class="isTestingInput && !type2Val ? 'i-err' : ''"
                        ></el-input>
                        <span
                                class="sp-err"
                                v-show="isTestingInput && !type2Val"
                        >请输入</span
                        >
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="24">
                <slot name="file"></slot>
            </el-row>
        </el-form>
        <span v-html="style">

    </span>
    </div>
</template>

<script>
    import pageTitle from "@/components/page-title";
    import selectComponent from '@/components/select-component';

    export default {
        props: {
            activeTab: {
                type: String,
                default: () => 'organization'
            },
            queryModel: {
                type: Object,
                default: () => {
                },
            },
            msgTypeList: {
                type: Array,
                default: () => [],
            },
            title: {
                type: String,
                default: () => "流转步骤",
            },
            isReject: {
                type: Boolean,
                default: () => false,
            },
            childTitle1: {
                type: String,
                default: () => "选择步骤",
            },
            childTitle2: {
                type: String,
                default: () => "审核人员",
            },
            type: {
                type: Number,
                default: () => 1,
            },
            isGet: {
                type: Boolean,
                default: () => false
            },
            bizId: {
                type: Number,
            },
            bizType: {
                type: String,
                default: () => "",
            },
            isTestingInput: {
                type: Boolean,
                default: false,
            },
            isRejectStatus: {
                type: Boolean,
                default: false,
            },
            width: {
                type: String,
                default: () => "90%",
            },
            nextTaskList: {
                type: Array,
                default: () => []
            },
            clickType: {
                type: String,
                default: () => "",
            },
            tabList: {
                type: Array,
                default: () => ['deptPerson'],
            },
            currentStepId: {
                default: ''
            }
        },
        data() {
            return {
                style: `<style> .el-tabs__content { overflow: initial !important; } </style>`,
                type2Val: '',
                taskDefineKey: '',
                isWorkFlow: true,
                showPersonChoice: false,
                personList: [],
                otherList: [],
                taskList: [],
                curIsFixed: '',
                curFixedPersonName: '',
                curMulti: '',
                assigneeListIds: [],
                end: false,
                circuForm: {
                    resource: 1,
                    assigneeList: "",
                    remindType: []
                },
                selectComIds: '',
                selectComNames: '',
                isCloseCom: false,
                fixedPersonID: []
            };
        },
        created() {
            this.getBizType(this.bizType, this.bizId);
            this.proviteVal()
        },
        components: {
            pageTitle,
            selectComponent
        },
        watch: {
            clickType(val) {
                this.proviteVal()
            },
            'circuForm.resource': {
                handler(val) {
                    ++this.spKey;
                },
                deep: true,
            },
            msgTypeList: {
                handler(val) {
                    if (!val.length) return;

                    this.circuForm.remindType[0] = val[0].value
                },
                deep: true,
                immediate: true
            },
            circuForm: {
                handler(val) {
                    this.handlerTaskList();
                },
                deep: true,
            },
            end(val) {
                !this.isReject && val && (this.type2Val = '同意')
            },
            isGet(val) {
                val && this.receiveIds(false)
            },
            bizType(val, oldValue) {
                if (val !== oldValue) {
                    this.getBizType(this.bizType, this.bizId);
                }
            },
            queryModel: {
                handler(val) {
                    setTimeout(() =>{
                         this.getBizType(this.bizType, this.bizId);
                    }, 100)
                },
                deep: true
            }
        },
        methods: {
            proviteVal() {
                if (this.isReject) {
                    this.type2Val = this.clickType == 'reject' ? '驳回': '';
                    return
                }

                if (this.end) {
                     this.type2Val = '同意';
                     return
                }

                let str;
                switch(this.currentStepId) {
                    case 'circulation':
                        str = '流转';
                        break
                    default: 
                        str = '同意'
                }

                this.type2Val = str;
            },
            receiveIds(flag) {
                let status = false;
                let str = this.selectComIds;
                status = str == '' ? false : true;
                let resources = this.circuForm.resource, key;
                if (this.taskList.length > 1) {
                    key = this.type == 1 ? 'submit_branch' : '_branch';
                }

                let data2 = this.end ? {
                    comment: this.type2Val,
                    end: this.end,
                    remindType: this.circuForm.remindType.join()
                } : {
                    ids: str,
                    comment: this.type2Val,
                    attr: this.taskList[Number(resources) - 1].assignee,
                    [key]: this.taskList[Number(resources) - 1].stepId,
                    otherAttr: key,
                    end: this.end,
                    isFixed: this.curIsFixed,
                    remindType: this.circuForm.remindType.join()
                };

                if (this.end || this.curIsFixed == '1') {
                    status = true;
                }
                this.isCloseCom = status ? true : false;
                this.$emit('getIds', data2, status, this.taskDefineKey);
            },
            handlerTaskList() {
                // 如果isFixed=0，说明为选人模式，如果是1的话，固定人员显示fixedPersonName
                let resoures = this.circuForm.resource;
                let {isFixed, fixedPersonName, multi, stepId, fixedPersonID} = {...this.taskList[Number(resoures) - 1]};
                //   end表示流程结束
                stepId == 'end' && (this.end = true)
                if (this.end) return;
                this.curIsFixed = isFixed;
                this.curMulti = multi;
                this.fixedPersonID = fixedPersonID.length ? fixedPersonID.split(",") : [];
                //针对旧数据容错处理
                if (this.fixedPersonID.length > 1) {
                    this.curIsFixed = 2;
                }
                if (this.curIsFixed == 1) {
                    this.selectComNames = fixedPersonName;
                    this.selectComIds = fixedPersonID;
                }
            },
            getFirstTask(params) {
                this.$http.getFirstTask(params).then((res) => {
                    this.taskList = res.data[0].taskList;
                    this.handlerTaskList();
                });
            },
            getBizType(bizType, bizId) {
                switch (this.type) {
                    case 1:
                        //首部任务接口数据
                        this.getFirstTask({bizType, bizId})
                        break;
                    case 2:
                        this.taskList = this.nextTaskList;
                        this.handlerTaskList();
                        break;
                    case 3:
                        //首部任务接口数据
                        this.getFirstTask({bizType, bizId, ...this.queryModel})
                }
            },

        }
    };
</script>

<style lang="scss" scoped>
    .add-circulation {
        /deep/ .gutter {
            display: block !important;
        }

        /deep/ .has-gutter {
            .cell {
                background: none !important;
            }

            .el-checkbox {
                background: none !important;
            }
        }

        /deep/ .el-checkbox {
            margin-bottom: 0;
        }

        /deep/ .el-checkbox__input.is-checked + .el-checkbox__label {
            color: #333 !important;
        }

        @media screen and (max-width: 1501px) {
            /deep/ .el-dialog {
                width: 55%;
            }
        }

        .text-area {
            /deep/ .el-form-item__content {
                width: 100%;
            }
        }

        .no-select {
            background-color: #F5F7FA;
            border-color: #E4E7ED;
            color: #ccc;
        }

        .no-select:hover {
            cursor: not-allowed;
        }

        .sp-text {
            border: none;
        }
    }
</style>
