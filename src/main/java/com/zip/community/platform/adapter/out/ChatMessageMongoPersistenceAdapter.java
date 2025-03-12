package com.zip.community.platform.adapter.out;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.ChatErrorCode;
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
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatMessageMongoPersistenceAdapter implements ChatMessageMongoPort {

    private final ChatMessageMongoRepository chatMessageRepository;
    private final ReportedChatMessageMongoRepository reportedChatMessageRepository;

    @Override
    public ChatMessage save(ChatMessage message) {
        ChatMessageDocument doc = ChatMessageDocument.builder()
                .id(message.getId())
                .chatRoomId(message.getChatRoomId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .senderName(message.getSenderName())
                .sentAt(message.getSentAt())
                .readYn(message.getReadYn())
                .deletedYn(message.getDeletedYn())
                .build();
        ChatMessageDocument savedDoc = chatMessageRepository.save(doc);
        return savedDoc.toDomain();
    }

    @Override
    public List<ChatMessage> getMessages(String chatRoomId, Integer page) {
        // page=0: 1~20, page=1: 21~40 ...
        PageRequest pageable = PageRequest.of(page, 20);
        List<ChatMessageDocument> docs =
                chatMessageRepository.findByChatRoomIdOrderBySentAtDesc(chatRoomId, pageable);
        return docs.stream().map(ChatMessageDocument::toDomain).collect(Collectors.toList());
    }

    @Override
    public ChatMessage findById(String messageId) {
        return chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ChatErrorCode.NOT_FOUND_CHAT_MESSAGE))
                .toDomain();
    }

    @Override
    public ReportedChatMessage reportMessage(String messageId, Long reportMemberId, Long reportedMemberId, String reason) {
        reportedChatMessageRepository.findByMessageIdAndReportMemberId(messageId, reportMemberId).ifPresent(doc -> {
            throw new CustomException(ChatErrorCode.ALREADY_REPORTED_MESSAGE);
        });
        return reportedChatMessageRepository.save(ReportedChatMessageDocument.builder()
                .messageId(messageId)
                .reportMemberId(reportMemberId)
                .reportedMemberId(reportedMemberId)
                .reason(reason)
                .build()
        ).toDomain();
    }

    @Override
    public List<ChatMessage> searchMessages(String chatRoomId, String keyword) {
        return null;
    }
}
