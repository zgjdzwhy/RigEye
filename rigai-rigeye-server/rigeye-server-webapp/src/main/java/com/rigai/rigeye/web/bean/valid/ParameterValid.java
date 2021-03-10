package com.rigai.rigeye.web.bean.valid;

import java.lang.annotation.*;

/**
 * @author yh
 * @date 2018/8/8 16:12
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterValid {
    Class<?> type();

    String msg();

    boolean request() default true;

    boolean isEmpty() default true;

    boolean isBlank() default true;

    boolean isNull() default false;

    int min() default 0;
    int max() default 0;
    int[] section() default {0,0};
    boolean isMin() default false;
    boolean isMax() default false;
    boolean isSection() default false;
}

