export const sysParameterListHash = [
    {
        name: '系统运行参数',
        icon: 'system-parameters',
        package: 'SystemParameters'
    },
    {
        name: '管理属性',
        icon: 'management',
        package: 'Management'
    },
    {
        name: '系统显示设置',
        icon: 'system-displays',
        package: 'SystemDisplays'

    },
]

export const setTypeHash = {
    'SystemParameters': 1,
    'Management': 2,
    'SystemDisplays': 3,

}

export const formHash = {
    1: { type: "input" },
    2: { type: "select" },
    3: { type: "checkbox" },
    4: {
        type: "radio",
        children: [{
            name: '是',
            value: '1'
        }, {
            name: '否',
            value: '0'
        }]
    },
    5: {
        type: 'uploadLogo',
        actionUrl: "/sys/file/upload",
        uploadType: "10001",
    },
    7: { type: "radio" },
    8: { type: 'input', typeName: 'textarea' }
}
