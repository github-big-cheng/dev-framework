package com.wisely.ucenter.controller;


import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.ucenter.entity.UcenterObjFunc;
import com.wisely.ucenter.service.UcenterObjFuncService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 菜单(TUcenterFunction)表控制层
 *
 * @author system
 * @since 2021-05-28 17:42:04
 */
@RestController
@RequestMapping("ucenterObjFunc")
public class UcenterObjFuncController {

    @Resource
    UcenterObjFuncService ucenterObjFuncService;

    /**
     * 菜单权限树
     * <p>
     * 参数 projectId ---> 应用ID
     * 参数 objType ---> 权限类型 T_SYS_CODE.value 部门10079-10 职位10079-20 、用户10079-30、角色 10079-40
     * 参数 objId   ---> ID
     *
     * @return
     */
    @RequestMapping("/list")
    @Converter(path = "objfunc/list")
    public Object list(UcenterObjFunc query) {
        return ResponseBuilder.buildSuccess(ucenterObjFuncService.list(query));
    }


    /**
     * 修改
     * 参数 objType ---> 权限类型 T_UCENTER_CODE.value
     * 参数 objId   ---> ID
     * 部门10079-10 职位10079-20 、用户10079-30、角色 10079-40
     *
     * @param record
     * @return
     */
    @Converter(path = "objfunc/save")
    @RequestMapping("/save")
    public Object save(UcenterObjFunc record) {
        ucenterObjFuncService.save(record);
        return ResponseBuilder.buildSuccess();
    }
}
