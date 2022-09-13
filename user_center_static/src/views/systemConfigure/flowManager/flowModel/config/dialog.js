/*
 * @Author: your name
 * @Date: 2021-08-12 16:40:48
 * @LastEditTime: 2021-08-13 13:15:31
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\views\systemConfigure\flowManager\flowModel\config\dialog.js
 */
export default {
    data () {
        return {
            showComponent: false,
            title: '新增模型管理',
            DLbtnLoading: false,
            isClcikEdit: false,
            editForm: [],
            dialogForm: [
                {
                    type: 'select',
                    prop: 'category',
                    value: '',
                    label: '流程分类',
                    children: [
                        {
                            value: '常用流程',
                            name: '常用流程'
                        },
                        {
                            value: '办公流程',
                            name: '办公流程'
                        }
                    ]

                },
                {
                    type: 'input',
                    prop: 'name',
                    label: '模型名称',
                    value: '',
                    rules: {
                        require: true
                    }

                },
                {
                    type: 'input',
                    prop: 'version',
                    label: '版本',
                    value: ''
                },
                {
                    type: 'input',
                    prop: 'key',
                    label: '关键字',
                    value: ''
                },
                {
                    type: 'input',
                    typeName: 'textarea',
                    prop: 'description',
                    class: 'single item-remark',
                    label: '模型描述',
                    value: ''
                },
            ]
        }
    },
    watch: {
        isClcikEdit(val) {
            console.log(val,'vvvvvvvvvvvvvv')
            this.title = val ? '编辑模型管理' : '新增模型管理';
        }
    },
    methods: {
        async handleTrueClick() {
            let {status, data} = await this.$refs.dialogForm.getFormAndValidate()

            if (!status) {
                return;
            }

            this.DLbtnLoading = true;
            let requestApi, parmas = data;
            if (this.isClcikEdit) {
                requestApi = 'EditFlowModel';
                parmas.modelId = this.clickSelectionList[0].id;
            }else {
                requestApi = 'AddFlowModel'
            }
            this.$http[requestApi](parmas).then(res => {
                if (res.code == 0) {
                    this.$showSuccess(res.message)
                    this.$refs.tableGroup.reloadTableList();
                }else {
                    this.$showError(res.message)
                }
                this.DLbtnLoading = false;
                this.showComponent = false;
            }).catch(err => {
                this.$showError(err)
                this.DLbtnLoading = false;
            })
        },
        handleCancelClick() {
            this.showComponent = false;
        }
    }
}