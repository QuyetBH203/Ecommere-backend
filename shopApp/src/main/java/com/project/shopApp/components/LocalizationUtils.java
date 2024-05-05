package com.project.shopApp.components;


import com.project.shopApp.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public String getLocalizedMessage(String messageKey,Object... params){
        // xác định thông tin về request
        HttpServletRequest request= WebUtils.getCurrentRequest();
        //xác định ngôn ngữ của request hiện tại
        Locale locale=localeResolver.resolveLocale(request);
        return  messageSource.getMessage(messageKey,params,locale);
    }
}
