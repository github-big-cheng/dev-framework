package com.wisely.framework.exception;


import com.wisely.framework.exception.eum.ExceptionCodeEnum;

/**
 * 系统异常
 */
public class SystemException extends BaseException {

    protected SystemException(Throwable rootCause, String message, Object... objects) {
        super(rootCause, ExceptionCodeEnum.SYSTEM.getCode(), message, objects);
    }


    /**
     * 构造器
     *
     * @param message
     * @param objects
     * @return
     */
    public static SystemException builder(String message, Object... objects) {
        return builder(null, message, objects);
    }

    /**
     * 构造器
     *
     * @param rootCause
     * @param message
     * @param objects
     * @return
     */
    public static SystemException builder(Throwable rootCause, String message, Object... objects) {
        return new SystemException(rootCause, message, objects);
    }

}
