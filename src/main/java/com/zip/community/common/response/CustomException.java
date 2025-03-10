package com.zip.community.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{

    private ErrorCode errorCode;
    private String exceptionMessage;

    public CustomException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public String getMessage() {
        return errorCode.getMessage();
    }
}
