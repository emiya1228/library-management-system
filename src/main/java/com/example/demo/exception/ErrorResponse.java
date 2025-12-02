package com.example.demo.exception;

public class ErrorResponse {
    private Boolean success;  // 是否成功：false
    private Integer code;     // 业务错误码
    private String message;   // 错误信息
    private Long timestamp;   // 时间戳
    private String path;      // 请求路径（可选）

    public ErrorResponse(Integer code, String message, String path) {
        this.success = false;
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.path = path;
    }

    // Getters
    public Boolean getSuccess() { return success; }
    public Integer getCode() { return code; }
    public String getMessage() { return message; }
    public Long getTimestamp() { return timestamp; }
    public String getPath() { return path; }
}
