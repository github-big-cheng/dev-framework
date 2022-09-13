/*
 * @Author: your name
 * @Date: 2021-05-07 15:16:08
 * @LastEditTime: 2021-05-12 13:59:58
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\icons\index.js
 */
import Vue from 'vue'
import SvgIcon from '@/components/svg-icon'// svg component

// register globally
Vue.component('svg-icon', SvgIcon)

const req = require.context('./svg', false, /\.svg$/)
const requireAll = requireContext => requireContext.keys().map(requireContext)
requireAll(req)