package com.wmsj.annotation;

import java.lang.annotation.*;

/**
 * Annotation for Domain Class field Title
 * generate gsp use it
 * author :xiaopeng date:2009-06-01
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Title {
	/**
	 *
	 * @return  the title
	 */
    String value() default "";
    String en() default "";
    String zh_CN() default "";
    String da() default "";
    String de() default "";
    String es() default "";
    String fr() default "";
    String it() default "";
    String ja() default "";
    String nl() default "";
    String pt_BR() default "";
    String pt_PT() default "";
    String ru() default "";
    String th() default "";
}
