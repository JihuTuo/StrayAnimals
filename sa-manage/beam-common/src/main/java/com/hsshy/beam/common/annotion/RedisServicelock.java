package com.hsshy.beam.common.annotion;
import java.lang.annotation.*;


@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface RedisServicelock {
	 String description()  default "";
}
