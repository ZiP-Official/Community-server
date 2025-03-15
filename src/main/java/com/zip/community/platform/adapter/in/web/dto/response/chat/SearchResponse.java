package com.zip.community.platform.adapter.in.web.dto.response.chat;

import com.zip.community.platform.domain.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    private String chatRoomId;
    private String messageId;
    private String content;
    private Long senderId;
    private String senderName;
    private LocalDateTime sentAt;

    public static SearchResponse from(ChatMessage message) {
        return SearchResponse.builder()
                .chatRoomId(message.getChatRoomId())
                .messageId(message.getId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .senderName(message.getSenderName())
                .sentAt(message.getSentAt())
                .build();
    }
}