<template>
    <div class="step-loading">
        <div class="loader" v-if="!showClose">
            <div class="timerWrap">
                <svg version="1.1" viewBox="131.623 175.5 120 160" preserveAspectRatio="xMinYMin meet" class="timer">
                    <path
                        fill="#FFFFFF"
                        d="M212.922,255.45l36.855-64.492c1.742-3.069,1.742-6.836-0.037-9.896c-1.783-3.06-5.037-4.938-8.581-4.938
        h-99.158c-3.524,0-6.797,1.878-8.569,4.938c-1.773,3.06-1.792,6.827-0.03,9.896l36.846,64.491l-36.845,64.492
        c-1.762,3.068-1.743,6.836,0.03,9.896c1.772,3.061,5.044,4.938,8.569,4.938h99.158c3.544,0,6.798-1.878,8.581-4.938
        c1.779-3.06,1.779-6.827,0.037-9.896L212.922,255.45z M142.001,324.86l39.664-69.41l-39.664-69.41h99.158l-39.663,69.41
        l39.663,69.41H142.001z"
                    />
                </svg>
            </div>
        <p>跳转中...</p>
        </div>
        <p v-else class="error"> <i class="el-icon-circle-close"></i>{{ errorMessage }}</p>
    </div>
</template>

<script>
export default {
    name: "redirectLogin",
    data() {
        return {
            errorMessage: "",
            showClose: false,
        };
    },
    created() {
        let { token } = this.$route.query;
        if (!token) {
          this.showClose = true;
          this.errorMessage = "登录验证不通过，请重试！";
          return ;
        }
        this.redirectLogin(window.btoa(token));
    },
    methods: {
        redirectLogin(token) {
            this.$store
                .dispatch("RedirectLogin", token)
                .then((res) => {
                    this.$store
                        .dispatch("GetMenuList")
                        .then((menuRes) => {
                            if (menuRes) {
                              let menu = menuRes.data || [];
                              this.$setMenulist(this, menu);
                              this.$store.dispatch("tagsView/delAllViews");
                              this.$router.replace("/");
                            }
                        })
                        .catch((menuErr) => {
                            this.$message({
                                type: "error",
                                message: menuErr.message ? menuErr.message : "登录信息获取异常，请联系管理员！",
                            });
                        });
                })
                .catch((err) => {
                    this.showClose = true;
                    this.errorMessage = err.message;
                });
        },
    },
};
</script>
<style lang="scss" scoped>
@-webkit-keyframes timerRotate {
    0% {
        transform: rotateZ(0deg);
    }
    50% {
        transform: rotateZ(0deg);
    }
    100% {
        transform: rotateZ(-180deg);
    }
}
@-moz-keyframes timerRotate {
    0% {
        transform: rotateZ(0deg);
    }
    50% {
        transform: rotateZ(0deg);
    }
    100% {
        transform: rotateZ(-180deg);
    }
}
@-ms-keyframes timerRotate {
    0% {
        transform: rotateZ(0deg);
    }
    50% {
        transform: rotateZ(0deg);
    }
    100% {
        transform: rotateZ(-180deg);
    }
}
@keyframes timerRotate {
    0% {
        transform: rotateZ(0deg);
    }
    50% {
        transform: rotateZ(0deg);
    }
    100% {
        transform: rotateZ(-180deg);
    }
}
@-webkit-keyframes bottomFill {
    0% {
        border-bottom-width: 0px;
    }
    50% {
        border-bottom-width: 60px;
    }
    100% {
        border-bottom-width: 60px;
    }
}
@-moz-keyframes bottomFill {
    0% {
        border-bottom-width: 0px;
    }
    50% {
        border-bottom-width: 60px;
    }
    100% {
        border-bottom-width: 60px;
    }
}
@-ms-keyframes bottomFill {
    0% {
        border-bottom-width: 0px;
    }
    50% {
        border-bottom-width: 60px;
    }
    100% {
        border-bottom-width: 60px;
    }
}
@keyframes bottomFill {
    0% {
        border-bottom-width: 0px;
    }
    50% {
        border-bottom-width: 60px;
    }
    100% {
        border-bottom-width: 60px;
    }
}
@-webkit-keyframes topEmpty {
    0% {
        top: 10px;
        border-top-width: 60px;
        border-left-width: 35px;
        border-right-width: 35px;
    }
    50% {
        top: 120px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
    }
    100% {
        top: 120px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
    }
}
@-moz-keyframes topEmpty {
    0% {
        top: 10px;
        border-top-width: 60px;
        border-left-width: 35px;
        border-right-width: 35px;
    }
    50% {
        top: 120px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
    }
    100% {
        top: 120px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
    }
}
@-ms-keyframes topEmpty {
    0% {
        top: 10px;
        border-top-width: 60px;
        border-left-width: 35px;
        border-right-width: 35px;
    }
    50% {
        top: 120px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
    }
    100% {
        top: 120px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
    }
}
@keyframes topEmpty {
    0% {
        top: 10px;
        border-top-width: 60px;
        border-left-width: 35px;
        border-right-width: 35px;
    }
    50% {
        top: 120px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
    }
    100% {
        top: 120px;
        border-top-width: 0px;
        border-left-width: 0px;
        border-right-width: 0px;
    }
}
.step-loading{
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    text-align: center;
}
.loader {
    display: flex;
    align-items: center;
    .timerWrap {
        position: relative;
        animation: timerRotate 2s infinite ease;
        width: 120px;
        height: 160px;
        left: 50%;
        margin-left: -120px;
        &:before {
            content: "";
            width: 0;
            height: 0;
            border-style: solid;
            border-width: 60px 35px 0 35px;
            border-color: #e08f24 transparent transparent transparent;
            position: absolute;
            top: 10px;
            left: 0;
            right: 0;
            margin: auto;
            animation: topEmpty 2s infinite ease;
            -webkit-animation: topEmpty 2s infinite ease;
            -moz-animation: topEmpty 2s infinite ease;
            -ms-animation: topEmpty 2s infinite ease;
        }
        &:after {
            content: "";
            width: 0;
            height: 0;
            border-style: solid;
            border-width: 0 35px 60px 35px;
            border-color: transparent transparent #e08f24 transparent;
            position: absolute;
            bottom: 10px;
            left: 0;
            right: 0;
            margin: auto;
            animation: bottomFill 2s infinite ease;
            -webkit-animation: bottomFill 2s infinite ease;
            -moz-animation: bottomFill 2s infinite ease;
            -ms-animation: bottomFill 2s infinite ease;
        }
        .timer {
            width: 120px;
            max-width: 100%;
            height: 160px;
            position: relative;
            top: 0;
            left: 0;
        }
        .timer path {
            fill: #3f6b9d;
        }
    }
    p {
        margin-top: 20px;
        text-align: center;
        padding-left: 300px;
        font-size: 40px;
    }
}

.error{
    text-align: center;
    font-size: 60px;
    i{
        display: block;
        margin-bottom: 50px;
        color: red;
        margin-right: 15px;
        vertical-align: middle;
        font-size: 200px;
    }
}
</style>
