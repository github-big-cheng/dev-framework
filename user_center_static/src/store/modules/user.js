import {
    getToken,
    setToken,
    setUserUuid,
    clearLoginInfo,
    setLocalStorage,
    getLocalStorage
} from "@/utils/auth";
import api from "@/api/api"
import lodash from 'lodash'
import {
    createRouter
} from "@/router/addRouter";
import {
    getMenuList
} from "@/utils/formatMenu";
import router, { resetRouter } from "@/router";

const user = {
    state: {
        token: getToken(),
        name: "",
        uuid: "",
        userId: "",
        menus: [],
        roles: [],
        routers: [],
        menuList: [],
        allMenu: [],
        modifier: false,
        userManageInfo: {},
        projectId: null,
        menuListMap: new Map(),
        menuId: null,
    },
    actions: {
        Login({
            commit
        }, userForm) {
            let handleForm = {
                username: userForm.username,
                password: userForm.password
            };
            return new Promise((resolve, reject) => {
                api.login(handleForm)
                    .then(res => {
                        if (res.code == 0) {
                            let { data } = res;
                            let token = data.ticket;
                            setLocalStorage("userInfo", data)
                            commit("SET_USERID", data.id);
                            setToken(token);
                            commit("SET_TOKEN", token);
                            resolve(data);
                        } else {
                            reject(res);
                        }
                    })
                    .catch(err => {
                        reject(err);
                    })
            })
        },
        QrCodeLogin({
            commit
        }, user) {
            return new Promise((resolve, reject) => {
                if (user) {
                    let token = user.ticket;
                    setLocalStorage("userInfo", user)
                    setToken(token);
                    commit("SET_TOKEN", token);
                    resolve(user);
                } else {
                    reject({ code: -1, message: '登录失败，请重试' });
                }
            })
        },
        RedirectLogin({commit}, token) {
            return new Promise((resolve, reject) => {
                api.redirectLogin({token})
                    .then(res => {
                        if(res.code == 0){
                            let { data } = res;
                            setLocalStorage("userInfo", data)
                            commit("SET_USERID", data.id);
                            let _token = data.ticket;
                            setToken(_token);
                            commit("SET_TOKEN", _token);
                            resolve(data);
                        }else{
                            reject(res);
                        }
                    }).catch(err => {
                        reject(err);
                    })
            })
        },
        GetMenuList({
            commit
        }) {
            return new Promise((resolve, reject) => {
                api.getUserMenuList()
                    .then(response => {
                        if (response && response.data) {
                            // response.data = response.data[1].menus;
                            // let routers = createRouter(response.data || []);
                            // let newData = lodash.cloneDeep(response.data);
                            // let menuList = getMenuList(newData || []);
                            // router.addRoutes(routers);
                            // commit("SET_ROUTERS", routers);
                            // setLocalStorage("menuList", menuList || []);
                            // commit("SET_MENULIST", menuList);
                            // setLocalStorage("menuData", response.data || []);
                            commit("SET_ALLMENU", response.data)
                            setLocalStorage("allMenu", response.data)
                            resolve(response);

                        } else {
                            reject("您没有当前系统操作权限。");
                        }
                    })
                    .catch(error => {
                        reject(error);
                    });
            })
        },
        Getinfo({
            commit
        }) {
            return new Promise((resolve, reject) => {
                let userInfo,
                    menuList,
                    allMenu,
                    menuId,
                    menuData;
                if (window.localStorage) {
                    userInfo = getLocalStorage("userInfo");

                    if (!userInfo) {
                        reject(false);
                        return;
                    }

                    // 用户信息
                    setToken(userInfo.ticket);
                    setUserUuid(userInfo.userUuid);
                    commit("SET_NAME", userInfo.userName);
                    commit("SET_USERID", userInfo.id);
                    commit("SET_UUID", userInfo.userUuid);
                    commit("SET_TOKEN", userInfo.ticket);

                    // 菜单
                    menuData = getLocalStorage("menuData");
                    menuList = getLocalStorage("menuList");
                    allMenu = getLocalStorage("allMenu");
                    menuId = getLocalStorage("menuId");
                    if (menuData) {
                        commit("SET_MENUID", menuId)
                        
                        commit("SET_ALLMENU", allMenu);

                        let menuMap = new Map(), menuLists = [];
                        allMenu.forEach(item => {
                            let data = getMenuList(lodash.cloneDeep(item.subFunction))
                            menuMap.set(item.id, data)
                            menuLists.push(...item.subFunction)
                        })
                        let mapItem = menuMap.get(Number(menuId))
                        if (!mapItem || !mapItem.length) {
                            // reject(false);
                            // return;
                        }
                        commit("SET_MENULIST", mapItem);
                        commit('SET_MENULIST_MAP', menuMap || new Map())
                        let routers = createRouter(allMenu || []);
                        resetRouter()
                        routers.length && router.addRoutes(routers);
                        commit("SET_ROUTERS", routers);

                        resolve(true);
                    }
                } else {
                    reject("is not localStorage");
                }
            })
        },
        AddMenus: ({ commit }, menus) => {
            commit("SET_MENUS", menus);
        },
        // 登出
        LogOut({
            commit,
            state
        }) {
            return new Promise((resolve, reject) => {
                api.loginOut({})
                    .then(res => {
                        commit("SET_ROLES", "");
                        commit("SET_MENUS", "");
                        commit("SET_MENULIST", "");
                        commit("SET_NAME", "");
                        commit("SET_USERID", "");
                        commit("SET_UUID", "");
                        commit("SET_TOKEN", "");
                        clearLoginInfo();
                        resolve();
                    })
                    .catch(error => {
                        reject(error);
                    });
            });
        },
        // 前端 登出
        FedLogOut({
            commit
        }) {
            return new Promise(resolve => {
                commit("SET_ROLES", "");
                commit("SET_MENUS", "");
                commit("SET_NAME", "");
                commit("SET_USERID", "");
                commit("SET_UUID", "");
                commit("SET_TOKEN", "");
                clearLoginInfo();
                resolve();
            });
        },
        //修改密码弹窗状态
        ModifyDialog({
            commit
        }, boolean) {
            commit("SET_MODIFIER", boolean);
        },
        // 人员信息变更
        ChangeUserManageInfo({
            commit
        }, userManageInfo) {
            commit("SET_USERMANAGEINFO", userManageInfo);
        }
    },
    mutations: {
        SET_MENUID: (state, id) => {
            state.menuId = id;
        },
        SET_MENULIST_MAP: (state, map) => {
            state.menuListMap = map;
        },
        SET_PROJECTID: (state, id) => {
            state.projectId = id
        },
        SET_ALLMENU: (state, data) => {
            state.allMenu = data
        },
        SET_TOKEN: (state, token) => {
            state.token = token;
        },
        SET_NAME: (state, name) => {
            state.name = name;
        },
        SET_USERID: (state, userId) => {
            state.userId = userId;
        },
        SET_UUID: (state, uuid) => {
            state.uuid = uuid;
        },
        SET_MENUS: (state, menus) => {
            state.menus = menus;
        },
        SET_ROLES: (state, roles) => {
            state.roles = roles;
        },
        SET_ROUTERS: (state, routers) => {
            state.routers = routers;
        },
        SET_MODIFIER: (state, status) => {
            state.modifier = status;
        },
        SET_MENULIST(state, menuList) {
            state.menuList = menuList;
        },
        SET_USERMANAGEINFO(state, userManageInfo) {
            state.userManageInfo = userManageInfo;
        }
    }
}

export default user;
