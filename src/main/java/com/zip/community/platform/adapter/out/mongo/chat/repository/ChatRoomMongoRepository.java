package com.zip.community.platform.adapter.out.mongo.chat.repository;

import com.zip.community.platform.adapter.out.mongo.chat.ChatRoomDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRoomMongoRepository extends MongoRepository<ChatRoomDocument, String> {

    List<ChatRoomDocument> findByParticipantsId(Long memberId);
    @Query("{ 'participants.id': { $all: [?0, ?1] } }")
    List<ChatRoomDocument> findByParticipantsContainingBoth(Long senderId, Long receiverId);
}
