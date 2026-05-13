package com.lzf.portfolio.common;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * 全局异常处理器，把常见异常转换为统一的 API 响应结构。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 查询不到作品时返回 404。
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNotFound(NoSuchElementException exception) {
        log.warn("资源不存在：{}", exception.getMessage());
        return ApiResponse.error(404, exception.getMessage());
    }

    /**
     * 参数校验失败或非法参数统一返回 400。
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBadRequest(Exception exception) {
        log.warn("请求参数错误：{}", exception.getMessage());
        return ApiResponse.error(400, exception.getMessage());
    }

    /**
     * 未预期异常统一返回 500，避免把内部异常细节暴露给前端。
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleServerError(Exception exception) {
        log.error("接口处理失败，返回 500", exception);
        return ApiResponse.error(500, "server error");
    }
}
