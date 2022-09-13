/*
 * @Author: your name
 * @Date: 2021-05-08 13:33:49
 * @LastEditTime: 2021-08-12 18:04:29
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\router\modules\base-router.js
 */
import { RouteModule } from "@/utils";
const sysConfig = {
    system_module: {
        path: "/systemConfigure",
        redirect: "/sysParameterList",
        meta: { title: "系统配置", icon: "el-icon-alisystem", deepth: 1, path: "" }
    },

    //参数配置
    sys_parameter_list: {
        path: "/sysParameterList",
        name: "sysParameterList",
        component: () => import( /* webpackChunkName: 'sysParameterList' */"@/views/systemConfigure/sysParameterList"),
        meta: { title: "参数配置", icon: "", deep:1,noWait: true, path: "/sysParameterList" }
    },

    //系统代码
    sys_code_list: {
        path: "/systemCode",
        name: "codeList",
        component: () => import( /* webpackChunkName: 'systemCode/pageAdd' */"@/views/systemConfigure/systemCode"),
        meta: { title: "系统代码", icon: "", deep:1,noWait: true, path: "/systemCode" }
    },
    sys_code_add: {
        path: "/systemCode/pageAdd",
        name: "codeAdd",
        hidden: true,
        component: () => import( /* webpackChunkName: 'systemCode/pageAdd' */"@/views/systemConfigure/systemCode/pageAdd"),
        meta: {title: "系统代码新增", icon: "", deepth: 2, path: "/systemCode", noWait: true}
    },
    sys_code_edit: {
        path: "/systemCode/pageEdit/:id",
        name: "codeEdit",
        hidden: true,
        component: () => import( /* webpackChunkName: 'systemCode/pageEdit' */"@/views/systemConfigure/systemCode/pageEdit"),
        meta: {title: "系统代码编辑", icon: "", deepth: 2, path: "/systemCode"}
    },
    sys_code_view: {
        path: "/systemCode/pageView/:id",
        name: "codeView",
        hidden: true,
        component: () => import( /* webpackChunkName: 'systemCode/pageView' */"@/views/systemConfigure/systemCode/pageView"),
        meta: {title: "系统代码查看", icon: "", deepth: 2, path: "/systemCode"}
    },

    //日志管理
    ucenter_log_list: {
        path: "/logManager",
        name: "logList",
        component: () => import( /* webpackChunkName: 'logManager' */"@/views/systemConfigure/logManager"),
        meta: { title: "日志管理", icon: "", deep:1,noWait: true, path: "/logManager" }
    },


    //应用管理
    sys_project_list: {
        path: "/applyManager",
        name: "applyList",
        component: () => import( /* webpackChunkName: 'applyManager' */"@/views/systemConfigure/applyManager"),
        meta: { title: "应用管理", icon: "", deep:1,noWait: true, path: "/applyManager" }
    },
    sys_project_add: {
        path: "/applyManager/pageAdd",
        name: "applyAdd",
        hidden: true,
        component: () => import( /* webpackChunkName: 'applyManager/pageAdd' */"@/views/systemConfigure/applyManager/pageAdd"),
        meta: {title: "应用管理新增", icon: "", deepth: 2, path: "/applyManager", noWait: true}
    },
    sys_project_save: {
        path: "/applyManager/pageEdit/:id",
        name: "applyEdit",
        hidden: true,
        component: () => import( /* webpackChunkName: 'applyManager/pageEdit' */"@/views/systemConfigure/applyManager/pageEdit"),
        meta: {title: "应用管理编辑", icon: "", deepth: 2, path: "/applyManager"}
    },
    
    //流程管理
    flow_manager: {
        path: '/flowManager',
        redirect: "/flowManager/model",
        meta: { title: "流程管理", icon: "", deepth: 2, path: "" }
    },
    // 模型管理
    flow_model_list: {
        path: '/flowManager/model',
        name: 'flowModel',
        component: () => import("@/views/systemConfigure/flowManager/flowModel"),
        meta: { title: "模型管理", icon: "",  deepth: 2, path: "/flowManager/model" }
    },
    // 流程实例管理
    oa_flow_query: {
        path: '/flowManager/instance',
        name: 'flowInstance',
        component: () => import("@/views/systemConfigure/flowManager/flowInstance"),
        meta: { title: "流程实例管理", icon: "",  deepth: 2, path: "/flowManager/instance" }
    },
    // 流程定义管理
    flow_define_manager: {
        path: '/flowDefineManager',
        name: 'flowDefine',
        component: () => import("@/views/systemConfigure/flowManager/flowDefine"),
        meta: { title: "流程定义管理", icon: "",  deepth: 2, path: "/flowDefineManager" }
    },
    oa_flow_define_add: {
        path: "/flowDefineAdd/:type",
        name: "flowDefineAdd",
        component: RouteModule("systemConfigure/flowManager/flowDefine/define/pageSave", "flowDefineAdd"),
        meta: {title: "流程定义管理新增", icon: "", deepth: 2, path: "/flowDefineManager", noWait: true}
    },
    oa_flow_define_edit: {
        path: "/flowDefineEdit/:type/:id",
        name: "flowDefineEdit",
        component: RouteModule("systemConfigure/flowManager/flowDefine/define/pageSave", "flowDefineEdit"),
        meta: {title: "流程定义管理编辑", icon: "", deepth: 2, path: "/flowDefineManager"}
    },
    oa_flow_define_view: {
        path: "/flowDefineView/:id",
        name: "flowDefineView",
        hidden: true,
        component: () => import( /* webpackChunkName: 'flowManager/flowDefine/view' */"@/views/systemConfigure/flowManager/flowDefine/define/pageView"),
        meta: {title: "流程定义管理查看", icon: "", deepth: 2, path: "/flowDefineManager"}
    },

};

export default sysConfig;