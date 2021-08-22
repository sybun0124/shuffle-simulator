package com.simulator.application.common.config.annotation;

import com.simulator.application.common.config.validation.DealValidation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;



@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Constraint(validatedBy = DealValidation.class)
public @interface Deal {
    /**
     * バリデーションエラーメッセージ。
     * @return
     */
    String message() default "ディールシャッフルは枚数を指定してください。";

    /**
     * バリデーション対象フィールド。
     * @return
     */
    String[] fields();

    /**
     * バリデーション必須メソッド。
     * @return
     */
    Class<?>[] groups() default {};


    /**
     * バリデーション必須メソッド。
     * @return
     */
    Class<? extends Payload>[] payload() default {};


    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        /**
         * バリデーション必須メソッド。
         * @return
         */
        Deal[] value();
    }
}
