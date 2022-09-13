package com.wisely.ucenter.controller;


import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.ucenter.entity.UcenterPosition;
import com.wisely.ucenter.service.UcenterPositionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 职务(UcenterPosition)表控制层
 *
 * @since 2021-05-31 09:23:04
 */
@RestController
@RequestMapping("/ucenterPosition")
public class UcenterPositionController {

    @Resource
    UcenterPositionService ucenterPositionService;


    /**
     * 分页列表
     *
     * @param query
     * @param pageVo
     * @return
     */
    @RequestMapping(value = {"/list", "/combox"})
    @Converter(path = "position/list")
    public Object list(UcenterPosition query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(ucenterPositionService.findByPage(query, pageVo));
    }


    /**
     * 新增
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    @Converter(path = "position/add")
    public Object add(UcenterPosition record) {
        return ResponseBuilder.buildSuccess(ucenterPositionService.save(record));
    }

    /**
     * 修改
     *
     * @param record
     * @return
     */
    @RequestMapping("/save")
    @Converter(path = "position/edit")
    public Object save(UcenterPosition record) {
        return ResponseBuilder.buildSuccess(ucenterPositionService.save(record));
    }

    /**
     * 单条查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/view")
    @Converter(path = "position/view")
    public Object load(Integer id) {
        return ResponseBuilder.buildSuccess(ucenterPositionService.load(id));
    }


    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    @Converter(path = "position/delete")
    public Object delete(String ids) {
        ucenterPositionService.delete(ids);
        return ResponseBuilder.buildSuccess();
    }
}
