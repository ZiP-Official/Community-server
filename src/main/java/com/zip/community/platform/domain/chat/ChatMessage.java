package com.zip.community.platform.domain.chat;

import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ChatMessage extends BaseDomain {

    private String id;
    private String chatRoomId;
    private String content;
    private Long senderId;
    private String senderName;
    private LocalDateTime sentAt;
    private Boolean readYn;
    private Boolean deletedYn;

    public static ChatMessage of(String chatRoomId, String content, Long senderId, String senderName, LocalDateTime sentAt, Boolean readYn, Boolean deletedYn) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .content(content)
                .senderId(senderId)
                .senderName(senderName)
                .sentAt(sentAt)
                .readYn(readYn)
                .deletedYn(deletedYn)
                .build();
    }

    public void delete() {
        this.content = "삭제된 메세지입니다";
        this.deletedYn = true;
    }

    public void block() {
        this.content = "부적절한 내용으로 차단된 메세지입니다";
        this.deletedYn = true;
    }
}
