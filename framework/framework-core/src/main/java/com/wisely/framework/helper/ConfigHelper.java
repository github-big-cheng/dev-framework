package com.wisely.framework.helper;

import com.wisely.framework.entity.Model;
import org.springframework.core.env.Environment;

import java.util.regex.Matcher;


/**
 * ConfigHelper
 * 基于 ENVIRONMENT
 */
public class ConfigHelper {

    private ConfigHelper() {

    }

    public static Environment getInstance() {
        return SpringHelper.getBean(Environment.class);
    }

    public static Integer getInt(String property, int _default) {
        return getInstance() == null ? _default : getInstance().getProperty(property, Integer.class, _default);
    }

    public static Integer getInt(String property) {
        return getInt(property, 0);
    }

    public static Long getLong(String property) {
        return getLong(property, 0l);
    }

    public static Long getLong(String property, long _default) {
        return getInstance() == null ? _default : getInstance().getProperty(property, Long.class, _default);
    }

    public static boolean getBoolean(String property, boolean _default) {
        return getInstance() == null ? _default : getInstance().getProperty(property, Boolean.class, _default);
    }

    public static boolean getBoolean(String property) {
        return getBoolean(property, false);
    }


    public static String getString(String property, String _default) {
        return getInstance() == null ? _default : getInstance().getProperty(property, _default);
    }

    public static String getString(String property) {
        return getInstance() == null ? null : getInstance().getProperty(property);
    }

    public static Boolean equals(String property, String value) {
        return ValidHelper.isEquals(getString(property), value);
    }

    public static boolean isEmpty(String property) {
        return ValidHelper.isEmpty(getString(property));
    }

    public static boolean isNotEmpty(String property) {
        return ValidHelper.isNotEmpty(getString(property));
    }

    /**
     * 替换占位符${?}为properties内容
     *
     * @param source
     * @param defaultValue
     * @return
     */
    public static String evaluate(String source, Model defaultValue) {
        Matcher matcher = RegexHelper.placeholder.matcher(source);
        while (matcher.find()) {
            source = source.replace(matcher.group(0),
                    ConfigHelper.getString(matcher.group(1), null == defaultValue ? "" : defaultValue.getString(defaultValue, "")));
        }
        return source;
    }

    public static String evaluate(String source) {
        return evaluate(source, null);
    }
}
