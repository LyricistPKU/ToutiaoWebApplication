package com.lyubinbin.interceptor;

import com.lyubinbin.model.Hostholder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * login intercepter
 * Created by Lyu binbin on 2016/7/25.
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{
    @Autowired
    Hostholder hostholder;

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(hostholder.getUser() == null){
            httpServletResponse.sendRedirect("/?pop=1");
            return false;
        }
        return true;
    }
}
