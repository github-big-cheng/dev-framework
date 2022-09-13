export const formConfig = ({ getLinkageVal }) => [
    {
        type: "input",
        label: "名称",
        prop: "name",
        changeCB: getLinkageVal,
        value: "",
        focus: true,
        rules: {
            require: true,
        },
        class: "single input-360",
    },
    {
        type: "input",
        label: "代码",
        prop: "code",
        rules: {
            require: true,
        },
    },
    {
        type: "select",
        label: "职务类型",
        prop: "type",
        value: "",
        url: "getUcenterCodeCombox",
        params: { type: "10001-12006" },
    },
    {
        type: "input",
        typeName: "textarea",
        label: "备注",
        prop: "memo",
        value: "",
        class: "item-remark",
        maxlength: "100",
        placeholder: "",
    },
];
export const formBtns = [
    {
        btnLoading: false,
        btnText: "取消",
        handlerType: "cancelClick",
        args: 0,
    },
    {
        type: "primary",
        btnLoading: false,
        btnText: "保存",
        handlerType: "submitForm",
        args: 1,
    },
];
