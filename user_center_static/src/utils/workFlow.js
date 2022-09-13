/*
 * @Author: your name
 * @Date: 2021-03-11 14:08:55
 * @LastEditTime: 2021-04-11 17:37:06
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\utils\minix\workFlow.js
 * 
 * 该文件处理工作流业务，用minix混入到调用该文件的组件中
 */
// 全局mixin


export default {
    data() {
        return {
            isGet: false, //主动获取流转步骤数据指令
            isNext: true,
            isSubmitType: null,
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
    created() {
        this._getMsgTypeList();
    },
    methods: {
        _closeWorkFlowBtn() {
            this.$children.forEach(item => {
                item.name = "workFlow" && (item.btnLoading = false)
            })
        },
        _getMsgTypeList() {
            this.$http.getUcenterCodeCombox({type:'10001-10042'}).then((res) => {
                if (res && res.code==0) {
                    this.msgTypeList = res.data.list;
                }
            });
        },
        handleCancelClick() {
            this._showChoice = false;
        },
        getIds(data, status) {
            this.isGet = false;
            this.isJSON(this[this.attr].approvalModel) && (this[this.attr].approvalModel = JSON.parse(this[this.attr].approvalModel));
            data.otherAttr !== undefined && (this[this.attr].approvalModel[data.otherAttr] = data[data.otherAttr]);

            if (!status && data.isFixed == '0' && this.isSubmitType == 1 && !data.end) {
                this.isNext = false;
                this.$showWarning("请选择审核人员")
            } else if (this.isSubmitType == 0) {
                this.isNext = true;
            } else {
                this[this.attr].approvalModel.remindType = data.remindType;
                this.isNext = true;
                this[this.attr].approvalModel[data.attr] = data.ids;
                !this.isJSON(this[this.attr].approvalModel) && (this[this.attr].approvalModel = JSON.stringify(this[this.attr].approvalModel));
                this[this.attr].hasOwnProperty('taskModel') && (delete this[this.attr].taskModel)
            }
            this.isSubmitType = null;
        },
        getIdsView(data, status, taskDefineKey) {
            this.isGet = false;
            //驳回状态
            if (!data.ids && this.isSubmitType != 'reject' && this.isSubmitType != 'disagree' && !data.end) {
                this.$showWarning("请选择审批人");
                this._closeWorkFlowBtn();
                this.isNext = false;
                return;
            }
            if (!data.comment) {
                this.$showWarning("请填写审批意见");
                this._closeWorkFlowBtn();
                this.isNext = false;
                return;
            }
            this[this.attr].approvalModel.remindType = data.remindType;
            this.isJSON(this[this.attr].approvalModel) && (this[this.attr].approvalModel = JSON.parse(this[this.attr].approvalModel));
            this[this.attr].approvalModel.comment = data.comment;
            let currentStepId = this[this.attr].taskModel.currentStepId;
            this[this.attr].approvalModel[currentStepId + '_branch'] = data[data.otherAttr];
           
            if (this.isSubmitType != 'agree') {
                this.setFuleForm(taskDefineKey)
                this.isSubmitType = null;
                this.isNext = true;
                return;
            }
            
            if (data.end) {
                this.isNext = true;
            } else if (data.isFixed == '1') {
                this.isNext = true;
            } else {
                this.isNext = true;
                this[this.attr].approvalModel[data.attr] = data.ids;
            }
            !this.isJSON(this[this.attr].approvalModel) && (this[this.attr].approvalModel = JSON.stringify(this[this.attr].approvalModel));
            this[this.attr].hasOwnProperty('taskModel') && (delete this[this.attr].taskModel)
            this.isSubmitType = null;
        },
        isJSON(str) {
            if (typeof str == 'string') {
                try {
                    JSON.parse(str)
                    return true
                } catch (e) {
                    return false
                }
            } else {
                return false
            }
        },
        // 该方法只有在执行驳回  和  驳回至发起人才会执行
        setFuleForm(taskDefineKey) {
            let data = {};
            data.id = this[this.attr].id;
            data.taskDefineKey = taskDefineKey;
            data.comment = this[this.attr].approvalModel.comment;
            data.docType = this[this.attr].docType;
            this[this.attr] = data;
        },
        checkNext(type, attr = 'ruleForm') {
            let isNext = true;
            // this.$refs.oaAttachment.validate((valid) => {
               
            //     if (valid) {
                   
            //     } else {
            //         isNext = false;
            //         return false;
            //     }
            // });
            if (!isNext) return;
            this.isGet = true;
            this.attr = attr;
            if (!this[this.attr].hasOwnProperty('approvalModel')) this[this.attr].approvalModel = {};
            this.isSubmitType = type;
            let _this = this;
            return new Promise(function (resolve, reject) {
                setTimeout(() => {
                    resolve(_this.isNext)
                }, 0)
            })
        },
    }
}
