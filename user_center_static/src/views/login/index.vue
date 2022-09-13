<template>
    <div class="login-wrapper">
        <div class="header">
            <h1 class="logo"><img src="@/assets/logo.png"/>用户中心</h1>
        </div>
        <div class="login-maincon" id="bannerImg">
            <p class="banner-text">
                <span v-for="(text, index) in curText.split(' ')" :key="index">{{text}}</span>
            </p>
            <!-- <el-carousel class="banner" :interval="5000">
                <el-carousel-item v-for="(item, index) in bannerText" :key="index">
                    <p class="banner-text">
                        <span v-for="(text, index) in item.split(' ')" :key="index">{{text}}</span>
                    </p>
                </el-carousel-item>
            </el-carousel> -->
            <div class="login-con">
                <!-- <span class="login-switch" @click="switchLogin">
                    <i class="el-icon-aliqrcode" title="扫码登录" v-if="loginMode"></i>
                    <i class="el-icon-alicomputer" title="普通登录" v-if="!loginMode"></i>
                </span>
                <div class="scan-login" v-if="!loginMode">
                    <h3>手机扫码，安全登录</h3>
                    <div class="dynamic-qrcode" v-if="!isScanSuccess">
                        <div class="img">
                            <img :src="qrCodeImage" />
                            <div class="qrcode-invalid" v-show="isQrCodeValid">
                                <p>二维码已失效</p>
                                <a href="javascript:void(0)" @click="qrcodeRefresh">请点击刷新</a>
                            </div>
                        </div>
                        <p><i class="el-icon-aliscan"></i>打开手机端扫一扫登录</p>
                    </div>
                    <div class="qrcode-success" v-if="isScanSuccess">
                        <div class="img-success"><img src="@/assets/images/img-login-success.png" /></div>
                        <p>扫码成功!</p>
                        <p class="tip">请在手机上确认登录</p>
                        <div class="link"><a href="javascript:void(0)" @click="qrcodeRefresh">返回二维码登录</a></div>
                    </div>
                </div> -->
                <el-form
                    class="login-form"
                    autocomplete="on"
                    :model="loginForm"
                    :rules="loginRules"
                    ref="loginForm"
                    :class="isTestingInput && !loginRules.username ? 'i-err' : ''"
                    v-if="loginMode"
                >
                    <h3 class="title">用户登录</h3>
                    <div class="bd">
                        <span class="error-tip" v-show="iptErr"><i class="el-icon-alierror"></i>{{ errMsg }}</span>
                        <span class="warning-tip" v-show="iptwarn"><i class="el-icon-aliwarn"></i>{{ warnMsg }}</span>
                        <el-form-item prop="username">
                            <el-input
                                class="input-text"
                                name="userId"
                                type="text"
                                autocomplete="on"
                                placeholder="用户名"
                                v-model="loginForm.username"
                                ref="touchFocus"
                                prefix-icon="el-icon-aliusername" 
                                @keyup.native.enter="focusNext('password')"
                            />
                        </el-form-item>
                        <el-form-item prop="password">
                            <el-input
                                class="input-text"
                                name="password"
                                type="password"
                                ref="password"
                                autocomplete="on"
                                placeholder="密码"
                                v-model="loginForm.password"
                                prefix-icon="el-icon-alipassword"
                                @keyup.enter.native="handleLoginClick('loginForm')"
                            />
                        </el-form-item>
                        <!-- <el-form-item class="auto-login"> -->
                            <!-- <el-checkbox v-model="checked">下次自动登录</el-checkbox> -->
                        <!-- </el-form-item> -->
                        <el-form-item>
                            <el-button
                                class="login-btn"
                                type="primary"
                                :loading="logining"
                                @click.native.prevent="handleLoginClick('loginForm')"
                            >
                                {{ logining ? "登录中" : "登 录" }}
                            </el-button>
                        </el-form-item>
                    </div>
                </el-form>
            </div>
        </div>
        <div class="login-describe">
            <ul>
                <li v-for="(item, index) in describes" :key="index">
                    <i :class="item.iconClass"></i>
                    {{ item.text }}
                </li>
            </ul>
        </div>
        <div class="footer">
            <p class="safe-text"><i class="el-icon-alisafe"></i>安全保密提示：本系统严禁上传、处理涉密文件资料及敏感信息</p>
            <!-- <p>{{copyRight}}</p> -->
        </div>
    </div>
</template>

<script>
import { apiUrl } from "@/api/api";
import { clearLoginInfo, getLocalStorage } from "@/utils/auth";
import moment from "moment";
import bannerPic from '@/assets/images/login-bg.jpg';
import bannerPic1 from '@/assets/images/login-bg1.jpg';
import bannerPic2 from '@/assets/images/login-bg2.jpg';

export default {
    name: "login",
    data() {
        let year = moment().format("YYYY");
        return {
            copyRight: "©COPYRIGHT" + year + "，SHNWZFZD ALL RIGHTS RESERVED.",
            qrCode: "",
            qrCodeImage: "",
            isQrCodeValid: false,
            isScanSuccess: false,
            checked: false,
            isTestingInput: false,
            logining: false,
            iptErr: false,
            iptwarn: false,
            errMsg: "",
            warnMsg: "",
            loginMode: true,
            loginForm: {
                username: "",
                password: "",
                vCode: "",
            },
            loginRules: {
                password: [{ required: true, trigger: "blur", message: "请输入密码" }],
                username: [{ required: true, trigger: "blur", message: "请输入用户名" }],
            },
            countdown: 120,
            describes: [
               // { id: 1, iconClass: "el-icon-aliplan", text: "依法行政　勤勉务实" },
               // { id: 2, iconClass: "el-icon-alimonitor", text: "在线办公　信息共享 " },
               // { id: 3, iconClass: "el-icon-aliread", text: "公文快速批阅" },
               // { id: 4, iconClass: "el-icon-aliiclockin", text: "移动考勤　方便快捷" },
            ],
            bannerBg: [bannerPic, bannerPic1, bannerPic2],
            // bannerText:['欢 迎 OA 系 统 上 线', '全 新 OA 省 时 高 效','协 同 办 公 方 便 快 捷'],
            curText: "",
            timer: null
        };
    },
    created() {
        if (this.loginMode) {
            this.$nextTick(() => {
                this.$refs["touchFocus"].focus();
            });
        }
    },
    mounted() {
        if (localStorage && getLocalStorage("userInfo")) {
            clearLoginInfo();
            location.reload();
        }

        let curIndex = this.bannerBgChange();
        this.timer = setInterval(() => {
            curIndex = this.bannerBgChange(curIndex);
        }, 8000);
    },
    methods: {
        choiseIndex(len, curIndex) {
            let num = Math.floor(Math.random()*len);
            if (num != curIndex) {
                return num;
            }
            return this.choiseIndex(len, curIndex);
        },

        bannerBgChange(curIndex){
            const index = this.choiseIndex(this.bannerBg.length, curIndex);
            let bannerImg = document.getElementById("bannerImg");
            if(bannerImg !== null ){
                bannerImg.style.backgroundImage="url("+this.bannerBg[index]+")"; 
            }
            this.curText = this.bannerText[index]; 

            return index;
        },
        focusNext(nextRef) {
            this.$refs[nextRef].focus();
        },
        switchLogin() {
            this.loginMode = !this.loginMode;
            if (this.loginMode) {
                this.$nextTick(() => {
                    this.$refs["touchFocus"].focus();
                });
            } else {
                this.qrcodeStart();
                this.goPay();
            }
        },
        handleLoginClick(formName) {
            this.isTestingInput = true;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.logining = true;
                    let password = window.btoa(this.loginForm.password);
                    let saveData = {
                        ...this.loginForm,
                        password,
                    };
                    this.$store
                        .dispatch("Login", saveData)
                        .then((res) => {
                            if (res) {
                                if (res.firstLogin === 1) {
                                    this.$store.dispatch("ModifyDialog", true);
                                }
                                this.$store
                                    .dispatch("GetMenuList")
                                    .then((menuRes) => {
                                        if (menuRes) {
                                            //默认展示第一个应用模块的菜单
                                            // this.$store.commit('SET_PROJECTID', menuRes.data.length ? menuRes.data[0].id: null)
                                            let menu = menuRes.data || [];
                                            this.$setMenulist(this, menu);
                                            this.$store.dispatch("tagsView/delAllViews");
                                            this.$router.replace("/");
                                        }
                                        this.logining = false;
                                    })
                                    .catch((menuErr) => {
                                        this.logining = false;
                                        this.$message({
                                            type: "error",
                                            message: menuErr.message ? menuErr.message : "登录失败，请重试！",
                                        });
                                    });
                            }
                        })
                        .catch((err) => {
                            this.logining = false;
                            this.iptErr = true;
                            this.errMsg = err.message;
                            this.iptwarn = false;
                        });
                } else {
                    this.iptwarn = true;
                    this.iptErr = false;
                    this.warnMsg = "请输入账号或密码";
                    // this.$showWarning("请输入账号或密码");
                    return false;
                }
            });
        },
        qrcodeStart() {
            this.isQrCodeValid = false;
            this.isScanSuccess = false;
            this.$http.getQrcodeGenerate({}).then((res) => {
                if (res && res.code == 0) {
                    let { qrImage, qrCode } = res.data;
                    this.isQrCodeValid = false;
                    this.qrCode = qrCode;
                    this.qrCodeImage = "data:image/png;base64," + qrImage;
                    this.webSocket();
                }
            });
        },
        // 二维码2分钟失效倒计时
        payTimeoutCount() {
            let _this = this;
            if (this.countdown >= 0) {
                if (this.countdown == 0) {
                    this.isQrCodeValid = true;
                }
                --this.countdown;
            } else {
                window.clearInterval(_this.paytimerout);
                this.isQrCodeValid = true;
                this.countdown = 120;
            }
        },
        //开始倒计时
        goPay() {
            let _this = this;
            _this.paytimerout = window.setInterval(function() {
                _this.payTimeoutCount();
            }, 1000);
        },
        // 清空计时器
        closeCodeMask() {
            let _this = this;
            window.clearInterval(_this.paytimerout);
        },
        qrcodeRefresh() {
            this.qrcodeStart();
            this.goPay();
        },
        webSocket() {
            if ("WebSocket" in window) {
                // 打开一个 web socket
                const wsUrl = apiUrl.replace("http://", "ws://").replace("https://", "ws://") + '/sso' + "/websocket/qrCode";
                const ws = new WebSocket(wsUrl);
                let _that = this;

                ws.onopen = function() {
                    // Web Socket 已连接上，使用 send() 方法发送数据
                    const json = JSON.stringify({ msgType: "register", qrCode: _that.qrCode });
                    ws.send(json);
                };

                ws.onmessage = function(evt) {
                    let json = JSON.parse(evt.data);
                    if (json && json.status) {
                        switch (json.status) {
                            case "2":
                                _that.logining = true;
                                _that.$store.dispatch("QrCodeLogin", json.user).then((res) => {
                                    if (res) {
                                        _that.$store
                                            .dispatch("GetMenuList")
                                            .then((menuRes) => {
                                                if (menuRes) {
                                                    _that.$store.dispatch("tagsView/delAllViews");
                                                    _that.$router.replace("/");
                                                }
                                                _that.logining = false;
                                            })
                                            .catch((menuErr) => {
                                                _that.logining = false;
                                                _that.$message({
                                                    type: "error",
                                                    message: menuErr.message ? menuErr.message : "登录失败，请重试！",
                                                });
                                            });
                                        this.close(); // 关闭websocket
                                    }
                                });
                                break;
                            case "1":
                                _that.isScanSuccess = true;
                                break;
                            case "0":
                            default:
                                _that.isScanSuccess = false;
                                break;
                        }
                    }
                };

                ws.onclose = function() {
                    // 关闭 websocket
                    console.log("连接已关闭...");
                };
            } else {
                // 浏览器不支持 WebSocket
                console.log("您的浏览器不支持 WebSocket，请选择账号密码方式登录!");
            }
        },
    },
    beforeDestroy(){
        clearInterval(this.timer);
        this.timer = null;
    }
};
</script>

<style lang="scss" scoped>
@import "@/styles/login.scss";
</style>
