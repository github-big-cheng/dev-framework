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
                    v-model="connectName"
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
                    v-model="connectCode"
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
import { formConfigs, formBtns } from "./config";

export default({
    name:"postAdd",
    components: {
        formCom
    },
    data() {
        return {
            formConfigs:formConfigs({}),
            formBtns,
            connectName:"",
            connectCode:"",
            btnLoading: false,
            saveLoading: false,
        }
    },
    mounted() {
        this.getComboxList();
    },
    watch: {
        connectCode(val) {
            this.$refs.ruleFormBox.ruleForm.code = val;
        }
    },
    methods: {
        submit({handlerType, args}) {
            this[handlerType](args)
        },
        getComboxList() {
            this.$http.getUcenterCodeCombox({type: '10001-12006'}).then((res) => {
                const {code, data} = res;
                if (code == 0) {//职称类别
                    this.formConfigs = formConfigs({jobTypeChildren:data})
                }
            });
        },
        getInputValue(val) {
            this.connectCode = vPinyin.chineseToPinYin(val);
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
            this.$http.getPositionAdd(data).then(res => {
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