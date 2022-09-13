package com.wisely.framework.exception;


import com.wisely.framework.exception.eum.ExceptionCodeEnum;

/**
 * 业务异常
 */
public class BusinessException extends BaseException {

    
    protected BusinessException(Throwable rootCause, String message, Object... objects) {
        super(rootCause, ExceptionCodeEnum.BUSINESS.getCode(), message, objects);
    }


    /**
     * 构造器
     *
     * @param message
     * @param objects
     * @return
     */
    public static BusinessException builder(String message, Object... objects) {
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
    public static BusinessException builder(Throwable rootCause, String message, Object... objects) {
        return new BusinessException(rootCause, message, objects);
    }

}
