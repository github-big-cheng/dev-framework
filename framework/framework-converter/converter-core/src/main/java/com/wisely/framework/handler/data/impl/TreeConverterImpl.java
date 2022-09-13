package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.handler.operation.ConverterOperation;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.ValidHelper;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

/**
 * 支持树结构，无限节点循环
 * 默认解析器 MapConverterImpl
 */
public class TreeConverterImpl implements ConverterConstants, DefaultDataConverter {

    @Lazy // 循环引用，使用懒加载
    @Resource
    ConverterOperation converterOperation;

    @Override
    public boolean accept(ConverterItemEntity item) {
        return ValidHelper.isEquals(item.getType(), DATA_TYPE_TREE);
    }

    @Override
    public Object validation(ConverterItemEntity item, Object o) {
        ConverterItemEntity copy = this.itemCopyWithLowerLevel(item);
        return converterOperation.validation(copy, o);
    }


    @Override
    public Object convert(ConverterItemEntity item, Object o) {
        ConverterItemEntity copy = this.itemCopyWithLowerLevel(item);
        return converterOperation.converter(copy, o);
    }


    /**
     * 动态创建下级节点
     *
     * @param itemEntity
     * @return
     */
    protected ConverterItemEntity itemCopyWithLowerLevel(ConverterItemEntity itemEntity) {

        String treeKey = itemEntity.getExtendField(ATTR_TREE_KEY);
        String realType = itemEntity.getExtendField(ATTR_REAL_TYPE);

        // 复制为普通类型处理数据
        String itemJson = JsonHelper.obj2Json(itemEntity);
        ConverterItemEntity copy = JsonHelper.json2Obj(itemJson, ConverterItemEntity.class);
        copy.setType(realType);

        // 动态创建children节点
        ConverterItemEntity copyChildren = JsonHelper.json2Obj(itemJson, ConverterItemEntity.class);
        copyChildren.setName(treeKey);
        copyChildren.setTarget(null); // 移除target
        copyChildren.setRequired(false); // 设置非必输
        copy.getChildren().add(copyChildren);

        return copy;
    }

}
