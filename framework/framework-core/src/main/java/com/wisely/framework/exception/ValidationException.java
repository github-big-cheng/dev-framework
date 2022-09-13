package com.wisely.framework.exception;


import com.wisely.framework.exception.eum.ExceptionCodeEnum;

/**
 * 参数验证类异常
 */
public class ValidationException extends BaseException {

    protected ValidationException(Throwable rootCause, String message, Object... objects) {
        super(rootCause, ExceptionCodeEnum.VALIDATION.getCode(), message, objects);
    }


    /**
     * 构造器
     *
     * @param message
     * @param objects
     * @return
     */
    public static ValidationException builder(String message, Object... objects) {
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
    public static ValidationException builder(Throwable rootCause, String message, Object... objects) {
        return new ValidationException(rootCause, message, objects);
    }

}
