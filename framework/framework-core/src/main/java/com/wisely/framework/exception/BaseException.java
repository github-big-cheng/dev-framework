package com.wisely.framework.exception;

import com.wisely.framework.exception.eum.ExceptionCodeEnum;
import com.wisely.framework.helper.ValidHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.text.MessageFormat;


/**
 * 异常基类
 */
@Setter
@Getter
@Slf4j
public class BaseException extends RuntimeException implements Serializable {

    private Boolean isFirst = true;
    private Throwable rootCause;

    /*异常代码*/
    private int code;
    private Object[] objects;

    protected BaseException(Throwable rootCause, int code, String message, Object... objects) {
        super(message, rootCause);
        this.code = code;
        this.rootCause = rootCause;
        if (null != this.rootCause) {
            isFirst = false;
        }
        this.objects = objects;
    }

    // 异常信息过多，导致常量池OOM，暂取消使用
//    @Override
//    public String toString() {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        this.printStackTrace(new PrintStream(baos));
//        return baos.toString();
//    }

    @Override
    public String getMessage() {
        String message = super.getMessage();

        try {
            // try to use MessageFormat
            if (ValidHelper.isNotEmpty(objects)) {
                return MessageFormat.format(message, objects);
            }
        } catch (Exception e) {
            // ignore it, it's fine
            log.debug("BaseException.MessageFormat failed:{}", e);
        }
        return message;
    }


    /**
     * 构造器
     *
     * @param message
     * @param objects
     * @return
     */
    public static BaseException builder(String message, Object... objects) {
        return builder(ExceptionCodeEnum.FAILED.getCode(), message, objects);
    }

    /**
     * 构造器
     *
     * @param code
     * @param message
     * @param objects
     * @return
     */
    public static BaseException builder(int code, String message, Object... objects) {
        return new BaseException(null, code, message, objects);
    }

    /**
     * 构造器
     *
     * @param rootCause
     * @param message
     * @param objects
     * @return
     */
    public static BaseException builder(Throwable rootCause, String message, Object... objects) {
        return builder(rootCause, ExceptionCodeEnum.FAILED.getCode(), message, objects);
    }

    /**
     * 构造器
     *
     * @param rootCause
     * @param code
     * @param message
     * @param objects
     * @return
     */
    public static BaseException builder(Throwable rootCause, int code, String message, Object... objects) {
        return new BaseException(rootCause, code, message, objects);
    }

}
