package com.wisely.framework.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * org.apache.commons.lang3.time.DateUtils
 * 基础上扩展 LocalDateTime
 */
@Slf4j
public class DateHelper extends DateUtils {

    private DateHelper() {
    }

    private final static ConcurrentHashMap<String, DateTimeFormatter> FORMATTER_MAP = new ConcurrentHashMap();


    private static DateTimeFormatter getFormatter(String pattern) {
        if (!FORMATTER_MAP.contains(pattern)) {
            FORMATTER_MAP.putIfAbsent(pattern, DateTimeFormatter.ofPattern(pattern));
        }
        return FORMATTER_MAP.get(pattern);
    }


    /**
     * Date转换为LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        if (ValidHelper.isNull(date)) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param time
     * @return
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        if (ValidHelper.isNull(time)) {
            return null;
        }
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取指定日期的毫秒
     *
     * @param time
     * @return
     */
    public static Long getMilliByTime(LocalDateTime time) {
        if (ValidHelper.isNull(time)) {
            return 0l;
        }
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     *
     * @param time
     * @return
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        if (ValidHelper.isNull(time)) {
            return null;
        }
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取指定时间的指定格式
     *
     * @param time
     * @return
     */
    public static String format(LocalDateTime time) {
        return format(time, PATTERN_DATETIME_01);
    }

    /**
     * 获取指定时间的指定格式
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String format(LocalDateTime time, String pattern) {
        if (ValidHelper.isNull(time) || ValidHelper.isEmpty(pattern)) {
            return null;
        }
        return time.format(getFormatter(pattern));
    }


    /**
     * 获取指定时间的指定格式
     * 默认的时间格式化 yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String format(Date time) {
        return format(time, PATTERN_DATETIME_01);
    }


    /**
     * 获取指定时间的指定格式
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String format(Date time, String pattern) {
        if (ValidHelper.isNull(time) || ValidHelper.isEmpty(pattern)) {
            return null;
        }

        return convertDateToLDT(time).format(getFormatter(pattern));
    }


    /**
     * 日期格式字符串转换为其他格式
     *
     * @param timeStr
     * @param pattern
     * @return
     */
    public static String format(String timeStr, String pattern) {
        return format(getDate(timeStr), pattern);
    }

    /**
     * 获取当前时间的指定格式
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatNow() {
        return formatNow(PATTERN_DATETIME_01);
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param pattern
     * @return
     */
    public static String formatNow(String pattern) {
        if (ValidHelper.isEmpty(pattern)) {
            return null;
        }
        return format(LocalDateTime.now(), pattern);
    }

    /**
     * 日期加上一个数,根据field不同加不同值
     *
     * @param time
     * @param number
     * @param field  ChronoUnit.*
     * @return
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        if (ValidHelper.isNull(time) || ValidHelper.isNull(field)) {
            return null;
        }
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值
     *
     * @param time
     * @param number
     * @param field  ChronoUnit.*
     * @return
     */
    public static LocalDateTime minus(LocalDateTime time, long number, TemporalUnit field) {
        if (ValidHelper.isNull(time) || ValidHelper.isNull(field)) {
            return null;
        }
        return time.minus(number, field);
    }


    /**
     * 获取两个日期的差
     *
     * @param startTime
     * @param endTime
     * @param field     ChronoUnit.* 单位(年月日时分秒)
     * @return
     */
    public static long between(Date startTime, Date endTime, ChronoUnit field) {
        return between(convertDateToLDT(startTime), convertDateToLDT(endTime), field);
    }

    /**
     * 获取两个日期的差
     *
     * @param startTime
     * @param endTime
     * @param field     ChronoUnit.* 单位(年月日时分秒)
     * @return
     */
    public static long between(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {

        if (ValidHelper.isNull(startTime) || ValidHelper.isNull(endTime) || ValidHelper.isNull(field)) {
            return 0l;
        }

        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (field == ChronoUnit.MONTHS) {
            return period.getYears() * 12 + period.getMonths();
        }
        return field.between(startTime, endTime);
    }

    /**
     * 获取一天的开始时间，2020-10-12 00:00:00.000
     *
     * @param time
     * @return
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一天的结束时间，2020-10-12 23:59:59.999
     *
     * @param time
     * @return
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999);
    }


    private final static String[] FORMAT_PATTERNS = new String[]{
            "yyyyMM", // 0
            "yyyy-MM", // 1
            "yyyyMMdd", // 2
            "yyyy-MM-dd", // 3
            "yyyy-MM-dd HH:mm:ss", // 4
            "yyyy-MM-dd HH:mm:ss.SSS", // 5
            "yyyy-MM-dd HH:mm", // 6
            "yyyy-MM-ddTHH:mm:ss", // 7
            "yyyy-MM-ddTHH:mm:ss.SSS", // 8
            "yyyyMMddHHmmss", // 9
            "yyyyMMddHHmmssSSS", // 10
            "yyyy-MM-dd'T'HH:mm:ss", // 11
            "yyyy-MM-dd'T'HH:mm:ss.SSS", // 12
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", // 13
            "yyyy-MM-dd'T'HH:mm:ss.SSS+SS:SS", // 14
    };


    /**
     * yyyy-MM-dd
     */
    public static final String PATTERN_DATE_01 = FORMAT_PATTERNS[3];

    /**
     * yyyy-MM
     */
    public static final String PATTERN_DATE_02 = FORMAT_PATTERNS[1];
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String PATTERN_DATETIME_01 = FORMAT_PATTERNS[4];
    /**
     * yyyyMMddHHmmss
     */
    public static final String PATTERN_DATETIME_02 = FORMAT_PATTERNS[9];
    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String PATTERN_TIMESTAMP_01 = FORMAT_PATTERNS[10];

    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String PATTERN_TIMESTAMP_02 = FORMAT_PATTERNS[6];

    /**
     * 获取日期对象
     *
     * @param value
     * @param patterns
     * @return
     */
    public static Date getDate(Object value, String... patterns) {

        if (ValidHelper.isEmpty(value)) {
            return null;
        }

        if (value instanceof Date) {
            return (Date) value;
        }

        if (value instanceof LocalDate) {
            return convertLDTToDate(((LocalDate) value).atStartOfDay());
        }
        if (value instanceof LocalTime) {
            return convertLDTToDate(((LocalTime) value).atDate(LocalDate.now()));
        }
        if (value instanceof LocalDateTime) {
            return convertLDTToDate((LocalDateTime) value);
        }

        if (value instanceof String && ValidHelper.isNotEmpty(patterns)) {
            try {
                return DateUtils.parseDate((String) value, patterns);
            } catch (ParseException e) {
                log.error("LocalDateTimeHelper.getDate error:{}", e);
            }
        }
        return null;
    }


    /**
     * obj转日期
     *
     * @param value
     * @return
     */
    public static Date getDate(Object value) {
        return getDate(value, FORMAT_PATTERNS);
    }


    /**
     * 获取日期对象
     *
     * @param value
     * @return
     */
    public static LocalDateTime getLocalDateTime(Object value) {
        return getLocalDateTime(value, FORMAT_PATTERNS);
    }

    /**
     * 获取日期对象
     *
     * @param value
     * @return
     */
    public static LocalDateTime getLocalDateTime(Object value, String... pattern) {

        if (ValidHelper.isEmpty(value)) {
            return null;
        }

        if (value instanceof LocalDate) {
            return ((LocalDate) value).atStartOfDay();
        }
        if (value instanceof LocalTime) {
            return ((LocalTime) value).atDate(LocalDate.now());
        }
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }

        if (value instanceof Date) {
            return convertDateToLDT((Date) value);
        }

        Date result = getDate(value, pattern);
        return convertDateToLDT(result);
    }


    /**
     * 验证字符串date是否符合Date格式
     *
     * @param date 待验证日期
     * @return true    符合 false	不符合
     */
    public static boolean isDate(Object date) {
        if (date instanceof Date) {
            return true;
        }
        return getDate(date) != null;
    }


    /**
     * 获取当前年度第一天
     *
     * @return
     */
    public static LocalDateTime yearStart() {
        return yearStart(LocalDateTime.now());
    }

    /**
     * 获取指定时间年度第一天
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime yearStart(LocalDateTime localDateTime) {
        AssertHelper.EX_VALIDATION.isNotEmpty(localDateTime, "common.parameter_required.localDateTime");
        return LocalDateTime.of(LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear())), LocalTime.MIN);
    }


    /**
     * 获取本年度结束时间
     *
     * @return
     */
    public static LocalDateTime yearEnd() {
        return yearEnd(LocalDateTime.now());
    }

    /**
     * 获取指定时间年度结束时间
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime yearEnd(LocalDateTime localDateTime) {
        AssertHelper.EX_VALIDATION.isNotEmpty(localDateTime, "common.parameter_required.localDateTime");
        return LocalDateTime.of(LocalDate.from(localDateTime.with(TemporalAdjusters.lastDayOfYear())), LocalTime.MAX);
    }

    /**
     * 获取指定日期所在季度
     *
     * @param localDateTime
     * @return
     */
    public static Integer quarter(LocalDateTime localDateTime) {
        return localDateTime == null ? null : ((localDateTime.getMonthValue() - 1) / 3 + 3) % 4 + 1;
    }

    /**
     * 获取指定季度开始时间
     *
     * @return
     */
    public static LocalDateTime quarterStart() {
        return quarterStart(LocalDateTime.now());
    }

    /**
     * 获取指定季度开始时间
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime quarterStart(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }

        Month month = localDateTime.getMonth();
        return LocalDateTime.of(LocalDate.of(localDateTime.getYear(), month.firstMonthOfQuarter(), 1), LocalTime.MIN);
    }


    /**
     * 获取指定季度结束时间
     *
     * @return
     */
    public static LocalDateTime quarterEnd() {
        return quarterEnd(LocalDateTime.now());
    }

    /**
     * 获取指定季度结束时间
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime quarterEnd(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }

        Month endMonthOfQuarter = localDateTime.getMonth().firstMonthOfQuarter().plus(2);
        LocalDate localDate = LocalDate.of(localDateTime.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(localDateTime.toLocalDate().isLeapYear()));
        return LocalDateTime.of(localDate, LocalTime.MAX);
    }

    /**
     * 获取一个月第一天
     *
     * @return
     */
    public static LocalDateTime monthStart() {
        return monthStart(LocalDateTime.now());
    }

    /**
     * 获取指定月份第一天
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime monthStart(LocalDateTime localDateTime) {
        return localDateTime == null ? null : LocalDateTime.of(LocalDate.from(localDateTime.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
    }

    /**
     * 获取指定月份最后一天
     *
     * @return
     */
    public static LocalDateTime monthEnd() {
        return monthEnd(LocalDateTime.now());
    }

    /**
     * 获取指定月份最后一天
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime monthEnd(LocalDateTime localDateTime) {
        return localDateTime == null ? null : LocalDateTime.of(LocalDate.from(localDateTime.with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);
    }


    /**
     * 获取指定周开始时间
     *
     * @return
     */
    public static LocalDateTime weekStart() {
        return weekStart(LocalDateTime.now());
    }

    /**
     * 获取指定周开始时间
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime weekStart(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN).with(DayOfWeek.MONDAY);
    }


    /**
     * 获取指定周结束时间
     *
     * @return
     */
    public static LocalDateTime weekEnd() {
        return weekEnd(LocalDateTime.now());
    }

    /**
     * 获取指定周结束时间
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime weekEnd(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }

        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX).with(DayOfWeek.SUNDAY);
    }

    /**
     * 获取当前日期的周几日期
     *
     * @param dayOfWeek DayOfWeek.MONDAY
     *                  DayOfWeek.SUNDAY
     * @return
     */
    public static LocalDateTime weekDay(DayOfWeek dayOfWeek) {
        return weekDay(LocalDateTime.now(), dayOfWeek);
    }

    /**
     * 获取指定日期的周几日期
     *
     * @param localDateTime
     * @param dayOfWeek     DayOfWeek.MONDAY
     *                      DayOfWeek.SUNDAY
     * @return
     */
    public static LocalDateTime weekDay(LocalDateTime localDateTime, DayOfWeek dayOfWeek) {
        return localDateTime == null ? null : localDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeek));
    }


    /**
     * 获取一个天的开始
     *
     * @return
     */
    public static LocalDateTime dayStart() {
        return dayStart(LocalDateTime.now());
    }

    /**
     * 获取一个天的开始
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime dayStart(LocalDateTime localDateTime) {
        return localDateTime == null ? null : LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取一个天的结束
     *
     * @return
     */
    public static LocalDateTime dayEnd() {
        return dayEnd(LocalDateTime.now());
    }

    /**
     * 获取一个天的结束
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime dayEnd(LocalDateTime localDateTime) {
        return localDateTime == null ? null : LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
    }
}

