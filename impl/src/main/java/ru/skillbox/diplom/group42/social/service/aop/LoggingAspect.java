package ru.skillbox.diplom.group42.social.service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void loggingAllControllers() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void loggingAllService() {
    }

    @Around("loggingAllControllers()")
    public Object aroundAdviceFromLoggingAllControllers(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return getLoggingInfo(proceedingJoinPoint);
    }


    @Around("loggingAllService()")
    public Object aroundAdviceFromLoggingAllServices(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return getLoggingInfo(proceedingJoinPoint);

    }

    private Object getLoggingInfo(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        StringBuilder stringBuilder = new StringBuilder();
        for (Object ar : args) {
            stringBuilder.append(ar.toString()).append(" ");
        }
        String className = proceedingJoinPoint.getSourceLocation().getWithinType().getName();
        log.debug("Start " + className.substring(className.lastIndexOf(".") + 1) + ", Method - " + signature.getName()
                + ", MethodArgs - " + stringBuilder);

        Object proceed = proceedingJoinPoint.proceed();
        String proceedInfo = proceed == null ? "" : proceed.toString();
        log.debug("Finish " + className.substring(className.lastIndexOf(".") + 1) + ", Method - " + signature.getName()
                + ", Proceed - " + proceedInfo);
        return proceed;
    }


}
