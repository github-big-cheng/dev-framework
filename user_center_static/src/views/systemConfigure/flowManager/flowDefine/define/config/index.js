import { filterBtnShow } from '@/utils/common.js';

export const searchList = {
    easy: {
        placeholder: '请输入名称或类型',
        prop: 'nameOrTypeQueryLike'
    },
}

export const btnConfigs = [
    {
        type: 'add',
        text: '新增',
        icon: 'el-icon-aliadd',
        has: 'oa_flow_define_add',
        handlerType: 'add',
    },
    {
        type: 'modify',
        text: '修改',
        icon: 'el-icon-alimodify',
        has: 'oa_flow_define_edit',
        handlerType: 'save',
    },
    {
        type: 'remove',
        text: '删除',
        loading: false,
        icon: 'el-icon-alitrash',
        has: 'oa_flow_define_delete',
        url: 'flowDefineDelete',
        handlerType: 'delete',
    },
    {
        type: 'refresh',
        text: '刷新',
        icon: 'el-icon-alirefresh',
        handlerType: 'refresh'
    }
]

export const tableTitle = [
    {
        colKey: "orgName",
        prop: "orgName",
        label: "机关(单位)",
        minWidth: 60
    },
    {
        colKey: "bizType",
        prop: "bizTypeName",
        label: "类型",
        minWidth: 30
    },
    {
        colKey: "bizName",
        prop: "bizName",
        label: "流程名称",
        minWidth: 45,
        type: "click",
        isOperation: filterBtnShow(["oa_flow_define_view"]),
    },
    {
        colKey: "defineId",
        prop: "defineId",
        label: "流程id",
        minWidth: 120
    },
    {
        colKey: "createTime",
        prop: "createTime",
        label: "创建时间",
        minWidth: 30
    },
    {
        colKey: "createBy",
        prop: "createByName",
        label: "创建人",
        minWidth: 30
    },


]