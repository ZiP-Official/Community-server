package com.zip.community.platform.domain.member;

import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@SuperBuilder
@Getter
public class Member extends BaseDomain {

    private Long id;
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

    public static Member of(String nickName, String email, String loginType, String token, String birth
            , String gender, String status, String location, String createDate, String changeDate, String lastDate) {

        List<MemberRole> roles = new ArrayList<>();

        return Member.builder()
                .nickName(nickName)
                .email(email)
                .loginType(loginType)
                .token(token)
                .birth(birth)
                .gender(gender)
                .status(status)
                .location(location)
                .createDate(createDate)
                .changeDate(changeDate)
                .lastDate(lastDate)
                .build();
    }


}

