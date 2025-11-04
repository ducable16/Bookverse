package com.bookverse.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),

    // --- 9xxx: Lỗi hệ thống & Mặc định ---
    URL_NOT_FOUND(9998, "Đường dẫn không tồn tại", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Sai key của Error Code", HttpStatus.BAD_REQUEST),


    // --- 1xxx: Lỗi liên quan đến User & Auth ---
    USER_EXISTED(1002, "Người dùng đã tồn tại", HttpStatus.CONFLICT),
    USERNAME_INVALID(1003, "Tên đăng nhập phải có ít nhất 3 ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Mật khẩu phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1005, "Sai mật khẩu", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Vui lòng đăng nhập", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "Bạn không có quyền truy cập tài nguyên này", HttpStatus.FORBIDDEN),
    INVALID_OTP(1009, "Mã OTP không hợp lệ hoặc đã hết hạn", HttpStatus.BAD_REQUEST),
    INVALID_INPUT(1010, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT(1011, "Dữ liệu gửi lên không đúng định dạng JSON", HttpStatus.BAD_REQUEST),


    // --- 2xxx: Lỗi liên quan đến Book/Domain ---
    BOOK_NOT_FOUND(2001, "Sách không tồn tại", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(2002, "Thể loại không tồn tại", HttpStatus.NOT_FOUND),
    CHAPTER_NOT_FOUND(2003, "Không tìm thấy chương truyện", HttpStatus.NOT_FOUND),
    DUPLICATE_BOOK(2004, "Sách đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode httpStatus;

    ErrorCode(int code, String message, HttpStatusCode httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}