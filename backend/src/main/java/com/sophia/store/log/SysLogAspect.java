package com.sophia.store.log;

import com.sophia.store.utils.IPUtils;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
public class SysLogAspect {
    @Resource
    private SystemLogService systemLogService;

    @Pointcut("@annotation(com.sophia.store.log.Log)")
    public void pointcut() {
    }

    @SneakyThrows
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result;
        long beginTime = System.currentTimeMillis();
        result = point.proceed();
        // 执行时长
        long time = System.currentTimeMillis() - beginTime;

        saveLog(point, time);
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SystemLog systemLog = new SystemLog();
        Log annotation = method.getAnnotation(Log.class);
        if (annotation != null) {
            systemLog.setOperation(annotation.value());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        systemLog.setMethod(className + "." + methodName + "()");
        // 请求的方法参数
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = discoverer.getParameterNames(method);
        if (args != null && paramNames != null) {
            String params = "";
            for (int i = 0; i < args.length; i++) {
                params = " " + paramNames[i] + ": " + args[i];
            }
            systemLog.setParams(params);
        }
        // 获取request
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 获取IP地址
        systemLog.setIpAddress(IPUtils.getIpAddr(request));
        // 获取用户名
        // TODO, 目前是ADMIN，后续可以通过token获取名字
        systemLog.setUsername("admin");
        systemLog.setTime(time);
        // 保存系统日志
        systemLog.setCreateDate(new Date());
        systemLogService.add(systemLog);
    }


}
