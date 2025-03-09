package com.zip.community.platform.adapter.out;

import com.zip.community.common.util.RedisKeyGenerator;
import com.zip.community.platform.application.port.out.board.LoadBoardPort;
import com.zip.community.platform.application.port.out.board.LoadBoardReactionPort;
import com.zip.community.platform.application.port.out.board.RemoveBoardReactionPort;
import com.zip.community.platform.application.port.out.board.SaveBoardReactionPort;
import com.zip.community.platform.domain.board.BoardReaction;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardReactionPersistenceAdapter implements LoadBoardReactionPort, SaveBoardReactionPort, RemoveBoardReactionPort {

    private final LoadBoardPort loadBoardPort;
    private final RedisTemplate<String, Long> redisTemplate;

    ///  SavePort
    @Override
    public void saveLikeBoardReaction(Long boardId, Long userId) {

        // 게시글이 존재하지 않는다면, 좋아요수를 증가시키면 안된다.
        if (!loadBoardPort.existBoard(boardId)) {
            throw new EntityNotFoundException("해당하는 엔티티가 존재하지 않습니다.");
        }

        // key 값 가져오기
        String boardLikeKey = RedisKeyGenerator.getBoardLikeKey(boardId);
        System.out.println("Generated Redis Key: " + boardLikeKey);  // 로그 추가

        // RedisSet에 저장하여 중복을 방지한다.
        var setOps = redisTemplate.opsForSet();
        setOps.add(boardLikeKey, userId);
        System.out.println("Added userId " + userId + " to Redis set.");  // 로그 추가
    }

    @Override
    public void saveDisLikeBoardReaction(Long boardId, Long userId) {

        // 존재하지 않는다면, 좋아요수를 증가시키면 안된다.
        if (!loadBoardPort.existBoard(boardId)) {
            return;
        }

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
    @Override
    public void removeBoardLikeReaction(Long boardId, Long userId) {
        if (!loadBoardPort.existBoard(boardId)) {
            throw new EntityNotFoundException("해당하는 작성글이 존재하지 않습니다.");
        }

        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getBoardLikeKey(boardId), userId);
    }

    @Override
    public void removeBoardDisLikeReaction(Long boardId, Long userId) {
        var setOps = redisTemplate.opsForSet();
        setOps.remove(RedisKeyGenerator.getBoardDisLikeKey(boardId), userId);
    }


}
