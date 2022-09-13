package com.wisely.framework.helper;

import com.wisely.framework.exception.SystemException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * JSON转换处理工具类
 */
@Slf4j
public class JsonHelper {

    private JsonHelper() {
    }

    private static ObjectMapper om;

    static {

        om = SpringHelper.getBean(ObjectMapper.class);
        if (om == null) {
            om = new ObjectMapper();
            // 对象的所有字段全部列入，还是其他的选项，可以忽略null等
            om.setSerializationInclusion(Include.ALWAYS);
            // 设置Date类型的序列化及反序列化格式
            om.setDateFormat(new SimpleDateFormat(DateHelper.PATTERN_DATETIME_01));
            // 忽略空Bean转json的错误
            om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            // 忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 注册一个时间序列化及反序列化的处理模块，用于解决jdk8中localDateTime等的序列化问题
            om.registerModule(new JavaTimeModule());
            //允许使用未带引号的字段名
            om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            //允许使用单引号
            om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        }
    }

    /**
     * 对象 => json字符串
     *
     * @param obj 源对象
     */
    public static <T> String obj2Json(T obj) {

        String json = null;
        if (obj != null) {
            try {
                json = om.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                log.warn(e.getMessage(), e);
                throw SystemException.builder(e, "json.json_parser_error");
            }
        }
        return json;
    }

    public static <T> JsonNode obj2JsonNode(T obj) {
        return json2Obj(obj2Json(obj), JsonNode.class);
    }

    /**
     * json字符串 => 对象
     *
     * @param json  源json串
     * @param clazz 对象类
     * @param <T>   泛型
     */
    public static <T> T json2Obj(String json, Class<T> clazz) {
        return json2Obj(json, clazz, null);
    }

    /**
     * json字符串 => 对象
     *
     * @param json 源json串
     * @param type 对象类型
     * @param <T>  泛型
     */
    public static <T> T json2Obj(String json, TypeReference<T> type) {
        return json2Obj(json, null, type);
    }


    /**
     * 复制
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T copy(T obj) {
        return (T) json2Obj(obj2Json(obj), obj.getClass());
    }


    /**
     * 复制
     *
     * @param obj
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T copy(T obj, TypeReference<T> type) {
        return (T) json2Obj(obj2Json(obj), type);
    }

    /**
     * json => 对象处理方法
     * <br>
     * 参数clazz和type必须一个为null，另一个不为null
     * <br>
     * 此方法不对外暴露，访问权限为private
     *
     * @param json  源json串
     * @param clazz 对象类
     * @param type  对象类型
     * @param <T>   泛型
     */
    private static <T> T json2Obj(String json, Class<T> clazz, TypeReference<T> type) {

        T obj = null;
        if (!StringUtils.isEmpty(json)) {
            try {
                if (clazz != null) {
                    obj = om.readValue(json, clazz);
                } else {
                    obj = om.readValue(json, type);
                }
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        return obj;
    }


    /**
     * 强制类型转换
     *
     * @param o
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T cast(Object o, Class<T> clazz) {
        return json2Obj(obj2Json(o), clazz);
    }

    /**
     * 强制类型转换
     *
     * @param o
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T cast(Object o, TypeReference<T> typeReference) {
        return json2Obj(obj2Json(o), typeReference);
    }

    /**
     * 强制类型转换
     *
     * @param o
     * @param <T>
     * @return
     */
    public static <T> T cast(Object o) {
        return json2Obj(obj2Json(o), new TypeReference<T>() {
        });
    }
}
