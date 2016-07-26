package com.lyubinbin.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Created by Lyu binbin on 2016/7/25.
 */
@Aspect
@Component
public class LogAspect {
    private final Logger logger = Logger.getLogger(LogAspect.class);

    @Before("execution(* com.lyubinbin.controller.*Controller.*(..))")
    public void beforMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object arg : joinPoint.getArgs()){
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("Before Method: " + joinPoint.toString() + ";" + sb.toString());
    }

    @After("execution(* com.lyubinbin.controller.*Controller.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object arg : joinPoint.getArgs()){
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("After Method: " + joinPoint.toString() + ";" + sb.toString());
    }
}
