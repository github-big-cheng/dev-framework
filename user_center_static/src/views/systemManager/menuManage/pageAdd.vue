<template>
    <div class="main-content-wrap inner-maincon">
        <form-com
            ref="ruleFormBox"
            :config="formConfigs"
        >
            
        </form-com>
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
import vPinyin from "@/utils/v-py.js";

export default({
    name:"menuManageAdd",
    components: {
        formCom,
    },
    data() {
        return {
            connectName:"",
            connectCode:"",
            searchForm:{
                pageNo:1,
                pageSize:5,
            },
            formConfigs: [
                {
                    type: 'selectCom',
                    label: '上级菜单',
                    prop: 'parentId',
                    prop2: 'parentName',
                    value: '',
                    names: '',
                    title: '上级菜单',
                    tabList: ['menu'],
                    focus: true,
                    rules: {
                        require: true
                    },
                    class:"single input-w360"
                },
                {
                    type: 'input',
                    label: '菜单名称',
                    prop: "name",
                    value: '',
                    rules: {
                        require: true
                    },
                },
                {
                    type: 'input',
                    label: '请求地址',
                    prop: "action",
                    value: '',
                    rules: {
                        require: true
                    },
                },
                {
                    type: 'input',
                    label: '代码',
                    prop: "code",
                    value: '',
                    rules: {
                        require: true
                    },
                },
                {
                    type: 'input',
                    label: '图标路径',
                    prop: "imgPath",
                    value: '',
                    rules: {
                        require: true
                    },
                },
                {
                    type: 'input',
                    label: '排序号',
                    prop: "orderNo",
                    value: '',
                    rules: {
                        require: true
                    },
                },
                {
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
                            name: 'tab页签',
                            value: 3
                        },
                        {
                            name: '按钮',
                            value: 4
                        }

                    ]
                },
            ],
            btnLoading: false
        }
    },
    watch: {
        connectCode(val) {
            this.$refs.ruleFormBox.ruleForm.code = val;
        }
    },
    async created() {
        // let res = await this.getMenuListChild(), children = [];
        
        // if (res.code == 0) {
        //     res.data.list.forEach(item => {
        //         children.push({
        //             name: item.name,
        //             value: item.id
        //         })
        //     })
        // }

        // this.formConfigs[0].children = children;
    },
    methods: {
        getMenuListChild() {
            return new Promise((resolve, reject) => {
                this.$http.getMenuListChild({
                    pageSize: 1000000,
                    projectId: this.MXmenuId,
                }).then(res => resolve(res)).catch(err => reject(err))
            })
        },
        getInputValue(val) {
            this.connectCode = vPinyin.chineseToPinYin(val);
            this.$refs.ruleFormBox.ruleForm.cname = val;
        },
        //btn
        cancelClick(){
            this.goBack(this.$route)
        },
        async submitForm(type) {
            const ruleFormBox = await this.$refs.ruleFormBox.getFormAndValidate();

            if(!ruleFormBox.status) return;

            this.btnLoading = true;
             this.$http.ucenterFunctionAdd({...ruleFormBox.data}).then(res => {
                 if (res.code == 0) {
                    this.$showSuccess(res.message);
                    this.goBack(this.$route, true)
                 }
                 this.btnLoading = false;
             })
              .catch((err) => {
                    this.btnLoading = false;
                });
        }
    }
})
</script>