package com.zip.community.platform.domain.chat;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private String id;
    private String chatRoomId;
    private String content;
    private String senderId;
    private LocalDateTime sentAt;
    private Boolean readYn;
    private Boolean deletedYn;

    private static ChatMessage of(String id, String chatRoomId, String content, String senderId, LocalDateTime sentAt, Boolean readYn, Boolean deletedYn) {
        return ChatMessage.builder()
                .id(id)
                .chatRoomId(chatRoomId)
                .content(content)
                .senderId(senderId)
                .sentAt(sentAt)
                .readYn(readYn)
                .deletedYn(deletedYn)
                .build();
    }
}
