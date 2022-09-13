export const btnConfigs = [
    {
        type: "add",
        text: "新增",
        icon: "el-icon-aliadd",
        has: "ucenter_org_add",
        handlerType: "handleAddClick",
    },
    {
        type: "modify",
        text: "修改",
        icon: "el-icon-alimodify",
        has: "ucenter_org_edit",
        handlerType: "handleEditClick",
    },
    {
        type: "remove",
        text: "删除",
        icon: "el-icon-aliback",
        has: "ucenter_org_delete",
        handlerType: "handleDeleteClick",
    },
    {
        type: "refresh",
        text: "刷新",
        icon: "el-icon-alirefresh",
        handlerType: "getTableList",
    },
];
export const titList = [
    {
        colKey: "name",
        prop: "cname",
        label: "名称",
        width: null,
        orderBy: "cname",
        asc: "",
        type: "click",
        isOperation: true,
    },
    {
        colKey: "code",
        prop: "code",
        label: "代码",
        width: null,
        orderBy: "code",
        asc: "",
    },
    {
        colKey: "shortName",
        prop: "sname",
        label: "简称",
        minWidth: 40,
    },
    {
        colKey: "contactPerson",
        prop: "linker",
        label: "联系人",
        minWidth: 40,
    },
    {
        colKey: "orderNo",
        prop: "orderNo",
        label: "顺序号",
        minWidth: 40,
    },
    {
        colKey: "orgName",
        prop: "orgName",
        label: "机关（单位）",
        width: null,
    },
];
