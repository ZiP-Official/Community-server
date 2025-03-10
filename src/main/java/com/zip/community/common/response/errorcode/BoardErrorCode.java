package com.zip.community.common.response.errorcode;

import com.zip.community.common.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardErrorCode implements ErrorCode {

    // Test Error
    TEST_ERROR(100, HttpStatus.BAD_REQUEST, "테스트 에러입니다."),
    // 400 Bad Request
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // 403 Bad Reques
    Forbidden(403, HttpStatus.FORBIDDEN, "접속 권한이 없습니다."),
    // 404 Not Found
    NOT_FOUND_END_POINT(404, HttpStatus.NOT_FOUND, "요청한 대상이 존재하지 않습니다."),
    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // 아파트


    // 게시판

    DUPLICATE_ERROR(409, HttpStatus.CONFLICT, "중복된 값이 이미 존재합니다");

    // 주문 관련


    // 오더 관련


    // 상품 관련


    // 리뷰 관련


    // 유저 관련


    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}