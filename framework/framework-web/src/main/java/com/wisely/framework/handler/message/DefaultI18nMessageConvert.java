package com.wisely.framework.handler.message;

import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.StringHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

@Slf4j
public class DefaultI18nMessageConvert implements MessageConvert {

    @Override
    public String messageConvert(String message) {
        try {
            // try to use i18n
            String[] arr = StringHelper.split(message, ".");
            if (arr.length <= 1) {
                // 不做处理
                return message;
            }

            List<String> list = Lists.newArrayList(arr);
            // methodName.message
            String key = list.get(0) + "." + list.get(1);
            Object[] params = null;
            if (list.size() > 2) {
                list = list.subList(2, list.size());
                params = list.toArray();
            }

            message = this.format(key, params);
        } catch (Exception e) {
            // ignore it, it's fine
            log.trace("BaseException.I18n failed:{}", e);
        }
        return message;
    }

    @Resource
    MessageSource messageSource;

    /**
     * 国际化消息转换
     *
     * @param message
     * @return
     */
    protected String format(String message, Object... objects) {
        if (StringHelper.isBlank(message) || messageSource == null) {
            return message;
        }

        Locale locale = RequestContextUtils.getLocale(RequestHelper.getRequest());
        if (locale == null) {
            locale = Locale.getDefault();
        }

        log.debug("locale ==> {}", locale);
        return messageSource.getMessage(message, objects, locale);
    }

}
