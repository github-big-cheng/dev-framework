export const noticesTabNames = [
    {
        label: "通知",
        name: "notice",
        sum: 0,
        params: {
            sendTypeQueryIn: "10042-10",
            bizTypeQueryIn: "10041-50",
        },
    },
    {
        label: "会议通知",
        name: "noticeMeet",
        sum: 0,
        params: {
            sendTypeQueryIn: "10042-10",
            bizTypeQueryIn: "10041-10,10041-20,10041-30,10041-40",
        },
    },
    {
        label: "系统消息",
        name: "noticeSys",
        sum: 0,
        params: {
            sendTypeQueryIn: "10042-10",
            bizTypeQueryNotIn: "10041-10,10041-20,10041-30,10041-40,10041-50",
        },
    },
];
export const approvalsTabNames = [
    {
        label: "待审批",
        name: "approvalStay",
        sum: 0,
        params: {
            approvalQueryType: 1,
        },
    },
    {
        label: "已审批",
        name: "approvalAlready",
        sum: 0,
        params: {
            approvalQueryType: 2,
        },
    },
    {
        label: "已办结",
        name: "approvalCompleted",
        sum: 0,
        params: {
            approvalQueryType: 3,
        },
    },
];
export const documentsTabNames = [
    {
        label: "收文阅读",
        name: "receiveRead",
        sum: 0,
        show:false,
        code: 'oa_doc_receive_query_list'
    },
    {
        label: "发文阅读",
        name: "sendRead",
        sum: 0,
        show:false,
        code: 'oa_doc_send_query_list'
    },
];
export const openInfoTabNames = [
    {
        params : {
            status : '28004-20',
        },
    },
];
export const dynamicInfoList = [
    {
        content: '支持使用图标',
        timestamp: '2018-04-12 20:46',
        size: 'large',
        type: 'primary',
        icon: 'el-icon-more'
    },
    {
        content: '支持自定义颜色',
        timestamp: '2018-04-03 20:46',
        color: '#0bbd87'
    },
    {
        content: '支持自定义尺寸',
        timestamp: '2018-04-03 20:46',
        size: 'large'
    },
    {
        content: '默认样式的节点',
        timestamp: '2018-04-03 20:46'
    }
]
export const workFormAdd = [
    {
        type: "input",
        label: "标题",
        prop: "title",
        value: "",
        rules: {
            require: true,
        },
        focus: true,
        class: "input-w360",
    },
    {
        type: "datetimeRange",
        label: "时间",
        prop: "workTime",
        attr: ["startTime", "endTime"],
        value: '',
        rules: {
            require: true,
        },
        class: "input-w360",
    },
    {
        type: "input",
        typeName: "textarea",
        label: "内容",
        prop: "content",
        class: "item-remark",
    },
]