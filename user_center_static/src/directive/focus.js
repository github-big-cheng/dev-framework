/*
 * @Author: your name
 * @Date: 2021-05-08 10:57:41
 * @LastEditTime: 2021-05-08 11:07:37
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\directive\focus.js
 */
import Vue from 'vue'

Vue.directive('focus', {
    inserted(el, val) {
        // 聚焦元素
        if (val.value) {
            let dom = el.querySelector('input') || el.querySelector('textarea');
            dom.focus()
        }
    }
})
