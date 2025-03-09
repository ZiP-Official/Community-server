package com.zip.community.platform.adapter.out;

import com.zip.community.common.util.RedisKeyGenerator;
import com.zip.community.platform.adapter.out.jpa.comment.CommentReactionJpaEntity;
import com.zip.community.platform.adapter.out.jpa.comment.CommentReactionJpaRepository;
import com.zip.community.platform.adapter.out.redis.comment.CommentRedisHash;
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

        // 도메인 생성하기
        CommentReaction reaction = CommentReaction.of(UUID.randomUUID().toString(), commentId, userId, UserReaction.LIKE);
        var commentReaction = CommentReactionJpaEntity.from(reaction);

        // DB내 저장
        repository.save(commentReaction);

        // 레디스 저장, 숫자 증가시키기
        String likeKey = RedisKeyGenerator.getCommentLikeKey(commentId);

        var setOps = redisTemplate.opsForSet();
        setOps.add(likeKey, userId);

    }

    @Override
    public void saveDisLikeCommentReaction(String commentId, Long userId) {

        // 도메인 생성하기
        CommentReaction reaction = CommentReaction.of(UUID.randomUUID().toString(), commentId, userId, UserReaction.DISLIKE);
        var commentReaction = CommentReactionJpaEntity.from(reaction);

        /// DB
        repository.save(commentReaction);

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
                .limit(2)
                .collect(Collectors.toList());

        // 상위 3개 댓글에 대해 Redis에 저장
        String commentKey = RedisKeyGenerator.getPinnedCommentKey(topComments.get(0).getBoardId());

        topComments.forEach(comment -> {
            stringRedisTemplate.opsForList().rightPush(commentKey, comment.getId());
        });
    }



    @Override
    public void synchronizeCommentReaction(CommentReaction commentReaction) {

    }

    /// LoadPort 관련
    @Override
    public boolean checkCommentReaction(String commentId, Long memberId) {

        return checkCommentDisLikeReaction(commentId, memberId) || checkCommentLikeReaction(commentId, memberId);
    }

    @Override
    public boolean checkCommentLikeReaction(String commentId, Long memberId) {

        ///  레디스 처리
        // 키 값 가져오기
        String likeKey = RedisKeyGenerator.getCommentLikeKey(commentId);

        var setOps = redisTemplate.opsForSet();
        return setOps.isMember(likeKey, memberId);

        /// DB 처리

    }

    @Override
    public boolean checkCommentDisLikeReaction(String commentId, Long memberId) {

        ///  레디스 처리
        // 키 값 가져오기
        String disLikeKey = RedisKeyGenerator.getCommentDisLikeKey(commentId);

        var setOps = redisTemplate.opsForSet();
        return setOps.isMember(disLikeKey, memberId);

        /// DB 처리

    }

    @Override
    public Long loadCommentLikeCount(String commentId) {

        var setOps = redisTemplate.opsForSet();
        return setOps.size(RedisKeyGenerator.getCommentLikeKey(commentId));
    }

    @Override
    public Long loadCommentDisLikeCount(String commentId) {

        var setOps = redisTemplate.opsForSet();
        return setOps.size(RedisKeyGenerator.getCommentDisLikeKey(commentId));
    }

    /// RemovePort 관련
    @Override
    public void removeCommentLikeReaction(String commentId, Long userId) {
        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getCommentLikeKey(commentId), userId);
    }

    @Override
    public void removeCommentDisLikeReaction(String commentId, Long userId) {
        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getCommentDisLikeKey(commentId), userId);

    }
}
