package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.ValidHelper;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字符串转换器
 */
public class JsonStringConverterImpl implements DefaultDataConverter<String>, ConverterConstants {


    @Resource
    ListConverterImpl listConverter;

    @Resource
    MapConverterImpl mapConverter;


    @Override
    public boolean accept(ConverterItemEntity item) {
        return ValidHelper.isEquals(item.getType(), DATA_TYPE_JSONSTRING);
    }


    @Override
    public Object validation(ConverterItemEntity item, Object o) {

        if (ValidHelper.isEmpty(o)) {
            item.validRequired();
            return o;
        }

        String realType = item.getExtendField(ATTR_REAL_TYPE);

        String itemJson = JsonHelper.obj2Json(item);
        ConverterItemEntity copy = JsonHelper.json2Obj(itemJson, ConverterItemEntity.class);
        copy.setType(realType);

        Object result = o;

        // List 格式
        if (ValidHelper.isEquals(DATA_TYPE_LIST, realType)) {
            List<Model> dataList = DataHelper.getModelList(o);
            result = listConverter.validation(copy, dataList);
        }

        // Map 格式
        if (ValidHelper.isEquals(DATA_TYPE_MAP, realType)) {
            result = mapConverter.validation(copy, Model.parseObject(o));
        }

        return result;
    }

    /**
     * 返回操作
     *
     * @param item
     * @param obj
     * @return
     */
    @Override
    public String convert(ConverterItemEntity item, Object obj) {

        String realType = item.getExtendField(ATTR_REAL_TYPE);
        if (ValidHelper.isBlank(realType)) {
            return obj == null ? null : JsonHelper.obj2Json(obj);
        }

        String itemJson = JsonHelper.obj2Json(item);
        ConverterItemEntity copy = JsonHelper.json2Obj(itemJson, ConverterItemEntity.class);

        // 复制为普通类型处理数据
        copy.setType(realType);

        Object result = obj;
        // List 格式
        if (ValidHelper.isEquals(DATA_TYPE_LIST, realType)) {
            List<Model> dataList = DataHelper.getModelList(obj);
            result = listConverter.convert(copy, dataList);
        }

        // Map 格式
        if (ValidHelper.isEquals(DATA_TYPE_MAP, realType)) {
            result = mapConverter.convert(copy, Model.parseObject(obj));
        }

        return result == null ? null : JsonHelper.obj2Json(result);
    }

}
