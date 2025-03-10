package com.zip.community.platform.adapter.out.mongo.chat.repository;

import com.zip.community.platform.adapter.out.mongo.chat.ChatMessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocument, Long> {

    List<ChatMessageDocument> findByChatRoomIdAndContentContainingAndSentAtBetween(String chatRoomId, String keyword, LocalDateTime from, LocalDateTime to);

    // 추가로 findById, save 등은 기본적으로 제공됨
    ChatMessageDocument findById(String id);
}
