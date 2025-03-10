package com.zip.community.platform.adapter.out.mongo.chat.repository;

import com.zip.community.platform.adapter.out.mongo.chat.ChatRoomDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomMongoRepository extends MongoRepository<ChatRoomDocument, String> {

    List<ChatRoomDocument> findByParticipantsContaining(String userId);
}
