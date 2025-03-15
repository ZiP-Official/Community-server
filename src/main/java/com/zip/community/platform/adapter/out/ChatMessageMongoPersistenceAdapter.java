package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.mongo.chat.ChatMessageDocument;
import com.zip.community.platform.adapter.out.mongo.chat.ReportedChatMessageDocument;
import com.zip.community.platform.adapter.out.mongo.chat.repository.ChatMessageMongoRepository;
import com.zip.community.platform.adapter.out.mongo.chat.repository.ReportedChatMessageMongoRepository;
import com.zip.community.platform.application.port.out.chat.ChatMessageMongoPort;
import com.zip.community.platform.domain.chat.ChatMessage;
import com.zip.community.platform.domain.report.ReportedChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatMessageMongoPersistenceAdapter implements ChatMessageMongoPort {

    private final ChatMessageMongoRepository chatMessageRepository;
    private final ReportedChatMessageMongoRepository reportedChatMessageRepository;

    @Override
    public ChatMessage save(ChatMessage message) {
        ChatMessageDocument doc = ChatMessageDocument.from(message);
        return chatMessageRepository.save(doc).toDomain();
    }

    @Override
    public Optional<ChatMessageDocument> findById(String messageId) {
        return chatMessageRepository.findById(messageId);
    }

    @Override
    public ReportedChatMessage reportMessage(ReportedChatMessage reportedChatMessage) {
        return reportedChatMessageRepository.save(ReportedChatMessageDocument.from(reportedChatMessage)).toDomain();
    }

    @Override
    public List<ChatMessage> getMessages(String chatRoomId, String cursor, Integer pageSize, String direction, Boolean includeCursor) {
        LocalDateTime cursorTime = null;
        if (cursor != null && !cursor.isEmpty()) cursorTime = LocalDateTime.parse(cursor);

        return ("older".equalsIgnoreCase(direction)) ?
                getOlderMessages(chatRoomId, cursorTime, pageSize, includeCursor)
                : getNewerMessages(chatRoomId, cursorTime, pageSize, includeCursor);
    }

    @Override
    public List<ChatMessage> searchMessages(String chatRoomId, String keyword) {
        List<ChatMessageDocument> docs = chatMessageRepository.findByChatRoomIdAndContentContainingOrderBySentAtDesc(chatRoomId, keyword);
        return docs.stream().map(ChatMessageDocument::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<ReportedChatMessageDocument> getByMessageIdAndReportMemberId(String messageId, Long reportMemberId) {
        return reportedChatMessageRepository.findByMessageIdAndReportMemberId(messageId, reportMemberId);
    }

    private List<ChatMessage> getOlderMessages(String chatRoomId, LocalDateTime cursor, Integer pageSize, Boolean includeCursor) {
        Pageable pageable = PageRequest.of(0, pageSize);
        List<ChatMessageDocument> docs;
        if (cursor != null) {
            docs = includeCursor ?
                    chatMessageRepository.findByChatRoomIdAndSentAtLessThanEqualOrderBySentAtDesc(chatRoomId, cursor, pageable)
                    : chatMessageRepository.findByChatRoomIdAndSentAtLessThanOrderBySentAtDesc(chatRoomId, cursor, pageable);
        } else {
            docs = chatMessageRepository.findByChatRoomIdOrderBySentAtDesc(chatRoomId, pageable);
        }
        return docs.stream().map(ChatMessageDocument::toDomain).collect(Collectors.toList());
    }

    private List<ChatMessage> getNewerMessages(String chatRoomId, LocalDateTime cursor, Integer pageSize, Boolean includeCursor) {
        Pageable pageable = PageRequest.of(0, pageSize);
        if (cursor != null) {
            List<ChatMessageDocument> docs = includeCursor ?
                    chatMessageRepository.findByChatRoomIdAndSentAtGreaterThanEqualOrderBySentAtAsc(chatRoomId, cursor, pageable)
                    : chatMessageRepository.findByChatRoomIdAndSentAtGreaterThanOrderBySentAtAsc(chatRoomId, cursor, pageable);
            return docs.stream().map(ChatMessageDocument::toDomain).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}