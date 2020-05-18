package com.hsshy.beam.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleMessage {

    @Autowired
    private MessageSource messageSource;

    /**
     * @param code：对应文本配置的key.
     * @return 对应地区的语言消息字符串
     */
    public String getMessage(String code){
        return this.getMessage(code,new Object[]{});
    }

    public String getMessage(String code,String defaultMessage){
        return this.getMessage(code,null,defaultMessage);
    }

    public String getMessage(String code,String defaultMessage,Locale locale){
        return this.getMessage(code,null,defaultMessage,locale);
    }

    public String getMessage(String code,Locale locale){
        return this.getMessage(code,null,"",locale);
    }

    public String getMessage(String code,Object[] args){
        return this.getMessage(code,args,"");
    }

    public String getMessage(String code,Object[] args,Locale locale){
        return this.getMessage(code,args,"",locale);
    }

    public String getMessage(String code,Object[] args,String defaultMessage){
        Locale locale = LocaleContextHolder.getLocale();
        return this.getMessage(code,args, defaultMessage,locale);
    }

    public String getMessage(String code,Object[]args,String defaultMessage,Locale locale){
        return messageSource.getMessage(code,args, defaultMessage,locale);
    }

}

