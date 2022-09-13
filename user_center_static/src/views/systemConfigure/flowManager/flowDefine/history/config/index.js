export const searchList = {
    easy: {
        placeholder: '请输入流程定义名称',
        prop: 'processDefinitionNameQueryLike'
    }
}

export const btnConfigs = [
    {
        text: '删除',
        icon: 'el-icon-aliback',
        has: 'flow_define_delete',
        handlerType: 'delFlowModel',
    },
    {
        text: '刷新',
        icon: 'el-icon-alirefresh',
        handlerType: 'refresh'
    },
]

export const tableTitle = [
    {
        colKey: "processDefineName",
        prop: "processDefineName",
        label: "流程定义名称",
        minWidth: 45
    },
    
    {
        colKey: "version",
        prop: "version",
        label: "版本号",
        minWidth: 30
    },
    {
        colKey: "category",
        prop: "category",
        label: "流程分类",
        minWidth: 45,
    },
    {
        colKey: "key",
        prop: "key",
        label: "模型标识",
        minWidth: 45
    },
    {
        colKey: "processDefineId",
        prop: "processDefineId",
        label: "流程定义id",
        minWidth: 100,
    },
]