package com.hsshy.beam.common.annotion;

import java.lang.annotation.*;

/**
 * 忽略用户utoken验证
 * @author rachel.li
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreUTokenAuth {
}
