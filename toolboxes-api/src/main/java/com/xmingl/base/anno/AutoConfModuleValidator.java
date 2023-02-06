package com.xmingl.base.anno;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * AutoConfModule的校验器<br/>
 * @version 0.0.1 第一版：只能用于继承BaseService的实现类
 */
public class AutoConfModuleValidator implements ConstraintValidator<AutoConfModule,Object> {

    @Override
    public void initialize(AutoConfModule constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        return true;
    }
}
