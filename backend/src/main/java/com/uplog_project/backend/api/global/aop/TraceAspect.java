package com.uplog_project.backend.api.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TraceAspect {

    private static final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0); // 현재 실행되는 메서드의 Depth 추적

    @Pointcut("execution(* com.uplog_project.backend.api..*(..))")
    public void allApplicationMethods() {}

    @Around("allApplicationMethods()")
    public Object traceMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        int currentDepth = depth.get(); // 현재 Depth 가져오기
        depth.set(currentDepth + 1); // Depth 증가

        String indent = "│  ".repeat(currentDepth); // 들여쓰기 생성
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs(); // 메서드 매개변수

        log.info("{}▶ Executing: {} | Args: {}", indent, methodName, args);

        Object result;
        try {
            result = joinPoint.proceed(); // 실제 메서드 실행
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("{}✔ Completed: {} | Execution Time: {}ms | Result: {}", indent, methodName, executionTime, result);
        } catch (Exception e) {
            log.error("{}❌ Exception in: {} | Message: {}", indent, methodName, e.getMessage(), e);
            throw e; // 예외를 다시 던져서 정상적인 흐름 유지
        } finally {
            depth.set(currentDepth); // Depth 복원
        }

        return result;
    }
}
