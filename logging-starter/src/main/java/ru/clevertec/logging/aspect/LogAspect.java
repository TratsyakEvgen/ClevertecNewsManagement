package ru.clevertec.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Around(value = "logMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> {}() - {}", methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        log.info("<< {}() - {}", methodName, result);
        return result;
    }
    @Pointcut("@annotation(ru.clevertec.logging.annotation.Log)")
    private void logMethods() {
    }
}
