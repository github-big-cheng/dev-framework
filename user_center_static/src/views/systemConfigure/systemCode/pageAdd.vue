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
                    ref="name"
                    type="text"
                    v-model="name"
                    placeholder=""
                    v-focus="true"
                    clearable
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

export default{
    name:"codeAdd",
    components: {
        formCom
    },
    data() {
        return {
            name:"",
            code:"",
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
                    // class: 'single'
                },
                {
                    type: "input",
                    label: "国际化",
                    prop: "locale",
                    value: "zh_CN"
                },
                {
                    type: "select",
                    label: "类别",
                    prop: "type",
                    value: "",
                    children:[],
                    changeCB: this.computerCodeValue,
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
                    changeCB: this.computerCodeValue,
                    slotName: 'rCode',
                    class: 'single'
                },
                {
                    type: 'input',
                    label: '代码值',
                    prop: "value",
                    value: '',
                    rules: {
                        require: true
                    },
                    class: 'single'
                },
                {
                    type: "input",
                    label: "路径名称",
                    prop: "pathValue",
                    value: "",
                    class: 'single'
                },
                {
                    type: "input",
                    label: "顺序号",
                    prop: "orderNo",
                    value: "",
                    class: 'single'
                },
                {
                    type: "radio",
                    label: "系统/用户",
                    prop: "isSys",
                    value: "",
                    children: [
                        {
                            value: "1",
                            name: "系统"
                        },
                        {
                            value: "0",
                            name: "用户"
                        }
                    ],
                    disabled: true,
                },
            ],
            formBtns:[
                {
                    btnLoading:false,
                    btnText:"取消",
                    handlerType:"cancelClick",
                },
                {
                    type: 'primary',
                    btnLoading:false,
                    btnText:"保存",
                    handlerType:"submitForm",
                }
            ]
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.$refs.ruleFormBox.setFormValue("isSys", '0');
        })
        this.getComboxList();
    },
    watch: {
        code(val) {
            this.$refs.ruleFormBox.ruleForm.code = val;
            this.computerCodeValue()
        },
    },
    methods: {
        computerCodeValue() {
            let ruleForm = this.$refs.ruleFormBox.ruleForm;
            let type = ruleForm["type"].split("-")[1];
            let code = ruleForm["code"];
            let typeCodeValue= "";
            if(type && code){
                typeCodeValue = type + "-" + code;
            }
            this.$refs.ruleFormBox.setFormValue("value", typeCodeValue);
        },
        submit({handlerType, args}) {
            this[handlerType](args)
        },
        setFormConfigs(index, data, attr="children"){
            this.formConfigs[index][attr] = data;
        },
        getComboxList() {
            this.$http.getUcenterCodeCombox({typeQueryIsNull: 1}).then((res) => {
                const {code, data} = res;
                if (code == 0) {
                    this.setFormConfigs(2, data);
                }
            });
        },
        getInputValue(val) {
            this.code = vPinyin.chineseToPinYin(val);
            this.$refs.ruleFormBox.ruleForm.name = val;
        },
        //btn
        cancelClick(){
            this.goBack(this.$route)
        },
        async submitForm(type) {
            if(type == 0) this.goBack(this.$route);
            let {status, data} = await this.$refs.ruleFormBox.getFormAndValidate()
            if (!status) {
                this.$refs[data[0].field].focus();
                return;
            }
            this.MXsetBtnLoading(this.formBtns, true);
            this.$http.getUcenterCodeAdd(data).then(res => {
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
}
</script>