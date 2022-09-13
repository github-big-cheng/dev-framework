/*
 * @Author: your name
 * @Date: 2021-05-07 10:05:30
 * @LastEditTime: 2021-07-28 10:14:13
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\utils\auth.js
 */
import Cookies from "js-cookie"

const TokenKey = window.location.origin + 'accessToken',
    UserUuid = window.location.origin + 'userUuid';


export const setToken = (token) => {
    return Cookies.set(TokenKey, token);
}

export const w = document.documentElement.clientWidth;
export const chartsColor = ['#FA8C16', '#FFC820', '#0AB6CA', '#118AF7']

export const getToken = () => {
    return Cookies.get(TokenKey);
}


export const removeToken = () => {
    return Cookies.remove(TokenKey);
}

export const setLocalStorage = (key, value) => {
    try {
        let ls = window.localStorage;
        if (value) {
            value = typeof value == "object" ? JSON.stringify(value) : value;
            ls.setItem(key, value);
        }
    } catch {
        console.log("此浏览器不支持本地储存");
    }
}

export const getLocalStorage = (key) => {
    let value = window.localStorage.getItem(key);
    try {
        let newValue = JSON.parse(value);
        if (typeof newValue == 'object' && newValue) {
            return newValue;
        } else {
            return value;
        }
    } catch {
        return value ? value : '';
    }
}


export const getUserUuid = () => {
    return Cookies.get(UserUuid)
}


export const setUserUuid = (value) => {
    return Cookies.set(UserUuid, value)
}


export const removeUserUuid = () => {
    return Cookies.remove(UserUuid)
}


export const clearLoginInfo = () => {
    if (window.localStorage) {
        window.localStorage.clear()
        removeToken()
        removeUserUuid()
    }
}