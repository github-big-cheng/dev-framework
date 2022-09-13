/*
 * @Author: your name
 * @Date: 2021-05-07 10:05:30
 * @LastEditTime: 2021-08-24 13:49:59
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\api\api.js
 */


export const isProduction = process.env.NODE_ENV === 'production';
export const requestUrl = isProduction ? window.location.origin : process.env.VUE_APP_BASE_API;


export const apiUrl = window.location.origin;

const modulesFiles = require.context('./modules', true, /\.js$/)
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
    const value = modulesFiles(modulePath)

    Object.assign(modules, value.default)
    return modules
}, {})
// 
let Api = {...modules}
export default Api;
