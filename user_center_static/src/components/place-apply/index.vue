<template>
    <div class="place-container">
        <Dialog
                iconfont="el-icon-alifile-tit"
                :dialogVisible.sync="placeVisible"
                title="归档申请"
                class="add-form-dialog"
                @trueClick="handleTrueClick"
                @cancelClick="handleCancelClick"
        >
            <form-com
                    ref="formCom"
                    :form-data="formData"
                    :config="formConfig"
            ></form-com>
        </Dialog>
    </div>
</template>
<script>
    import Dialog from '@/components/dialog'
    import formCom from '@/components/form-com'

    export default {
        name: 'placeApplyCom',
        props: {
            placeVisible: {
                type: Boolean,
                default: () => false,
            },
            bizType: {
                type: String,
                default: () => '',
            },
            bizId: {
                type: Number,
                default: () => 0,
            },
            formData: {
                type: Object,
                default: () => {
                },
            },
        },
        components: {
            Dialog,
            formCom,
        },
        data() {
            return {
                formConfig: [
                    {
                        type: 'input',
                        label: '文件材料题名',
                        prop: 'fileName',
                        value: '',
                        rules: {require: true},
                    },
                    {
                        type: 'input',
                        label: '文件别名',
                        prop: 'fileAlias',
                        value: '',
                    },
                    {
                        type: 'input',
                        label: '文件编号',
                        prop: 'fileNo',
                        value: '',
                    },
                    {
                        type: 'date',
                        label: '文件日期',
                        prop: 'fileTime',
                        value: '',
                    },
                    {
                        type: 'selectCom',
                        label: '归档人',
                        prop: 'placeId',
                        prop2: 'placeName',
                        tabList: ['deptPerson'],
                        value: '',
                        rules: {require: true},
                        class: 'single',
                    },
                    {
                        type: 'input',
                        label: '页数',
                        prop: 'pages',
                        value: '',
                    },
                    {
                        type: 'input',
                        label: '文件起始页',
                        prop: 'startPages',
                        value: '',
                    },
                    {
                        type: 'input',
                        label: '文件结束页',
                        prop: 'endPages',
                        value: '',
                    },
                    {
                        type: 'select',
                        label: '密级',
                        prop: 'securityType',
                        value: '',
                        url: 'getUcenterCodeCombox',
                        params: {type: '20001-22002'},
                    },
                    {
                        type: 'input',
                        typeName: 'textarea',
                        label: '备注',
                        prop: 'memo',
                        value: '',
                        class: 'item-remark single',
                    },
                    {
                        type: 'uploadFile',
                        label: '附件',
                        prop: 'attachments',
                        url: '/sys/file/upload',
                        fileType: '10068-210',
                        class: 'single',
                    },
                ],
            }
        },
        created() {
        },
        methods: {
            async handleTrueClick() {
                const {data, status} = await this.$refs.formCom.getFormAndValidate()
                if (!status) return

                data.bizType = this.bizType
                data.bizId = this.bizId
                data.attachments = JSON.stringify(data.attachments);
                this.$emit('trueClick', data)
            },
            handleCancelClick() {
                this.$emit('cancelClick')
            },

            clear() {
                this.$refs?.formCom?.clearFrom();
            }
        },
    }
</script>
