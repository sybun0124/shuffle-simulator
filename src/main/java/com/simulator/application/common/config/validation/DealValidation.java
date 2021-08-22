package com.simulator.application.common.config.validation;

import com.simulator.application.common.config.annotation.Deal;
import com.simulator.application.common.enums.ShuffleType;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

public class DealValidation implements ConstraintValidator<Deal, Object> {

    private String[] fields;

    private String message;


    public void initialize(Deal annotation) {
        this.fields = annotation.fields();
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        //fields[0]=シャッフル種別、fields[1]=枚数
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        //ディールシャッフル以外は対象外
        Object shuffleType = beanWrapper.getPropertyValue(fields[0]);
        if (shuffleType != ShuffleType.DEAL) {
            return true;
        }

        //枚数文字列
        String number = beanWrapper.getPropertyValue(fields[1]).toString();
        //0～9の半角数字チェック

        if (ObjectUtils.isEmpty(number) || !number.chars().allMatch(Character::isDigit)) {
            //falseを返し、messageを出す
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(fields[0])
                .addConstraintViolation();
            return false;
        }
        return true;
    }

}
