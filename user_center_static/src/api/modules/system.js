/*
 * @Author: your name
 * @Date: 2021-06-07 18:14:37
 * @LastEditTime: 2021-08-13 14:50:37
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\api\modules\system.js
 */
import Ajax from '@/utils/request';

const system = {
    //人员管理列表
    getUcenterPersonList:(params) => Ajax.get('/ucenter/ucenterPerson/list', params),
    //添加
    getUcenterPersonAdd:(params) => Ajax.post('/ucenter/ucenterPerson/add', params),
    //编辑
    getUcenterPersonEdit:(params) => Ajax.post('/ucenter/ucenterPerson/save', params),
    //删除
    getUcenterPersonDelete:(params) => Ajax.get('/ucenter/ucenterPerson/delete', params),
    //查看
    getUcenterPersonView:(params) => Ajax.getWithLoading('/ucenter/ucenterPerson/view', params),
    //重置密码
    getUcenterPersonResetPwd:(params) => Ajax.get('/ucenter/ucenterPerson/resetPwd', params),
    //锁定
    getUcenterPersonLock:(params) => Ajax.get('/ucenter/ucenterPerson/lock', params),
    //解锁
    getUcenterPersonUnlock:(params) => Ajax.get('/ucenter/ucenterPerson/unlock', params),
    //注销
    getUcenterPersonDestroy:(params) => Ajax.get('/ucenter/ucenterPerson/destroy', params),


    //部门列表
    getUcenterOrgList:(params) => Ajax.get(`/ucenter/ucenterOrg/list`, params),
    //新增
    getUcenterOrgAdd:(params) => Ajax.post(`/ucenter/ucenterOrg/add`, params),
    //编辑
    getUcenterOrgEdit:(params) => Ajax.post(`/ucenter/ucenterOrg/save`, params),
    //查看
    getUcenterOrgView:(params) => Ajax.getWithLoading(`/ucenter/ucenterOrg/view`, params),
    //删除
    getUcenterOrgDelete:(params) => Ajax.get(`/ucenter/ucenterOrg/delete`, params),


    // 部门调整列表
    getUcenterDeptpersonList:(params) => Ajax.get(`/ucenter/ucenterOrg/list`, params),

    getDeptAdjustmentList:(params, type) => Ajax.get(`/ucenter/ucenterDeptPerson/list`, params),
    // 部门调整新增
    getUcenterDeptpersonAdd:(params) => Ajax.post(`/ucenter/ucenterDeptPerson/add`, params),
    // 部门调整编辑
    getUcenterDeptpersonEdit:(params) => Ajax.post(`/ucenter/ucenterDeptPerson/save`, params),
    // 部门调整查看
    getUcenterDeptpersonView:(params) => Ajax.getWithLoading(`/ucenter/ucenterDeptPerson/view`, params),
    
    
    //角色列表
    getUcenterRoleList:(params) => Ajax.getWithLoading('/ucenter/ucenterRole/list', params),
    //新增
    getUcenterRoleAdd:(params) => Ajax.post('/ucenter/ucenterRole/add', params),
    //编辑
    getUcenterRoleEdit:(params) => Ajax.post('/ucenter/ucenterRole/save', params),
    //查看
    getUcenterRoleView:(params) => Ajax.getWithLoading('/ucenter/ucenterRole/view', params),
    //删除
    getUcenterRoleDelete:(params) => Ajax.get('/ucenter/ucenterRole/delete', params),
    //树
    getUcenterRoleCombox:(params) => Ajax.get('/ucenter/ucenterRole/combox', {...params, pageSize: 10000}),


    //职位列表-list
    getPositionList: (params) => Ajax.get('/ucenter/ucenterPosition/list', params),
    //职位列表-combox
    getPositionCombox: (params) => Ajax.get('/ucenter/ucenterPosition/combox', params),
    //新增
    getPositionAdd: (params) => Ajax.post('/ucenter/ucenterPosition/add', params),
    //编辑
    getPositionEdit: (params) => Ajax.post('/ucenter/ucenterPosition/save', params),
    //查看
    getPositionView: (params) => Ajax.getWithLoading('/ucenter/ucenterPosition/view', params),
    //删除
    getPositionDelete: (params) => Ajax.post('/ucenter/ucenterPosition/delete', params),

    //code
    getUcenterCodeCombox:(params) => Ajax.get('/sys/sysCode/combox', params),

    
    
    //界面配置
    getUcenterpageconfigList:(params) => Ajax.get('/sys/sysColumnConfig/list', params),
    getUcenterpageconfigFullList:(params) => Ajax.get('/sys/sysColumnConfig/fullList', params),
    getUcenterpageconfigSave:(params) => Ajax.post('/sys/sysColumnConfig/save', params),
    getUcenterpageconfigReset:(params) => Ajax.post('/sys/sysColumnConfig/reset', params)
}

export default system;
