<template>
    <div>
        <dialog-com
                iconfont="el-icon-alicolumn-tit"
                :btnLoading="btnLoading"
                :dialogVisible="modifyDialog"
                @cancelClick="initModify"
                @trueClick="handleTrueClick"
                title="修改密码"
                class="modify-password"
        >
            <p class="tippwd-text" v-if="firstLogin == 1">
                <i class="el-icon-aliwarn"></i>为了您账号的安全，请修改初始密码！
            </p>
            <form-com ref="ruleFormBox" :config="fromConfig" :columnNum="`row-col1`"></form-com>
        </dialog-com>
    </div>
</template>

<script>
    import DialogCom from '@/components/dialog';
    import formCom from "@/components/form-com";
    import {getLocalStorage} from '@/utils/auth';

    export default {
        name: 'modifyPadss',
        components: {
            DialogCom,
            formCom
        },
        data() {
            return {
                firstLogin: 0,
                newPassword: "",
                fromConfig: [
                    {
                        type: "password",
                        label: "原密码",
                        prop: "password",
                        value: '',
                        checkType: 'oldPwd',
                        // focus: true,
                        rules: {
                            require: true
                        }
                    },
                    {
                        type: "password",
                        label: "新密码",
                        prop: "newPassword",
                        value: '',
                        checkType: 'newPwd',
                        rules: {
                            require: true
                        }
                    },
                    {
                        type: "password",
                        label: "确认新密码",
                        prop: "newPwdConfirm",
                        value: '',
                        checkType: 'newPwdConfirm',
                        rules: {
                            require: true
                        }
                    },
                ],
                btnLoading: false
            }
        },
        mounted() {
            this.firstLogin = getLocalStorage('userInfo')?.firstLogin;
        },
        computed: {
            modifyDialog() {
                return this.$store.state.user.modifier;
            },
        },
        methods: {
            async handleTrueClick() {
                let {status, data} = await this.$refs.ruleFormBox.getFormAndValidate()
                if (!status) {
                    this.$refs[data[0].field].focus();
                    return;
                }
                const {newPassword, newPwdConfirm} = data;
                if (newPwdConfirm !== newPassword) {
                    this.$showWarning('两次输入密码不一致');
                    return;
                }
                this.isTestingInput = true;
                this.$refs.ruleFormBox.$refs.formCom.validate((valid) => {
                    if (valid) {
                        this.btnLoading = true;
                        const {password, newPassword} = data;
                        let params = {
                            password: window.btoa(password),
                            newPassword: window.btoa(newPassword)
                        };
                        this.$http.getUpdatePassword(params)
                            .then((res) => {
                                if (res.code == 0) {
                                    this.initModify();
                                    this.$showSuccess('修改成功,3秒后跳转到登录页！');
                                    setTimeout(() => {
                                        this.$store
                                            .dispatch('LogOut')
                                            .then(() => {
                                                this.$router.replace({name: 'login'});
                                                resetRouter();
                                            })
                                            .catch((err) => {
                                                console.log('LogOut', err);
                                            });
                                    }, 3000);
                                    this.btnLoading = false;
                                }
                            }).catch(() => {
                            this.btnLoading = false;
                        });
                    } else {
                        return false;
                    }
                });
            },
            initModify() {
                this.$store.dispatch('ModifyDialog', false);
            },
        }
    }
</script>

<style lang="scss" scoped>
    @import "@/styles/modify-password.scss";
</style>

