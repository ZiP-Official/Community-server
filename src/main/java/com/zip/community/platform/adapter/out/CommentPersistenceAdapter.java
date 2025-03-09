package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.jpa.comment.CommentJpaEntity;
import com.zip.community.platform.adapter.out.jpa.comment.CommentJpaRepository;
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentPort;
import com.zip.community.platform.application.port.out.comment.SaveCommentPort;
import com.zip.community.platform.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements LoadCommentPort, SaveCommentPort, RemoveCommentPort {

    private final CommentJpaRepository repository;

    @Override
    public Comment saveComment(Comment comment) {

        var entity = CommentJpaEntity.from(comment);

        return repository.save(entity)
                .toDomain();
    }

    @Override
    public boolean getCheckedExistComment(String id) {
        return repository.existsById(id);

    }

    @Override
    public Optional<Comment> loadCommentById(String id) {

        return repository.findById(id)
                .map(CommentJpaEntity::toDomain);
    }

    @Override
    public Page<Comment> loadCommentsByBoardId(Long boardId, Pageable pageable) {
        Page<CommentJpaEntity> result = repository.findRootCommentsByBoardId(boardId, pageable);

        List<Comment> comments = result.stream()
                .map(CommentJpaEntity::toDomain)
                .toList();

        return new PageImpl<>(comments, pageable, result.getTotalElements());
    }

    @Override
    public List<Comment> loadCommentsByCommentId(String parentId) {
        return repository.findCommentByParentId(parentId)
                .stream().map(CommentJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void removeComment(String id) {
        repository.deleteById(id);
    }


}
