package com.aidan.alblogserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局错误处理器
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    // 记录error
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        // 日志记录
        logger.error("Request url:{},Exception:{}",request.getRequestURI(),e);
        // 如果是一个自己定义表示了status的错误，就让sb自己处理
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null)
        {
            throw e;
        }
        // 否则对于其他错误，都返回错误信息体给页面
        ModelAndView mv=new ModelAndView();
        mv.addObject("url",request.getRequestURI());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return mv;
    }

}
