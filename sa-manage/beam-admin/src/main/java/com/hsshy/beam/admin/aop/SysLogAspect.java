package com.hsshy.beam.admin.aop;
import com.alibaba.fastjson.JSON;
import com.hsshy.beam.admin.common.annotion.SysLog;
import com.hsshy.beam.admin.common.log.LogManager;
import com.hsshy.beam.admin.common.log.factory.LogTaskFactory;
import com.hsshy.beam.admin.common.shiro.ShiroUser;
import com.hsshy.beam.admin.common.shiro.ShiroUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

/**
 * 系统日志，切面处理类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Aspect
@Component
public class SysLogAspect {

	@Pointcut("@annotation(com.hsshy.beam.admin.common.annotion.SysLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		String bussinessName = "";
		SysLog syslog = method.getAnnotation(SysLog.class);
		if(syslog != null){
			//注解上的描述
			bussinessName = syslog.value();
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();


		//请求的参数
		Object[] args = joinPoint.getArgs();
		String params = "";
		try{
			 params = JSON.toJSONString(args[0]);
		}catch (Exception e){

		}
		//如果当前用户未登录，不做日志
		ShiroUser user = ShiroUtils.getUserEntity();
		if (null == user) {
			return;
		}

		LogManager.me().executeLog(LogTaskFactory.bussinessLog(user.getId(), bussinessName, className, methodName, params));


	}
}
