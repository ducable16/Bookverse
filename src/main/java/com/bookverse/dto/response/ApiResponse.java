package com.bookverse.dto.response;

import com.bookverse.utils.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int code = ResponseCode.SUCCESS;
    private T data;

    public ApiResponse() {
        this.code = ResponseCode.SUCCESS;
    }

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(int code) {
        this.code = code;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> defaultResponse() {
        return new ApiResponse<>();
    }

    public static <T> ApiResponse<T> error(int code) {
        return new ApiResponse<>(code);
    }
}
