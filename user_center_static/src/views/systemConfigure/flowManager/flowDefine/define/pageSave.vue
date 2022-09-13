<template>
    <div class="main-content-wrap inner-maincon">
        <FormCom
            ref="form"
            :form-data="formData"
            :config="formConfig"
            :isform-btn="true"
            @submit="submit"
            :form-btn="formButton"
            :formInline="false"
        />
    </div>
</template>

<script>
import FormCom from "@/components/form-com";
import { formConfig, formButton } from "./config/add";
export default {
    components: {
        FormCom,
    },
    data() {
        return {
            id: null,
            formConfig,
            formButton,
            formData: {},
        };
    },
    mounted() {
        const { id } = this.$route.params;
        if (id) {
            this.id = id;
            this.requestView(id);
        }
    },
    methods: {
        async onSave(isSubmit, item, list) {
            try {
                const { data, status } = await this.$refs.form.getFormAndValidate();
                if (!status) {
                    this.buttonManage(item, list, false);
                    return;
                }
                const formData = {
                    ...data,
                    id: this.id,
                    isSubmit,
                    attachments: JSON.stringify(data.attachments),
                };

                const { code, message } = await this.$http[this.id ? "flowDefineSave" : "flowDefineAdd"](formData);

                if (+code !== 0) return;
                this.$showSuccess(message);
                this.goBack(this.$route, true);
            } catch (error) {}
            this.buttonManage(item, list, false);
        },
        async requestView(id) {
            try {
                const { data } = await this.$http.flowDefineView({ id });
                this.formData = data;
            } catch (error) {}
        },
        submit(item, list) {
            if (item.submitType !== undefined) {
                this.buttonManage(item, list, true);
                this.onSave(item.submitType, item, list);
            } else {
                this.cancelClick();
            }
        },
        buttonManage(item, list, state) {
            list.forEach((i) => (i.disabled = state));
            item.btnLoading = state;
        },
        cancelClick() {
            this.goBack(this.$route);
        },
    },
};
</script>
