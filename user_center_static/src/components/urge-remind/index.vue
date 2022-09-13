<template>
    <div class="urging-container">
        <Dialog
                iconfont="el-icon-alipeople-tit"
                :dialogVisible="urgingVisible" 
                title="提醒"
                @trueClick="handleUrgingTrueClick"
                @cancelClick="handleCancelUrgingClick">
            <form-com ref="formCom" :config="formConfig" columnNum="row-col1"></form-com>
        </Dialog>
    </div>
</template>
<script>
    import Dialog from "@/components/dialog";
    import formCom from '@/components/form-com'

    export default {
        name: 'urgeRemindCom',
        props: {
            urgingVisible: {
                type: Boolean,
                default: () => false,
            },
            urgingContent: {
                type: String,
                default: () => ""
            },
            bizType: {
                type: String,
                default: () => ""
            },
            bizId: {
                type: Number,
                default: () => 0
            },
            msgType: {
                type: String,
                default: "10001-10042"
            }
        },
        components: {
            Dialog,
            formCom
        },
        data() {
            return {
                urgingPersonList: [],
                baseMsgTypeList: [],
                formConfig: [
                    {
                        type: 'select',
                        label: '提醒对象',
                        prop: 'personIds',
                        url: 'getTodoperson',
                        rules: {
                            require: true
                        },
                        normalizer: {
                            label: "personName",
                            value: "id",
                        },
                        selectProps: {
                            multiple: true
                        },
                        params: {
                            bizType: this.bizType,
                            bizId: this.bizId
                        },
                    },
                    {
                        type: 'input',
                        typeName: 'textarea',
                        label: '消息内容',
                        prop: 'content',
                        value: '',
                        class: 'item-remark',
                        rules: {
                            require: true
                        }
                    },
                    {
                        type: 'checkbox',
                        label: '提醒方式',
                        prop: 'remindType',
                        value: ['10042-10'],
                        children: []
                    },
                ]
            };
        },
        created() {
            this.getMsgTypeList()
        },
        methods: {
            async handleUrgingTrueClick() {
                const {data, status} = await this.$refs.formCom.getFormAndValidate();
                if (!status) return;
                data.remindType = data.remindType.join(',')
                this.$emit("trueClick", data);
            },
            handleCancelUrgingClick() {
                this.$emit("cancelClick");
            },
            getMsgTypeList() {
                this.$http.getUcenterCodeCombox({type: this.msgType}).then((res) => {
                    if (res.code == 0) {
                        this.formConfig[2].children = res.data.filter(item => item.value != '10042-30')
                    }
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    /deep/.rule-form {
            width: auto;
            min-width: auto;
            .el-form-item__label{
                width: 100px;
                min-width: 80Px;
            }
            .item-remark .el-textarea__inner {
                width: 300px;
                height: 110px;
                min-width: 330Px;
            }
        }
</style>
