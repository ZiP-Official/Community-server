package com.zip.community.platform.application.port.out.member;

import com.zip.community.platform.domain.member.Member;

public interface MemberPort {

    Member saveMember(Member member);       // 회원가입

    Member loginMember(String email);       // 로그인 회원정보 가져오기

    Member chkEmail(String email);         // 회원가입 이메일 체크

    boolean chkNickName(String nickName);   // 닉네임 중복체크

    void chgNickName(Long id, String nickName);    // 닉네임 변경

    void chgLocation(Long id, String location);     // 지역 변경


    /// 게시글 관련 기능에서 유저를 확인하기 위해 사용하는 로직입니다.
    boolean getCheckedExistUser(Long memberId);

}
