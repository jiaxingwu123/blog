package com.wjx.blog1.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice               //将会拦截所有的Controller
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass()); //获取日志对象

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHander(HttpServletRequest request,Exception e) throws Exception {
        logger.error("Result URL:{},Exception:{}",request.getRequestURL(),e);//记录异常信息
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null){    //如果注解是涉及到状态的情况对其进行抛出
            throw e;
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error"); //返回页面
        return mv;
    }
}
