export const searchList = {
    easy: {
        placeholder: '请输入企业名称',
        prop: 'companyNameQueryLike'
    },
    list: [
        {
            type: "select",
            label: "企业名称",
            prop: "companyId",
            normalizer: {
                label: "cname",
                value: "id",
            },
            children: []
        },
        {
            type: "treeSelect",
            label: "地区",
            prop: 'adminisCode',
            children: [],
            normalizer: { value: 'code', label: 'name' },
        },
        {
            type: 'select',
            label: '状态',
            prop: 'status',
            children: [],
        },
        {
            type: 'select',
            label: '数据来源',
            prop: 'dataSource',
            children: [
                {
                    value: "1",
                    name: "企业在线填报"
                },
                {
                    value: "2",
                    name: "区县局填报"
                }, {
                    value: "3",
                    name: "批量导入"
                }
            ],
        },
        {
            type: 'customTime',
            label: '填报日期',
            attr: ['filingDateTimeQueryGe', 'filingDateTimeQueryLe']
        },
        {
            type: 'slot',
            label: '',
            slotName: 'slot-botton',
        }
    ]
}

export const btnConfigs = [
    {
        text: '新增',
        icon: 'el-icon-aliadd',
        handlerType: 'handleAddClick',
        has: "biz_comp_survey_add"
    },
    {
        text: '批量导入',
        icon: 'el-icon-download',
        handlerType: 'handleBulkloadClick',
        has: "biz_comp_survey_importExcel"
    },
    {
        text: '修改',
        icon: 'el-icon-alimodify',
        handlerType: 'handleEditClick',
        has: "biz_comp_survey_edit"
    },
    {
        text: '删除',
        icon: 'el-icon-alitrash',
        handlerType: 'handleDeleteClick',
        has: "biz_comp_survey_delete",
    },
    {
        text: '审核',
        icon: 'el-icon-check',
        handlerType: 'handleExamineClick',
        has: "biz_comp_survey_check",
    },
    {
        text: '取消审核',
        icon: 'el-icon-close',
        handlerType: 'handleCancelExamineClick',
        has: "biz_comp_survey_cancelCheck",
    },
    {
        text: '查看',
        icon: 'el-icon-alifreeze',
        handlerType: 'handleLockClick',
        has: "biz_comp_survey_view",
    },
    {
        text: '上报',
        icon: 'el-icon-finished',
        handlerType: 'handleReportClick',
        has: "biz_comp_survey_upReport",
    },
    {
        text: '刷新',
        icon: 'el-icon-alirefresh',
        handlerType: 'getTableList'
    }
]

export const tableTitle = [
    {
        colKey: "companyName",
        prop: "companyName",
        label: "企业名称",
        width: null,
        type: "click",
        isOperation: true,
    },
    {
        colKey: "statusName",
        prop: "status",
        label: "状态",
        minWidth: 30,
        type: "isSlot",
        slotName: "statusSlot",
        orderBy: "status",
        asc: "",
    },
    {
        colKey: "fillDate",
        prop: "fillDate",
        label: "填报日期",
        minWidth: 40,
        orderBy: "fillDate",
        asc: "",

    },
    {
        colKey: "legalName",
        prop: "legal",
        label: "法定代表人",
        minWidth: 30,
    },
    {
        colKey: "regAddress",
        prop: "address",
        label: "地址",
        minWidth: 50,
        type: "isSlot",
        slotName: "address",
    },
    {
        colKey: "area",
        prop: "area",
        label: "占地面积",
        minWidth: 30,
        orderBy: "area",
        asc: "",
    },
    {
        colKey: "totalNo",
        prop: "totalNo",
        label: "从业人数",
        minWidth: 30,
        orderBy: "totalNo",
        asc: "",
    },
    {
        colKey: "zycpmc",
        prop: "machBizType",
        label: "主要经营类型",
        minWidth: 30,
    },
    {
        colKey: "brand",
        prop: "brand",
        label: "注册商标（品牌）",
        minWidth: 60,
    },
    {
        colKey: "director",
        prop: "director",
        label: "统计负责人",
        minWidth: 35,
    },
    {
        colKey: "filledByName",
        prop: "filledByName",
        label: "填报人",
        minWidth: 35,
    },
    {
        colKey: "dataSourceName",
        prop: "dataSource",
        label: "数据来源",
        minWidth: 35,
        type: "isSlot",
        slotName: "dataSource",
        orderBy: "dataSource",
        asc: "",
    },
]