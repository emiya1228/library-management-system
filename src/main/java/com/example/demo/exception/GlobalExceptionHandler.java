package com.example.demo.exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理你的业务异常
     * @ResponseStatus 可以设置返回的HTTP状态码，这里用200表示业务逻辑错误
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.OK)  // 返回200，错误信息在body中
    public ErrorResponse handleServiceException(ServiceException ex, WebRequest request) {
        return new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
    }

    /**
     * 处理其他未捕获的异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 返回500
    public ErrorResponse handleGlobalException(Exception ex, WebRequest request) {
        // 生产环境可以只返回通用错误，不暴露异常详情
        return new ErrorResponse(
                500,
                "系统内部错误: " + ex.getMessage(),  // 开发阶段可显示详情
                request.getDescription(false).replace("uri=", "")
        );
    }

}
