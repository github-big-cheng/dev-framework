/*
 * @Author: your name
 * @Date: 2021-03-27 17:57:59
 * @LastEditTime: 2021-06-22 14:06:18
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\store\modules\app.js
 */
import Cookies from 'js-cookie'
import Api from "@/api/api";

const app = {
    state: {
        sidebar: {
            opened: !+Cookies.get('sidebarStatus'),
            withoutAnimation: false
        },
        device: 'desktop',
        loading: null,
        noticeSum: 0, //通知显示条数
        noticeCount: 0,
        meetingCount: 0,
        systemCount: 0,
    },
    mutations: {
        SET_NOTICESUM: (state, num) => {
            state.noticeSum = num;
        },
        SET_NOTICECOUNT: (state, num) => {
            state.noticeCount = num;
        },
        SET_MEETINGCOUNT: (state, num) => {
            state.meetingCount = num;
        },
        SET_SYSTEMCOUNT: (state, num) => {
            state.systemCount = num;
        },
        TOGGLE_SIDEBAR: state => {
            if (state.sidebar.opened) {
                Cookies.set('sidebarStatus', 1)
            } else {
                Cookies.set('sidebarStatus', 0)
            }
            state.sidebar.opened = !state.sidebar.opened
            state.sidebar.withoutAnimation = false
        },
        CLOSE_SIDEBAR: (state, withoutAnimation) => {
            Cookies.set('sidebarStatus', 1)
            state.sidebar.opened = false
            state.sidebar.withoutAnimation = withoutAnimation
        },
        TOGGLE_DEVICE: (state, device) => {
            state.device = device
        },
        OPEN_LOADING: (state, config) => {
            let dialog;
            !config.dom && (dialog = document.getElementById('_Dialog').getElementsByClassName('el-dialog__body')[0]);
            let loading = config.that.$loading({
                lock: true,
                target: config.dom || dialog,
                text: config.text || '提交数据中...',
                spinner: 'el-icon-loading',
                background: 'rgba(255, 255, 255, 1)'
            });
            state.loading = loading;
        },
        CLOSE_LOADING: (state) => {
            state.loading && state.loading.close()
        }
    },
    actions: {
        ToggleSideBar: ({commit}) => {
            commit('TOGGLE_SIDEBAR')
        },
        CloseSideBar({commit}, {withoutAnimation}) {
            commit('CLOSE_SIDEBAR', withoutAnimation)
        },
        ToggleDevice({commit}, device) {
            commit('TOGGLE_DEVICE', device)
        },
        GetNoticeSum: ({commit}) => {
            return;

            Api.getNoticeCount({sendTypeQueryIn: '10042-10'}).then((res) => {
                let {code, data} = res;
                if (code == 0) {
                    let systemMessage = data['10042-10'];

                    const noticeSet = new Set(['10041-50']);
                    const meetingSet = new Set(['10041-10', '10041-20', '10041-30', '10041-40']);
                    let notice = 0, meeting = 0, system = 0;
                    for (let key in systemMessage) {

                        if (noticeSet.has(key)) {
                            notice += systemMessage[key].unRead || 0;
                            continue;
                        }
                        if (meetingSet.has(key)) {
                            meeting += systemMessage[key].unRead || 0;
                            continue;
                        }

                        system += systemMessage[key].unRead || 0;
                    }

                    // console.log(notice, meeting, system, '............');

                    let noticeSum = notice + meeting + system;
                    commit('SET_NOTICESUM', noticeSum)
                    commit('SET_MEETINGCOUNT', meeting)
                    commit('SET_NOTICECOUNT', notice)
                    commit('SET_SYSTEMCOUNT', system)
                }
            });
        }
    }
}

export default app
