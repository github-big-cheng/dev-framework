package com.wisely.framework.exception;


import com.wisely.framework.exception.eum.ExceptionCodeEnum;

/**
 * 第三方异常
 */
public class ThirdPartyException extends BaseException {

    protected ThirdPartyException(Throwable rootCause, String message, Object... objects) {
        super(rootCause, ExceptionCodeEnum.THIRD_PARTY.getCode(), message, objects);
    }


    /**
     * 构造器
     *
     * @param message
     * @param objects
     * @return
     */
    public static ThirdPartyException builder(String message, Object... objects) {
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
    public static ThirdPartyException builder(Throwable rootCause, String message, Object... objects) {
        return new ThirdPartyException(rootCause, message, objects);
    }

}
