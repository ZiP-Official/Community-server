package com.zip.community.platform.application.port.in.chat;

import com.zip.community.platform.domain.chat.ChatMessage;
import com.zip.community.platform.domain.report.ReportedChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageUseCase {

    ChatMessage sendMessage(ChatMessage message);
    List<ChatMessage> getMessages(String chatRoomId, Integer page);
    ChatMessage deleteMessage(String messageId, Long memberId);
    ReportedChatMessage reportMessage(String messageId, Long reportMemberId, Long reportedMemberId, String reason);
    ChatMessage blockMessage(String messageId);
    List<ChatMessage> searchMessages(String chatRoomId, String keyword);
}
