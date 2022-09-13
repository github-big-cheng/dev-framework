package com.wisely.framework.exception.eum;

import com.wisely.framework.entity.Model;

import java.util.stream.Stream;

public enum ExceptionCodeEnum {

    SUCCESS(0, "操作成功"),
    FAILED(50, "操作失败"),
    THIRD_PARTY(10, "中间服务异常"),
    VALIDATION(20, "参数验证失败"),
    BUSINESS(30, "业务逻辑异常"),
    SYSTEM(40, "系统异常"),

    USER_NOT_LOGIN_IN(2001, "用户未登录，请登录"),
    ;

    ExceptionCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
    private static Model<Integer, String> values = Model.builder();
    static {
        Stream.of(ExceptionCodeEnum.values()).forEach(x -> values.set(x.code, x.message));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static Model getModel(){
        return values;
    }

    public static String findMessage(int code){
        return values.get(code);
    }
}
