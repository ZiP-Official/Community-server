package com.zip.community.common.response;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    Integer getCode();
    HttpStatus getHttpStatus();
    String getMessage();
}
