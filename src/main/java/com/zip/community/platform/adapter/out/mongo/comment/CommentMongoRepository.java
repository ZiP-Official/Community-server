package com.zip.community.platform.adapter.out.mongo.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CommentMongoRepository extends MongoRepository<CommentDocument, String> {
    Page<CommentDocument> findByBoardId(Long boardId, Pageable pageable);
}
