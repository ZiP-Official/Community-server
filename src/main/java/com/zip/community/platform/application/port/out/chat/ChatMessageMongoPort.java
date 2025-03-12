package com.zip.community.platform.application.port.out.chat;

import com.zip.community.platform.domain.chat.ChatMessage;
import com.zip.community.platform.domain.report.ReportedChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageMongoPort {

    ChatMessage save(ChatMessage message);
    List<ChatMessage> getMessages(String chatRoomId, Integer page);
    ChatMessage findById(String messageId);
    ReportedChatMessage reportMessage(String messageId, Long reportMemberId, Long reportedMemberId, String reason);
    List<ChatMessage> searchMessages(String chatRoomId, String keyword);
}
