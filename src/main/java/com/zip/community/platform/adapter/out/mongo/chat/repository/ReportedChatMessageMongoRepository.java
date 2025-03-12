package com.zip.community.platform.adapter.out.mongo.chat.repository;

import com.zip.community.platform.adapter.out.mongo.chat.ReportedChatMessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportedChatMessageMongoRepository extends MongoRepository<ReportedChatMessageDocument, String> {
}
