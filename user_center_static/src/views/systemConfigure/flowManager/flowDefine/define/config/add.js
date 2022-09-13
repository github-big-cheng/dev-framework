export const formConfig = [
    {
        type: "selectCom",
        label: "机关(单位)",
        prop: "orgId",
        prop2: "orgName",
        value: "",
        names: "",
        title: "选择机关(单位)",
        tabList: ['dept'],
        class: 'single',
        params: {compType: '10027-30'},
        rules: {
            require: true
        }
    },
    {
        type: 'select',
        label: '业务类型',
        prop: 'bizType',
        rules: {
            require: true,
        },
        url: 'getUcenterCodeCombox',
        params: { type: '20001-21001' },
        class: 'input-w360',
    },
    {
        type: 'input',
        label: '流程名称',
        prop: 'bizName',
        rules: {
            require: true,
        },
        class: 'input-w360',
    },
    {
        type: 'input',
        label: '分组名称',
        prop: 'groupName',
        class: 'input-w360',
    },
    {
        type: 'input',
        label: 'icon',
        prop: 'backUrl',
        class: 'input-w360',
    },
    {
        type: 'input',
        label: '流程定义ID',
        prop: 'defineId',
        class: 'input-w360',
    },

]

export const formButton = [
    {
        btnLoading: false,
        disabled: false,
        btnText: "取消",
        handlerType: "cancelClick",
    },
    {
        submitType: 0,
        type: "primary",
        disabled: false,
        btnLoading: false,
        btnText: "保存",
        handlerType: "submitForm",
    },
]