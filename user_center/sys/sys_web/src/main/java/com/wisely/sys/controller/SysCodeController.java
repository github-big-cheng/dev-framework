package com.wisely.sys.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sys.entity.SysCode;
import com.wisely.sys.service.SysCodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统代码(SysCode)控制层
 *
 * @author system
 * @date 2021-05-28 17:42:04
 */
@RestController
@RequestMapping(value = "/sysCode")
public class SysCodeController {

    @Resource
    private SysCodeService sysCodeService;


    /**
     * 公用查询接口
     *
     * @param query SysCode对象
     * @return Object对象
     */
    @RequestMapping("/combox")
    @Converter(path = "code/combox")
    public Object combox(SysCode query) {

        // 防全表查询
        boolean flag1 = StringHelper.isNotBlank(query.getType());
        boolean flag2 = StringHelper.isNotBlank(query.getTypeQueryIsNull());
        boolean flag3 = StringHelper.isNotBlank(query.getTypeQueryNotIn());
        AssertHelper.EX_VALIDATION.isTrue(flag1 || flag2 || flag3, "common.parameter_required.parameters");

        return ResponseBuilder.buildSuccess(sysCodeService.findList(query));
    }


    /**
     * 分页查询
     *
     * @param query  SysCode对象
     * @param pageVo PageVo对象
     * @return Object对象
     */
    @RequestMapping("/list")
    @Converter(path = "code/list")
    public Object list(SysCode query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(sysCodeService.findPage(query, pageVo));
    }


    /**
     * 代码新增
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    @Converter(path = "code/add")
    public Object add(SysCode record) {
        return ResponseBuilder.buildSuccess(sysCodeService.add(record));
    }

    /**
     * 修改代码
     *
     * @param record
     * @return
     */
    @RequestMapping("/save")
    @Converter(path = "code/save")
    public Object save(SysCode record) {
        return ResponseBuilder.buildSuccess(sysCodeService.save(record));
    }

    /**
     * 批量删除代码
     *
     * @param values 需要批量删除的代码值values
     * @return 删除的结果
     */
    @RequestMapping("/delete")
    @Converter(path = "code/delete")
    public Object delete(String values) {
        return ResponseBuilder.buildSuccess(sysCodeService.deleteByValues(values));
    }

    /**
     * 单个查询
     *
     * @param id 主键VALUE
     * @return Object对象
     */
    @RequestMapping("/view")
    @Converter(path = "code/view")
    public Object view(Integer id) {
        return ResponseBuilder.buildSuccess(sysCodeService.load(id));
    }


    @RequestMapping("/tree")
    @Converter(path = "code/tree")
    public Model tree(SysCode query) {

        // 防全表查询
        boolean flag1 = ValidHelper.isNotBlank(query.getType());
        boolean flag2 = ValidHelper.isNotBlank(query.getPathValueQueryLike());
        AssertHelper.EX_VALIDATION.isTrue(flag1 || flag2, "common.parameter_required.parameters");

        return ResponseBuilder.buildSuccess(sysCodeService.tree(query));
    }
}
