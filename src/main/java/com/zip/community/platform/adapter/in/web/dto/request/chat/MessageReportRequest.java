package com.zip.community.platform.adapter.in.web.dto.request.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageReportRequest {

    private String messageId;
    private Long reportMemberId;    // 신고한 회원 ID
    private Long reportedMemberId;  // 신고당한 회원 ID
    private String reason;
}
