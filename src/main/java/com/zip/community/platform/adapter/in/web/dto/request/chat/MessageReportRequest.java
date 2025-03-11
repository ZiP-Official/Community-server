package com.zip.community.platform.adapter.in.web.dto.request.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageReportRequest {

    private String messageId;
    private Long reportMemberId;
    private String reason;
}
