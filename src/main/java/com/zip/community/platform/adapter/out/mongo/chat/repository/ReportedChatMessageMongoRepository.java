package com.zip.community.platform.adapter.out.mongo.chat.repository;

import com.zip.community.platform.adapter.out.mongo.chat.ReportedChatMessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReportedChatMessageMongoRepository extends MongoRepository<ReportedChatMessageDocument, String> {

    Optional<ReportedChatMessageDocument> findByMessageIdAndReportMemberId(String messageId, Long reportMemberId);
}
