/*
 * @Author: your name
 * @Date: 2021-06-16 17:16:38
 * @LastEditTime: 2021-09-02 16:11:04
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \gssp_fe\src\api\modules\common.js
 */
export const apiUrl = window.location.origin;
import Ajax from "@/utils/request";
import {getLocalStorage} from "@/utils/auth";

const common = {
    // 部门管理
    getUcenterOrgCombox: (params) => Ajax.get("/ucenter/ucenterOrg/combox", params),
    //部门树
    getUcenterOrgTree: (params) => Ajax.get("/ucenter/ucenterOrg/tree", params),
    //获取部门树与人员
    getUcenterOrgTreePerson: (params) => Ajax.get("/ucenter/ucenterOrg/listDeptPerson/tree", params),

    //获取部门列表与人员
    getUcenterOrgListOrPerson: (params) => Ajax.get("/ucenter/ucenterPerson/list/combox", params),
    //职务
    getUcenterPositionCombox: (params) => Ajax.get("/ucenter/ucenterPosition/combox", params),

    //code
    getUcenterCodeCombox: (params) => Ajax.get("/sys/sysCode/combox", params),
    getUcenterCodeTree: (params) => Ajax.get("/sys/sysCode/tree", params),

    //修改密码
    getUpdatePassword: (params) => Ajax.get("ucenter/personalCenter/updatePassword", params),

    //界面配置
    getUcenterpageconfigList: (params) => Ajax.get("/sys/sysColumnConfig/list", params),
    getUcenterpageconfigFullList: (params) => Ajax.get("/sys/sysColumnConfig/fullList", params),
    getUcenterpageconfigSave: (params) => Ajax.post("/sys/sysColumnConfig/save", params),
    getUcenterpageconfigReset: (params) => Ajax.post("/sys/sysColumnConfig/reset", params),

    //系统参数配置
    sysParameterList: (params) => Ajax.get("/sys/sysParameter/list", params),
    sysParameterRunList: (params) => Ajax.get("/sys/sysParameter/run/list", params),
    sysParameterSave: (params) => Ajax.post("/sys/sysParameter/save", params),
    sysParameterLoadApi: (params) => Ajax.post("/sys/sysParameter/load/api", params),

    //上传
    uploadFile: (params) => Ajax.post('/sys/file/upload', params, {nostringify: true}),
    //下载中心
    getUcenterDownloadList: (params) => Ajax.get("/ucenter/download/api", params),
    //下载中心
    createQrCode: (params) => Ajax.get("/gssp/download/createQrCode", params),

    //二维码
    getQrcodeGenerate: (params) => Ajax.get("sso/qrcode/generate", params),

    // 审批
    workFlowAgree: (params) => Ajax.post("/oa/oaFlow/agree", params),
    // 驳回
    workFlowReject: (params) => Ajax.post("/oa/oaFlow/reject", params),
    // 反对
    workFlowOppose: (params) => Ajax.post("/oa/oaFlow/oppose", params),
    // 任务代办人员查询
    getTodoperson: (params) => Ajax.post("/oa/oaFlow/common/todoperson/combox", params),
    //催办
    oaDocReceiveUrge: (params) => Ajax.post("/oa/oaDocReceive/urge", params),
    //工作流查询
    flowImg: (params) => Ajax.get("/activiti/image/json", params),

    //首页我的任务
    getIndexTask: (params) => Ajax.get("oa/oaIndex/myTask/combox", params),
    //工作计划列表
    getWorkPlanList: (params) => Ajax.get("oa/oaWorkArrangements/list/combox", params),
    //工作计划新增
    getWorkPlanAdd: (params) => Ajax.get("oa/oaWorkArrangements/add/combox", params),
    //工作计划修改
    getWorkPlanSave: (params) => Ajax.get("oa/oaWorkArrangements/save/combox", params),
    //工作计划删除
    getWorkPlanDelete: (params) => Ajax.get("oa/oaWorkArrangements/delete/combox", params),
    //首页动态信息查看
    getIndexDynamicInfo: (params) => Ajax.get("/party/partyIndex/view/combox", params),
    //首页信息公开查看
    getIndexInfoOpen: (params) => Ajax.get("oa/oaIndex/view/combox", params),
   


    // 工作流控制器
    //获取对应工作流开启的首步任务
    getFirstTask(params) {
        return Ajax.post("/oa/oaFlow/common/firstTask/combox", params);
    },


    /**
     * 构建queryString参数
     *
     * @param params
     * @returns {string}
     */
    queryParams(url, data) {
        let prefix = url.indexOf("?") > -1 ? '&' : '?';
        let _result = [];
        for (let key in data) {
            let value = data[key];
            // 去掉为空的参数
            if (['', undefined, null].includes(value)) {
                continue;
            }
            if (value.constructor === Array) {
                value.forEach(_value => {
                    _result.push(encodeURIComponent(key) + '[]=' + encodeURIComponent(_value));
                });
            } else {
                _result.push(encodeURIComponent(key) + '=' + encodeURIComponent(value));
            }
        }
        return _result.length ? url + prefix + _result.join('&') : url;
    },

    //车辆导出
    getCarReportExport(params) {
        let {ticket} = getLocalStorage("userInfo");

        let NODE_ENV = process.env.NODE_ENV === 'production' ? '' : '/dev-api';
        let exportUrl = window.location.origin + NODE_ENV + '/oa/oaCarReport/export?_sgk=' + ticket;

        exportUrl = this.queryParams(exportUrl, params);
        // exportUrl = exportUrl + "&applyType=" + (params.applyType !== undefined ? params.applyType : "");
        // exportUrl = exportUrl + "&deptId=" + (params.deptId !== undefined ? params.deptId : "");
        // exportUrl = exportUrl + "&startTimeQueryGe=" + (params.startTimeQueryGe !== undefined ? params.startTimeQueryGe : "");
        // exportUrl = exportUrl + "&endTimeQueryLe=" + (params.endTimeQueryLe !== undefined ? params.endTimeQueryLe : "");
        // exportUrl = exportUrl + "&carId=" + (params.carId !== undefined ? params.carId : "");
        // exportUrl = exportUrl + "&containsRepair=" + (params.containsRepair !== undefined ? params.containsRepair : "");
        const downLoadLink = document.createElement('a');
        downLoadLink.href = exportUrl;
        downLoadLink.click();
    },

    // 导出
    export(url, params) {
        let {ticket} = getLocalStorage("userInfo");

        let NODE_ENV = process.env.NODE_ENV === 'production' ? '' : '/dev-api';
        let exportUrl = window.location.origin + NODE_ENV + url + '?_sgk=' + ticket;

        exportUrl = this.queryParams(exportUrl, params);
        // exportUrl = exportUrl + "&applyType=" + (params.applyType !== undefined ? params.applyType : "");
        // exportUrl = exportUrl + "&deptId=" + (params.deptId !== undefined ? params.deptId : "");
        // exportUrl = exportUrl + "&startTimeQueryGe=" + (params.startTimeQueryGe !== undefined ? params.startTimeQueryGe : "");
        // exportUrl = exportUrl + "&endTimeQueryLe=" + (params.endTimeQueryLe !== undefined ? params.endTimeQueryLe : "");
        // exportUrl = exportUrl + "&carId=" + (params.carId !== undefined ? params.carId : "");
        // exportUrl = exportUrl + "&containsRepair=" + (params.containsRepair !== undefined ? params.containsRepair : "");
        const downLoadLink = document.createElement('a');
        downLoadLink.href = exportUrl;
        downLoadLink.click();
    },


    getPartyReportExport(params, type) {
        let {ticket} = getLocalStorage("userInfo");
        let NODE_ENV = process.env.NODE_ENV === 'production' ? '' : '/dev-api';
        let exportUrl = window.location.origin + NODE_ENV + '/party/partyReport/export/' + type + '?_sgk=' + ticket;
        exportUrl = this.queryParams(exportUrl, params);
        // exportUrl = exportUrl + "&ids=" + params.ids;
        const downLoadLink = document.createElement('a');
        downLoadLink.href = exportUrl;
        downLoadLink.click();
    },


    /**
     *  打印公共方法
     */
    openPrintPreview(module, params) {

        // console.log(params, 'params...');
        if (!params?.dataList?.length) {
            return;
        }

        let newWindow = window.open('', params.title + new Date().getTime());
        let form = newWindow.document.createElement('form');
        newWindow.document.body.appendChild(form);
        let NODE_ENV = process.env.NODE_ENV === "production" ? "" : "/dev-api";
        form.action =
            window.location.origin +
            NODE_ENV +
            "/" +
            module +
            "/print/preview/combox?t=" +
            new Date().getTime();
        // console.log(form.action, requestUrl, '............');
        form.method = 'post';

        // 数据集
        let input = newWindow.document.createElement('input');
        input.name = 'dataList';
        input.value = JSON.stringify(params.dataList);
        form.appendChild(input);

        // 登录票据
        let {ticket} = getLocalStorage("userInfo");
        let inputSgk = newWindow.document.createElement('input');
        inputSgk.name = '_sgk';
        inputSgk.value = ticket;
        form.appendChild(inputSgk);

        form.submit();
        newWindow.document.body.removeChild(form);
    },
};

export default common;
