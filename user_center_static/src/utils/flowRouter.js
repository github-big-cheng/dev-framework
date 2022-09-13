import { RouteModule } from '@/utils';

export const flowRouter = {
    // 21001-10 收文登记
    "21001-10": {
        ADD: "oa_doc_receive_add",
        EDIT: "oa_doc_receive_save",
        DELETE: "oa_doc_receive_delete",
        VIEW: "oa_doc_receive_view",
        APPROVAL_VIEW: "oa_doc_receive_approval_view",
    },
    // 21001-20 发文拟稿
    "21001-20": {
        ADD: "oa_doc_send_add",
        EDIT: "oa_doc_send_save",
        DELETE: "oa_doc_send_delete",
        VIEW: "oa_doc_send_view",
        APPROVAL_VIEW: "oa_doc_send_approval_view",
    },
    // 21001-40 用车申请-配置
    "21001-40": {
        ADD: "oa_car_net_add_my",
        EDIT: "oa_car_net_edit_my",
        DELETE: "oa_car_net_delete_my",
        VIEW: "oa_car_net_view_my",
        APPROVAL_VIEW: "oa_car_net_approval_view",
    },
    // 21001-50 用车申请-统筹
    "21001-50": {
        ADD: "oa_car_net_add_my",
        EDIT: "oa_car_net_edit_my",
        DELETE: "oa_car_net_delete_my",
        VIEW: "oa_car_net_view_my",
        APPROVAL_VIEW: "oa_car_net_approval_view",
    },
    // 21001-60 用车申请-租用
    "21001-60": {
        ADD: "oa_car_net_add_my",
        EDIT: "oa_car_net_edit_my",
        DELETE: "oa_car_net_delete_my",
        VIEW: "oa_car_net_view_my",
        APPROVAL_VIEW: "oa_car_net_approval_view",
    },
    // 21001-70 合同起草申请
    "21001-70": {
        ADD: "oa_contract_add",
        EDIT: "oa_contract_save",
        DELETE: "oa_contract_delete",
        VIEW: "oa_contract_view",
        APPROVAL_VIEW: "oa_contract_approval_view",
    },
    // 21001-100 干部晋升
    "21001-100": {
        ADD: "oa_management_promote_add",
        EDIT: "oa_management_promote_edit",
        DELETE: "oa_management_promote_delete",
        VIEW: "oa_management_promote_view",
        APPROVAL_VIEW: "oa_management_promote_approval_view",
    },
    // 21001-110 会议申请
    "21001-110": {
        ADD: "oa_meeting_my_add",
        EDIT: "oa_meeting_my_edit",
        DELETE: "oa_meeting_my_delete",
        VIEW: "oa_meeting_my_view",
        APPROVAL_VIEW: "oa_meeting_approval_view",
    },
    // 21001-120 费用报销申请
    "21001-120": {
        ADD: "oa_expenses_apply_add",
        EDIT: "oa_expenses_apply_save",
        DELETE: "oa_expenses_apply_delete",
        VIEW: "oa_expenses_apply_view",
        APPROVAL_VIEW: "oa_expenses_apply_approval_view",
    },
    // 21001-130 经费报审
    "21001-130": {
        ADD: "oa_fund_payment_add",
        EDIT: "oa_fund_payment_save",
        DELETE: "oa_fund_payment_delete",
        VIEW: "oa_fund_payment_view",
        APPROVAL_VIEW: "oa_fund_payment_approval_view",
    }
};

export const approvalViewRouter = {
    oa_doc_receive_approval_view: {
        path: "/receiveManagerApprovalView/:type/:id",
        name: "receiveManagerApprovalView",
        hidden: true,
        component: RouteModule("docManager/receiveManager/receiveView", "receiveManagerApprovalView"),
        meta: {title: "收文审批-查看", icon: "", deepth: 2, path: "/myApproval"}
    },
    oa_doc_send_approval_view: {
        path: "/sendManagerApprovalView/:type/:id",
        name: "sendManagerApprovalView",
        hidden: true,
        component: RouteModule("docManager/sendManager/pageView", "sendManagerApprovalView"),
        meta: {title: "发文审批-查看", icon: "", deepth: 2, path: "/myApproval"}
    },
    oa_car_net_approval_view: {
        path: "/netListApprovalView/:type/:id",
        name: "netListApprovalView",
        component: RouteModule("carManager/myCarList/pageView", 'netListApprovalView'),
        meta: {title: "用车申请-查看", deepth: 2, path: "/myApproval"}
    },
    oa_contract_approval_view: {
        path: "/contractApprovalView/:type/:id",
        name: "contractApprovalView",
        component: RouteModule("contractManager/allContract/pageView", "contractApprovalView"),
        meta: {title: "合同申请-查看", deepth: 2, path: "/myApproval"}
    },
    oa_management_promote_approval_view: {
        path: "/managementPromoteApprovalView/:type/:id",
        name: 'managementPromoteApprovalView',
        component: RouteModule( "management/managementPromote/pageView", "managementPromoteApprovalView"),
        meta: {title: "干部晋升申请-查看", deepth: 2, path: "/myApproval"}
    },
    oa_meeting_approval_view: {
        path: "/meetingApprovalView/:type/:id",
        name: 'meetingApprovalView',
        component:  RouteModule("meetingManager/meetingApply/pageView", "meetingApprovalView"),
        meta: { title: "会议申请-查看", deepth: 2, path: "/myApproval" }
    },
    oa_expenses_apply_approval_view: {
        path: "/expensesApplyApprovalView/:type/:id",
        name: 'expensesApplyApprovalView',
        component: RouteModule( "expensesApply/expensesApplyInfo/pageView", "expensesApplyApprovalView"),
        meta: {title: "费用报销申请-查看", deepth: 2, path: "/myApproval"}
    },
    oa_fund_payment_approval_view: {
        path: "/fundPaymentApprovalView/:type/:id",
        name: 'fundPaymentApprovalView',
        component: RouteModule( "expensesApply/fundsApplyList/pageView", "fundPaymentApprovalView"),
        meta: {title: "经费报审申请-查看", deepth: 2, path: "/myApproval"}
    },
}
