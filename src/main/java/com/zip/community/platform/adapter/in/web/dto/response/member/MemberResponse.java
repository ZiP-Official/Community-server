package com.zip.community.platform.adapter.in.web.dto.response.member;

import com.zip.community.platform.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponse {

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

    // 생성자
    public static MemberResponse from(Member member) {

        return MemberResponse.builder()
                .id(member.getId())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .loginType(member.getLoginType())
                .token(member.getToken())
                .birth(member.getBirth())
                .gender(member.getGender())
                .status(member.getStatus())
                .location(member.getLocation())
                .createDate(member.getCreateDate())
                .changeDate(member.getChangeDate())
                .lastDate(member.getLastDate())
                .build();
    }

    // 리스트 변환
    public static List<MemberResponse> from(List<Member> member) {
        return member.stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }


}
