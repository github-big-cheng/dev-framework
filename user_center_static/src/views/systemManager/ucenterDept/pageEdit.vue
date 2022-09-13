<template>
    <div class="main-content-wrap inner-maincon">
        <form-com
            ref="ruleFormBox"
            :config="formConfigs"
            :form-data="formData"
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
                <el-input ref="rCode" type="text" v-model="connectCode" placeholder="" clearable />
            </template>
        </form-com>
    </div>
</template>

<script>
import formCom from "@/components/form-com";
import { formConfigs, formBtns } from "./config/add";
import vPinyin from "@/utils/v-py.js";

export default {
    name: "deptEdit",
    components: {
        formCom,
    },
    data() {
        return {
            connectName: "",
            connectCode: "",
            baseFormConfigs: [],
            formConfigs,
            formData: null,
            formBtns,
        };
    },
    created() {
        this.getFormData();
    },
    watch: {
        connectCode(val) {
            this.$refs.ruleFormBox.ruleForm.code = val;
        },
    },
    methods: {
        //btn
        cancelClick() {
            this.goBack(this.$route);
        },
        submit({ handlerType }) {
            this[handlerType]();
        },
        getInputValue(val) {
            this.connectCode = vPinyin.chineseToPinYin(val);
            this.$refs.ruleFormBox.ruleForm.cname = val;
        },
        //回显
        getFormData() {
            let id = this.$route.params.id;
            this.$http
                .getUcenterOrgView({ id })
                .then((res) => {
                    this.closeLoading(this.$route);
                    if (res.code == 0) {
                        let { data } = res;
                        this.formData = data;
                        this.connectName = data.cname;
                        this.connectCode = data.code;
                    }
                })
                .catch(() => this.closeLoading(this.$route));
        },
        //submit
        async submitForm() {
            let { status, data } = await this.$refs.ruleFormBox.getFormAndValidate();
            if (!status) {
                this.$refs[data[0].field]?.focus();
                return;
            }
            this.MXsetBtnLoading(this.formBtns, true);
            let id = this.$route.params.id;
            const sendFormData = {
                ...data,
                id,
            };
            this.$http
                .getUcenterOrgEdit(sendFormData)
                .then((res) => {
                    if (res.code == 0) {
                        this.$showSuccess(res.message);
                        this.goBack(this.$route, true);
                    }
                    this.MXsetBtnLoading(this.formBtns, false);
                })
                .catch((err) => {
                    this.MXsetBtnLoading(this.formBtns, false);
                });
        },
    },
};
</script>
