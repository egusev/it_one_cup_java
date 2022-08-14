package ru.vk.competition.minbenchmark.utils;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class AspectLog {

    @Before("execution(* *(..)) && @annotation(ru.vk.competition.minbenchmark.utils.Loggable)")
    public void auditStart() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestUrl = request.getMethod() + " " + request.getContextPath() + request.getRequestURI();
        if (request.getQueryString() != null) {
            requestUrl = requestUrl + "?" + request.getQueryString();
        }

        log.info("{}", requestUrl);
    }

    @AfterReturning(pointcut = "execution(* *(..)) && @annotation(ru.vk.competition.minbenchmark.utils.Loggable)",
                    returning = "result")
    public void auditInfo(JoinPoint joinPoint, Object result) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String requestUrl = request.getMethod() + " " + request.getContextPath() + request.getRequestURI();
        if (request.getQueryString() != null) {
            requestUrl = requestUrl + "?" + request.getQueryString();
        }

        int httpResponseStatus = 0;
        if (result instanceof ResponseEntity) {
            ResponseEntity responseObj = (ResponseEntity) result;
            httpResponseStatus = responseObj.getStatusCodeValue();
        }
        log.info("{} -> {}", requestUrl, httpResponseStatus);
    }
}