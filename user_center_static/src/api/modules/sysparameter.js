/*
 * @Author: your name
 * @Date: 2021-08-06 11:23:33
 * @LastEditTime: 2021-08-13 10:47:34
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\api\modules\sysparameter.js
 */
import Ajax from '@/utils/request';

const sysParameter = {
    //系统参数配置
    sysParameterList: (params) => Ajax.post('/sys/sysParameter/list', params),
    sysParameterSave: (params) => Ajax.post('/sys/sysParameter/save', params),
    sysParameterCombox: (params) => Ajax.post('/sys/sysParameter/combox', params),
    sysParameterLoad: (params) => Ajax.post('/sys/sysParameter/load', params),
    //代码管理列表
    getUcenterCodeList: (params) => Ajax.get('/sys/sysCode//list', params),
    //添加
    getUcenterCodeAdd: (params) => Ajax.post('/sys/sysCode/add', params),
    //编辑
    getUcenterCodeEdit: (params) => Ajax.post('/sys/sysCode/save', params),
    //查看
    getUcenterCodeView: (params) => Ajax.getWithLoading('/sys/sysCode/view', params),
    //删除
    getUcenterCodeDelete: (params) => Ajax.post('/sys/sysCode/delete', params),


    //日志管理
    getUcenterLogList: (params) => Ajax.get('/ucenter/ucenterLog/list', params),
    //操作日志
    getSysOperationLogList: (params) => Ajax.get('/sys/sysOperationLog/list', params),


    //应用管理列表
    getUcenterProjectList: (params) => Ajax.get('/sys/sysProject/list', params),
    //新增
    getUcenterProjectAdd: (params) => Ajax.post('/sys/sysProject/add', params),
    //编辑
    getUcenterProjectEdit: (params) => Ajax.post('/sys/sysProject/save', params),
    //查看
    getUcenterProjectView: (params) => Ajax.getWithLoading('/sys/sysProject/view', params),
    //删除
    getUcenterProjectDelete: (params) => Ajax.post('/sys/sysProject/delete', params),

    //流程管理-列表
    getFlowModelList: (params) => Ajax.getWithLoading('/activiti/model/list', params),
    // -新增
    AddFlowModel: (params) => Ajax.post('/activiti/model/add', params),
    // -删除
    delFlowModel: (params) => Ajax.post('/activiti/model/delete', params),
    // 部署
    deployFlowModel: (params) => Ajax.post('/activiti/model/deploy', params),
    // 详情
    detailFlowModel:(params) => Ajax.getWithLoading('/activiti/model/list', params),
    // 编辑 
    EditFlowModel: (params) => Ajax.post('/activiti/model/save', params),

    //流程定义管理-列表
    flowDefineList:(params) => Ajax.getWithLoading('/oa/oaFlowDefine/list', params),
    // 新增
    flowDefineAdd: (params) => Ajax.post('/oa/oaFlowDefine/add', params),
    // 编辑
    flowDefineSave: (params) => Ajax.post('/oa/oaFlowDefine/save', params),
    // 删除
    flowDefineDelete: (params) => Ajax.post('/oa/oaFlowDefine/delete', params),
    // 查看
    flowDefineView: (params) => Ajax.getWithLoading('/oa/oaFlowDefine/view', params),

    
    //历史流程定义
    getProcessDefineList: (params) => Ajax.getWithLoading('/activiti/processDefine/page', params),
    // -删除
    delProcessDefine: (params) => Ajax.post('/activiti/processDefine/delete', params),

    //流程实例管理
    flowInstanceList: (params) => Ajax.getWithLoading('/oa/oaFlow/list', params),
}

export default sysParameter;
