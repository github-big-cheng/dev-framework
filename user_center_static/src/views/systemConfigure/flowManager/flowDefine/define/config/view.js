export const viewConfig = (data = {}) => [
    {
        label: "业务类型",
        content: data.bizTypeName,
    },
    {
        label: "流程名称",
        content: data.bizName,
    },

    {
        label: "分组名称",
        content: data.groupName,
    },
    {
        label: '回调地址',
        content: data.backUrl,
    },
    {
        label: "流程定义ID",
        content: data.defineId,
    },
    {
        label: "创建人",
        content: data.createByName,
    },
    {
        label: "创建时间",
        content: data.createTime,
    },
    {
        label: "修改人",
        content: data.updateByName,
    },
    {
        label: "修改时间",
        content: data.updateTime,
    },
];
