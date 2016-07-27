package com.lyubinbin.configuration;

import com.lyubinbin.interceptor.LoginRequiredInterceptor;
import com.lyubinbin.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * web interceptor configuration
 * Created by Lyu binbin on 2016/7/25.
 */
@Component
public class ToutiaoNewsWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;
    @Autowired
    PassportInterceptor passportInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //put passportHandler into registry
        registry.addInterceptor(passportInterceptor);

        //use this interceptor only when visiting /...
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/msg/*").addPathPatterns("/like").addPathPatterns("/dislike");

        super.addInterceptors(registry);
    }
}
