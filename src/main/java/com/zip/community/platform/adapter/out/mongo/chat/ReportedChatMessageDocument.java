package com.zip.community.platform.adapter.out.mongo.chat;

import com.zip.community.platform.adapter.out.mongo.BaseDocument;
import com.zip.community.platform.domain.report.ReportedChatMessage;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reported_message")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportedChatMessageDocument extends BaseDocument {

    @Id
    private String id;
    private String messageId;
    private Long reportMemberId;   // 신고한 회원 ID
    private Long reportedMemberId; // 신고당한 회원 ID
    private String reason;

    public ReportedChatMessage toDomain() {
        return ReportedChatMessage.builder()
                .id(this.id)
                .messageId(this.messageId)
                .reportMemberId(this.reportMemberId)
                .reportedMemberId(this.reportedMemberId)
                .reason(this.reason)
                .reportedAt(this.getCreatedAt())
                .build();
    }
}
