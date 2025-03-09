package com.zip.community.platform.adapter.in.web.dto.member;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.ErrorCode;
import com.zip.community.platform.adapter.in.web.dto.request.member.MemberRequest;
import com.zip.community.platform.adapter.in.web.dto.response.member.MemberResponse;
import com.zip.community.platform.application.port.in.member.MemberUseCase;
import com.zip.community.platform.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberUseCase memberService;

    // 닉네임 변경
    @GetMapping("/login")
    public ApiResponse<MemberResponse> login(@RequestBody MemberRequest memberRequest) {
        // memberRequest (email)
        Member mem = memberService.chkEmail(memberRequest.getEmail());
        if(mem == null){
            throw new CustomException(ErrorCode.DUPLICATE_ERROR);   // 비회원
        }else if(mem != null && !mem.getStatus().isEmpty() && "0".equals(mem.getStatus())){
            throw new CustomException(ErrorCode.DUPLICATE_ERROR);   // 정지된 이용자
        }

        return ApiResponse.created(MemberResponse.from(memberService.loginMember(memberRequest.getEmail())));
    }

    // 회원가입
    @GetMapping("/joinMember")
    public ApiResponse<MemberResponse> saveMember(@RequestBody MemberRequest memberRequest) {
        // memberRequest (email, loginType, token, birth, gender)

        Member mem = memberService.chkEmail(memberRequest.getEmail());
        if(mem != null && !mem.getStatus().isEmpty() && "0".equals(mem.getStatus())){
            throw new CustomException(ErrorCode.DUPLICATE_ERROR);   // 정지된 이용자
        }
        if(mem != null){
            throw new CustomException(ErrorCode.DUPLICATE_ERROR);   // 이미 가입된 이용자입니다.
        }

        String[] str = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", };
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowStr = now.format(format); // 회원 가입 날짜

        // 랜덤 객체 생성
        Random random = new Random();

        StringBuilder nick = new StringBuilder();   // 회원가입시 임시 닉네임 생성
        boolean chkNick = false;
        while(!chkNick){
            nick = new StringBuilder();
            for(int i=0; i<(random.nextInt(4)+7); i++){
                int randomIdx = random.nextInt(str.length);
                nick.append(str[randomIdx]);
            }
            String nickstr = nick.toString();
            if(nickstr.toLowerCase().indexOf("fuck") == -1) {
                chkNick = memberService.chkNickName(nick.toString());   // 중복한 값이면 false
            }
        }

        memberRequest.setNickName(nick.toString());
        memberRequest.setLocation("1111010700");    // 초기 셋팅 서울시 시청
        memberRequest.setCreateDate(nowStr);
        memberRequest.setChangeDate(nowStr);
        memberRequest.setLastDate(nowStr);
        memberRequest.setStatus("1");

        return ApiResponse.created(MemberResponse.from(memberService.saveMember(memberRequest)));
    }

    // 닉네임 변경
    @GetMapping("/chgNickName")
    public ApiResponse<String> chgNickName(@RequestBody MemberRequest memberRequest) {
        // memberRequest (id, nickName)
        memberService.chgNickName(memberRequest);
        return ApiResponse.created("success");
    }

    // 지역 변경
    @GetMapping("/chgLocation")
    public ApiResponse<String> chgLocation(@RequestBody MemberRequest memberRequest) {
        // memberRequest (id, location)
        memberService.chgLocation(memberRequest);
        return ApiResponse.created("success");
    }
}
