/*
 * @Author: your name
 * @Date: 2021-03-11 14:08:55
 * @LastEditTime: 2021-08-19 16:28:34
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\utils\minix\workFlow.js
 * 
 * 该文件处理工作流业务，用minix混入到调用该文件的组件中
 */
// 全局mixin

import Api from "@/api/api";

export default {
    data() {
        return {
            otherData: null, //储存临时数据
            msgTypeList: [], //存储提醒方式接口数据
            ruleForm: {
                approvalModel: {}
            },
            attr: null,
            _showChoice: false,
            clickType: '',
            isReject: false,
            workFlowTitle: '',
            currentStepId: ''
        }
    },
    beforeRouteLeave(to, from, next) {
        /**
         * 关闭工作流的弹窗
         */
        this.$children.forEach(item => {
            item.name = "workFlow" && (item.showChoice = false)
        })
        next()
    },
    methods: {
        _closeWorkFlowBtn() {
            this.$children.forEach(item => {
                item.name = "workFlow" && (item.btnLoading = false)
            })
        },
        _getMsgTypeList() {
            Api.getUcenterCodeCombox({type:'10001-10042'}).then((res) => {
                if (res && res.code==0) {
                    this.msgTypeList = res.data.list;
                }
            });
        },
        handleCancelClick() {
            this._showChoice = false;
        },
        getIds(data) {
            let approvalModel = {};
        
            approvalModel.remindType = data.remindType;
            approvalModel[data.attr] = data.ids;
            approvalModel._assignKey = data.attr;
            const {otherAttr} = data;
            approvalModel[otherAttr] = data[otherAttr];
            return JSON.stringify(approvalModel)
        },
        getIdsView(data, taskDefineKey, clickType) {
            let ruleForm = {
                bizId: this.id,
                bizType: this.bizType,
            }, approvalModel = {};
            
            approvalModel.comment = data.comment;
            if (clickType != 'agree') {
                approvalModel.taskDefineKey = taskDefineKey;
                approvalModel.docType = this.docType;
            }else {
                approvalModel.remindType = data.remindType;
                approvalModel.comment = data.comment;
                approvalModel._assignKey = data.attr;
                let currentStepId = this.currentStepId;
                approvalModel[currentStepId + '_branch'] = data[data.otherAttr];
                !data.end && (approvalModel[data.attr] = data.ids);
                ruleForm.approvalModel = JSON.stringify(approvalModel);
            }
            return clickType != 'agree' ? Object.assign(ruleForm,  approvalModel) : ruleForm;
        },
    }
}
