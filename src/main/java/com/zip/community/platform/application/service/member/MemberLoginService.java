package com.zip.community.platform.application.service.member;

import com.zip.community.platform.adapter.in.web.dto.request.member.MemberRequest;
import com.zip.community.platform.application.port.in.member.MemberUseCase;
import com.zip.community.platform.application.port.out.member.MemberPort;
import com.zip.community.platform.domain.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberLoginService implements MemberUseCase {

    private final MemberPort memberPort;

    @Override
    public Member saveMember(MemberRequest request) {
        return memberPort.saveMember(
                Member.of(
                        request.getNickName(),
                        request.getEmail(),
                        request.getLoginType(),
                        request.getToken(),
                        request.getBirth(),
                        request.getGender(),
                        request.getStatus(),
                        request.getLocation(),
                        request.getCreateDate(),
                        request.getChangeDate(),
                        request.getLastDate()
                        )
        );
    }

    @Override
    public Member loginMember(String email) {
        return memberPort.loginMember(email);
    }

    @Override
    public Member chkEmail(String email) {
        return memberPort.chkEmail(email);
    }

    @Override
    public boolean chkNickName(String nickName) {
        return memberPort.chkNickName(nickName);
    }

    @Override
    public void chgNickName(MemberRequest request) {
        memberPort.chgNickName(request.getId(), request.getNickName());
    }

    @Override
    public void chgLocation(MemberRequest request) {
        memberPort.chgLocation(request.getId(), request.getLocation());
    }
}