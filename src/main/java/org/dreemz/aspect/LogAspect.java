package org.dreemz.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.dreemz.config.LoggingProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private final LoggingProps loggingProps;
    private final static Logger log = LoggerFactory.getLogger(LogAspect.class);

    public LogAspect(LoggingProps loggingProps) {
        this.loggingProps = loggingProps;
    }

    @Before(value = "@annotation(org.dreemz.annotation.LogBefore)")
    public void logBefore(JoinPoint joinPoint) {

        if(!loggingProps.isEnabled()) return;

        String msg = "Вызов метода: " + joinPoint.getSignature().getName();
        log(msg);
    }

    @AfterReturning(
            value = "@annotation(org.dreemz.annotation.LogAfterReturning)",
            returning = "result"
    )
    public void logAfter(JoinPoint joinPoint, Object result) {

        if(!loggingProps.isEnabled()) return;

        String msg = "Вызов метода: " + joinPoint.getSignature().getName() + " объект: "+ result.toString();
        log(msg);
    }

    @AfterThrowing(
            pointcut = "@annotation(org.dreemz.annotation.ExceptionHandling)",
            throwing = "exception"
    )
    public void handleException(JoinPoint joinPoint, RuntimeException exception) {

        if(!loggingProps.isEnabled()) return;

        String msg = "Выброшено исключение в методе: " + joinPoint.getSignature().getName() + " класс" +
                joinPoint.getTarget().getClass().getName() + " Получено сообещение:" + exception.getMessage();
        log(msg);
    }

    @Around(value = "@annotation(org.dreemz.annotation.TimeMeasure)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        if(!loggingProps.isEnabled()) return joinPoint.proceed();

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        String msg = "Время выполнения метода " + joinPoint.getSignature().getName() + " " + (end - start) + " ms";
        log(msg);
        return result;
    }

    private void log(String msg) {
        switch (loggingProps.getLevel().toLowerCase()) {
            case "error" -> LogAspect.log.error(msg);
            case "warn" -> LogAspect.log.warn(msg);
            case "debug" -> LogAspect.log.debug(msg);
            default -> LogAspect.log.info(msg);
        }
    }

}
