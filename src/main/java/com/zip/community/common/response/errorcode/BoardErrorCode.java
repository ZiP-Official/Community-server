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
    // 503 서비스 불가능
    REDIS_UNAVAILABLE(503, HttpStatus.SERVICE_UNAVAILABLE, "레디스를 사용할 수 없습니다."),
    // 아파트


    // 게시판
    NOT_FOUND_USER(1001, HttpStatus.NOT_FOUND, "해당 ID의 멤버가 존재하지 않습니다"),
    NOT_FOUND_CATEGORY(1002, HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다"),
    NOT_FOUND_PARENT_CATEGORY(1003, HttpStatus.NOT_FOUND, "해당 카테고리의 부모 카테고리가 존재하지 않습니다"),
    NOT_FOUND_BOARD(1004, HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다"),
    NOT_FOUND_TEMP_BOARD(1005, HttpStatus.NOT_FOUND, "해당 임시 게시글이 존재하지 않습니다"),
    NOT_FOUND_COMMENT(1006, HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다"),
    DUPLICATE_CODE(1009, HttpStatus.CONFLICT, "해당 카테고리 코드가 이미 존재합니다"),
    BAD_REQUEST_REACTION(1010, HttpStatus.BAD_REQUEST, "리액션관련 엔드포인트가 잘못 실행되었습니다."),;

    // 주문 관련


    // 오더 관련


    // 상품 관련


    // 리뷰 관련


    // 유저 관련


    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}