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
    private String senderId;
    private LocalDateTime sentAt;
    private Boolean readYn;
    private Boolean deletedYn;

    public static ChatMessageDocument from(String id, String chatRoomId, String content, String senderId, LocalDateTime sentAt, Boolean readYn, Boolean deletedYn) {
        return ChatMessageDocument.builder()
                .id(id)
                .chatRoomId(chatRoomId)
                .content(content)
                .senderId(senderId)
                .sentAt(sentAt)
                .readYn(readYn)
                .deletedYn(deletedYn)
                .build();
    }

    public ChatMessage toDomain() {
        return ChatMessage.builder()
                .id(this.id)
                .chatRoomId(this.chatRoomId)
                .content(this.content)
                .senderId(this.senderId)
                .sentAt(this.sentAt)
                .readYn(this.readYn)
                .deletedYn(this.deletedYn)
                .build();
    }
}
