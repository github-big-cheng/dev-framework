/*
 * @Author: your name
 * @Date: 2021-05-10 15:55:02
 * @LastEditTime: 2021-08-06 17:08:55
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\api\modules\menu.js
 */
import Ajax from '@/utils/request';

const menu = {
    getMenuList: (params) => Ajax.get('/ucenter/menu/auth/tree', params),

    getUserMenuList: (params) => Ajax.get('/ucenter/menu/user/tree', params),

    getUserMenuView: (params) => Ajax.get('/sys/sysFunction/view', params),

    getMenuListChild: (params) => Ajax.get('/sys/sysFunction/list', params),

    ucenterFunctionAdd: (params) => Ajax.post('/sys/sysFunction/add', params),

    ucenterFunctionEdit: (params) => Ajax.post('/sys/sysFunction/save', params),

    ucenterFunctionDel: (params) => Ajax.post('/sys/sysFunction/delete', params)
}

export default menu;
