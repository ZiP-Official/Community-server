package com.zip.community.platform.adapter.out;

import com.zip.community.common.util.RedisKeyGenerator;
import com.zip.community.platform.adapter.out.jpa.comment.CommentJpaEntity;
import com.zip.community.platform.adapter.out.jpa.comment.CommentJpaRepository;
import com.zip.community.platform.adapter.out.redis.comment.CommentRedisRepository;
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentPort;
import com.zip.community.platform.application.port.out.comment.SaveCommentPort;
import com.zip.community.platform.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements LoadCommentPort, SaveCommentPort, RemoveCommentPort {

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
        redisTemplate.opsForValue().increment(countKey, 1);
    }

    // 영속성을 위해 싱크 맞추기
    @Override
    public void syncCommentCount(Long boardId) {


    }

    // 인기 댓글로 지정하기

    /// LoadCommentPort
    @Override
    // 개수 가져오기
    public Long loadCommentCount(Long boardId) {

        return redisTemplate.opsForValue().get(RedisKeyGenerator.getCommentCountKey(boardId));
    }

    // 이미 존재하는 댓글인지 확인
    @Override
    public boolean getCheckedExistComment(String id) {
        return repository.existsById(id);
    }

    // 댓글 상세 조회
    @Override
    public Optional<Comment> loadCommentById(String id) {

        return repository.findById(id)
                .map(CommentJpaEntity::toDomain);
    }

    // 게시글에 해당하는 댓글들 가져오기
    @Override
    public Page<Comment> loadCommentsByBoardId(Long boardId, Pageable pageable) {
        Page<CommentJpaEntity> result = repository.findRootCommentsByBoardId(boardId, pageable);

        List<Comment> comments = result.stream()
                .map(CommentJpaEntity::toDomain)
                .toList();

        return new PageImpl<>(comments, pageable, result.getTotalElements());
    }

    // 대댓글 가져오기
    @Override
    public List<Comment> loadCommentsByCommentId(String parentId) {
        return repository.findCommentByParentId(parentId)
                .stream().map(CommentJpaEntity::toDomain)
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
                .collect(Collectors.toList());  // toList() 대신 collect() 사용
    }


    /// RemoveCommentPort
    // 댓글 삭제하기
    @Override
    public void removeComment(String id) {

        /*
            SoftDelete로 설정한다.
         */

        repository.deleteById(id);
    }


}
