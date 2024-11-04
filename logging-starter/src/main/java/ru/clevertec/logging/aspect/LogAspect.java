package ru.clevertec.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Аспект логирования
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    /**
     * Логирование входящих параметров метода и результата выполнения.
     * Метод должен быть аннотирован {@link ru.clevertec.logging.annotation.Log}
     */
    @Around(value = "@annotation(ru.clevertec.logging.annotation.Log)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> {}() - {}", methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        log.info("<< {}() - {}", methodName, result);
        return result;
    }
}
