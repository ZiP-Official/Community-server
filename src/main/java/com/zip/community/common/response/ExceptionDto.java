package com.zip.community.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@RequiredArgsConstructor
public class ExceptionDto {
    @NotNull
    private final Integer code;

    @NotNull
    private final String message;

    private final String exceptionMessage;

    public ExceptionDto(ErrorCode errorCode, String exceptionMessage) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.exceptionMessage = exceptionMessage;
    }

    public static ExceptionDto of(ErrorCode errorCode, String exceptionMessage) {
        return new ExceptionDto(errorCode, exceptionMessage);
    }
}
