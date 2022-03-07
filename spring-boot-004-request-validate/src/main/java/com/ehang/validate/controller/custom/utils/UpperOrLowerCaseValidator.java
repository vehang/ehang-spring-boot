package com.ehang.validate.controller.custom.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author ehang
 * @title: UpperOrLowerCaseValidator
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/4 14:13
 */
public class UpperOrLowerCaseValidator implements ConstraintValidator<UpperOrLowerCaseAnno, String> {
    private UpperOrLowerCase upperOrLowerCase;

    @Override
    public void initialize(UpperOrLowerCaseAnno upperOrLowerCaseAnno) {
        this.upperOrLowerCase = upperOrLowerCaseAnno.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // 判断是否是空值  这里可以根据个人的实际情况来定
        // 可以返回true 也可以返回false
        if (null == value) {
            return true;
        }

        // 没指定任何比对模式，就直接返回false
        if (null == upperOrLowerCase) {
            return false;
        }

        // 校验是否是字母 出现非字母的 直接返回false
        {
            //文本只能是字母的正则
            String pattern = "^[a-zA-Z]*$";
            //校验传进来的是否是只包含了字母的文本
            boolean isMatch = Pattern.matches(pattern, value);
            //如果存在其他字符则返回校验失败
            if (!isMatch) {
                return false;
            }
        }

        //判断是否符合大小写条件
        if (upperOrLowerCase == UpperOrLowerCase.UPPER) {
            return value.equals(value.toUpperCase());
        } else {
            return value.equals(value.toLowerCase());
        }
    }
}
