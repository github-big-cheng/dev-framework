export const searchList = {
    easy: {
        placeholder: '请输入标题',
        prop: 'contentQueryLike'
    }
}

export const btnConfigs = [
    {
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
        minWidth: 50
    },
    {
        colKey: "bizType",  
        prop: "bizTypeName",
        label: "类型",
        minWidth: 40
    },
    {
        colKey: "title",  
        prop: "title",
        label: "标题",
        width: null
    },
    {
        colKey: "status",
        prop: "status",
        label: "状态",
        minWidth: 30,
        type: 'isSlot',
        slotName: 'status'
    },
    {
        colKey: "taskDefineKey",
        prop: "taskName",
        label: "当前任务节点",
        minWidth: 50,
    },
    {
        colKey: "createTime",
        prop: "applyTime",
        label: "申请时间",
        minWidth: 50,
    },
    {
        colKey: "completeTime",
        prop: "completeTime",
        label: "完成时间",
        minWidth: 60,
    }
]

export const scopeRowStatusColor = {
    "21002-10": "cbrown", // 已保存
    "21002-20": "corange", // 已提交
    "21002-30": "cgreen", // 审核中
    "21002-40": "cgreen", // 审核通过
    "21002-50": "cgreen", // 归档申请
    "21002-60": "cgray", // 已归档
    "21002-70": "cred", // 已驳回
    "21002-80": "cgray", // 已撤销
    "21002-90": "cred", // 反对
    "21002-100": "cgray", // 无需归档
}
