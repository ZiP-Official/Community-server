package com.zip.community.platform.adapter.in.web.dto.request.member;

import lombok.Data;

@Data
public class MemberRequest {

    private Long id;            // 회원 번호
    private String nickName;    // 닉네임
    private String email;       // 이메일 (ID)
    private String loginType;   // 로그인 타입 (kakao, naver)
    private String token;       // 소셜 로그인 토큰
    private String birth;       // 생년월일(19940130)
    private String gender;      // 성별(M/W)
    private String status;      // 회원 상태값(1:정상, 2:장기미사용, 9:탈퇴, 0:정지)
    private String location;    // 지역 행정도코드
    private String createDate;  // 회원가입일
    private String changeDate;  // 지역 변경일
    private String lastDate;    // 마지막 접속일

}