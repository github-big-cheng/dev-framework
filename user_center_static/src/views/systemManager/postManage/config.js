import { filterBtnShow } from '@/utils/common.js';

export const searchList = ({jobTypeChildren})=>(
    {
        easy: {
            placeholder: '请输入名称或代码',
            prop: 'nameOrCodeQueryLike'
        },
        list: [
            {
                type: "select",
                label: "职务类型",
                prop: "type",
                children: jobTypeChildren || []
            },
            {
                type: 'input',
                label: '代码',
                prop: 'codeQueryLike',
            },
            {
                type: 'input',
                label: '名称',
                prop: 'nameQueryLike',
            },
        ]
    }
)

export const btnConfigs = [
    {
        type: 'add',
        text: '新增',
        icon: 'el-icon-aliadd',
        has : 'ucenter_position_add',
        handlerType: 'handleAddClick'
    },
    {
        type: 'modify',
        text: '修改',
        icon: 'el-icon-alimodify',
        has : 'ucenter_position_edit',
        handlerType: 'handleEditClick'
    },
    {
        type: 'remove',
        text: '删除',
        icon: 'el-icon-aliback',
        has: "ucenter_position_delete",
        handlerType: 'handleDeleteClick'
    },
    {
        type: 'refresh',
        text: '刷新',
        icon: 'el-icon-alirefresh',
        handlerType: 'getTableList'
    }
]

export const tableTitle = [
    {
        colKey: "name",
        prop: "name",
        label: "名称",
        orderBy: "name",
        asc: "",
        width: null,
        type: "click",
        isOperation: filterBtnShow(["ucenter_position_view"], false),
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
        colKey: "levelName",
        prop: "typeName",
        label: "职务类型",
        width: null,
    },
    {
        colKey: "createBy",
        prop: "createByName",
        label: "创建人",
        width: null,
    },
    {
        colKey: "createTime",
        prop: "createTime",
        label: "创建时间",
        minWidth: "60",
        orderBy: "createTime",
    }
]

export const formConfigs = ({jobTypeChildren})=> ([
    {
        type: 'slot',
        label: '名称',
        prop: "name",
        value: '',
        rules: {
            require: true
        },
        slotName: 'rName',
        class: 'single'
    },
    {
        type: 'slot',
        label: '代码',
        prop: "code",
        value: '',
        rules: {
            require: true
        },
        slotName: 'rCode',
        class: 'single'
    },
    {
        type: "select",
        label: "职务类型",
        prop: "type",
        value: "",
        children: jobTypeChildren || [],
        class: 'single'
    },
    {
        type: "input",
        typeName: 'textarea',
        label: "备注",
        prop: "memo",
        value: "",
        class: 'item-remark',
        maxlength: "100",
        placeholder:""
    }
])

export const formBtns = [
    {
        btnLoading:false,
        btnText:"取消",
        handlerType:"cancelClick",
    },
    {
        type: 'primary',
        btnLoading:false,
        btnText:"保存",
        handlerType:"submitForm",
    }
]