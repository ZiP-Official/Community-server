package com.zip.community.platform.application.port.in.chat;

import com.zip.community.platform.domain.chat.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageUseCase {

    // 실시간 메시지 전송 및 저장
    void sendMessage(ChatMessage message);

    // 그 외 CRUD 기능 (삭제, 신고, 차단, 검색 등)
    ChatMessage deleteMessage(String messageId, String userId);
    ChatMessage reportMessage(String messageId, String reporterId, String reason);
    ChatMessage blockMessage(String messageId);
    List<ChatMessage> searchMessages(String chatRoomId, String keyword, LocalDateTime from, LocalDateTime to);
}
