package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.exception.ValidationException;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.handler.regular.RegularTypeContext;
import com.wisely.framework.helper.*;

import java.util.Set;

/**
 * 字符串转换器
 */
public class StringConverterImpl implements DefaultDataConverter<String>, ConverterConstants {

    @Override
    public boolean accept(ConverterItemEntity item) {
        return ValidHelper.isEquals(item.getType(), DATA_TYPE_STRING);
    }


    /**
     * 验证顺序
     * 1. required 必输
     * 2. maxlength 最大长度校验
     * 3. regular-type 特殊校验类型
     * 4. regular 正则
     *
     * @param item
     * @param obj
     */
    @Override
    public Object validation(ConverterItemEntity item, Object obj) {


        String o = obj == null ? null : DataHelper.getString(obj);

        if (ValidHelper.isBlank(o)) {
            item.validRequired();
            return o;
        }

        // 最大长度校验
        if (item.getMaxlength() != null &&
                StringHelper.length(o) > item.getMaxlength()) {
            String v = o;
            if (v.length() > 100) {
                v = StringHelper.left(v, 100) + "...";
            }
            throw ValidationException.builder("converter.out_of_maxlength_string.{0}.{1}", item.getName(), v, item.getMapper());
        }

        // 固定类型校验 
        String regularType = item.getExtendField(ConverterConstants.VALIDA_ATTR_REGULAR_TYPE);
        if (ValidHelper.isNotBlank(regularType) && SpringHelper.hasBean(RegularTypeContext.class)) {
            RegularTypeContext context = SpringHelper.getBean(RegularTypeContext.class);
            AssertHelper.EX_VALIDATION.isTrue(context.matchesByTypes(regularType, o),
                    "converter.regular_type_check_failed.regexType.{0}.str.{1}", regularType, o);
        }

        // 正则校验
        String regular = item.getExtendField(ConverterConstants.VALIDA_ATTR_REGULAR);
        if (ValidHelper.isNotBlank(regular)) {
            AssertHelper.EX_VALIDATION.isTrue(RegexHelper.matches(regular, o),
                    "converter.regular_check_failed.regex.{0}.str.{1}", regular, o);
        }

        // 范围
        String scope = item.getExtendField(ConverterConstants.VALIDA_ATTR_SCOPE);
        if (ValidHelper.isNotBlank(scope)) {
            Set<String> scopeSet = StringHelper.splitToSet(scope, ",");
            AssertHelper.EX_VALIDATION.isTrue(scopeSet.contains(o), "converter.out_of_scope.{0}.{1}", item.getName(), scope);
        }
        return o;
    }


    /**
     * 处理顺序：
     * 1. lsub 左截取
     * 2. rsub 右截取
     * 3. lpad 左补齐
     * 4. rpad 右补齐
     *
     * @param item
     * @param obj
     * @return
     */
    @Override
    public String convert(ConverterItemEntity item, Object obj) {

        String o = obj == null ? null : DataHelper.getString(obj);

        // 左截取
        if (ValidHelper.isNotEmpty(item.getLsub())) {
            o = StringHelper.right(o, DataHelper.getInt(item.getLsub(), Integer.MAX_VALUE));
        }

        // 右截取
        if (ValidHelper.isNotEmpty(item.getRsub())) {
            o = StringHelper.right(o, DataHelper.getInt(item.getRsub(), Integer.MAX_VALUE));
        }


        // 左补齐
        if (StringHelper.isNotBlank(item.getLpad())) {
            int padLen = item.getPadLen() == null ? item.getMaxlength() : item.getPadLen();
            o = StringHelper.leftPad(o, padLen, item.getLpad());
        }
        // 右补齐
        if (StringHelper.isNotBlank(item.getRpad())) {
            int padLen = item.getPadLen() == null ? item.getMaxlength() : item.getPadLen();
            o = StringHelper.rightPad(o, padLen, item.getRpad());
        }

        return o;
    }

}
