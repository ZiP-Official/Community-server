package com.zip.community.platform.adapter.out;

import com.zip.community.common.util.RedisKeyGenerator;
import com.zip.community.platform.adapter.out.jpa.comment.CommentReactionJpaEntity;
import com.zip.community.platform.adapter.out.jpa.comment.CommentReactionJpaRepository;
import com.zip.community.platform.application.port.out.comment.LoadCommentReactionPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentReactionPort;
import com.zip.community.platform.application.port.out.comment.SaveCommentReactionPort;
import com.zip.community.platform.domain.board.UserReaction;
import com.zip.community.platform.domain.comment.Comment;
import com.zip.community.platform.domain.comment.CommentReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentReactionPersistenceAdapter implements SaveCommentReactionPort, LoadCommentReactionPort, RemoveCommentReactionPort {

    private final CommentReactionJpaRepository repository;

    private final RedisTemplate<String, Long> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;

    /// SavePort 관련
    @Override
    public void saveLikeCommentReaction(String commentId, Long userId) {

        // 레디스 저장, 숫자 증가시키기
        String likeKey = RedisKeyGenerator.getCommentLikeKey(commentId);

        var setOps = redisTemplate.opsForSet();

        setOps.add(likeKey, userId);

    }

    @Override
    public void saveDisLikeCommentReaction(String commentId, Long userId) {

        /// 레디스 처리
        // 레디스 저장, 숫자 증가시키기
        String disLikeKey = RedisKeyGenerator.getCommentDisLikeKey(commentId);

        var setOps = redisTemplate.opsForSet();

        setOps.add(disLikeKey, userId);
    }

    @Override /// GPT 도움
    public void savePinnedComment(List<Comment> comments) {
        // 레디스로 좋아요, 싫어요 출력
        comments.forEach(comment -> {
            Long likeCount = loadCommentLikeCount(comment.getId());
            Long disLikeCount = loadCommentDisLikeCount(comment.getId());
            comment.getStatistics().bindReactionCount(likeCount, disLikeCount);
        });

        // likeCount 기준 내림차순 정렬
        comments.sort((comment1, comment2) ->
                Long.compare(comment2.getStatistics().getLikeCount(), comment1.getStatistics().getLikeCount())
        );

        // 상위 3개 댓글만 선택
        List<Comment> topComments = comments.stream()
                .limit(3)
                .collect(Collectors.toList());

        // 상위 3개 댓글에 대해 Redis에 저장
        String commentKey = RedisKeyGenerator.getPinnedCommentKey(topComments.get(0).getBoardId());

        topComments.forEach(comment -> {
            stringRedisTemplate.opsForList().rightPush(commentKey, comment.getId());
        });
    }

    @Override
    public void syncBoardReaction(String commentId) {

        // Redis Set에서 목록을 가져오기
        String commentLikeKey = RedisKeyGenerator.getCommentLikeKey(commentId);
        String commentDisLikeKey = RedisKeyGenerator.getCommentDisLikeKey(commentId);

        var setOps = redisTemplate.opsForSet();

        // 좋아요에 대한 userId 목록 가져오기
        Set<Long> likeUserIds = setOps.members(commentLikeKey);

        // 싫어요에 대한 userId 목록 가져오기
        Set<Long> disLikeUserIds = setOps.members(commentDisLikeKey);

        // RDS 저장 로직 추가
        // ! -- 좋아요를 언제 눌렀는지는 중요하지 않기에 생략하겠다 -- !
        if (likeUserIds != null) {
            for (Long userId : likeUserIds) {
                // DB에 저장하는 로직 추가
                CommentReaction reaction = CommentReaction.of(UUID.randomUUID().toString(), commentId, userId, UserReaction.LIKE);
                repository.save(CommentReactionJpaEntity.from(reaction));
            }
        }

        if (disLikeUserIds != null) {
            for (Long userId : disLikeUserIds) {
                // DB에 저장하는 로직 추가
                CommentReaction reaction = CommentReaction.of(UUID.randomUUID().toString(), commentId, userId, UserReaction.DISLIKE);
                repository.save(CommentReactionJpaEntity.from(reaction));
            }
        }

        // 캐시 삭제하기
        removeCache(commentId);

    }

    /// LoadPort 관련
    @Override
    public boolean checkCommentLikeReaction(String commentId, Long memberId) {

        ///  레디스 처리
        // 키 값 가져오기
        String likeKey = RedisKeyGenerator.getCommentLikeKey(commentId);

        var setOps = redisTemplate.opsForSet();
        Boolean redis = setOps.isMember(likeKey, memberId);

        /// DB 처리
        if (Boolean.FALSE.equals(redis)) {
            return repository.existsByCommentIdAndMemberIdAndReactionType(commentId, memberId, UserReaction.LIKE);
        }
        return redis;
    }

    @Override
    public boolean checkCommentDisLikeReaction(String commentId, Long memberId) {


        ///  레디스 처리
        // 키 값 가져오기
        String disLikeKey = RedisKeyGenerator.getCommentDisLikeKey(commentId);

        var setOps = redisTemplate.opsForSet();
        Boolean redis = setOps.isMember(disLikeKey, memberId);

        /// DB 처리
        if (Boolean.FALSE.equals(redis)) {
            return repository.existsByCommentIdAndMemberIdAndReactionType(commentId, memberId, UserReaction.DISLIKE);
        }
        return redis;
    }

    @Override
    public Long loadCommentLikeCount(String commentId) {

        /// Redis에서 조회
        var setOps = redisTemplate.opsForSet();
        String redisKey = RedisKeyGenerator.getCommentLikeKey(commentId);

        Long likeCount = setOps.size(redisKey);

        /// RDS에서 조회
        if (likeCount == null || likeCount == 0) {
            likeCount = repository.countByCommentIdAndReactionType(commentId,UserReaction.LIKE);
        }

        return likeCount;
    }


    @Override
    public Long loadCommentDisLikeCount(String commentId) {

        var setOps = redisTemplate.opsForSet();
        String redisKey = RedisKeyGenerator.getCommentDisLikeKey(commentId);

        Long dislikeCount = setOps.size(redisKey);

        // RDS에서 조회
        if (dislikeCount == null) {
            dislikeCount = repository.countByCommentIdAndReactionType(commentId,UserReaction.DISLIKE);
        }

        return dislikeCount;
    }

    /// RemovePort 관련
    @Override
    public void removeCommentLikeReaction(String commentId, Long userId) {
        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getCommentLikeKey(commentId), userId);

        if (checkCommentLikeReaction(commentId, userId) && Boolean.FALSE.equals(setOps.isMember(RedisKeyGenerator.getCommentLikeKey(commentId), userId))) {
            repository.findByCommentIdAndMemberIdAndReactionType(commentId, userId, UserReaction.LIKE)
                    .ifPresent(repository::delete);
        }

    }

    @Override
    public void removeCommentDisLikeReaction(String commentId, Long userId) {
        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getCommentDisLikeKey(commentId), userId);

        if (checkCommentDisLikeReaction(commentId, userId) && Boolean.FALSE.equals(setOps.isMember(RedisKeyGenerator.getCommentDisLikeKey(commentId), userId))) {
            repository.findByCommentIdAndMemberIdAndReactionType(commentId, userId, UserReaction.DISLIKE)
                    .ifPresent(repository::delete);
        }

    }

    // 게시글에 해당 하는 내용 다 삭제하기, 글 삭제와 관련된 내용이 존재한다.
    @Override
    public void removeAllByBoardId(String commentId) {

        // 캐시 관련 내용 삭제
        removeCache(commentId);

        // 영속성 관련 내용 삭제
        removeEntity(commentId);

    }

    @Override
    public void removeEntity(String commentId) {
        repository.deleteById(commentId);
    }

    // 게시글 추천, 비추천에 대한 캐시 삭제
    @Override
    public void removeCache(String commentId) {

        redisTemplate.delete(RedisKeyGenerator.getCommentLikeKey(commentId));
        redisTemplate.delete(RedisKeyGenerator.getCommentDisLikeKey(commentId));

    }
}
