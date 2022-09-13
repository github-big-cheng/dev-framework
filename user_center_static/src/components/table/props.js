export default {

    stripe: {
        //是否显示斑马纹
        type: Boolean,
        default: () => false,
    },
    border: {
        //是否带有纵向边框
        type: Boolean,
        default: () => false,
    },
    emptySize: {
        //是否带有纵向边框
        type: String | Number,
        default: () => '',
    },
    height: {
        //设置table高度
        type: [Number, String],
        default: () => null,
    },
    fixed: {
        //列是否需固定
        type: [String, Boolean],
        default: () => false,
    },
    showSet: {
        //显示表格设置
        type: Boolean,
        default: () => true,
    },
    tableTit: {
        //表头
        type: Array,
        default: () => [],
    },
    alignType: {
        //表头文本位置
        type: String,
        default: () => "left",
    },
    tableData: {
        //表格内容
        type: Array,
        default: () => [],
    },
    iSelection: {
        //是否显示筛选框
        type: Boolean,
        default: () => true,
    },
    isOperation: {
        //是否显示操作列
        type: Boolean,
        default: () => false,
    },
    operationText: {
        //操作文本
        type: String,
        default: () => "操作",
    },
    operationWidth: {
        //操作列宽
        type: Number | String,
        default: () => 100,
    },
    isStatus: {
        type: Boolean,
        default: () => false,
    },
    isAnnex: {
        type: Boolean,
        default: () => false,
    },
    isNums: {
        type: Boolean,
        default: () => true,
    },
    isTooltip: {
        //当内容过长被隐藏时显示tooltip
        type: Boolean,
        default: () => true,
    },
    tbLoading: {
        //加载
        type: Boolean,
        default: () => false,
    },
    hasLook: {
        //查看权限
        type: Boolean | Array,
        default: () => false,
    },
    slotName: {
        //具名插槽
        type: String,
        default: () => "",
    },
    isCheckDisable: {
        //是否勾选复选框
        type: Boolean,
        default: () => false,
    },
    selectableFun: {
        //选中行
        type: Function,
        default: () => {
            return true;
        },
    },
    addRowClassName: {
        //添加行样式
        type: Function,
        default: () => {
            return true;
        },
    },
    addColumnStyle: {
        //添加列样式
        type: Function,
        default: () => {
            return true;
        },
    },
    addColumnClassName: {
        //添加列样式
        type: Function,
        default: () => {
            return true;
        },
    },
    addShowTooltip: {
        //显示Tooltip
        type: Function,
        default: () => {
            return true;
        },
    },
    addHideTooltip: {
        //隐藏Tooltip
        type: Function,
        default: () => {
            return true;
        },
    },
    fixedHeight: {
        /* 是否固定高度 */ type: Boolean,
        default: () => false,
    },
    isSupernatant: {
        type: Boolean,
        default: () => true,
    },
    pageNo: {
        type: Number,
        default: () => 1,
    },
    pageSize: {
        type: Number,
        default: () => 10,
    },
    tableTitcolumn: {
        type: String,
        default: () => "",
    },
    rowKey: {
        type: String,
        default: "",
    },
    defaultExpandAll: {
        type: Boolean,
        default: false,
    },
    tableUnit: {
        type: Object,
        default: () => { },
    },
    //table合计
    showSummary:{
        type: Boolean,
        default: false,
    },
    tableSumFmt:{
        type: Function,
    },
    // 合并单元格
    spanMethod: {
        type: Function,
        default: () => {
            return true;
        },
    },
    currentRowKey: {
        default: 'id'
    }

}
