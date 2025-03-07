package com.zip.community.common.handler;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.ErrorCode;
import com.zip.community.common.response.ExceptionDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리 핸들러
    @ExceptionHandler(CustomException.class)
    public ApiResponse<ExceptionDto> handleCustomException(CustomException ce, HttpServletRequest request) {
        log.error("[CustomException] code: {}, httpStatus: {}, message: {}, path: {}",
                ce.getErrorCode().getCode(),
                ce.getErrorCode().getHttpStatus(),
                ce.getMessage(),
                request.getRequestURI(),
                ce);
        return ApiResponse.fail(ce);
    }

    // CustomException 이외의 모든 Exception 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ApiResponse<ExceptionDto> handleException(Exception e, HttpServletRequest request) {
        CustomException ce = new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        log.error("[CustomException] code: {}, httpStatus: {}, message: {}, path: {}",
                ce.getErrorCode().getCode(),
                ce.getErrorCode().getHttpStatus(),
                ce.getMessage(),
                request.getRequestURI(),
                e);
        return ApiResponse.fail(ce);
    }
}
