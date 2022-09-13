package com.wisely.framework.exception.asse;

import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.BaseException;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ValidHelper;

import java.util.Collection;

public interface FrameworkAssert<T extends BaseException> {


    T builder(String message, Object... params);

    /**
     * false抛出異常
     * true无操作
     *
     * @param expression
     * @param message
     * @param params
     */
    default void isTrue(boolean expression, String message, Object... params) {
        if (!DataHelper.getBoolean(expression)) {
            throw builder(message, params);
        }
    }


    /**
     * true抛出異常
     * false无操作
     *
     * @param expression
     * @param message
     * @param params
     */
    default void isFalse(boolean expression, String message, Object... params) {
        this.isTrue(!expression, message, params);
    }

    /**
     * 必定为空
     *
     * @param object
     * @param message
     * @param params
     */
    default void isNull(Object object, String message, Object... params) {
        isTrue(ValidHelper.isNull(object), message, params);
    }

    /**
     * 必定为空
     *
     * @param object
     * @param message
     * @param params
     */
    default void isNotNull(Object object, String message, Object... params) {
        isTrue(ValidHelper.isNotNull(object), message, params);
    }

    /**
     * 必定为空
     *
     * @param object
     * @param message
     * @param params
     */
    default void isEmpty(Object object, String message, Object... params) {
        isTrue(ValidHelper.isEmpty(object), message, params);
    }

    /**
     * 必定为空
     *
     * @param input
     * @param field
     * @param message
     * @param params
     */
    default void isEmpty(Model input, String field, String message, Object... params) {
        isTrue(input.isEmpty(field), message, params);
    }

    /**
     * 必定不为空
     *
     * @param object
     * @param message
     * @param params
     */
    default void isNotEmpty(Object object, String message, Object... params) {
        isTrue(ValidHelper.isNotEmpty(object), message, params);
    }

    /**
     * 必定不为空
     *
     * @param input
     * @param field
     * @param message
     * @param params
     */
    default void isNotEmpty(Model input, String field, String message, Object... params) {
        isTrue(input.isNotEmpty(field), message, params);
    }


    /**
     * 字符串-必定是blank
     *
     * @param object
     * @param message
     * @param params
     */
    default void isBlank(String object, String message, Object... params) {
        isTrue(ValidHelper.isBlank(object), message, params);
    }

    /**
     * 字符串-必定不是blank
     *
     * @param object
     * @param message
     * @param params
     */
    default void isNotBlank(String object, String message, Object... params) {
        isTrue(ValidHelper.isNotBlank(object), message, params);
    }


    /**
     * 字符串-必定不是blank
     *
     * @param model
     * @param field
     * @param message
     * @param params
     */
    default void isNotBlank(Model model, String field, String message, Object... params) {
        isTrue(ValidHelper.isNotBlank(model.getString(field)), message, params);
    }


    /**
     * 必定相等
     *
     * @param obj
     * @param value
     * @param message
     * @param params
     */
    default void isEquals(Object obj, Object value, String message, Object... params) {
        isTrue(ValidHelper.isEquals(obj, value), message, params);
    }

    /**
     * 必定不相等
     *
     * @param obj
     * @param value
     * @param message
     * @param params
     */
    default void isNotEquals(Object obj, Object value, String message, Object... params) {
        isTrue(!ValidHelper.isEquals(obj, value), message, params);
    }

    /**
     * 必定相等
     *
     * @param input
     * @param field
     * @param value
     * @param message
     * @param params
     */
    default void isEquals(Model input, String field, Object value, String message, Object... params) {
        isTrue(input.equals(field, value), message, params);
    }

    /**
     * 必定不相等
     *
     * @param input
     * @param field
     * @param value
     * @param message
     * @param params
     */
    default void isNotEquals(Model input, String field, Object value, String message, Object... params) {
        isTrue(!input.equals(field, value), message, params);
    }


    /**
     * 必定是数字
     *
     * @param object
     * @param message
     * @param params
     */
    default void isNumber(Object object, String message, Object... params) {
        isTrue(ValidHelper.isNumber(DataHelper.getString(object)), message, params);
    }

    /**
     * 必定是数字
     *
     * @param input
     * @param field
     * @param message
     * @param params
     */
    default void isNumber(Model input, String field, String message, Object... params) {
        isTrue(!input.isNumber(field), message, params);
    }

    /**
     * 数组是指定大小
     *
     * @param arrays
     * @param len
     * @param message
     * @param params
     */
    default void isSize(Object[] arrays, int len, String message, Object... params) {
        isTrue(ValidHelper.isSize(arrays, len), message, params);
    }

    /**
     * 集合是指定大小
     *
     * @param collection
     * @param len
     * @param message
     * @param params
     */
    default void isSize(Collection collection, int len, String message, Object... params) {
        isTrue(ValidHelper.isSize(collection, len), message, params);
    }

}
