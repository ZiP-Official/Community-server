package com.zip.community.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@JsonPropertyOrder({ "code", "message", "exceptionMessage" })
public class ExceptionDto {
    @NotNull
    private final Integer code;

    @NotNull
    private final String message;

    @NotNull
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
