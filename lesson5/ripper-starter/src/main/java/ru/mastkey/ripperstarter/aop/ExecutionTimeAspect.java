package ru.mastkey.ripperstarter.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
@Slf4j
public class ExecutionTimeAspect {

    @Around("@within(ru.mastkey.ripperstarter.annotation.ExecutionTime) || @annotation(ru.mastkey.ripperstarter.annotation.ExecutionTime)")
    public Object ExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();

        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            log.info("Executed method: {}.{}() in {} ms",
                    declaringClass.getSimpleName(),
                    method.getName(),
                    elapsedTime);
        }

        return result;
    }
}
