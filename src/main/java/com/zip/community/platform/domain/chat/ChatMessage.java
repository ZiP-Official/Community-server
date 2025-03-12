package com.zip.community.platform.domain.chat;

import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
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
}
