<!--
 * @Author: your name
 * @Date: 2021-03-05 17:54:59
 * @LastEditTime: 2021-08-23 10:51:28
 * @FilePath: \zfzd_oa\src\components\AddCirculation\index.vue
 * 
-->
<template>
    <div class="add-circulation">
        <Dialog
            iconfont="el-icon-alinote-tit"
            :title="workFlowTitle"
            :dialogVisible="showChoice"
            trueText="确 定"
            class="min-dialog-form"
            @cancelClick="handleCancelClick"
            @trueClick="handleWorkflowTrue"
            v-show="showChoice"
            :btnLoading="btnLoading"
            id="_Dialog"
        >
            <el-form :model="circuForm" ref="circuForm" class="rule-form work-flow-form" :rules="rules">
                <slot name="before"></slot>
                <el-form-item :label="childTitle1" v-if="!isReject">
                    <el-radio-group v-model="circuForm.resource">
                        <el-radio v-for="(item, i) in taskList" :key="i" :label="i + 1">{{ item.stepName }}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="驳回方式" v-if="isReject && clickType != 'disagree'">
                    <el-radio-group v-model="taskDefineKey">
                        <el-radio label="">驳回</el-radio>
                        <el-radio label="submit">驳回至发起人</el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item :label="childTitle2" prop="selectComIds" v-if="!end && !isReject">
                    <el-tooltip
                        class="item"
                        :popper-class="getPopperClass('selectComIds')"
                        :manual="!rules.selectComIds.warn"
                        effect="pink"
                        :content="rules.selectComIds.warn"
                        placement="right"
                    >
                        <selectComponent
                            :title="childTitle2"
                            :tabList="tabList"
                            :isMulti="curMulti == 2"
                            :names.sync="selectComNames"
                            :idsScoped="fixedPersonID"
                            :ids.sync="selectComIds"
                        ></selectComponent>
                    </el-tooltip>
                </el-form-item>
                <el-form-item label="提醒方式" v-if="!isReject">
                    <el-checkbox-group v-model="circuForm.remindType">
                        <el-checkbox
                            v-for="item of msgTypeList"
                            :key="item.value"
                            :label="item.value"
                            text-color="red"
                            name="type"
                            >{{ item.name }}
                        </el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="审批意见" prop="type2Val" class="item-remark" v-if="type == 2">
                    <el-tooltip
                        class="item"
                        :popper-class="getPopperClass('type2Val')"
                        :manual="!rules.type2Val.warn"
                        effect="pink"
                        :content="rules.type2Val.warn"
                        placement="right"
                    >
                        <el-input
                            :maxlength="200"
                            type="textarea"
                            v-model="type2Val"
                            :rows="5"
                            :style="{ width }"
                            show-word-limit
                        ></el-input>
                    </el-tooltip>
                </el-form-item>
                <slot name="file"></slot>
            </el-form>

            <span v-html="style"> </span>
        </Dialog>
        
        <div class="form-button" v-if="type == 2">
            <el-button @click="goBack($route)">返回</el-button>
            <div v-if="taskModel.isApprove == 1 && !isShowEdit">
                <el-button
                    v-if="isShowOppose"
                    class="btn-error"
                    :loading="btnErrorLoading"
                    v-has="'oa_flow_oppose'"
                    :disabled="btnRejectLoading || btnErrorLoading || btnAgreeLoading"
                    @click="handleErrorClick"
                    >反对
                </el-button>
                <el-button
                    v-if="isShowReject"
                    class="btn-error"
                    :loading="btnRejectLoading"
                    v-has="'oa_flow_reject'"
                    :disabled="btnRejectLoading || btnErrorLoading || btnAgreeLoading"
                    @click="handleRejectClick"
                    >驳回
                </el-button>
                <el-button
                    v-if="isShowAgree"
                    type="primary"
                    :loading="btnAgreeLoading"
                    v-has="'oa_flow_agree'"
                    :disabled="btnRejectLoading || btnErrorLoading || btnAgreeLoading"
                    @click="handleTrueClick"
                    >同意
                </el-button>
            </div>
            <el-button v-if="isShowEdit" type="primary" @click="handleEditClick">再次编辑</el-button>
        </div>
    </div>
</template>

<script>
import pageTitle from "@/components/page-title";
import selectComponent from "@/components/select-component";
import Dialog from "@/components/dialog";

export default {
    name: "workFlowCom",
    props: {
        queryModel: {
            type: Object,
            default: () => {},
        },
        taskModel: {
            type: Object,
            default: () => {
                return {
                    isApprove: 1,
                    nextTaskList: [],
                };
            },
        },
        isShowEdit: {
            type: Boolean,
            default: () => false,
        },
        isShowOppose: {
            type: Boolean,
            default: () => true,
        },
        isShowAgree: {
            type: Boolean,
            default: () => true,
        },
        noWait: {
            type: Boolean,
            default: () => true,
        },
        isShowReject: {
            type: Boolean,
            default: () => true,
        },
        msgTypeList: {
            type: Array,
            default: () => {
                return [
                    {
                        value: "10042-10",
                        type: "10001-10042",
                        typeName: "消息类型",
                        code: "10",
                        name: "系统消息",
                        sname: "系统消息",
                        accountName: "系统管理员",
                    },
                ];
            },
        },
        title: {
            type: String,
            default: () => "流转步骤",
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
            default: () => false,
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
            default: () => "70%",
        },
        nextTaskList: {
            type: Array,
            default: () => [],
        },
        tabList: {
            type: Array,
            default: () => ["deptPerson"],
        },
        checkBefore: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        const checkSelectComIds = (rule, value, callback) => {
            let flag = true;
            if (rule.type == "Array") {
                flag = !this[rule.attr].length && rule.required;
            } else {
                flag = !this[rule.attr] && rule.required;
            }
            if (flag) {
                this.rules[rule.attr].warn = this.rules[rule.attr].message;
                return callback(new Error(this.rules[rule.attr].message));
            } else {
                this.rules[rule.attr].warn = null;
                callback();
            }
        };
        return {
            type2Val: this.isReject ? (this.clickType == "reject" ? "驳回" : "") : this.end ? "同意" : "拟同意",
            taskDefineKey: "",
            isWorkFlow: true,
            showPersonChoice: false,
            personList: [],
            otherList: [],
            taskList: [],
            curIsFixed: "",
            curFixedPersonName: "",
            curMulti: "",
            assigneeListIds: [],
            end: false,
            circuForm: {
                resource: 1,
                assigneeList: "",
                remindType: [],
            },
            selectComIds: "",
            selectComNames: "",
            isCloseCom: false,
            rules: {
                selectComIds: {
                    required: true,
                    message: "请选择人员",
                    validator: checkSelectComIds,
                    warn: null,
                    attr: "selectComIds",
                    trigger: ["change"],
                },
                type2Val: {
                    required: true,
                    message: "请填写审批意见",
                    validator: checkSelectComIds,
                    warn: null,
                    attr: "type2Val",
                    trigger: ["blur"],
                },
            },
            workFlowTitle: "流转步骤",
            btnRejectLoading: false,
            btnErrorLoading: false,
            btnAgreeLoading: false,
            showChoice: false,
            clickType: "",
            btnLoading: false,
            isReject: false,
            fixedPersonID: [],
        };
    },
    created() {
        this.getBizType(this.bizType, this.bizId);
    },
    components: {
        pageTitle,
        selectComponent,
        Dialog,
    },
    watch: {
        clickType(val) {
            this.type2Val = this.isReject ? (val == "reject" ? "驳回" : "") : this.end ? "同意" : "拟同意";
        },
        "circuForm.resource": {
            handler(val) {
                ++this.spKey;
            },
            deep: true,
        },
        msgTypeList: {
            handler(val) {
                if (!val.length) return;

                this.circuForm.remindType[0] = val[0].value;
            },
            deep: true,
            immediate: true,
        },
        circuForm: {
            handler(val) {
                this.handlerTaskList();
            },
            deep: true,
        },
        end(val) {
            !this.isReject && val && (this.type2Val = "同意");
        },
        isGet(val) {
            val && this.receiveIds(false);
        },
        bizType(val, oldValue) {
            if (val !== oldValue) {
                this.getBizType(this.bizType, this.bizId);
            }
        },
        queryModel: {
            handler(val) {
                setTimeout(() => {
                    this.getBizType(this.bizType, this.bizId);
                }, 100);
            },
            deep: true,
        },
        showChoice(val) {
            if (!val) {
                this.$refs.circuForm.resetFields();
                Object.keys(this.rules).forEach((item) => {
                    this.rules[item].warn = null;
                });
            }
        },
    },
    computed: {
        style() {
            return `<style>
                .el-tabs__content { overflow: initial !important; }
                .el-tooltip__popper {
                    background: #F56C6C;
                    line-height: 1;
                    color: #fff;
                }
                .el-tooltip__popper[x-placement^=right] .popper__arrow::after {
                    border-right-color:#F56C6C;
                }
                .el-tooltip__popper[x-placement^=right] .popper__arrow {
                    border-right-color: #F56C6C;
                }
                .hideTooltip {
                    display: none !important;
                }
                .el-tooltip__popper[x-placement^=left] .popper__arrow {
                    border-left-color: #F56C6C !important;
                }
                .el-tooltip__popper[x-placement^=right], .el-tooltip__popper[x-placement^=left] .popper__arrow::after {   
                    border-left-color: #F56C6C !important;
                }
            </style>`;
        },
    },
    methods: {
        receiveIds() {
            let status = false;
            let str = this.selectComIds;
            status = str == "" ? false : true;
            let resources = this.circuForm.resource,
                key;
            if (this.taskList?.length > 1) {
                key = this.type == 1 ? "submit_branch" : "_branch";
            }

            let data2 = this.end
                ? {
                      comment: this.type2Val,
                      end: this.end,
                      remindType: this.circuForm.remindType.join(),
                  }
                : {
                      ids: str,
                      comment: this.type2Val,
                      attr: this.taskList[Number(resources) - 1].assignee,
                      [key]: this.taskList[Number(resources) - 1].stepId,
                      otherAttr: key,
                      end: this.end,
                      isFixed: this.curIsFixed,
                      remindType: this.circuForm.remindType.join(),
                  };

            // if (this.type == 1 || this.type == 3) {
            //     return {
            //         remindType: data2.remindType,
            //         [data2.attr]: data2.ids
            //     }
            // }else {

            // }
            if (this.end || this.curIsFixed == "1") {
                status = true;
            }
            this.isCloseCom = status ? true : false;

            return {
                approvalModel: data2,
                status,
                taskDefineKey: this.taskDefineKey,
            };
        },
        getPopperClass(attr) {
            return this.rules[attr].warn == null ? "hideTooltip" : "";
        },
        handlerTaskList() {
            // 如果isFixed=0，说明为选人模式，如果是1的话，固定人员显示fixedPersonName
            let resoures = this.circuForm.resource;
            let { isFixed, fixedPersonName, multi, stepId, fixedPersonID } = { ...this.taskList[Number(resoures) - 1] };
            //   end表示流程结束
            stepId == "end" && (this.end = true);
            if (this.end) return;
            this.curIsFixed = isFixed;
            this.curMulti = multi;
            this.fixedPersonID = fixedPersonID?.length ? fixedPersonID.split(",") : [];
            //针对旧数据容错处理
            if (this.fixedPersonID?.length > 1) {
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
                    this.getFirstTask({ bizType, bizId });
                    break;
                case 2:
                    this.taskList = this.nextTaskList;
                    this.handlerTaskList();
                    break;
                case 3:
                    //首部任务接口数据
                    this.getFirstTask({ bizType, bizId, ...this.queryModel });
            }
        },

        // 反对
        handleErrorClick() {
            this.showChoice = true;
            this.clickType = "disagree";
            this.workFlowTitle = "反对";
            this.isReject = true;
            this.btnLoading = false;
            this.$emit("handlerBtnType", this.clickType);
        },
        //驳回
        handleRejectClick() {
            this.showChoice = true;
            this.clickType = "reject";
            this.workFlowTitle = "驳回";
            this.isReject = true;
            this.btnLoading = false;
            this.$emit("handlerBtnType", this.clickType);
        },
        // 同意
        handleTrueClick() {
            this.clickType = "agree";
            if (this.noWait) {
                this.workFlowTitle = "同意";
                this.isReject = false;
                this.btnLoading = false;
                this.showChoice = true;
                this.$emit("handlerBtnType", this.clickType);
            } else {
                this.$emit("waitCheck", this.clickType, (status) => {
                    if (!status) return;

                    this.workFlowTitle = "同意";
                    this.isReject = false;
                    this.btnLoading = false;
                    this.showChoice = true;
                });
            }
        },
        getIdsView(data, status, taskDefineKey) {
            this.$emit("getIds", data, status, taskDefineKey);
        },

        async handleWorkflowTrue() {
            if (this.checkBefore) {
                await this.$emit("handlerCheckBefore", (status) => {
                    if (!status) return;

                    this.proviteData();
                });
            } else {
                this.proviteData();
            }
        },
        proviteData() {
            this.$refs.circuForm.validate((valid) => {
                if (valid) {
                    this.btnLoading = true;
                    let approvalModel = this.receiveIds();
                    let params = Object.assign(approvalModel, { type: this.clickType });
                    approvalModel.type = this.clickType;
                    this.$emit("getworkFlowData", params);
                } else {
                    this.$showWarning("信息填写不正确");
                    return false;
                }
            });
        },
        handleCancelClick() {
            this.showChoice = false;
        },
        handleEditClick() {
            this.clickType = "editAgain";
            this.$emit("getworkFlowData", { type: "editAgain" });
        },
    },
};
</script>

<style lang="scss" scoped>
@import '@/styles/rule-form.scss';
.work-flow-form{
    /deep/.el-form-item__content{
        line-height: 30Px;/*no*/
        width: 360px;
        min-width: 341Px;/*no*/
    }
    /deep/.el-textarea{
      width: 100%!important;  
    }
}

@media screen and (min-width: 1501px) {
    .work-flow-form{
        /deep/.el-form-item__content{
            line-height: 30px;
        }
    }
    
}
</style>
