package com.wisely.framework.plugins.i18n;

import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Setter
@Getter
public class I18nProperties {


    /**
     * 是否启用
     */
    private boolean enabled;
    /**
     * 国际化key
     */
    private String localeKey = "lang";
    /**
     * 本地化
     */
    private String locale = "zh_CN";
    /**
     * 编码
     */
    private String encoding = "UTF-8";
    /**
     * message路径，多个','分割
     */
    private String baseNames = "classpath*:i18n/messages";


    public Locale getI18nLocale() {
        if (ValidHelper.isBlank(this.locale)) {
            return Locale.getDefault();
        }

        if (StringHelper.indexOf(this.locale, "_") > -1) {
            String[] arr = StringHelper.split(this.locale, "_");
            return new Locale(arr[0], arr[1]);
        }
        return new Locale(this.locale);
    }

}
