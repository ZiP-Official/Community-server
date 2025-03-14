package com.zip.community.platform.adapter.out;

import com.zip.community.common.util.RedisKeyGenerator;
import com.zip.community.platform.adapter.out.jpa.comment.CommentJpaEntity;
import com.zip.community.platform.adapter.out.jpa.comment.CommentJpaRepository;
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentPort;
import com.zip.community.platform.application.port.out.comment.SaveCommentPort;
import com.zip.community.platform.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements SaveCommentPort, LoadCommentPort, RemoveCommentPort {

    /*
        댓글 저장,삭제에 개수를 Redis 관리
        인기있는 게시글의 댓글은 Redis 캐시로 처리한다.
     */
    private final CommentJpaRepository repository;

    private final RedisTemplate<String, Long> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;


    /// SaveCommentPort
    @Override
    public Comment saveComment(Comment comment) {

        var entity = CommentJpaEntity.from(comment);

        return repository.save(entity)
                .toDomain();
    }

    // 게시글에 대한 댓글 개수 증가시키기
    @Override
    public void incrementCommentCount(Long boardId) {
        String countKey = RedisKeyGenerator.getCommentCountKey(boardId);

        /// Redis 존재할 경우
        if (redisTemplate.hasKey(countKey)) {
            redisTemplate.opsForValue().increment(countKey, 1);
        } else { /// db에서 값 가져와서 값 추가한다.
            long dbCount = repository.countCommentsByBoardIdAndDeletedFalse(boardId);
            redisTemplate.opsForValue().set(countKey, dbCount);
        }
    }

    /// LoadCommentPort
    @Override
    // 개수 가져오기
    public Long loadCommentCount(Long boardId) {

        // Redis에서 개수 가져오기
        Long redisCount = redisTemplate.opsForValue().get(RedisKeyGenerator.getCommentCountKey(boardId));

        // Redis에 값이 없거나 0일 경우 DB에서 조회
        if (redisCount == null || redisCount.equals(0L)) {
            // DB에서 댓글 개수를 직접 조회하여 리턴
            long dbCount = repository.countCommentsByBoardIdAndDeletedFalse(boardId);

            // DB에서 조회한 개수를 Redis에 저장
            redisTemplate.opsForValue().set(RedisKeyGenerator.getCommentCountKey(boardId), dbCount);

            return dbCount;
        }

        return redisCount;
    }


    // 이미 존재하는 댓글인지 확인
    @Override
    public boolean getCheckedExistComment(String id) {
        return repository.existsById(id);
    }

    // 삭제하려고 할 때, 대댓글이 존재하는 경우를 판단한다.
    @Override
    public boolean hasChildren(String parentId) {

        if (parentId == null) {
            return false;
        }

        return repository.existsByParentIdAndDeletedFalse(parentId);
    }

    // 게시글에 해당하는 댓글들 가져오기
    @Override
    public Page<Comment> loadCommentsByBoardId(Long boardId, Pageable pageable) {
        Page<CommentJpaEntity> result = repository.findRootCommentsByBoardId(boardId, pageable);

        List<Comment> comments = result.stream()
                .map(CommentJpaEntity::toDomain)
                .filter(comment -> !comment.isDeleted() || this.hasChildren(String.valueOf(comment.getId())))
                .toList();

        return new PageImpl<>(comments, pageable, result.getTotalElements());
    }

    @Override
    public List<Comment> loadCommentsByBoardId(Long boardId) {
        return repository.findCommentByBoardId(boardId)
                .stream().map(CommentJpaEntity::toDomain)
                .filter(comment -> !comment.isDeleted() || this.hasChildren(String.valueOf(comment.getId())))
                .toList();
    }

    // 대댓글 가져오기
    @Override
    public List<Comment> loadCommentsByCommentId(String parentId) {
        return repository.findCommentByParentId(parentId)
                .stream().map(CommentJpaEntity::toDomain)
                .filter(comment -> !comment.isDeleted() || this.hasChildren(String.valueOf(comment.getId())))
                .toList();
    }

    @Override
    public List<Comment> getPinnedComment(Long boardId) {
        // 게시판에 해당하는 인기 댓글 키 가져오기
        String commentKey = RedisKeyGenerator.getPinnedCommentKey(boardId);

        // Redis에서 인기 댓글 ID 목록 가져오기
        List<String> pinnedCommentIds = stringRedisTemplate.opsForList().range(commentKey, 0, -1);

        if (pinnedCommentIds == null || pinnedCommentIds.isEmpty()) {
            return List.of();  // 인기 댓글이 없으면 빈 리스트 반환
        }

        // 데이터베이스에서 인기 댓글들 조회 후 도메인 객체로 변환
        return repository.findAllByIds(pinnedCommentIds)
                .stream()
                .map(CommentJpaEntity::toDomain)
                .filter(comment -> !comment.isDeleted())
                .collect(Collectors.toList());  // toList() 대신 collect() 사용
    }


    /// RemoveCommentPort
    // 댓글만 삭제하기
    @Override
    public void removeComment(String commentId) {
        repository.findById(commentId).ifPresent(comment -> {
            // 캐시 삭제
            redisTemplate.delete(RedisKeyGenerator.getCommentCountKey(comment.getBoardId()));
            redisTemplate.delete(RedisKeyGenerator.getCommentLikeKey(commentId));
            redisTemplate.delete(RedisKeyGenerator.getCommentDisLikeKey(commentId));

            // DB에서 소프트웨어적 삭제를 진행한다.
            comment.delete();
        });
    }



    @Override
    public void removeEntity(Long boardId) {
        /// JPA 처리

        // 해당하는 글의 댓글 모두 가져오기
        List<CommentJpaEntity> comments = repository.findCommentByBoardId(boardId);

        // 기존에 존재하는 SoftDelete의 로직을 사용한다.
        comments.forEach(
                comment -> removeComment(String.valueOf(comment.getId()))
        );

    }

    // 게시글과 관련된 캐시를 모두 삭제한다.
    @Override
    public void removeCache(Long boardId) {

        /// 레디스 처리
        // 해당 글의 댓글 모두 가져오기
        List<CommentJpaEntity> comments = repository.findCommentByBoardId(boardId);

        // 댓글의 레디스키 관련 삭제하기
        comments.forEach(commentJpaEntity -> {
            // 해당 댓글의 좋아요, 싫어요 키 삭제
            redisTemplate.delete(RedisKeyGenerator.getCommentLikeKey(String.valueOf(commentJpaEntity.getId())));
            redisTemplate.delete(RedisKeyGenerator.getCommentDisLikeKey(String.valueOf(commentJpaEntity.getId())));
        });

        // 레디스에 해당 게시글의 댓글 개수 카운트 삭제
        redisTemplate.delete(RedisKeyGenerator.getCommentCountKey(boardId));

    }

    ///  게시글에 해당하는 댓글을 모두 삭제한다.
    @Override
    public void removeAllByBoardId(Long boardId) {
        // Redis 처리
        removeCache(boardId);

        // JPA 처리
        removeEntity(boardId);

    }

}
