package com.ehang.validate.controller.custom.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author ehang
 * @title: UpperOrLowerCaseAnno
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/4 14:10
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
//指定大小写校验器
@Constraint(validatedBy = UpperOrLowerCaseValidator.class)
public @interface UpperOrLowerCaseAnno {
    // 默认值 大写
    UpperOrLowerCase value() default UpperOrLowerCase.UPPER;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
