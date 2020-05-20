package com.wjx.blog1.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect       //进行切面操作
@Component        //注解扫描
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.wjx.blog1.web.*.*(..))")    //第一个*代表web目录下的任何方法  第二个*代表任何类 *(..)代表任何参数的所有方法   定义切点表达式
    public void log(){};


    @Before("log()")   //引入切点，代表这个方法会在log之前执行
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        //获取类名方法名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();  //类名.方法名
        //获取方法参数
        Object[] args = joinPoint.getArgs();
        //构造对象
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);
        //打印请求信息
        logger.info("Request:{}",requestLog);
//        logger.info("----------dobefore--------");
    }

    @After("log()")
    public void doAfter(){
        logger.info("--------doAfter-------");
    }

    @AfterReturning(returning = "result",pointcut = "log()")//记录返回的内容，在after之后执行
    public void doAfterReturn(Object result){
        //打回返回值
        logger.info("Result:{}",result);
    }
    private class RequestLog{     //用于封住请求信息
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {//用于封装对象
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
