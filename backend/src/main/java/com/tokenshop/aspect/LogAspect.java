package com.tokenshop.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志切面
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* com.tokenshop.controller..*(..))")
    public Object logControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        Map<String, Object> logInfo = new HashMap<>();
        if (request != null) {
            logInfo.put("method", request.getMethod());
            logInfo.put("uri", request.getRequestURI());
            logInfo.put("ip", request.getRemoteAddr());
        }
        logInfo.put("class", joinPoint.getSignature().getDeclaringTypeName());
        logInfo.put("method_name", joinPoint.getSignature().getName());

        // 记录请求参数
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            try {
                logInfo.put("params", objectMapper.writeValueAsString(args));
            } catch (Exception e) {
                logInfo.put("params", "无法序列化参数");
            }
        }

        log.info("请求开始: {}", logInfo);

        // 执行目标方法
        Object result;
        try {
            result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("请求成功 | 耗时: {}ms | 类: {} | 方法: {}", duration,
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("请求失败 | 耗时: {}ms | 类: {} | 方法: {} | 错误: {}",
                    duration, joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getMessage(), e);
            throw e;
        }

        return result;
    }
}
