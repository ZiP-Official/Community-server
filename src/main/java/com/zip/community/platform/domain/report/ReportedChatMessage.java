package com.zip.community.platform.domain.report;

import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ReportedChatMessage extends BaseDomain {

    private String id;
    private String messageId;
    private Long reportMemberId;
    private Long reportedMemberId;
    private String reason;
    private LocalDateTime reportedAt;

    public static ReportedChatMessage of(String messageId, Long reportMemberId, Long reportedMemberId, String reason, LocalDateTime reportedAt) {
        return ReportedChatMessage.builder()
                .messageId(messageId)
                .reportMemberId(reportMemberId)
                .reportedMemberId(reportedMemberId)
                .reason(reason)
                .reportedAt(reportedAt)
                .build();
    }
}
