package com.zip.community.platform.application.port.out.chat;

import com.zip.community.platform.domain.chat.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageMongoPort {

    ChatMessage save(ChatMessage message);
    ChatMessage findById(String messageId);
    List<ChatMessage> searchMessages(String chatRoomId, String keyword, LocalDateTime from, LocalDateTime to);
}
