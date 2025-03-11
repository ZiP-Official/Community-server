package com.zip.community.platform.adapter.out.member;

import com.zip.community.platform.adapter.out.jpa.member.MemberJpaEntity;
import com.zip.community.platform.adapter.out.jpa.member.repository.MemberRepository;
import com.zip.community.platform.application.port.out.member.MemberPort;
import com.zip.community.platform.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberLoginAdapter implements MemberPort {

    private final MemberRepository repository;

    @Override
    public Member saveMember(Member member) {
        return MemberJpaEntity.toDomain(repository.save(MemberJpaEntity.from(member)));
    }

    @Override
    public Member loginMember(String email) {
        Optional<Member> member = repository.getMemberByEmail(email).map(MemberJpaEntity::toDomain);
        if(member.isPresent()){
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
            String nowStr = now.format(format); // 마지막 접속일
            repository.updateLastDate(member.get().getId(), member.get().getLastDate());
            return member.get();
        }else{
            return null;
        }
    }

    @Override
    public Member chkEmail(String email) {
        Optional<Member> member = repository.getMemberByEmail(email).map(MemberJpaEntity::toDomain);
        if(member.isPresent()){
            return member.get();
        }else{
            return null;
        }
    }

    @Override
    public boolean chkNickName(String nickName) {
        Optional<Member> member = repository.chkNickName(nickName).map(MemberJpaEntity::toDomain);
        if(member.isPresent()){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void chgNickName(Long id, String nickName) {
        repository.updateNickNameById(id, nickName);
    }

    @Override
    public void chgLocation(Long id, String location) {
        repository.chgLocation(id, location);
    }
}
