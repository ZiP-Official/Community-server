package com.zip.community.platform.adapter.in.web.dto.request.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {

    private String content;
    private String senderId;
}
