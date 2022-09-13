/*
 * @Author: your name
 * @Date: 2021-03-27 17:57:59
 * @LastEditTime: 2021-07-13 19:06:26
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\store\getter.js
 */
const getters = {
    sidebar: state => state.app.sidebar,
    device: state => state.app.device,
    noticeSum: state => state.app.noticeSum,
    noticeCount: state => state.app.noticeCount,
    meetingCount: state => state.app.meetingCount,
    systemCount: state => state.app.systemCount,
    token: state => state.user.token,
    menus: state => state.user.menus,
    allMenuList: state => state.user.allMenu,
    projectId: state => state.user.projectId,
    menuList: state => state.user.menuList,
    routers: state => state.user.routers,
    userId: state => state.user.userId,
    name: state => state.user.name,
    modifier: state => state.user.modifier,
    userManageInfo: state => state.user.userManageInfo,
    visitedViews: state => state.tagsView.visitedViews,
    cachedViews: state => state.tagsView.cachedViews,
    visitedMoudle: state => state.tagsView.visitedMoudle,
    menuListMap: state => state.user.menuListMap,
    menuId: state => state.user.menuId,

};

export default getters;
