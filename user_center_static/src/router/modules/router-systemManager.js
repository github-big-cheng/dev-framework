/*
 * @Author: your name
 * @Date: 2021-06-24 15:59:09
 * @LastEditTime: 2021-08-11 18:13:56
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \gssp_fe\src\router\modules\router-systemManager.js
 */
import { RouteModule } from '@/utils';

const systemManager = {
    base_module: {
        path: "/systemManager",
        redirect: "/personList",
        meta: {title: "基础数据", icon: "el-icon-alisystem", path: ""}
    },

    //人员管理
    ucenter_person_list: {
        path: "/personList",
        name: "personList",
        component: () => import("@/views/systemManager/ucenterPerson"),
        meta: {title: "人员管理", icon: "", deepth: 1, path: "/personList"}
    },
    ucenter_person_add: {
        path: "/personAdd/:type",
        name: "personAdd",
        component: RouteModule( "systemManager/ucenterPerson/pageSave", "personAdd"),
        // component: () => import("@/views/systemManager/ucenterPerson/pageAdd"),
        meta: {title: "人员新增", icon: "", deepth: 2, path: "/personList", noWait: true}
    },
    ucenter_person_edit: {
        path: "/personEdit/:type/:id",
        name: "personEdit",
        component: RouteModule( "systemManager/ucenterPerson/pageSave", "personEdit"),
        // component: () => import("@/views/systemManager/ucenterPerson/pageEdit"),
        meta: {title: "人员编辑", icon: "", deepth: 2, path: "/personList"}
    },
    ucenter_person_view: {
        path: "/personView/:id",
        name: "personView",
        component: () => import("@/views/systemManager/ucenterPerson/pageView"),
        meta: {title: "人员查看", icon: "", deepth: 2, path: "/personList"}
    },

    //部门管理
    ucenter_org_list: {
        path: "/deptList",
        name: "ucenterDept",
        component: () => import("@/views/systemManager/ucenterDept"),
        meta: {title: "部门管理", icon: "", deepth: 1, path: "/deptList"}
    },
    ucenter_org_add: {
        path: "/deptList/pageAdd",
        name: "deptAdd",
        hidden: true,
        component: () => import("@/views/systemManager/ucenterDept/pageAdd"),
        meta: {title: "部门新增", icon: "", deepth: 2, path: "/deptList", noWait: true}
    },
    ucenter_org_edit: {
        path: "/deptList/pageEdit/:id",
        name: "deptEdit",
        hidden: true,
        component: () => import("@/views/systemManager/ucenterDept/pageEdit"),
        meta: {title: "部门编辑", icon: "", deepth: 2, path: "/deptList"}
    },
    ucenter_org_view: {
        path: "/deptList/pageView/:id",
        name: "deptView",
        hidden: true,
        component: () => import("@/views/systemManager/ucenterDept/pageView"),
        meta: {title: "部门查看", icon: "", deepth: 2, path: "/deptList"}
    },

    //部门调整
    ucenter_dept_person_list: {
        path: "/deptAdjustment",
        name: "deptAdjustment",
        component: () => import("@/views/systemManager/deptAdjustment"),
        meta: {title: "部门调整", icon: "", deepth: 1, path: "/deptAdjustment"}
    },
    ucenter_dept_person_add: {
        path: "/deptAdjustment/pageAdd",
        name: "deptAdjustmentAdd",
        hidden: true,
        component: () => import("@/views/systemManager/deptAdjustment/pageAdd"),
        meta: {title: "部门调整-调整", icon: "", deepth: 2, path: "/deptAdjustment", noWait: true}
    },

    //菜单管理
    ucenter_function_list: {
        path: "/menuManage",
        name: "menuManage",
        component: () => import("@/views/systemManager/menuManage"),
        meta: {title: "菜单管理", icon: "", deepth: 1, path: "/menuManage"}
    },
    ucenter_function_add: {
        path: "/menuManageAdd",
        name: "menuManageAdd",
        component: () => import("@/views/systemManager/menuManage/pageAdd"),
        meta: {title: "菜单管理-新增", icon: "", deepth: 1,noWait: true, path: "/menuManage"}
    },
    ucenter_function_save: {
        path: "/menuManageEdit/:id?",
        name: "menuManageEdit",
        component: () => import("@/views/systemManager/menuManage/pageEdit"),
        meta: {title: "菜单管理-编辑", icon: "", deepth: 1,path: "/menuManage"}
    },

    //权限管理
    ucenter_obj_func_list: {
        path: "/accessManage",
        name: "accessManage",
        component: () => import("@/views/systemManager/accessManage"),
        meta: {title: "权限管理", icon: "", deepth: 1, path: "/accessManage"}
    },

    //职位管理
    ucenter_position_list: {
        path: "/postManage",
        name: "postList",
        component: () => import("@/views/systemManager/ucenterPost"),
        meta: {title: "职位管理", icon: "", deepth: 1, path: "/postManage"}
    },
    ucenter_position_add: {
        path: "/postAdd/:type",
        name: "postAdd",
        component: RouteModule( "systemManager/ucenterPost/pageSave", "postAdd"),
        meta: {title: "职位新增", icon: "", deepth: 2, path: "/postManage", noWait: true}
    },
    ucenter_position_edit: {
        path: "/postEdit/:type/:id",
        name: "postEdit",
        component: RouteModule( "systemManager/ucenterPost/pageSave", "postEdit"),
        meta: {title: "职位编辑", icon: "", deepth: 2, path: "/postManage"}
    },
    ucenter_position_view: {
        path: "/postView/:id",
        name: "postView",
        hidden: true,
        component: () => import("@/views/systemManager/ucenterPost/pageView"),
        meta: {title: "职位查看", icon: "", deepth: 2, path: "/postManage"}
    },


    //角色管理
    ucenter_role_list: {
        path: "/roleList",
        name: "ucenterRole",
        component: () => import("@/views/systemManager/ucenterRole"),
        meta: {title: "角色管理", icon: "", deepth: 1, path: "/roleList"}
    },
    ucenter_role_add: {
        path: "/roleList/pageAdd",
        name: "roleAdd",
        hidden: true,
        component: () => import("@/views/systemManager/ucenterRole/pageAdd"),
        meta: {title: "角色新增", icon: "", deepth: 2, path: "/roleList", noWait: true}
    },
    ucenter_role_edit: {
        path: "/roleList/pageEdit/:id",
        name: "roleEdit",
        hidden: true,
        component: () => import("@/views/systemManager/ucenterRole/pageEdit"),
        meta: {title: "角色编辑", icon: "", deepth: 2, path: "/roleList"}
    },
    ucenter_role_view: {
        path: "/roleList/pageView/:id",
        name: "roleView",
        hidden: true,
        component: () => import("@/views/systemManager/ucenterRole/pageView"),
        meta: {title: "角色查看", icon: "", deepth: 2, path: "/roleList"}
    }
}

export default systemManager;