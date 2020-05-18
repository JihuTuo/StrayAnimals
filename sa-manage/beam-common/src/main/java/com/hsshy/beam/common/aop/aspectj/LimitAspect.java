package com.hsshy.beam.common.aop.aspectj;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * 限流 AOP
 * 创建者	张志朋
 * 创建时间	2015年6月3日
 */
@Component
@Scope
@Aspect
public class LimitAspect {
	//每秒只发出5个令牌，此处是单进程服务的限流,内部采用令牌捅算法实现
	private static RateLimiter rateLimiter = RateLimiter.create(5.0);
	
	//Service层切点  限流
	@Pointcut("@annotation(com.hsshy.beam.common.annotion.ServiceLimit)")
	public void ServiceAspect() {
		
	}
	
    @Around("ServiceAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) { 
    	Boolean flag = rateLimiter.tryAcquire();
    	Object obj = null;
		try {
			if(flag){
				obj = joinPoint.proceed();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} 
    	return obj;
    } 
}
