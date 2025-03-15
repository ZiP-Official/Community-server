package com.zip.community.platform.application.port.out.chat;

import com.zip.community.platform.adapter.out.mongo.chat.ChatMessageDocument;
import com.zip.community.platform.adapter.out.mongo.chat.ReportedChatMessageDocument;
import com.zip.community.platform.domain.chat.ChatMessage;
import com.zip.community.platform.domain.report.ReportedChatMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageMongoPort {

    ChatMessage save(ChatMessage message);
    Optional<ChatMessageDocument> findById(String messageId);
    ReportedChatMessage reportMessage(ReportedChatMessage reportedChatMessage);
    List<ChatMessage> getMessages(String chatRoomId, String cursor, Integer pageSize, String direction, Boolean includeCursor);
    List<ChatMessage> searchMessages(String chatRoomId, String keyword);
    Optional<ReportedChatMessageDocument> getByMessageIdAndReportMemberId(String messageId, Long reportMemberId);
}