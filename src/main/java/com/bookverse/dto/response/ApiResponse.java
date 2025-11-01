package com.bookverse.dto.response;

import com.bookverse.enums.ErrorCode;
import com.bookverse.utils.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int code = ResponseCode.SUCCESS;
    private String message;
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

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
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

    public static <T> ApiResponse<T> error(ErrorCode code, String message) {
        return new ApiResponse<>(code.getHttpStatus(), message);
    }
}
