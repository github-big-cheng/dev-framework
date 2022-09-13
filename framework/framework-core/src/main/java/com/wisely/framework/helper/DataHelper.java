package com.wisely.framework.helper;

import com.wisely.framework.entity.Model;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.util.*;

/**
 * DataHelper
 * 数据处理工具类
 */
@Slf4j
public class DataHelper {

    private DataHelper() {

    }

    /**
     * 获取整型
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static Integer getInt(Object value, Integer defaultValue) {
        if (ValidHelper.isNull(value)) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return ((Integer) value);
        }
        if (value instanceof Double) {
            return ((Double) value).intValue();
        }
        if (value instanceof Float) {
            return ((Float) value).intValue();
        }
        if (value instanceof Short) {
            return ((Short) value).intValue();
        }
        if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        }
        String stringValue = value.toString().trim();
        try {
            if (stringValue.isEmpty() || stringValue.equalsIgnoreCase("null")) {
                return defaultValue;
            } else {
                return Integer.parseInt(stringValue);
            }
        } catch (Exception ex) {
            log.error("参数：{}，在转换为 Integer 型时异常:{}", value, ex);
        }
        return defaultValue;
    }

    public static Integer getInt(Object value) {
        return getInt(value, 0);
    }


    public static Long getLong(Object value) {
        return getLong(value, 0l);
    }

    public static Long getLong(Object value, Long defaultValue) {

        if (ValidHelper.isNull(value)) {
            return defaultValue;
        }

        if (value instanceof Long) {
            return ((Long) value);
        }
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        if (value instanceof Double) {
            return ((Double) value).longValue();
        }
        if (value instanceof Float) {
            return ((Float) value).longValue();
        }
        if (value instanceof Short) {
            return ((Short) value).longValue();
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        }
        if (value instanceof Date) {
            return ((Date) value).getTime();
        }

        try {
            String stringValue = value.toString().trim();
            if (stringValue.indexOf(".") != -1) {
                stringValue = stringValue.substring(0, stringValue.indexOf("."));
            }
            if (stringValue.isEmpty() || stringValue.equalsIgnoreCase("null")) {
                return defaultValue;
            } else {
                return Long.parseLong(stringValue);
            }
        } catch (Exception ex) {
            log.error("参数：{}，在转换为 Long 型时异常:{}", value, ex);
        }
        return defaultValue;
    }

    public static boolean getBoolean(Object value) {
        if (ValidHelper.isNull(value)) {
            return false;
        }
        if (value instanceof Boolean) {
            return ((Boolean) value);
        }

        String val = StringHelper.trim(value.toString());
        return !(val.isEmpty() || StringHelper.equalsAnyIgnoreCase(val, "false", "null", "no"));
    }

    public static String getString(Object value) {
        return getString(value, "");
    }

    public static String getString(Object value, String defVal) {
        if (ValidHelper.isNull(value)) {
            return defVal;
        }
        if (value instanceof String) {
            return ((String) value);
        }

        if (value instanceof Date) {
            return DateHelper.format((Date) value);
        }
        if (value instanceof LocalDateTime) {
            return DateHelper.format((LocalDateTime) value);
        }

        return value.toString();
    }


    /**
     * 获取BigDecimal对象
     *      默认值固定为BigDecimal.ZERO
     *
     * @param value
     * @return
     */
    public static BigDecimal getBigDecimal(Object value) {
        return getBigDecimal(value, BigDecimal.ZERO);
    }

    /**
     * 获取BigDecimal对象
     *      如果对象为null或不可转换为数字则返回默认值
     *
     * @param value
     * @param defValue
     * @return
     */
    public static BigDecimal getBigDecimal(Object value, BigDecimal defValue) {

        if (ValidHelper.isEmpty(value)) {
            return defValue;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value);
        }

        if (value instanceof Character) {
            int i = (char) value - '0';
            return new BigDecimal(i);
        }

        try {
            // 支持千分位格式的转换
            DecimalFormat format = new DecimalFormat();
            format.setParseBigDecimal(true);
            ParsePosition position = new ParsePosition(0);
            BigDecimal val = (BigDecimal) format.parse(getString(value), position);
            return ValidHelper.isNull(val) ? defValue : val;
        } catch (Exception ex) {
            log.error("参数：{}，在转换为 BigDecimal 型时异常:{}", value, ex);
        }
        return defValue;
    }


    /**
     * 返回 value 在 values中出现的次数
     *
     * @param values
     * @param value
     * @return
     */
    public static int frequency(List values, Object value) {
        return Collections.frequency(values, value);
    }

    /**
     * 返回 value 在 values中出现的次数
     *
     * @param values
     * @param key
     * @param value
     * @return
     */
    public static int frequency(List<? extends Model> values, String key, Object value, String uniqueId) {
        List merges = new ArrayList();
        for (Model result : values) {
            if (result.equals("id", uniqueId)) {
                merges.add(value);
            } else {
                merges.add(result.get(key));
            }
        }
        return Collections.frequency(merges, value);
    }

    /**
     * BigDecimal 相加
     *
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal add(Object b1, Object b2) {
        return getBigDecimal(b1).add(getBigDecimal(b2));
    }

    /**
     * BigDecimal 相减
     *
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal subtract(Object b1, Object b2) {
        return getBigDecimal(b1).subtract(getBigDecimal(b2));
    }

    /**
     * BigDecimal 乘法
     *
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal multiply(Object b1, Object b2) {
        return getBigDecimal(b1).multiply(getBigDecimal(b2));
    }

    /**
     * dividend / divisor
     *
     * @param dividend
     * @param divisor
     * @param scale
     * @return
     */
    public static BigDecimal divide(Object dividend, Object divisor, int scale) {

        BigDecimal dividendBig = getBigDecimal(dividend);
        BigDecimal divisorBig = getBigDecimal(divisor);

        if (dividendBig.compareTo(BigDecimal.ZERO) < 1 || divisorBig.compareTo(BigDecimal.ZERO) < 1) {
            return BigDecimal.ZERO;
        } else {
            return dividendBig.divide(divisorBig, scale, BigDecimal.ROUND_HALF_UP);
        }
    }


    /**
     * 数值格式化
     *
     * @param num
     * @return
     */
    public static String getDecimalString(Object num) {
        return getDecimalString(num, true, "#,###.0");
    }

    /**
     * 数值格式化
     *
     * @param num
     * @return
     */
    public static String getDecimalString(Object num, String pattern) {
        return getDecimalString(num, true, pattern);
    }

    /**
     * 数值格式化
     *
     * @param num
     * @return
     */
    public static String getDecimalString(Object num, DecimalFormat df) {
        return getDecimalString(num, true, df);
    }

    /**
     * 数值格式化
     *
     * @param num
     * @param middleBar
     * @param pattern
     * @return
     */
    public static String getDecimalString(Object num, boolean middleBar, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return getDecimalString(num, middleBar, df);
    }

    /**
     * 数值格式化
     *
     * @param num
     * @param scale
     * @param pattern
     * @return
     */
    public static String getDecimalString(Object num, int scale, String pattern) {
        BigDecimal t = DataHelper.getBigDecimal(num).setScale(scale, BigDecimal.ROUND_HALF_UP);
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(t);
    }


    /**
     * 数值格式化
     *
     * @param num
     * @param middleBar
     * @param df
     * @return
     */
    public static String getDecimalString(Object num, boolean middleBar, DecimalFormat df) {
        BigDecimal val = getBigDecimal(num);
        return val.compareTo(BigDecimal.ZERO) == 0 && middleBar ? "-" : df.format(val);
    }


    public static <T> List<T> getList(Object obj) {
        if (ValidHelper.isNull(obj)) {
            return null;
        }

        try {
            // 尝试按 jsonArray 解析
            if (obj instanceof String) {
                String str = getString(obj).trim();
                if (StringHelper.startsWith(str, "[")) {
                    return JsonHelper.json2Obj((String) obj, new TypeReference<List<T>>() {
                    });
                }
            }
        } catch (Exception e) {
            // ignore
        }

        if (obj instanceof List) {
            return ((List<T>) obj);
        }
        return Lists.newArrayList((T) obj);
    }


    public static List<Model> getModelList(Object obj) {

        List<Model> results = Lists.newArrayList();

        List values = getList(obj);
        if (ValidHelper.isNull(values)) {
            return results;
        }

        values.forEach(value -> {
            if (value instanceof Model) {
                results.add((Model) value);
            } else if (value instanceof Map) {
                results.add(Model.builder((Map) value));
            } else {
                results.add(Model.parseObject(value));
            }
        });
        return results;
    }


    /**
     * 查找字符串email的邮箱名称
     *
     * @param email
     * @return 邮箱 用户名
     */
    public static String findEmailName(String email) {
        return ValidHelper.isBlank(email) || StringHelper.indexOf(email, "@") == -1
                ? "" : email.substring(0, email.indexOf("@"));
    }
}
