package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.mongo.chat.ChatMessageDocument;
import com.zip.community.platform.adapter.out.mongo.chat.repository.ChatMessageMongoRepository;
import com.zip.community.platform.application.port.out.chat.ChatMessageMongoPort;
import com.zip.community.platform.domain.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessageMongoPersistenceAdapter implements ChatMessageMongoPort {

    private final ChatMessageMongoRepository chatMessageRepository;

    @Override
    public ChatMessage save(ChatMessage message) {
        // 도메인 -> Document 변환 (예: ChatMessageDocument.from(...))
        ChatMessageDocument doc = ChatMessageDocument.from(
                message.getId(),
                message.getChatRoomId(),
                message.getContent(),
                message.getSenderId(),
                message.getSentAt(),
                message.getReadYn(),
                message.getDeletedYn()
        );
        ChatMessageDocument savedDoc = chatMessageRepository.save(doc);
        return savedDoc.toDomain();
    }

    @Override
    public ChatMessage findById(String messageId) {
        return chatMessageRepository.findById(messageId).toDomain();
    }

    @Override
    public List<ChatMessage> searchMessages(String chatRoomId, String keyword, LocalDateTime from, LocalDateTime to) {
        return null;
    }
}
