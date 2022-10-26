import { Message } from 'element-ui';
import { template } from '@/router/addRouter';
import Api from '@/api/api';
import lodash from 'lodash'
import {
    createRouter
} from "@/router/addRouter";
import {
    getMenuList
} from "@/utils/formatMenu";
import router from "@/router";
import {
    setLocalStorage,
    getLocalStorage
} from "@/utils/auth";
let now = new Date(); // 当前日期
let nowMonth = now.getMonth(); // 当前月
let nowYear = now.getYear(); // 当前年
nowYear += nowYear < 2000 ? 1900 : 0;
export const filterBtnShow = (btn = [], arrState = false) => {
    const btnList = getLocalStorage('btnArr') || [];
    return btn[arrState ? 'map' : 'some']((item) => btnList.includes(item));
}
const CommonFunc = {
    // RouteModule: (view, name) => {
    //     return new Promise((resolve => {
    //         import(`@/views/${view}`).then(template => {
    //             template.default.name = name;
    //             resolve(template)
    //         })
    //     }))
    // },

    filterBtnShow: (btn = [], arrState = false) => {
        const btnList = getLocalStorage('btnArr') || [];
        return btn[arrState ? 'map' : 'some']((item) => btnList.includes(item));
    },

    /**
     * 
     * @param {*} phone 验证手机号码
     * @param {*} title 错误提示标题
     * @returns 布尔类型
     */
    testingPhone: (phone, title) => {
        let regPhone = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
        let regTel = /^0\d{2,3}-?\d{7,8}$/;
        if (!regPhone.test(phone) && !regTel.test(phone)) {
            Message.error(`${title}填写错误`);
            return false;
        }
        return true;
    },

    /**
    * 
    * @param {*} telephone 验证座机号码
    * @param {*} title 错误提示标题
    * @returns 布尔类型
    */
    testingTelephone: (telephone, title) => {
        let regTelephone = /\d{3}-\d{8}|\d{4}-\d{7}/;
        let regPhone = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
        if (!regTelephone.test(telephone) || !regTelephone.test(regPhone)) {
            Message.error(`${title}填写错误`);
            return false;
        }
        return true;
    },

    /**
    * 
    * @param {*} email 验证邮箱
    * @param {*} title 错误提示标题
    * @returns 布尔类型
    */
    testingEmail: (email, title) => {
        let regEmail = /^\w+@[a-zA-Z0-9]{2,10}(?:\.[a-z]{2,4}){1,3}$/;
        if (!regEmail.test(email)) {
            Message.error(`${title}填写错误`);
            return false;
        }
        return true;
    },

    /**
     * 
     * @param {*} email 验证传真
     * @param {*} title 错误提示标题
     * @returns 布尔类型
     */
    testingFax: (fax, title) => {
        let regFax = /^0\d{2,3}-?\d{7,8}$/;
        if (!regFax.test(fax)) {
            Message.error(`${title}填写错误`);
            return false;
        }
        return true;
    },

    testingUrl: (url, title) => {
        let regUrl = /^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
        if (!regUrl.test(url)) {
            Message.error(`${title}填写错误`);
            return false;
        }
        return true;
    },

    testingPostcode: (postcode, title) => {
        let regPostcode = /^[0-9]{6}$/;
        if (!regPostcode.test(postcode)) {
            Message.error(`${title}填写错误`);
            return false;
        }
        return true;
    },

    showWarning: (tit) => {
        Message.warning(tit);
    },

    showSuccess: (tit) => {
        Message.success(tit);
    },

    showError: (tit) => {
        Message.error(tit);
    },

    formatTree: (
        list,
        children,
        type = true,
        oneIcon = '',
        twoIcon = '',
        layer = 0,
        isPersonChoice = false,
        isOrg = true,
        attr = 'children',
        idsScoped=[]
    ) => {
        let treeList = [];

        layer++;
        for (let i in list) {
            if (list.hasOwnProperty(i)) {
                if (list[i].children || list[i][children]) {
                    if (type) {
                        list[i].children = list[i].children.concat(
                            list[i][children] ? list[i][children] : []
                        );
                    } else {
                        list[i].children = list[i][children]
                            ? list[i][children]
                            : [];
                    }
                }
                if (list[i][attr] && list[i][attr].length) {
                    list[i].icon = oneIcon;
                } else {
                    list[i].icon = twoIcon;
                }

                list[i].layer = layer;
                //list[i]
                if (isPersonChoice && !list[i].name) {
                    list[i].id = isOrg ? list[i].id + 'org' : list[i].id;
                }
                list[i].children && idsScoped.length && list[i].children.forEach(item => {
                    let status = idsScoped.some(items => items == item.id)
                    
                    item.disabled = status ? false : true;
                })
                list[i].listPerson && idsScoped.length  && list[i].listPerson.forEach(item => {
                    let status = idsScoped.some(items => items == item.id)
                    
                    item.disabled = status ? false : true;
                })
                treeList.push(list[i]);
                if (list[i].children && list[i].children.length > 0) {
                    if (treeList[i]) {
                        treeList[i].children = CommonFunc.formatTree(
                            list[i].children,
                            children,
                            type,
                            oneIcon,
                            twoIcon,
                            layer,
                            isPersonChoice,
                            isOrg,
                            idsScoped
                        );
                    }
                }
            }
        }
        return treeList;
    },

    filterTreeId: (list) => {
        let treeIds = [];
        let allIds = [];
        for (let i in list) {
            if (list.hasOwnProperty(i)) {
                if (
                    list[i].isSelected == 1 && (!list[i].children || !list[i].children.length)
                    // (list[i].isSelect == 1 && list[i].type == 4) ||
                    // (list[i].isSelect == 1 &&
                    //     list[i].type == 2 &&
                    //     list[i].children.length == 0)
                ) {
                    treeIds.push(list[i].id);
                }
                if (list[i].isSelected == 1) {
                    allIds.push(list[i].id);
                }
                if (list[i].children && list[i].children.length > 0) {
                    treeIds = treeIds.concat(
                        CommonFunc.filterTreeId(list[i].children).treeIds
                    );
                    allIds = allIds.concat(CommonFunc.filterTreeId(list[i].children).allIds);
                }
            }
        }
        return {
            treeIds,
            allIds
        };
    },

    filterAll: (list) => {
        let allList = [];
        for (let i in list) {
            if (list.hasOwnProperty(i)) {
                if (list[i].name) {
                    allList.push(list[i]);
                }
                if (list[i].children && list[i].children.length > 0) {
                    allList = allList.concat(CommonFunc.filterAll(list[i].children));
                }
            }
        }
        return allList;
    },

    filterOrgAll: (list) => {
        let orgAllList = [];
        for (let i in list) {
            if (list.hasOwnProperty(i)) {
                orgAllList.push(list[i]);
                if (list[i].children && list[i].children.length > 0) {
                    orgAllList = orgAllList.concat(CommonFunc.filterOrgAll(list[i].children));
                }
            }
        }
        return orgAllList;
    },

    filterAllTreeId: (list) => {
        let alltreeids = [];
        for (let i in list) {
            if (list.hasOwnProperty(i)) {
                alltreeids.push(list[i].id);
                if (list[i].children && list[i].children.length > 0) {
                    alltreeids = alltreeids.concat(
                        CommonFunc.filterAllTreeId(list[i].children)
                    );
                }
            }
        }
        return alltreeids;
    },

    sleep: (t = 1) => {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve();
            }, t * 1000);
        });
    },

    formatTableHeight: async (options, dom) => {
        try {
            let _repeat = 20;
            let _pageHeight = 54;
            let tableHeader;
            options = options || {
                root: null,
                css: false,
                pageHeight: _pageHeight,
                repeat: _repeat
            };
            let {
                root = null,
                css = false,
                pageHeight = _pageHeight,
                repeat = _repeat
            } = options;
            if (!root || !root.tagName) {
                root = document.querySelector('.app-container').children[0];
            }
            let temp;
            let tableBox = null;
            let table = null;
            let page = null;
            temp = (dom || root).getElementsByClassName('el-table');
            if (temp[0]) {
                tableBox = temp[0];
            }
            temp = (dom || root).getElementsByClassName('table-list');
            if (temp[0]) {
                let top = 0;
                for (let v of temp) {
                    v = v.getElementsByClassName('el-table__body-wrapper');
                    if (v[0]) {
                        top = v[0].getBoundingClientRect().top;
                        if (top) {
                            table = v[0];
                            break;
                        }
                    }
                }
                if (top === 0) {
                    repeat = -2;
                }
                // temp = temp[0].getElementsByClassName('el-table__body-wrapper');
                // if (temp[0]) {
                //     for (const v of temp) {
                //         top = v.getBoundingClientRect().top;
                //         if (top) {
                //             table = v;
                //             break;
                //         }
                //     }
                //     if(top === 0) {
                //         repeat = -2;
                //     }
                // }
            }
            temp = (dom || root).getElementsByClassName('el-pagination');
            if (temp[0]) {
                page = temp[0];
            }
            let height = window.innerHeight;
            if (
                table &&
                ((page && page.clientHeight) || pageHeight || repeat === 0)
            ) {
                height -= table.getBoundingClientRect().top;
                if (page) {
                    height -= page.clientHeight;
                } else if (pageHeight) {
                    height -= pageHeight;
                }
                // 10 是下边框的宽度
                height -= 10;
                // 表头 最终Table组件的高度是包括表头的高度
                // 有Tab且Tab都有表格的页面 需在页面单独处理表头
                tableHeader = (dom || root).getElementsByClassName('el-table__header-wrapper');
                height += tableHeader[0].clientHeight;
                if (css) {
                    tableBox.style['max-height'] = height + 'px';
                    // 表格中出滚动条区域
                    table.style['max-height'] = height - tableHeader[0].clientHeight + 'px';
                }
            } else if (repeat >= 0) {
                await sleep(0.01 * (_repeat - repeat));
                height = await formatTableHeight({
                    root,
                    pageHeight,
                    repeat: --repeat
                });
            } else {
                height = 0;
            }
            return height;
        } catch (e) {
            return null;
        }
    },

    tbHegiht: (refs) => {
        let height = window.innerHeight;
        const tagsHeight =
            document.getElementById('tags-view-container').offsetHeight + 30;
        if (refs.vbox) {
            height = refs.vbox.offsetHeight;
        }
        if (refs.dvheader) {
            height -= refs.dvheader.offsetHeight;
        }
        // if (refs.footer) {
        //     height -= refs.footer.offsetHeight;
        // }
        if (refs.children && refs.children.length > 0) {
            for (const child of refs.children) {
                height -= child.offsetHeight;
            }
        }
        height -= tagsHeight;
        return height;
    },

    getPageList: (code, list) => {
        return new Promise((resolve, reject) => {
            Api.getUcenterpageconfigList({
                code
            }).then((res) => {
                if (res.code == 0) {
                    let newTit = [];
                    res.data.forEach((item) => {
                        list.forEach((i) => {
                            if (item.colKey == i.colKey) {
                                i.label = item.colName;
                                newTit.push(i);
                            }
                        });
                    });
                    resolve(newTit);
                } else {
                    reject();
                }
            });
        });
    },

    getPageFullList: (code) => {
        return new Promise((resolve, reject) => {
            Api.getUcenterpageconfigFullList({
                code
            }).then((res) => {
                if (res.code == 0) {
                    let list = res.data;
                    let fullList = [];
                    list.forEach((item, index) => {
                        fullList.push({
                            colKey: item.colKey,
                            colName: item.colName,
                            newName: item.newName,
                            isSelected: item.isSelected,
                            code: item.code,
                            name: item.name,
                            orderNo: item.orderNo
                        });
                    });
                    resolve(fullList);
                } else {
                    reject();
                }
            });
        });
    },

    setTrueTable: (records) => {
        return new Promise((resolve, reject) => {
            Api.getUcenterpageconfigSave({
                records: JSON.stringify(records)
            }).then((res) => {
                if (res.code == 0) {
                    resolve(res);
                } else {
                    reject();
                }
            });
        });
    },

    setDefaultTable: (code) => {
        return new Promise((resolve, reject) => {
            Api.getUcenterpageconfigReset({
                code
            }).then((res) => {
                if (res.code == 0) {
                    resolve(res);
                } else {
                    reject();
                }
            });
        });
    },

    filterPath: (code, attr ='name') => {
        if (template[code]) {
            return template[code][attr];
        } else {
            Message.error('未授予的访问权限');
        }
    },

    computedDate: (type) => {
        let now = new Date(), // 当前日期
            nowDayOfWeek = now.getDay(), // 今天本周的第几天
            nowDay = now.getDate(), // 当前日
            nowMonth = now.getMonth(), // 当前月
            nowYear = now.getYear(); // 当前年
        nowYear += nowYear < 2000 ? 1900 : 0;
        if (type == 'today') {
            return CommonFunc.formatDate(new Date());
        } else if (type == 'week') {
            let weekDay = nowDayOfWeek || 7;
            return (
                CommonFunc.formatDate(
                    new Date(now.getFullYear(), nowMonth, nowDay + 1 - weekDay)
                ) +
                '/' +
                CommonFunc.formatDate(
                    new Date(now.getFullYear(), nowMonth, nowDay + 7 - weekDay)
                )
            );
        } else if (type == 'mounth') {
            let monthStartDate = new Date(nowYear, nowMonth, 1);
            let monthEndDate = new Date(nowYear, nowMonth, CommonFunc.getMonthDays());
            return CommonFunc.formatDate(monthStartDate) + '/' + CommonFunc.formatDate(monthEndDate);
        } else if (type == 'prevMonth') {
            let monthlyEndDate = new Date(nowYear, nowMonth, 0);
            return (
                CommonFunc.formatDate(monthlyEndDate).split('-')[0] +
                '-' +
                CommonFunc.formatDate(monthlyEndDate).split('-')[1] +
                '-01' +
                '/' +
                CommonFunc.formatDate(monthlyEndDate)
            );
        } else if (type == 'season') {
            let quarterStartDate = new Date(nowYear, CommonFunc.getQuarterStartMonth(), 1);
            let quarterEndMonth = CommonFunc.getQuarterStartMonth() + 2;
            let quarterEndtDate = new Date(
                nowYear,
                quarterEndMonth,
                CommonFunc.getMonthDays(quarterEndMonth)
            );
            return CommonFunc.formatDate(quarterStartDate) + '/' + CommonFunc.formatDate(quarterEndtDate);
        } else if (type == 'year') {
            return nowYear + '-01-01' + '/' + nowYear + '-12-31';
        } else if (type == 'prevYear') {
            let year = nowYear - 1;
            return year + '-01-01' + '/' + year + '-12-31';
        }
    },

    formatDate: (date) => {
        let myyear = date.getFullYear();
        let mymonth = date.getMonth() + 1;
        let myweekday = date.getDate();

        if (mymonth < 10) {
            mymonth = '0' + mymonth;
        }
        if (myweekday < 10) {
            myweekday = '0' + myweekday;
        }
        return myyear + '-' + mymonth + '-' + myweekday;
    },

    getMonthDays: () => {
        var monthStartDate = new Date(nowYear, nowMonth, 1);
        var monthEndDate = new Date(nowYear, nowMonth + 1, 1);
        var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
        return days;
    },

    //获得本季度的开始月份
    getQuarterStartMonth: () => {
        let quarterStartMonth = 0;
        if (nowMonth < 3) {
            quarterStartMonth = 0;
        }
        if (2 < nowMonth && nowMonth < 6) {
            quarterStartMonth = 3;
        }
        if (5 < nowMonth && nowMonth < 9) {
            quarterStartMonth = 6;
        }
        if (nowMonth > 8) {
            quarterStartMonth = 9;
        }
        return quarterStartMonth;
    },

    dateConfig: (isNowDate = true) => {
        return {
            disabledDate(time) {
                if (!isNowDate) {
                    return time.getTime() > Date.now();
                } else {
                    return false;
                }
            },
            cellClassName(Date) {
                if (Date.getDay() === 0 || Date.getDay() === 6) {
                    return 'c-red';
                }
            }
        };
    },

    //kb转换
    formatBytes: (bytes, decimals) => {
        if (bytes == 0) return '0 Bytes';
        let k = 1024,
            dm = decimals || 2,
            sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
    },

    // 重置时间   去除秒
    clearSecond: (time) => {
        let newDate = /\d{4}-\d{1,2}-\d{1,2}\s\d{2}:\d{2}/g.exec(time);
        return time != null && newDate ? newDate[0] : time;
    },

    //将毫秒转换成时间
    transSecond: (mss) => {
        if (mss == null || mss == "") {
            return ""
        }
        let reg = new RegExp('[\\u4E00-\\u9FFF]+', 'g');
        if (mss != null && reg.test(mss)) {
            return mss;
        }



        var days = parseInt(mss / (1000 * 60 * 60 * 24));
        var hours = parseInt((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));

        if (minutes < 1) {
            return '1分钟';
        }

        let str =
            (days < 1 ? '' : days + '天') +
            (hours < 1 ? '' : hours + '小时') +
            (minutes < 1 ? '1分钟' : minutes + '分钟');
        return str;
    },

    //防抖
    throttle: (fn, delay) => {
        // last为上一次触发回调的时间, timer是定时器
        let last = 0,
            timer = null;
        // 将throttle处理结果当作函数返回

        return function () {
            // 保留调用时的this上下文
            let context = this;
            // 保留调用时传入的参数
            let args = arguments;
            // 记录本次触发回调的时间
            let now = +new Date();
            // 判断上次触发的时间和本次触发的时间差是否小于时间间隔的阈值
            if (now - last < delay) {
                // 如果时间间隔小于我们设定的时间间隔阈值，则为本次触发操作设立一个新的定时器
                clearTimeout(timer);
                timer = setTimeout(function () {
                    last = now;
                    fn.apply(context, args);
                }, delay);
            } else {
                // 如果时间间隔超出了我们设定的时间间隔阈值，那就不等了，无论如何要反馈给用户一次响应
                last = now;

                fn.apply(context, args);
            }
        }
    },

    addClass: (ele, cls) => {
        function hasClass(ele, cls) {
            return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
        }
        if (!hasClass(ele, cls)) ele.className += ' ' + cls
    },

    /**
     * Remove class from element
     * @param {HTMLElement} elm
     * @param {string} cls
     */
    removeClass: (ele, cls) => {
        function hasClass(ele, cls) {
            return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
        }
        if (hasClass(ele, cls)) {
            const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)')
            ele.className = ele.className.replace(reg, ' ')
        }
    },
    setMenulist: (that, data, menuId = null) => {



        //设置当前子系统id
        let curMenuId = menuId || data[0]?.id;

        if (!curMenuId) {
            setLocalStorage("menuData", []);
            return;
        };

        setLocalStorage("menuId", curMenuId)
        that.$store.commit('SET_MENUID', curMenuId)
        let menuMap = new Map(), menuLists = [];
        data.forEach(item => {
			item.subFunction = item.subFunction || item.menus;
            let data = getMenuList(lodash.cloneDeep(item.subFunction), item.id)
            menuMap.set(item.id, data)
            item.subFunction.id = item.id;
            menuLists.push(...item.subFunction)
        })
        that.$store.commit('SET_MENULIST_MAP', menuMap)
        
        let routers = createRouter(menuLists || []);
        routers.length && router.addRoutes(routers);
        that.$store.commit("SET_ROUTERS", routers);
        that.$store.commit("SET_MENULIST", menuMap.get(curMenuId) || []);
        setLocalStorage("menuData", data || []);

    },
    importAll: context => {
        const requireAll = requireContext => requireContext.keys().map(requireContext);

        const components = {};

        requireAll(context).forEach(element => {
            components[element.default.name] = element.default
        });

        return components
    }
}

export default CommonFunc
