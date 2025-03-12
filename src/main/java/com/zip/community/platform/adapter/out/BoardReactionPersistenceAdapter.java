package com.zip.community.platform.adapter.out;

import com.zip.community.common.util.RedisKeyGenerator;
import com.zip.community.platform.adapter.out.jpa.board.BoardReactionJpaEntity;
import com.zip.community.platform.adapter.out.jpa.board.repository.BoardReactionJpaRepository;
import com.zip.community.platform.application.port.out.board.LoadBoardReactionPort;
import com.zip.community.platform.application.port.out.board.RemoveBoardReactionPort;
import com.zip.community.platform.application.port.out.board.SaveBoardReactionPort;
import com.zip.community.platform.domain.board.BoardReaction;
import com.zip.community.platform.domain.board.UserReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardReactionPersistenceAdapter implements LoadBoardReactionPort, SaveBoardReactionPort, RemoveBoardReactionPort {

    private final BoardReactionJpaRepository repository;
    private final RedisTemplate<String, Long> redisTemplate;

    ///  SavePort
    @Override
    public void saveLikeBoardReaction(Long boardId, Long userId) {

        // DB에 저장
        BoardReaction reaction = BoardReaction.of(boardId, userId, UserReaction.LIKE);
        repository.save(BoardReactionJpaEntity.from(reaction));

        // key 값 가져오기
        String boardLikeKey = RedisKeyGenerator.getBoardLikeKey(boardId);

        // RedisSet에 저장하여 중복을 방지한다.
        var setOps = redisTemplate.opsForSet();
        setOps.add(boardLikeKey, userId);
    }

    @Override
    public void saveDisLikeBoardReaction(Long boardId, Long userId) {

        // DB에 저장
        BoardReaction reaction = BoardReaction.of(boardId, userId, UserReaction.DISLIKE);
        repository.save(BoardReactionJpaEntity.from(reaction));

        // key 값 가져오기
        String boardLikeKey = RedisKeyGenerator.getBoardDisLikeKey(boardId);

        // RedisSet에 저장하여 중복을 방지한다.
        var setOps = redisTemplate.opsForSet();
        setOps.add(boardLikeKey, userId);
    }

    @Override
    public void synchronizeBoardReaction(Long boardId) {

    }

    /// LoadPort
    @Override
    public boolean checkBoardLikeReaction(Long boardId, Long memberId) {
        var setOps = redisTemplate.opsForSet();
        Boolean redis = setOps.isMember(RedisKeyGenerator.getBoardLikeKey(boardId), memberId);

        /// RDS 판단한다.
        if (Boolean.FALSE.equals(redis)) {
            boolean rds = repository.existsByBoardIdAndMemberIdAndReactionType(boardId, memberId, UserReaction.LIKE);
            return rds;
        }

        return redis;
    }

    @Override
    public boolean checkBoardDisLikeReaction(Long boardId, Long memberId) {
        var setOps = redisTemplate.opsForSet();
        Boolean redis = setOps.isMember(RedisKeyGenerator.getBoardDisLikeKey(boardId), memberId);

        /// RDS 판단한다.
        if (Boolean.FALSE.equals(redis)) {
            boolean rds = repository.existsByBoardIdAndMemberIdAndReactionType(boardId, memberId, UserReaction.DISLIKE);
            return rds;
        }

        return redis;
    }

    @Override
    public Long loadBoardLikeCount(Long boardId) {

        /// 레디스 처리
        var setOps = redisTemplate.opsForSet();
        String likeKey = RedisKeyGenerator.getBoardLikeKey(boardId);

        // Redis에서 좋아요 개수 확인
        Long count = setOps.size(likeKey);

        /// 레디스가 없다면 RDS 처리
        if (count == null || count == 0) {
            count = repository.countByBoardIdAndReactionType(boardId, UserReaction.LIKE);
        }

        return count;
    }

    @Override
    public Long loadBoardDisLikeCount(Long boardId) {
        // 레디스 처리
        var setOps = redisTemplate.opsForSet();
        String disLikeKey = RedisKeyGenerator.getBoardDisLikeKey(boardId);

        // Redis에서 싫어요 개수 확인
        Long count = setOps.size(disLikeKey);

        /// 레디스가 없다면 RDS 처리
        if (count == null || count == 0) {
            count = repository.countByBoardIdAndReactionType(boardId, UserReaction.DISLIKE);
        }

        return count;
    }


    /// DeletePort
    // 특정 유저의 좋아요 취소
    @Override
    public void removeBoardLikeReaction(Long boardId, Long userId) {

        // 레디스
        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getBoardLikeKey(boardId), userId);

        // RDS
        repository.findByBoardIdAndMemberIdAndReactionType(boardId, userId, UserReaction.LIKE)
                .ifPresent(repository::delete);

    }

    @Override
    // 특정 유저의 싫어요 취소
    public void removeBoardDisLikeReaction(Long boardId, Long userId) {

        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getBoardDisLikeKey(boardId), userId);

        // RDS
        repository.findByBoardIdAndMemberIdAndReactionType(boardId, userId, UserReaction.DISLIKE)
                .ifPresent(repository::delete);
    }

    // 게시글의 모든 반응 삭제하기, 주로 글 삭제와 연관되어있을 듯 싶다
    @Override
    public void removeAllByBoardId(Long boardId) {

        // 캐싱 삭제
        removeCache(boardId);

        // 영속성 삭제
        removeEntity(boardId);
    }

    // 영속성 관련 내용 삭제하기
    @Override
    public void removeEntity(Long boardId) {

        // 소프트 처리등을 여기서 진행한다.

    }

    // 캐시 관련 내용 삭제하기
    @Override
    public void removeCache(Long boardId) {

        redisTemplate.delete(RedisKeyGenerator.getBoardDisLikeKey(boardId));
        redisTemplate.delete(RedisKeyGenerator.getBoardLikeKey(boardId));

        // 해당 게시글 내부의 댓글마다의 추천 비추천을 삭제는 RemoveBoardPort에서 처리한다.
    }


}
