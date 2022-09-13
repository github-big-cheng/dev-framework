/*
 * @Author: your name
 * @Date: 2021-08-17 18:27:08
 * @LastEditTime: 2021-08-19 15:59:59
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \nwxt_oa\src\directive\hasPermission.js
 */
import Vue from 'vue'
import { getLocalStorage } from "@/utils/auth";


const has = Vue.directive('has', {
    inserted: function (el, binding) {
        let btnPermission = [];
        if (window.localStorage) {
            btnPermission = getLocalStorage('btnArr');
        }
        
        if (binding.value && btnPermission && btnPermission.indexOf(binding.value) < 0) {
            el.parentNode && el.parentNode.removeChild(el);
        }
    }
});


export default {
    install(Vue, options) {
        Vue.prototype.$has = has
    }
};