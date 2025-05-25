package com.example.springboot.exception;

import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器，拦截 Controller 层抛出的异常，并返回标准的 JSON 响应。
 */
@ControllerAdvice("com.example.springboot.controller") // 只拦截 com.example.springboot.controller 包下的 Controller
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result handleCustomException(CustomException e) {
        // 使用專業日誌工具記錄（例如 Slf4j）
        // log.error("捕獲自定義異常", e);
        System.err.println("捕獲自定義異常：" + e.getMessage());
        return Result.error(e.getCode(), e.getMsg(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleOtherExceptions(Exception e) {
        // 捕捉所有未預期的異常
        System.err.println("捕獲未知異常：" + e.getMessage());
        return Result.error("500", "系統錯誤", e.getMessage());
    }
}
