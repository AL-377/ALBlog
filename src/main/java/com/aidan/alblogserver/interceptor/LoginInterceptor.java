package com.aidan.alblogserver.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//定义拦截器
public class LoginInterceptor implements HandlerInterceptor {
    // 预处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("---LoginInterceptor---");
        // 如果user还没登陆，那么就重定向
        if(request.getSession().getAttribute("user")==null){
            response.sendRedirect("/admin");
        }
        return true;
    }
}
