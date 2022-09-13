/*
 * @Author: your name
 * @Date: 2021-05-07 10:05:31
 * @LastEditTime: 2021-08-26 10:35:21
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\utils\formatMenu.js
 */
import { template } from "@/router/addRouter"
import lodash from 'lodash'
let newTemplate = lodash.cloneDeep(template);
function createMenu(menus){
  let menuList = [];
  for(let i in menus){
    if(menus.hasOwnProperty(i)){
      if(menus[i].type != 3 && menus[i].type != 4 && newTemplate[menus[i].code]){
        newTemplate[menus[i].code].meta.title = menus[i].name;
        newTemplate[menus[i].code].meta.icon = menus[i].imgPath;
        newTemplate[menus[i].code].meta.menuId = menus[i].projectId;
        newTemplate[menus[i].code].meta.asyncRoute = true;
        let item = lodash.cloneDeep(newTemplate[menus[i].code]);
        menuList.push(item);
        if (!Array.isArray(menus[i].subFunction)) return;

        item.children = menus[i].subFunction.length > 0 ? 
          createMenu(menus[i].subFunction).menuList : [];
      }
    }
  }
  return { menuList };
}


function getMenuList(menus){
  let menuList = createMenu(menus).menuList;
  // menuList.unshift({
  //   meta: {title: "首页", icon: "el-icon-alihome"},
  //   icon: "el-icon-alihome",
  //   title: "首页",
  //   path: "/home",
  //   redirect: "/home",
  //   children:[]
  // })
  return menuList;
}

export { getMenuList };
