<template>
    <div class="header">
        <div class="head-left">
            <h1><img src="@/assets/logo.png"/>用户中心</h1>
            <div class="navWrapper">
                <el-button
                        size="mini"
                        type="success"
                        :class="item.id === menuId ? 'curNav' : ''"
                        @click="setRoute(item)"
                        :icon="'el-icon-ali' + item.code"
                        v-for="item in allMenuList"
                        :key="item.id"
                >{{ item.name }}
                </el-button
                >
            </div>
        </div>

        <!-- <svg-icon iconClass="logo"/> -->
        <div class="h-operation">
            <div class="top-menu">
<!--                <ul>-->
<!--                    <li title="消息" @click="handleNotificatCenter">-->
<!--                        <span>-->
<!--                            <i class="el-icon-alinews"></i>-->
<!--                            <el-badge v-if="noticeSum" :value="noticeSum" :max="99" class="item"> </el-badge>-->
<!--                        </span>-->
<!--                    </li>-->
<!--                     <li title="下载" @click="handleDownloadCenter">-->
<!--            <span><i class="el-icon-alidownload"></i></span>-->
<!--                     </li>-->
<!--                </ul>-->
            </div>
            <el-dropdown class="avatar-container" trigger="click">
                <div class="user-avatar-msg">
                    <div class="user-avatar">
                        <el-avatar icon="el-icon-aliuser" :src="avatarImg"></el-avatar>
                    </div>
                    <div class="user-info">
                        {{ name }}
                        <i class="el-icon-aliarrdown"></i>
                    </div>
                </div>
                <el-dropdown-menu class="header-user-dropdown" slot="dropdown">
                    <div class="user-account-info">
                        <div class="user-avatar">
                            <el-avatar
                                    icon="el-icon-aliuser"
                                    :src="Object.keys(userManageInfo).length ? userManageInfo.avatarImg : avatarImg"
                            ></el-avatar>
                        </div>
                        <div class="user-info" style="width: 0; flex: 1">
                            <p>
                                <span class="user-name">{{ name }}</span>
                                <span class="work-post" v-if="workPost">{{ workPost }}</span>
                            </p>
                            <p class="work-depart">
                                {{
                                Object.keys(userManageInfo).length ? userManageInfo.workDepartment : workDepartment
                                }}
                            </p>
                        </div>
                    </div>
                    <el-dropdown-item divided>
                        <a href="javascript:;" @click="_ModifyPwd"> <i class="el-icon-alipassword"></i>修改密码 </a>
                    </el-dropdown-item>
                    <el-dropdown-item>
                        <a href="javascript:;" style="width: 100%; height: 100%; display: inline-block" @click="logout">
                            <span><i class="el-icon-aliexit"></i>退出登录</span>
                        </a>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
    import {mapGetters} from "vuex";
    import {getLocalStorage, getToken} from "@/utils/auth";
    import {resetRouter} from "@/router";
    import {requestUrl} from "@/api/api";

    export default {
        data() {
            return {
                isShow: true,
                workPost: "",
                workDepartment: "",
                name: "",
                avatarImg: "", //默认头像
            };
        },
        created() {
            if (getLocalStorage("userInfo") && getToken()) {
                let userInfo = getLocalStorage("userInfo");
                let {deptName, imgPath, personName, positionName} = userInfo;
                this.workDepartment = deptName;
                this.name = personName;
                this.workPost = positionName;
                this.avatarImg = imgPath ? requestUrl + "/file" + imgPath : this.avatarImg;
            }
            this.$store.dispatch("GetNoticeSum");
        },
        computed: {
            ...mapGetters(["sidebar", "userManageInfo", "noticeSum", "allMenuList", "menuList", "menuListMap", "menuId"]),
        },
        methods: {
            setRoute(menu) {
                //清空页签
                if (menu.id === this.menuId) {
                    //后期需更改
                    this.next(menu);
                    return;
                }
                //后期需更改
                this.next(menu);
            },
            next(menu) {
                this.$store.dispatch("tagsView/delAllViews").then(({visitedViews}) => {
                    let menuList = this.menuListMap.get(menu.id);
                    let path = this.findPath(menuList[0]);
                    if (path) {
                        this.$store.commit("SET_MENULIST", menuList);
                        this.$store.commit("SET_MENUID", menu.id);
                        this.$router.push(path);
                    }
                });
            },

            findPath(menu) {
                if (!menu) {
                    return null;
                }

                let path = menu.redirect || menu.path;
                let matched = this.$router.match(path);
                if (matched && matched.name) {
                    return path;
                }

                if (!menu.children || !menu.children.length) {
                    return null;
                }

                return this.findPath(menu.children[0]);
            },

            _ModifyPwd() {
                this.$store.dispatch("ModifyDialog", true);
            },
            logout() {
                this.$store
                    .dispatch("LogOut")
                    .then(() => {
                        this.$router.replace({name: "login"});
                        resetRouter();
                    })
                    .catch((err) => {
                        console.log("LogOut", err);
                    });
            },
            goView(name) {
                this.$router.push({
                    name: name,
                    params: {
                        noCache: true,
                    },
                });
            },
            handleNotificatCenter() {
                this.goView("noticeManager");
            },
            handleDownloadCenter() {
                this.goView("downloadCenter");
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/styles/header.scss";
</style>
