package com.zip.community.platform.adapter.out.mongo.chat;

import com.zip.community.platform.adapter.out.mongo.BaseDocument;
import com.zip.community.platform.domain.chat.ChatMessage;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_message")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDocument extends BaseDocument {

    @Id
    private String id;
    private String chatRoomId;
    private String content;
    private Long senderId;
    private String senderName;
    private LocalDateTime sentAt;
    private Boolean readYn;
    private Boolean deletedYn;

    public static ChatMessageDocument from(ChatMessage chatMessage) {
        return ChatMessageDocument.builder()
                .id(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoomId())
                .content(chatMessage.getContent())
                .senderId(chatMessage.getSenderId())
                .senderName(chatMessage.getSenderName())
                .sentAt(chatMessage.getSentAt())
                .readYn(chatMessage.getReadYn())
                .deletedYn(chatMessage.getDeletedYn())
                .build();
    }

    public ChatMessage toDomain() {
        return ChatMessage.builder()
                .id(this.id)
                .chatRoomId(this.chatRoomId)
                .content(this.content)
                .senderId(this.senderId)
                .senderName(this.senderName)
                .sentAt(this.sentAt)
                .readYn(this.readYn)
                .deletedYn(this.deletedYn)
                .build();
    }
}
