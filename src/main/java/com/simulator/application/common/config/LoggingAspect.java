package com.simulator.application.common.config;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * ログ出力の共通処理クラス。
 * @author asou
 *
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * コントローラーメソッド開始時にログを出力する。
     * @param joinPoint JoinPoint
     */
    @Before("execution(public * com.simulator.application.controller.*.*(..))")
    public void controllerStartLog(JoinPoint joinPoint) {
        String string = joinPoint.toString();
        String args = Arrays.toString(joinPoint.getArgs());

        logger.info("処理開始 {}, args: {}", string, args);
    }

    /**
     * コントローラーメソッド終了時にログを出力する。
     * @param joinPoint JoinPoint
     */
    @AfterReturning("execution(public * com.simulator.application.controller.*.*(..))")
    public void controllerEndLog(JoinPoint joinPoint) {
        String string = joinPoint.toString();
        String args = Arrays.toString(joinPoint.getArgs());

        logger.info("処理終了 {}, args: {}", string, args);
    }

    /**
     * 例外終了時にエラーを出力する。
     * @param joinPoint JoinPoint
     * @param e 発生した例外
     */
    @AfterThrowing(value = "execution(public * com.simulator.application.controller.*.*(..))",
                   throwing = "e")
    public void afterException(JoinPoint joinPoint, Exception e) {
        String string = joinPoint.toString();
        String args = Arrays.toString(joinPoint.getArgs());

        logger.error("例外発生, Exception: {}, {}, args = {}", e.getMessage(), string, args);
    }

}
