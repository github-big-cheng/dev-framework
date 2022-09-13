/*
 * @Author: your name
 * @Date: 2021-06-02 09:56:59
 * @LastEditTime: 2021-09-27 09:23:09
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\permission.js
 */
import router from "./router/index"
import store from "./store/index"

import MProgress from "nprogress"
import "nprogress/nprogress.css"; 


import { Message } from "element-ui";

import { getToken, getLocalStorage} from "./utils/auth"

const whiteList = ["/login", "/redirectLogin"];
router.beforeEach(async (to,from,next) => {
    MProgress.start();
    if(getToken() && getLocalStorage("userInfo")){
        if(to.path === "/login"){
            next();
            MProgress.done();
        }else{
            if(store.getters.menuList && store.getters.menuList.length === 0){
                store.dispatch("Getinfo")
                    .then(() => {
                        next({...to,replace:true});
                    }).catch((e) => {
                        store
                            .dispatch("FedLogOut")
                            .then(() => {
                                Message.error("验证失败,请重新登录");
                                next({ path: "/login" });
                            });
                    })
            }else{
                next();
                MProgress.done();
            }
        }
    }else{ 
        if(whiteList.indexOf(to.path) !== -1){
            next();
            MProgress.done();
        }else{  
            next("/login");
            MProgress.done();
        }
    }
})
