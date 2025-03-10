package com.zip.community.platform.application.port.out.chat;

import com.zip.community.platform.domain.chat.ChatMessage;

public interface MessageSendPort {

    ChatMessage send(ChatMessage message);
}
