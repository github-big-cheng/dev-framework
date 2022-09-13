import Ajax from "@/utils/request";

const notice = {
    //未读总数
    getNoticeCount: (params) => Ajax.get("/remind/notice/selectReadCountByGroup/combox", params),

    //系统消息
    getNewsList: (params) => Ajax.getWithLoading("/remind/notice/list/combox", params),
    getNewsView: (params) => Ajax.getWithLoading("/remind/notice/view/combox", params),
    getNewsDelete: (params) => Ajax.get("/remind/notice/deleteByBizTypeAndBizIds/api", params),
    getNewsRead: (params) => Ajax.get("/remind/notice/read/combox", params),
    

    //通知
    getNoticeList: (params) => Ajax.getWithLoading("/oa/oaNotice/list", params),
    getNoticeAdd: (params) => Ajax.post("/oa/oaNotice/add", params),
    getNoticeSave: (params) => Ajax.post("/oa/oaNotice/save", params),
    getNoticeView: (params) => Ajax.getWithLoading("/oa/oaNotice/view", params),
    getNoticeDelete: (params) => Ajax.get("/oa/oaNotice/delete", params),
    getNoticePublish: (params) => Ajax.post("/oa/oaNotice/publish", params),
    getNoticeCancel: (params) => Ajax.get("/oa/oaNotice/withdraw", params),
}

export default notice
