package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

import static com.sky.constant.MessageConstant.ALREADY_EXISTS;
import static com.sky.constant.MessageConstant.UNKNOWN_ERROR;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */
    //@ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //message => Duplicate entry '小智' for key 'idx_username
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")){
            // 空格切割
            String[] split = message.split(" ");
            // 取出 '小智'
            String username = split[2];
            // '小智'已存在
            String msg = username + ALREADY_EXISTS;
            return Result.error(msg);
        } else {
            // 未知错误
            return Result.error(UNKNOWN_ERROR);
        }
    }

}
