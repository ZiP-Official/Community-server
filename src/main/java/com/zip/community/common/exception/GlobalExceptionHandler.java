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
//@RestControllerAdvice
public class GlobalExceptionHandler {

    // 이것은 어떤 예외인지는 모르겠다.
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<?> handleNoPageFoundException(Exception e) {
        log.error("GlobalExceptionHandler catch : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT));
    }

    // 이것은 어떤 예외인지는 모르겠다.
    @ExceptionHandler(value = TypeMismatchException.class)
    public ApiResponse<?> handleMismatchException(Exception e) {
        log.error("GlobalExceptionHandler catch : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST));
    }

    // NullPointException 일 때, 예외처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<?> handleNullPointException(NullPointerException e) {
        log.error("GlobalExceptionHandler catch : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST));
    }

    // NoSuchElementException, EntityNotFoundException 일 때, 예외처리
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NoSuchElementException.class, EntityNotFoundException.class})
    public ApiResponse<?> handleNoSuchElementException(Exception e) {
        log.error("GlobalExceptionHandler catch : {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT));
    }
}
