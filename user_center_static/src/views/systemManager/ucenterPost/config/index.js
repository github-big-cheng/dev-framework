export const searchFormConfig = [
    {
        type: "select",
        label: "职务类型",
        prop: "type",
        children: [],
        url: 'getUcenterCodeCombox',
        params:{type: "10001-12006"}
    },
    {
        type: "input",
        label: "代码",
        prop: "codeQueryLike",
    },
    {
        type: "input",
        label: "名称",
        prop: "nameQueryLike",
    },
    {
        type: "slot",
        label: "",
        slotName: "slot-botton",
    },
];
export const btnConfigs = [
    {
        type: "add",
        text: "新增",
        icon: "el-icon-aliadd",
        has: "ucenter_position_add",
        handlerType: "handleAddClick",
    },
    {
        type: "modify",
        text: "修改",
        icon: "el-icon-alimodify",
        has: "ucenter_position_edit",
        handlerType: "handleEditClick",
    },
    {
        type: "remove",
        text: "删除",
        icon: "el-icon-aliback",
        has: "ucenter_position_delete",
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
        prop: "name",
        label: "名称",
        orderBy: "name",
        asc: "",
        width: null,
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
        minWidth: 45,
    },
    {
        colKey: "type",
        prop: "typeName",
        label: "职位类型",
        minWidth: 45,
    },
    {
        colKey: "createBy",
        prop: "createByName",
        label: "创建人",
        minWidth: 35,
    },
    {
        colKey: "createTime",
        prop: "createTime",
        label: "创建时间",
        minWidth: 60,
        orderBy: "createTime",
    },
    {
        colKey: "orgName",
        prop: "orgName",
        label: "机关（单位）",
        width: null,
    },
];
