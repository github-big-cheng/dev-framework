package com.wisely.framework.helper;


import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.eum.ExceptionCodeEnum;

/**
 * 返回消息构造器
 */
public class ResponseBuilder {


    private final static String RESPONSE_MODEL_NAME = "responseModel";
    public final static String KEY_CODE = "code";
    public final static String KEY_MESSAGE = "message";
    public final static String KEY_DATA = "data";


    /**
     * 构建返回消息
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static Model build(int code, String message, Object data){
        return Model.builder(RESPONSE_MODEL_NAME)
                .set(KEY_CODE, code).set(KEY_MESSAGE, message).set(KEY_DATA, data);
    }

    /**
     * 构建返回消息
     * @param eum
     * @return
     */
    public static Model build(ExceptionCodeEnum eum){
        return build(eum, null);
    }

    /**
     * 构建返回消息
     * @param eum
     * @param data
     * @return
     */
    public static Model build(ExceptionCodeEnum eum, Object data){
        return Model.builder(RESPONSE_MODEL_NAME)
                .set(KEY_CODE, eum.getCode()).set(KEY_MESSAGE, eum.getMessage()).set(KEY_DATA, data);
    }


    /**
     * 构建成功返回消息
     * @return
     */
    public static Model buildSuccess(){
        return build(ExceptionCodeEnum.SUCCESS);
    }

    /**
     * 构建成功返回消息
     * @param message
     * @param data
     * @return
     */
    public static Model buildSuccess(String message, Object data){
        return build(ExceptionCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 构建成功返回消息
     * @param data
     * @return
     */
    public static Model buildSuccess(Object data){
        return build(ExceptionCodeEnum.SUCCESS, data);
    }



    /**
     * 构建失败返回消息
     * @return
     */
    public static Model buildFailed(){
        return build(ExceptionCodeEnum.FAILED);
    }

    /**
     * 构建失败返回消息
     * @param code
     * @param message
     * @return
     */
    public static Model buildFailed(int code, String message) {
        return build(code, message, null);
    }

    /**
     * 构建失败返回消息
     * @param code
     * @param message
     * @return
     */
    public static Model buildFailed(int code, String message,Object data) {
        return build(code, message, data);
    }

    /**
     * 构建失败返回消息
     * @param message
     * @return
     */
    public static Model buildFailed(String message){
        return build(ExceptionCodeEnum.FAILED.getCode(), message, null);
    }

    /**
     * 构建失败返回消息
     * @param message
     * @param data
     * @return
     */
    public static Model buildFailed(String message, Object data){
        return build(ExceptionCodeEnum.FAILED.getCode(), message, data);
    }

}
