package com.zip.community.platform.adapter.out;

import com.zip.community.common.util.RedisKeyGenerator;
import com.zip.community.platform.application.port.out.board.LoadBoardReactionPort;
import com.zip.community.platform.application.port.out.board.RemoveBoardReactionPort;
import com.zip.community.platform.application.port.out.board.SaveBoardReactionPort;
import com.zip.community.platform.domain.board.BoardReaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardReactionPersistenceAdapter implements LoadBoardReactionPort, SaveBoardReactionPort, RemoveBoardReactionPort {

    private final RedisTemplate<String, Long> redisTemplate;

    ///  SavePort
    @Override
    public void saveLikeBoardReaction(Long boardId, Long userId) {

        // key 값 가져오기
        String boardLikeKey = RedisKeyGenerator.getBoardLikeKey(boardId);

        // RedisSet에 저장하여 중복을 방지한다.
        var setOps = redisTemplate.opsForSet();
        setOps.add(boardLikeKey, userId);
    }

    @Override
    public void saveDisLikeBoardReaction(Long boardId, Long userId) {

        // key 값 가져오기
        String boardLikeKey = RedisKeyGenerator.getBoardDisLikeKey(boardId);

        // RedisSet에 저장하여 중복을 방지한다.
        var setOps = redisTemplate.opsForSet();
        setOps.add(boardLikeKey, userId);
    }

    @Override
    public void synchronizeBoardReaction(BoardReaction boardReaction) {
       /*
            레디스의 좋아요 값을 가져와서
            DB에 저장하도록 한다.
        */
    }

    /// LoadPort
    @Override
    public boolean checkBoardReaction(Long boardId, Long memberId) {
        // OR 연산
        return checkBoardDisLikeReaction(boardId, memberId) || checkBoardLikeReaction(boardId, memberId);
    }

    @Override
    public boolean checkBoardLikeReaction(Long boardId, Long memberId) {
        var setOps = redisTemplate.opsForSet();
        return setOps.isMember(RedisKeyGenerator.getBoardLikeKey(boardId), memberId);
    }

    @Override
    public boolean checkBoardDisLikeReaction(Long boardId, Long memberId) {
        var setOps = redisTemplate.opsForSet();
        return setOps.isMember(RedisKeyGenerator.getBoardDisLikeKey(boardId), memberId);
    }

    @Override
    public Long loadBoardLikeCount(Long boardId) {
        var setOps = redisTemplate.opsForSet();
        return setOps.size(RedisKeyGenerator.getBoardLikeKey(boardId));
    }

    @Override
    public Long loadBoardDisLikeCount(Long boardId) {
        var setOps = redisTemplate.opsForSet();
        return setOps.size(RedisKeyGenerator.getBoardDisLikeKey(boardId));
    }


    /// DeletePort
    // 특정 유저의 좋아요 취소
    @Override
    public void removeBoardLikeReaction(Long boardId, Long userId) {

        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getBoardLikeKey(boardId), userId);
    }

    @Override
    // 특정 유저의 싫어요 취소
    public void removeBoardDisLikeReaction(Long boardId, Long userId) {
        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getBoardDisLikeKey(boardId), userId);
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
