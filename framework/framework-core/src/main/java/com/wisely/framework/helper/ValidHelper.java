package com.wisely.framework.helper;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * ValidHelper
 * 验证工具类
 */
public class ValidHelper implements Serializable {

    private ValidHelper() {

    }

    public static Boolean isNull(Object object) {
        return object == null;
    }

    public static Boolean isNotNull(Object object) {
        return !isNull(object);
    }

    public static Boolean isEmpty(Object obj) {

        if (isNull(obj)) {
            return true;
        }
        if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // 数值 -- 小于0 认为empty
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue() <= 0;
        }

        // else
        return false;
    }


    public static Boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }


    public static Boolean isBlank(String string) {
        return isEmpty(string) || string.trim().length() == 0;
    }

    public static Boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    /**
     * 集合是否指定大小
     *
     * @param arrays
     * @param len
     * @return
     */
    public static Boolean isSize(Object[] arrays, int len) {
        if (len == 0) {
            return arrays != null && isEmpty(arrays);
        }
        return isNotEmpty(arrays) && isEquals(arrays.length, len);
    }

    /**
     * 集合是否指定大小
     *
     * @param collection
     * @param len
     * @return
     */
    public static Boolean isSize(Collection collection, int len) {
        if (len == 0) {
            return collection != null && isEmpty(collection);
        }
        return isNotEmpty(collection) && isEquals(collection.size(), len);
    }

    /**
     * <b>功能：</b>验证字符串str是否只包含英文,数字和"-_"
     *
     * @param str 待验证字符串
     * @return true    是只包含英文,数字和"-_" false	不是只包含英文,数字和"-_"
     */
    public static boolean onlyCharAndNumber(String str) {
        return RegexHelper.onlyCharAndNumberRegex.matcher(str).find();
    }


    /**
     * <b>功能：</b>验证字符串str是否只包含英文,数字,中文,空格和"-_"
     *
     * @param str 待验证字符串
     * @return true    是只包含英文,数字,中文和"-_" false	不是只包含英文,数字,中文和"-_"
     */
    public static boolean onlyCharAndNumberAndCh(String str) {
        return RegexHelper.onlyCharAndNumberAndChRegex.matcher(str).find();
    }

    /**
     * 只包含字母
     *
     * @param str
     * @return
     */
    public static boolean onlyLetter(String str) {
        return RegexHelper.onlyLetterRegex.matcher(str).find();
    }


    /**
     * <b>功能：</b>验证字符串email是否符合email格式
     *
     * @param email 待验证邮件地址
     * @return true    符合 false	不符合
     */
    public static boolean isEmail(String email) {
        return !isEmpty(email) && RegexHelper.emailRegex.matcher(email).find();
    }

    /**
     * <b>功能：</b>验证字符串url是否符合url格式
     *
     * @param url 待验证URL地址
     * @return true    符合 false	不符合
     */
    public static boolean isUrl(String url) {
        return !isEmpty(url) && RegexHelper.urlRegex.matcher(url).find();
    }


    /**
     * <b>功能：</b>验证字串是否手机号
     *
     * @param mobile 待验证手机号
     * @return true    符合 false	不符合
     */
    public static boolean isMobile(String mobile) {
        return !isEmpty(mobile) && RegexHelper.mobileRegex.matcher(mobile).find();
    }

    /**
     * <b>功能：</b>验证字串图片文件是否属于限定的某中类型格式
     *
     * @param filepath 待验证图片名称
     * @return true    符合 false	不符合
     */
    public static boolean isImageFile(String filepath) {
        return !isEmpty(filepath) && RegexHelper.imagePostfixRegex.matcher(filepath).find();
    }

    public static boolean isNumber(String number) {
        return !isEmpty(number) && RegexHelper.numberRegex.matcher(number).find();
    }

    /**
     * <b>功能：</b>验证字符串tags是否符合 Tags标签 正确格式 (aaa ddd ccc)
     *
     * @param tags
     * @return true|false 符合标签格式 true 否则false
     */
    public static boolean tags(String tags) {
        return !isEmpty(tags) && RegexHelper.tagRegex.matcher(tags).find();
    }


    /**
     * 正则验证
     *
     * @param value
     * @param regex
     * @return
     */
    public static boolean matches(Object value, String regex) {
        return !(isEmpty(value) || isEmpty(regex)) && String.valueOf(value).trim().matches(regex);
    }


    public static boolean isIn(Object value, Object... matches) {
        Optional<Object> optional = Arrays.stream(matches).filter(match -> isEquals(value, match)).findFirst();
        return isEmpty(optional);
    }


    /**
     * 对象比较
     * 1、如果实现Comparable调用compareTo比较
     * 2、o1.equals(o2)返回0
     * 3、比较hashCode值
     * 4、比较toString值
     *
     * @param o1
     * @param o2
     * @param isNullGreater
     * @param <T>
     * @return
     */
    public static <T> int compare(T o1, T o2, boolean isNullGreater) {
        if (isEquals(o1, o2)) {
            return 0;
        }
        if (isNull(o1)) {// null 排在后面
            return isNullGreater ? 1 : -1;
        }
        if (isNull(o2)) {
            return isNullGreater ? -1 : 1;
        }
        if (o1 instanceof Comparable && o2 instanceof Comparable) {
            //如果bean可比较，直接比较bean
            return ((Comparable) o1).compareTo(o2);
        }

        // 比较hashCode值
        int result = Integer.compare(o1.hashCode(), o2.hashCode());
        if (result == 0) {
            // 比较toString值
            result = compare(o1.toString(), o2.toString(), isNullGreater);
        }
        return result;
    }

    /**
     * 比较处理
     * 默认 isNullGreater = true
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T> int compare(T o1, T o2) {
        return compare(o1, o2, true);
    }


    /**
     * o1是否大于o2
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T> boolean isGreater(T o1, T o2) {
        return isGreater(o1, o2, true);
    }

    /**
     * o1是否大于o2
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T> boolean isGreater(T o1, T o2, boolean isNullGreater) {
        return compare(o1, o2, isNullGreater) > 0;
    }


    /**
     * o1是否大于等于o2
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T> boolean isGreaterEquals(T o1, T o2) {
        return isGreaterEquals(o1, o2, true);
    }

    /**
     * o1是否大于等于o2
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T> boolean isGreaterEquals(T o1, T o2, boolean isNullGreater) {
        return compare(o1, o2, isNullGreater) >= 0;
    }

    /**
     * o1小于o2
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T> boolean isLess(T o1, T o2) {
        return isLess(o1, o2, true);
    }

    /**
     * o1小于o2
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T> boolean isLess(T o1, T o2, boolean isNullGreater) {
        return compare(o1, o2, isNullGreater) < 0;
    }

    /**
     * o1小于等于o2
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T> boolean isLessEquals(T o1, T o2) {
        return isLessEquals(o1, o2, true);
    }

    /**
     * o1小于等于o2
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T> boolean isLessEquals(T o1, T o2, boolean isNullGreater) {
        return compare(o1, o2, isNullGreater) <= 0;
    }

    /**
     * 是否在区间内
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static boolean isBetween(Object value, Object min, Object max) {
        return isGreaterEquals(value, min, false) && isLessEquals(value, max, true);
    }

    /**
     * 判断 长度是否在该区间内
     *
     * @param text
     * @param min
     * @param max
     * @return
     */
    public static boolean isLength(String text, int min, int max) {
        text = StringHelper.trim(text);
        return (text.length() <= max && text.length() >= min);
    }

    /**
     * 是否equals
     *
     * @param o1
     * @param o2
     * @return
     */
    public static Boolean isEquals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

    /**
     * 不相等
     *
     * @param o1
     * @param o2
     * @return
     */
    public static Boolean isNotEquals(Object o1, Object o2) {
        return !isEquals(o1, o2);
    }
}
