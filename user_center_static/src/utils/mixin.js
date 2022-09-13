/*
 * @Author: your name
 * @Date: 2021-03-28 11:04:46
 * @LastEditTime: 2021-08-25 18:07:17
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\utils\mixin.js
 */

const mixin = {
    data() {
        return {
            RegExpNum: new RegExp("^[0-9]*$"),
        }
    },
    computed: {
        MXmenuId() {
            return this.$store.getters.menuId;
        }
    },
    methods: {
        /**
         * 全局返回方法
         * @param route 当前页面路由对象
         * @param {Boolean} close  是否关闭当前页面tag
         *
         */
        goBack(route, refresh = false) {
            let fromName = route.meta ? route.meta.from : null;
            let fromParams = route.meta ? route.meta.fromParams || {} : {};
            const fromQuery = route?.meta?.fromQuery || {}

            // 关闭当前tab
            this.$store.dispatch('tagsView/delView', route);
            if (fromName) {
                let fromView = this.$store.getters.visitedViews.filter(item => {
                    return fromName == item.name;
                });
                if (fromView && fromView.length > 0) {
                    this.$router.push({
                        name: fromName,
                        params: {
                            ...fromParams,
                            noCache: refresh
                        },
                        query: {
                            ...fromQuery,
                        }
                    });
                    return;
                }
            }

            // 默认打开最后一个tab
            let lastView = this.$store.getters.visitedViews[this.$store.getters.visitedViews.length - 1];
            this.$router.push({
                name: lastView.name
            });
        },
        /**
         * 
         * @param {*} route 当前路由
         * 关闭等待层
         */
        closeLoading(route) {
            route.meta.noLoading = true;
        },
        MXsetBtnLoading(obj, status) {
            obj.forEach(item => item.hasOwnProperty('btnLoading') && (item.btnLoading = status))
        },

    }
}
export default mixin