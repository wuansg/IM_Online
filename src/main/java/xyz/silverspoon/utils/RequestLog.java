package xyz.silverspoon.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@Aspect
@Component
public class RequestLog {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(public * xyz.silverspoon.controller..*(..))")
    public void requestLog(){}

    @Before("requestLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        logger.info("URL: " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD: " + request.getMethod());
        logger.info("IP: " + request.getRemoteAddr());
        logger.info("CLASS_METHOD: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "requestLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("RESPONSE: " + ret);
    }

    @AfterThrowing(throwing = "ex", pointcut = "requestLog()")
    public void doAfterThrowing(Exception ex) {
        logger.error(ex.getMessage());
    }
}
