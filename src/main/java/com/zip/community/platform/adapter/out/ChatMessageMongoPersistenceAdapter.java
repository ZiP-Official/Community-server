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
import org.springframework.stereotype.Component;

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
    public List<ChatMessage> getMessages(String chatRoomId, Integer page) {
        // page=0: 1~20, page=1: 21~40 ...
        PageRequest pageable = PageRequest.of(page, 20);
        List<ChatMessageDocument> docs = chatMessageRepository.findByChatRoomIdOrderBySentAtDesc(chatRoomId, pageable);
        return docs.stream().map(ChatMessageDocument::toDomain).collect(Collectors.toList());
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
    public List<ChatMessage> searchMessages(String chatRoomId, String keyword) {
        return null;
    }

    @Override
    public Optional<ReportedChatMessageDocument> getByMessageIdAndReportMemberId(String messageId, Long reportMemberId) {
        return reportedChatMessageRepository.findByMessageIdAndReportMemberId(messageId, reportMemberId);
    }
}
