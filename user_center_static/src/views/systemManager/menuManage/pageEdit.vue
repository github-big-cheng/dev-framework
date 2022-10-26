<template>
    <div class="main-content-wrap inner-maincon">
        <form-com
                ref="ruleFormBox"
                :config="formConfigs"
        ></form-com>
        <div class="form-button">
            <el-button  @click="goBack($route)">取消</el-button>
            <el-button
                    type="primary"
                    v-loading="btnLoading"
                    @click="submitForm"
            >保存</el-button
            >
        </div>
    </div>
</template>

<script>
    import formCom from "@/components/form-com";

    export default{
        name:"menuManageEdit",
        components: {
            formCom,
        },
        data() {
            return {
                id: null,
                searchForm:{
                    pageNo:1,
                    pageSize:5,
                },
                formConfigs: [
                    {
                        index: 0,
                        type: 'input',
                        label: '菜单名称',
                        prop: "name",
                        value: '',
                        rules: {
                            require: true
                        },
                    },
                    {
                        index: 1,
                        type: 'select',
                        label: '类型',
                        prop: "type",
                        value: '',
                        rules: {
                            require: true
                        },
                        children: [
                            {
                                name: '菜单',
                                value: 1
                            },
                            {
                                name: '子菜单',
                                value: 2
                            },

                            {
                                name: 'tab页',
                                value: 3
                            },
                            {
                                name: '按钮',
                                value: 4
                            }
                        ]
                    },
                    {
                        index: 2,
                        type: 'input',
                        label: '请求地址',
                        prop: "action",
                        value: '',
                    },
                    {
                        index: 3,
                        type: 'input',
                        label: '代码',
                        prop: "code",
                        value: '',
                        rules: {
                            require: true
                        },
                    },
                    {
                        index: 4,
                        type: 'select',
                        label: '所属应用',
                        prop: 'projectId',
                        children: [],
                        rules: {
                            require: true
                        },
                        normalizer: { value: 'id', label: 'name' },
                        changeCB: (value) => this.getMenuList(value)
                    },
                    {
                        index: 5,
                        type: 'select',
                        label: '上级菜单',
                        prop: 'parentId',
                        normalizer: { value: 'id', label: 'name' },
                        children: [],
                    },
                    {
                        index: 6,
                        type: 'input',
                        label: '图标路径',
                        prop: "imgPath",
                        value: '',
                    },
                    {
                        index: 7,
                        type: 'input',
                        label: '排序号',
                        prop: "orderNo",
                        value: '',
                    },
                    {
                        index: 8,
                        type: 'input',
                        typeName: 'textarea',
                        label: '菜单描述',
                        prop: 'description',
                        class: "single item-remark",
                        maxlength: "100",
                    }
                ],
                btnLoading: false
            }
        },
        created() {
            let { id } = this.$route.params;
            if (id) {
                this.id = id;
                this.getMenuView({id})
            }
            this.$route.meta.noLoading = true;
            this.getProjectList();
        },
        methods: {
            async getMenuView(params) {
                const {code,data} = await this.$http.getMenuView(params);
                if (code != 0) {
                    return;
                }

                this.formConfigs.forEach(item => {
                    let value = data[item.prop];
                    this.$refs.ruleFormBox.setFormValue(item.prop, value)
                })

                if (data.projectId) {
                    this.getMenuList(data.projectId);
                }
            },
            async getProjectList() {
                const {code, data} = await this.$http.projectCombox();
                if (code == 0) {
                    this.formConfigs[4].children = data.list;
                }
            },
            async getMenuList(projectId) {
                const {code, data} = await this.$http.getMenuListChild({pageSize: 300, projectId});
                if (code == 0) {
                    this.formConfigs[5].children = data.list;
                }
            },
            // btn
            async submitForm() {

                const ruleFormBox = await this.$refs.ruleFormBox.getFormAndValidate();
                if(!ruleFormBox.status) return;

                this.btnLoading = true;
                this.$http.ucenterFunctionEdit({id:this.id, ...ruleFormBox.data}).then(res => {
                    if (res.code == 0) {
                        this.$showSuccess(res.message);
                        this.goBack(this.$route, true)
                    }
                    this.btnLoading = false;
                }).catch((err) => {
                    this.btnLoading = false;
                });
            }
        }
    }
</script>