package com.zip.community.platform.adapter.out.jpa.member;

import com.zip.community.platform.adapter.out.jpa.BaseEntity;
import com.zip.community.platform.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
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


    // from
    public static MemberJpaEntity from(Member member) {

        return MemberJpaEntity.builder()
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

    // toDomain
    public static Member toDomain(MemberJpaEntity entity){

        return Member.builder()
                .id(entity.getId())
                .nickName(entity.getNickName())
                .email(entity.getEmail())
                .loginType(entity.getLoginType())
                .token(entity.getToken())
                .birth(entity.getBirth())
                .gender(entity.getGender())
                .status(entity.getStatus())
                .location(entity.getLocation())
                .createDate(entity.getCreateDate())
                .changeDate(entity.getChangeDate())
                .lastDate(entity.getLastDate())
                .build();
    }
}
