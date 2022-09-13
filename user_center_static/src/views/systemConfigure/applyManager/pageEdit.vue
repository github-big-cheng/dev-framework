<template>
    <div class="main-content-wrap inner-maincon">
        <form-com
            ref="ruleFormBox"
            :config="baseFormConfigs"
            :isformBtn="true"
            :formBtn="formBtns"
            @submit="submit"
        >
        </form-com>
    </div>
</template>

<script>
import formCom from "@/components/form-com";

export default({
    name:"applyEdit",
    components: {
        formCom
    },
    data() {
        return {
            baseFormConfigs:[],
            formConfigs: [
                {
                    type: 'input',
                    label: '名称',
                    prop: "name",
                    value: '',
                    rules: {
                        require: true
                    },
                    class: 'single'
                },
                {
                    type: 'input',
                    label: '代码',
                    prop: "code",
                    value: '',
                    rules: {
                        require: true
                    },
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
    created() {
        this.getFormData();
    },
    methods: {
        submit({handlerType, args}) {
            this[handlerType](args)
        },
        //回显
        getFormData() {
            let id = this.$route.params.id;
            this.$http.getUcenterProjectView({ id }).then((res) => {
                this.closeLoading(this.$route);
                if (res.code == 0) {
                    let {data} = res;
                    this.formConfigs.forEach(item => {
                        let value = data[item.prop];
                        item.value = value;
                        this.baseFormConfigs.push(item)
                        if(this.hasOwnProperty(item.prop)) {
                            this[item.prop] = item.value;
                        }
                    })
                }
            }).catch(() => this.closeLoading(this.$route));
        },
        //btn
        cancelClick(){
            this.goBack(this.$route)
        },
        async submitForm(type) {
            if (type == 0) this.goBack(this.$route);
            let {status, data} = await this.$refs.ruleFormBox.getFormAndValidate()
            if (!status) {
                this.$refs[data[0].field].focus();
                return;
            }
            this.MXsetBtnLoading(this.formBtns, true);
            this.$http.getUcenterProjectEdit({
                id: this.$route.params.id,
                ...data
            }).then(res => {
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