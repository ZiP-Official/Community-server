package com.zip.community.common.response.errorcode;

import com.zip.community.common.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements ErrorCode {

    NOT_FOUND_CHAT_ROOM(3001, HttpStatus.NOT_FOUND, "채팅방이 존재하지 않습니다."),
    NOT_FOUND_CHAT_MESSAGE(3002, HttpStatus.NOT_FOUND, "채팅 메세지가 존재하지 않습니다."),
    CHAT_ROOM_CREATION_FAILED(3003, HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 생성에 실패했습니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
