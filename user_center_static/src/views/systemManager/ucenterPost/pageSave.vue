<template>
    <div class="main-content-wrap inner-maincon">
        <FormCom
            ref="form"
            :form-data="formData"
            :config="formConfig"
            :isform-btn="true"
            @submit="submit"
            :form-btn="formBtns"
            :formInline="false"
        />
    </div>
</template>

<script>
import FormCom from "@/components/form-com";
import vPinyin from "@/utils/v-py.js";
import {formConfig , formBtns} from './config/add';
export default {
    components: {
        FormCom,
    },
    data() {
        return {
            formConfig: formConfig({}),
            formBtns,
            formData: {},
        };
    },
    mounted() {
        const { id } = this.$route.params;
        if(id){
            this.requestView(id);
        }
        this.formConfig = formConfig({
            getLinkageVal: this.getLinkageVal,
        });
        
    },
    methods: {
        getLinkageVal(val) {
            this.$refs.form.setFormValue("code", vPinyin.chineseToPinYin(val));
        },
        submit(item, list) {
            if (item.handlerType === 'submitForm') {
                this.buttonManage(item, list, true);
                this.onSave(item, list);
            } else {
                this.cancelClick();
            }
        },
        buttonManage(item, list, state) {
            list.forEach((i) => (i.disabled = state));
            item.btnLoading = state;
        },
        async onSave(item, list) {
            try {
                let { status, data } = await this.$refs.form.getFormAndValidate();
                if (!status) {
                    this.buttonManage(item, list, false);
                    return;
                }
                const formData = {
                    ...data,
                };
                const res = await this.$http[data.id ? "getPositionEdit" : "getPositionAdd"](formData);
                const { code, message } = res;

                if (+code !== 0) return;
                this.$showSuccess(message);
                this.goBack(this.$route, true);
            } catch (error) {}

            this.buttonManage(item, list, false);
        },
        async requestView(id) {
            try {
                const { data } = await this.$http.getPositionView({ id });
                this.formData = data;
            } catch (error) {}
        },
        cancelClick() {
            this.goBack(this.$route);
        },
    },
};
</script>
