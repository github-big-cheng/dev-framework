/*
 * @Author: your name
 * @Date: 2021-05-10 15:55:02
 * @LastEditTime: 2021-08-02 16:01:36
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\api\modules\menu.js
 */
import Ajax from '@/utils/request';

const menu = {
    getMenuList: (params) => Ajax.get('/sys/sysFunction/list/tree', params),

    projectCombox: (params) => Ajax.get('/sys/sysProject/list/combox', params),

    getUcenterobjfunc: (params) => Ajax.getWithLoading('/ucenter/ucenterObjFunc/list', params),

    functionSave: (params) => Ajax.post('/ucenter/ucenterObjFunc/save', params),

    ucenterroleList: (params) => Ajax.get('/ucenter/ucenterRole/list', params),
}

export default menu;