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

export default({
    name:"applyAdd",
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
                // {
                //     type: "input",
                //     label: "内网地址",
                //     prop: "inPath",
                //     value: "",
                //     class: 'single'
                // },
                // {
                //     type: "input",
                //     label: "外网地址",
                //     prop: "outPath",
                //     value: "",
                //     class: 'single'
                // },
                {
                    type: "input",
                    label: "key值",
                    prop: "keyValue",
                    value: "",
                    class: 'single'
                },
                {
                    type: "input",
                    label: "排序",
                    prop: "orderNo",
                    value: "",
                    class: 'single'
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
    watch: {
        code(val) {
            this.$refs.ruleFormBox.ruleForm.code = val;
        },
    },
    methods: {
        submit({handlerType, args}) {
            this[handlerType](args)
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
            this.$http.getUcenterProjectAdd(data).then(res => {
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