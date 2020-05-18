package com.hsshy.beam.common.aop.aspectj;
import com.hsshy.beam.common.utils.redis.RedissLockUtil;
import com.hsshy.beam.common.utils.ToolUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@Scope
@Aspect
@Order(1)
/*order越小越是最先执行，但更重要的是最先执行的最后结束。order默认值是2147483647*/
public class RedisLockAspect {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	//Service层切点     用于记录错误日志
	@Pointcut("@annotation(com.hsshy.beam.common.annotion.RedisServicelock)")
	public void lockAspect() {
		
	}
	
    @Around("lockAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
		boolean res=false;
    	Object obj = null;
		String key = "";
		try {
			if(ToolUtil.isEmpty(joinPoint.getArgs()[0])){
				//参数为空不上锁
				return joinPoint.proceed();
			}
			//接口方法名
			String method = joinPoint.getSignature().getName();
			//方法名+方法内第一个参数值为key
			key = method+":"+joinPoint.getArgs()[0].toString();
			logger.info("key:{}",key);
			res = RedissLockUtil.tryLock(key, TimeUnit.SECONDS, 3, 20);
			if(res){
				obj = joinPoint.proceed();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("异常："+e);
			logger.error("异常："+e.getMessage());
			logger.error("异常："+e.getLocalizedMessage());
		} finally{
			if(res){//释放锁
				RedissLockUtil.unlock(key);
			}
		}
    	return obj;
    } 
}
