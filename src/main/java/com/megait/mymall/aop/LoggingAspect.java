package com.megait.mymall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @Around("within(com.megait.mymall.service..*)")
    public Object loggin(ProceedingJoinPoint pjp) throws Throwable {
        String params = getRequestParams();

        long start = System.currentTimeMillis();
        Signature requestSignature = pjp.getSignature();
        log.info("request : {} ({}) = {}", requestSignature.getDeclaringTypeName(), requestSignature.getName(), params);

        Object result = pjp.proceed();

        long end = System.currentTimeMillis();

        Signature responseSignature = pjp.getSignature();
        log.info("response : {} ({}) = {} ({}ms)",
                responseSignature.getDeclaringTypeName(), requestSignature.getName(), result, end - start);

        return result;

    }

    private String getRequestParams() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if(attributes == null){
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, String[]> parameterMap = request.getParameterMap();

        final StringBuilder stringBuilder = new StringBuilder();

        parameterMap.forEach((k, v) -> {
            stringBuilder.append("파라미터 : ").append(k).append(", 값 : ").append(Arrays.toString(v)).append("\n");
        });

        return stringBuilder.toString();

    }

}