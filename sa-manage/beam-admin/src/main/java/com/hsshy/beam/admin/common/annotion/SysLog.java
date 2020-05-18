
package com.hsshy.beam.admin.common.annotion;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
