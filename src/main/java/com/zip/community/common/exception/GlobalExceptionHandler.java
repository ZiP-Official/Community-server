package com.zip.community.common.exception;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 페이지를 찾을 수 없거나 지원되지 않는 HTTP 메서드일 때 예외 처리
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<?> handlePageNotFoundOrMethodNotSupportedException(Exception e) {
        log.error("에러 로그 : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT, e.getMessage()));
    }

    // 타입 불일치 예외 처리
    @ExceptionHandler(value = TypeMismatchException.class)
    public ApiResponse<?> handleTypeMismatchException(Exception e) {
        log.error("에러 로그 : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST, e.getMessage()));
    }

    // NullPointerException 또는 IllegalArgumentException 발생 시 예외 처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {NullPointerException.class, IllegalArgumentException.class})
    public ApiResponse<?> handleNullPointerAndIllegalArgumentException(Exception e) {
        log.error("에러 로그 : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST, e.getMessage()));
    }

    // NoSuchElementException 또는 EntityNotFoundException 발생 시 예외 처리
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NoSuchElementException.class, EntityNotFoundException.class})
    public ApiResponse<?> handleEntityNotFoundException(Exception e) {
        log.error("에러 로그 : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT, e.getMessage()));
    }
}