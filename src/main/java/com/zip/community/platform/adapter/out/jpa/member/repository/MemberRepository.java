package com.zip.community.platform.adapter.out.jpa.member.repository;

import com.zip.community.platform.adapter.out.jpa.member.MemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberJpaEntity, Long> {

    // 이메일 체크
    @Query("SELECT B FROM MemberJpaEntity B WHERE B.email = :email")
    Optional<MemberJpaEntity> getMemberByEmail(@Param("email") String email);

    // 중복 닉네임 검색
    @Query("SELECT B FROM MemberJpaEntity B WHERE B.nickName = :nickName")
    Optional<MemberJpaEntity> chkNickName(@Param("nickName") String nickName);

    // 마지막 접속일 업데이트
    @Modifying
    @Query("UPDATE MemberJpaEntity B SET B.lastDate = :lastDate WHERE B.id = :id")
    void updateLastDate(@Param("id") Long id, @Param("lastDate") String lastDate);

    // 닉네임 수정
    @Modifying
    @Query("UPDATE MemberJpaEntity B SET B.nickName = :nickName WHERE B.id = :id")
    void updateNickNameById(@Param("id") Long id, @Param("nickName") String nickName);

    // 지역 변경
    @Modifying
    @Query("UPDATE MemberJpaEntity B SET B.location = :location WHERE B.id = :id")
    void chgLocation(@Param("id") Long id, @Param("location") String location);
}
