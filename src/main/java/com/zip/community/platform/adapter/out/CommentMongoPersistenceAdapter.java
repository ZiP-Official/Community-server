package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.mongo.comment.CommentDocument;
import com.zip.community.platform.adapter.out.mongo.comment.CommentMongoRepository;
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentPort;
import com.zip.community.platform.application.port.out.comment.SaveCommentPort;
import com.zip.community.platform.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

//@Component
@RequiredArgsConstructor
public class CommentMongoPersistenceAdapter implements SaveCommentPort, LoadCommentPort, RemoveCommentPort {

    private final CommentMongoRepository repository;

    /// SaveCommentPort
    @Override
    public Comment saveComment(Comment comment) {
        CommentDocument document = CommentDocument.from(comment);

        return repository.save(document)
                .toDomain();
    }

    @Override
    public boolean getCheckedExistComment(String id) {
        return repository.existsById(id);
    }

    /// LoadCommentPort
    @Override
    public Optional<Comment> loadCommentById(String id) {

        return repository.findById(id)
                .map(CommentDocument::toDomain);
    }

    @Override
    public Page<Comment> loadCommentsByBoardId(Long boardId, Pageable pageable) {

        return repository.findByBoardId(boardId, pageable)
                .map(CommentDocument::toDomain);
    }

    @Override
    public List<Comment> loadCommentsByCommentId(String parentId) {
        return List.of();
    }

    /// RemoveCommentPort
    @Override
    public void removeComment(String id) {

    }
}
