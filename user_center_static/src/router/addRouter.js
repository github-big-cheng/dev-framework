/*
 * @Author: your name
 * @Date: 2021-05-08 10:06:36
 * @LastEditTime: 2022-02-24 13:43:42
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\router\addRouter.js
 */
/*
 * @Author: your name
 * @Date: 2021-03-27 17:57:59
 * @LastEditTime: 2021-05-07 16:55:50
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\utils\addRouter.js
 */
import { setLocalStorage } from '@/utils/auth';
import {approvalViewRouter} from '@/utils/flowRouter';

const routersFiles = require.context('./modules', true, /\.js$/)

const routers = routersFiles.keys().reduce((modules, modulePath) => {
    const value = routersFiles(modulePath)
    Object.assign(modules, value.default)
    return modules
}, {})

let template = Object.assign({}, routers, approvalViewRouter);
function recursion(arr, btnArr) {
    let router = [];
    btnArr = btnArr || [];
    for (let i in arr) {
        if (arr.hasOwnProperty(i)) {
            if ((arr[i].type == 1 || arr[i].type == 2 || arr[i].type == 4) && template[arr[i].code]) {
                template[arr[i].code].meta.title = arr[i].name;
                //设置标签名字
                template[arr[i].code].meta.tagTitle = arr[i].tagTitle;
                //设置等待层
                template[arr[i].code].meta.noLoading = false;
                //设置临时固定
                template[arr[i].code].meta.temAffix = false;

                template[arr[i].code].meta.menuId = arr[i].projectId;
                template[arr[i].code].meta.asyncRoute = true;

                router.push(template[arr[i].code]);
                
            }
            // 审批查看特殊处理
            if (arr[i].code == 'oa_flow_view_approval') {
                for(let key in approvalViewRouter) {
                    let tem = approvalViewRouter[key];
                    tem.meta.parentId = arr[i].parentId;
                    tem.meta.menuId = arr[i].projectId;
                    tem.meta.tagTitle = tem.meta.title;
                    tem.meta.noLoading = false;
                    // tem.meta.title = arr[i].name;
                    router.push(tem);
                }
            }
            if (arr[i].type == 4) {
                btnArr.push(arr[i].code);
            }
            if (arr[i].subFunction && arr[i].subFunction.length > 0) {
                router = router.concat(recursion(arr[i].subFunction, btnArr).router)
            }
        }
    }
    return {
        router: router,
        btnArr: btnArr
    };

}

function createRouter(routeData) {
    if (!routeData.length) return [];
    //设置标签名字
    setTagTitle(routeData);
    let data = recursion(routeData);
    let router = data.router;
    let btnArr = data.btnArr;
    setLocalStorage("btnArr", btnArr);
    // router.unshift(...indexRouter);
    return router;
}

//设置标签名字
let newTagTitle = {};

function setTagTitle(routeData) {
    for (let i in routeData) {
        if (routeData.hasOwnProperty(i)) {
            if ((routeData[i].type == "1" || routeData[i].type == "2" || routeData[i].type == "3") && routeData[i].name != null && routeData[i].id != null) {
                newTagTitle[routeData[i].id] = routeData[i].name;
            }
            if (routeData[i].type == 4) {
                const parentName = newTagTitle[routeData[i].parentId]
                routeData[i].tagTitle = (parentName ? (`${parentName}-`) : '') + routeData[i].name;
            }
            if (routeData[i].subFunction && routeData[i].subFunction.length > 0) {
                setTagTitle(routeData[i].subFunction);
            }
        }
    }
}

export { createRouter, template };
