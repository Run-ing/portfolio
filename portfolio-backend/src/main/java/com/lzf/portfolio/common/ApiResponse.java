package com.lzf.portfolio.common;

/**
 * 前后端统一响应结构。
 *
 * @param code 业务状态码，当前成功固定为 200
 * @param message 响应消息
 * @param data 实际响应数据
 */
public record ApiResponse<T>(int code, String message, T data) {

    /**
     * 构造成功响应，保持与需求文档中的返回格式一致。
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    /**
     * 构造错误响应，错误时不返回业务数据。
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
