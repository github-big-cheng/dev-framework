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
                    ref="cname"
                    type="text"
                    v-model="connectName"
                    placeholder=""
                    clearable
                    @input="getInputValue"
                    v-focus="true"
                />
            </template>
            <template #rCode>
                <el-input
                    ref="rCode"
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
import {formConfigs, formBtns} from './config/add';
import vPinyin from "@/utils/v-py.js";

export default({
    name:"deptAdd",
    components: {
        formCom,
    },
    data() {
        return {
            connectName:"",
            connectCode:"",
            formConfigs,
            formBtns
        }
    },
    watch: {
        connectCode(val) {
            this.$refs.ruleFormBox.ruleForm.code = val;
        }
    },
    mounted() {
        const {parentId, parentValue} = this.$route.params;
        this.$refs.ruleFormBox.setFormValue('parentId',parentId);
        this.$refs.ruleFormBox.setFormValue('parentValue',parentValue);
    },
    methods: {
        submit({handlerType}) {
            this[handlerType]()
        },
        getInputValue(val) {
            this.connectCode = vPinyin.chineseToPinYin(val);
            this.$refs.ruleFormBox.ruleForm.cname = val;
        },
        //btn
        cancelClick(){
            this.goBack(this.$route)
        },
        async submitForm() {
            let {status, data} = await this.$refs.ruleFormBox.getFormAndValidate()
            if (!status) {
                this.$refs[data[0].field].focus();
                return;
            }
            this.MXsetBtnLoading(this.formBtns, true)
            // Object.assign(data, {compType: '10027-10'})
            this.$http.getUcenterOrgAdd(data).then(res => {
                if (res.code == 0) {
                    this.$showSuccess(res.message);
                    
                    this.goBack(this.$route, true);
                }
                this.MXsetBtnLoading(this.formBtns, false)
            })
            .catch((err) => {
                this.MXsetBtnLoading(this.formBtns, false)
                
            });
        }
    }
})
</script>