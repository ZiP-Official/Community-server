package com.zip.community.platform.adapter.out.mongo.chat.repository;

import com.zip.community.platform.adapter.out.mongo.chat.ChatMessageDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocument, String> {

    // 채팅방 내역 조회용 메소드: 채팅방 ID 기준으로 sentAt 내림차순 정렬 후 페이징 처리
    List<ChatMessageDocument> findByChatRoomIdOrderBySentAtDesc(String chatRoomId, Pageable pageable);
    List<ChatMessageDocument> findByChatRoomIdAndContentContainingOrderBySentAtDesc(String chatRoomId, String keyword);

    // 페이지네이션 (older)    // exclusive: 커서값을 포함하지 않음
    List<ChatMessageDocument> findByChatRoomIdAndSentAtLessThanOrderBySentAtDesc(String chatRoomId, LocalDateTime sentAt, Pageable pageable);
    // inclusive: 커서값을 포함함
    List<ChatMessageDocument> findByChatRoomIdAndSentAtLessThanEqualOrderBySentAtDesc(String chatRoomId, LocalDateTime sentAt, Pageable pageable);

    // 페이지네이션 (newer)    // exclusive: 커서값을 포함하지 않음
    List<ChatMessageDocument> findByChatRoomIdAndSentAtGreaterThanOrderBySentAtAsc(String chatRoomId, LocalDateTime sentAt, Pageable pageable);
    // inclusive: 커서값을 포함함
    List<ChatMessageDocument> findByChatRoomIdAndSentAtGreaterThanEqualOrderBySentAtAsc(String chatRoomId, LocalDateTime sentAt, Pageable pageable);
}