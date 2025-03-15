package com.zip.community.platform.adapter.in.web.dto.request.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequest {

    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String receiverName;
}
