package com.hsshy.beam.common.annotion;

import java.lang.annotation.*;

/**
 * 自定义注解  限流
 * 创建者	张志朋
 * 创建时间	2015年6月3日
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface ServiceLimit { 
	 String description()  default "";
}
