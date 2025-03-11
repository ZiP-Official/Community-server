package com.zip.community.platform.application.port.in.member;

import com.zip.community.platform.adapter.in.web.dto.request.member.MemberRequest;
import com.zip.community.platform.domain.member.Member;

public interface MemberUseCase {

    Member saveMember(MemberRequest request);       // 회원가입

    Member loginMember(String email);       // 로그인 회원정보 가져오기

    Member chkEmail(String email);         // 회원가입 이메일 체크

    boolean chkNickName(String nickName);   // 닉네임 중복체크(중복한 값이면 false)

    void chgNickName(MemberRequest request);    // 닉네임 변경

    void chgLocation(MemberRequest memberRequest);  // 지역 변경
}
