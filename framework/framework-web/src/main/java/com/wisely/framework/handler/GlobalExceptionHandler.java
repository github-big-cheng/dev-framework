package com.wisely.framework.handler;

import com.wisely.framework.exception.BaseException;
import com.wisely.framework.handler.message.MessageConvert;
import com.wisely.framework.helper.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理类
 */
@Slf4j
public class GlobalExceptionHandler extends AbstractHandlerExceptionResolver {

    MessageConvert messageConvert;

    @Autowired(required = false)
    public void setMessageConverter(MessageConvert messageConvert) {
        this.messageConvert = messageConvert;
    }


    /**
     * 返回消息转换
     *
     * @param message
     * @return
     */
    private String messageConvert(String message) {
        if (this.messageConvert == null) {
            return message;
        }

        return messageConvert.messageConvert(message);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.error("{}", ex);

        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        if (ex instanceof BaseException) {
            BaseException baseException = (BaseException) ex;
            return modelAndView.addAllObjects(
                    ResponseBuilder.buildFailed(
                            baseException.getCode(), messageConvert(baseException.getMessage())));
        }
        return modelAndView.addAllObjects(ResponseBuilder.buildFailed());
    }
}
