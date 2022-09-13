<template>
    <div class="main-content-wrap inner-maincon">
        <form-com
            ref="ruleFormBox"
            :config="formConfigs"
            :isformBtn="true"
            :formBtn="formBtns"
            @submit="submit"
        >
            <template #rName>
                 <el-input
                    type="text"
                    ref="name"
                    v-model="name"
                    placeholder=""
                    clearable
                    v-focus="true"
                    @input="getInputValue"
                />
            </template>
            <template #rCode>
                <el-input
                    ref="code"
                    type="text"
                    v-model="code"
                    placeholder=""
                    clearable
                />
            </template>
        </form-com>
    </div>
</template>

<script>
import formCom from "@/components/form-com";
import vPinyin from "@/utils/v-py.js";

export default({
    name:"roleAdd",
    components: {
        formCom
    },
    data() {
        return {
            name:"",
            code:"",
            btnLoading: false,
            saveLoading: false,
            formConfigs: [
                {
                    type: 'slot',
                    label: '名称',
                    prop: "name",
                    value: '',
                    rules: {
                        require: true
                    },
                    slotName: 'rName',
                    class: 'single'
                },
                {
                    type: 'slot',
                    label: '代码',
                    prop: "code",
                    value: '',
                    rules: {
                        require: true
                    },
                    slotName: 'rCode',
                    class: 'single'
                },
                {
                    type: "input",
                    typeName: 'textarea',
                    label: "备注",
                    prop: "memo",
                    value: "",
                    class: 'item-remark',
                    maxlength: "100",
                    placeholder:""
                }
            ],
            formBtns:[
                {
                    btnLoading:false,
                    btnText:"取消",
                    handlerType:"cancelClick",
                    args: 0,
                },
                {
                    type: 'primary',
                    btnLoading:false,
                    btnText:"保存",
                    handlerType:"submitForm",
                    args: 1,
                }
            ]
        }
    },
    watch: {
        code(val) {
            this.$refs.ruleFormBox.ruleForm.code = val;
        }
    },
    methods: {
        getInputValue(val) {
            this.code = vPinyin.chineseToPinYin(val);
            this.$refs.ruleFormBox.ruleForm.name = val;
        },
        //btn
        cancelClick(){
            this.goBack(this.$route)
        },
        submit({handlerType, args}) {
            this[handlerType](args)
        },
        async submitForm(type) {
            if(type == 0) this.goBack(this.$route);
            let {status, data} = await this.$refs.ruleFormBox.getFormAndValidate()
            if (!status) {
                this.$refs[data[0].field].focus();
                return;
            }
            this.MXsetBtnLoading(this.formBtns, true);
            this.$http.getUcenterRoleAdd(data).then(res => {
                if (res.code == 0) {
                    this.$showSuccess(res.message);
                    this.goBack(this.$route, true);
                }
                this.MXsetBtnLoading(this.formBtns, false);
            }).catch((err) => {
                this.MXsetBtnLoading(this.formBtns, false);
                
            });
        }
    }
})
</script>