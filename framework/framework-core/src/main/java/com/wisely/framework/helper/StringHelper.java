package com.wisely.framework.helper;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * org.apache.commons.lang3.StringUtils
 */
public class StringHelper extends StringUtils {

    private StringHelper() {

    }


    /**
     * 字符串分割成list
     *
     * @param str
     * @param pattern
     * @return
     */
    public static Set<String> splitToSet(String str, String pattern) {
        return splitToStream(str, pattern).collect(Collectors.toSet());
    }


    /**
     * 字符串分割成list
     *
     * @param str
     * @param pattern
     * @return
     */
    public static List<String> splitToList(String str, String pattern) {
        if (isBlank(str)) {
            return Lists.newArrayList();
        }
        return Splitter.on(pattern).trimResults().omitEmptyStrings().splitToList(str);
    }

    /**
     * 字符串分割成list
     *
     * @param str
     * @param pattern
     * @return
     */
    public static Stream<String> splitToStream(String str, String pattern) {
        if (isBlank(str)) {
            return Stream.empty();
        }
        return Splitter.on(pattern).splitToStream(str);
    }


    /**
     * 获取表达式中的变量名
     *
     * @param expression
     * @return
     */
    public static String getVariableNameByExpression(String expression) {
        return expression.replace("${", "")
                .replace("}", "");
    }


    /**
     * 消息格式化
     *
     * @param message
     * @param patterns
     * @return
     */
    public static String format(String message, Object... patterns) {
        String[] arr = new String[0];
        if (ValidHelper.isNotEmpty(patterns)) {
            arr = new String[patterns.length];
            for (int i = 0; i < patterns.length; i++) {
                arr[i] = DataHelper.getString(patterns[i]); // null数据处理
            }
        }
        return MessageFormat.format(message, arr);
    }

    /**
     * 字符串不相等比较
     *
     * @param cs1 字符串1
     * @param cs2 字符串2
     * @return boolean
     */
    public static boolean notEquals(CharSequence cs1, CharSequence cs2) {
        return !equals(cs1, cs2);
    }

    /**
     * 字符串不相等比较 (忽略大小写)
     *
     * @param cs1 字符串1
     * @param cs2 字符串2
     * @return boolean
     */
    public static boolean notEqualsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        return !equalsIgnoreCase(cs1, cs2);
    }

    /**
     * 字符串不包含指定内容
     *
     * @param target 目标数据
     * @param search 匹配内容
     * @return boolean
     */
    public static boolean notContains(CharSequence target, CharSequence search) {
        return !contains(target, search);
    }

    /**
     * 字符串不包含指定内容 (忽略大小写)
     *
     * @param target 目标数据
     * @param search 匹配内容
     * @return boolean
     */
    public static boolean notContainsIgnoreCase(CharSequence target, CharSequence search) {
        return !containsIgnoreCase(target, search);
    }


    /**
     * 过滤blank元素并拼接
     *
     * @param separator
     * @param values
     * @return
     */
    public static String joinSkipBlank(final String separator, String... values) {
        return Joiner.on(separator).skipNulls().join(values);
    }


    /**
     * 过滤blank元素并拼接
     *
     * @param separator
     * @param values
     * @return
     */
    public static String joinSkipBlank(Collection<String> values, final String separator) {
        return Joiner.on(separator).skipNulls().join(values);
    }
}
