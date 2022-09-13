import axios from "axios";
import { requestUrl } from '@/api/api'
import qs from "qs";
import {
    Message,
    MessageBox
} from "element-ui";
import store from "../store";
import {
    getToken
} from "@/utils/auth";
import router from "@/router";
let setTimeoutStatus = false;
import {
    resetRouter
} from "@/router";

axios.defaults.baseURL = requestUrl;
axios.interceptors.request.use(
    (config) => {
        if (getToken()) {
            config.headers["_sgk"] = getToken();
        }
        return config;
    },
    (error) => {
        Promise.reject(error);
    }
);
axios.interceptors.response.use(
    (response) => {
        if (response.config.hasOwnProperty('loading')) {
            setTimeout(() => router.currentRoute.meta.hasOwnProperty('noLoading') && (router.currentRoute.meta.noLoading = true), 300)
        }
        if (response.config.hasOwnProperty('responseType')) {
            if (response.config.responseType === 'blob') {
                return response;
            }
        }
        if (response.headers["content-type"].indexOf("json") === -1) {
            return {
                success: false,
                msg: "登录状态失效，请清除Cookie重新登录！"
            };
        }

        const res = response.data;
        if (res.code == 0) {
            return response.data;
        } else if (res.code == 2001) {
            if (!setTimeoutStatus) {
                setTimeoutStatus = true;
                MessageBox.alert(res.message, "提示", {
                    confirmButtonText: "确定",
                    showClose: false,
                    callback: (action) => {
                        store.dispatch("FedLogOut").then((res) => {
                            router.replace("/login");
                            setTimeoutStatus = false;
                            resetRouter();
                        });
                    },
                });
            }
        } else {
            if (window.location.href.indexOf("login") == -1) {
                Message({
                    message: res.message ? res.message : res.msg,
                    type: "error",
                    duration: 2 * 1000,
                });
            }
            return Promise.reject(response.data);
        }
        return response.data;
    },
    (error) => {
        if (error.response.config.hasOwnProperty('loading')) {
            setTimeout(() => router.currentRoute.noLoading = true, 300)
        }
        if (error.response) {
            let status = error.response.status;
            let err_msg;
            switch (status) {
                case 400:
                    err_msg = "请求错误(400)";
                    break;
                case 401:
                    err_msg = "未授权，请重新登录(401)";
                    break;
                case 403:
                    err_msg = "拒绝访问(403)";
                    break;
                case 404:
                    err_msg = "请求出错(404)";
                    break;
                case 408:
                    err_msg = "请求超时(408)";
                    break;
                case 500:
                    err_msg = "服务器错误(500)";
                    break;
                case 501:
                    err_msg = "服务未实现(501)";
                    break;
                case 502:
                    err_msg = "网络错误(502)";
                    break;
                case 503:
                    err_msg = "服务不可用(503)";
                    break;
                case 504:
                    err_msg = "网络超时(504)";
                    break;
                case 505:
                    err_msg = "HTTP版本不受支持(505)";
                    break;
                default:
                    err_msg = `连接出错(${error.response.status})!`;
            }
            return Promise.reject({
                success: false,
                msg: err_msg
            });
        }
        return Promise.reject({
            success: false,
            msg: "Sorry, 页面错误！"
        });
    }
);

const Ajax = {
    ajax(url, params, opts) {
        if (!url.startsWith("http")) {
            // url =  url;
        }

        let config = {
            url: url,
            method: "post",
            responseType: "json",
            timeout: 60000,
        };
        if (opts) {
            config = {
                ...config,
                ...opts
            };
        }
        //params._sgk = getToken();
        if (config["method"] === "post") {
            config["data"] = config.nostringify ? params : qs.stringify(params);
        } else {
            config["params"] = params;
        }
        return axios(config);
    },
    get(url, params, opts) {
        if (opts) {
            opts = Object.assign(opts, {
                method: "get"
            });
        } else {
            opts = {
                method: "get"
            };
        }
        return this.ajax(url, params, opts);
    },
    post(url, params, opts) {
        if (opts) {
            opts = Object.assign(opts, {
                method: "post"
            });
        } else {
            opts = {
                method: "post"
            };
        }
        return this.ajax(url, params, opts);
    },

    getWithLoading(url, params, opts) {
        if (opts) {
            opts = Object.assign(opts, {
                method: "get"
            });
        } else {
            opts = {
                method: "get"
            };
        }
        return this.ajax(url, params, { ...opts, loading: true });
    },
};
export default Ajax;
