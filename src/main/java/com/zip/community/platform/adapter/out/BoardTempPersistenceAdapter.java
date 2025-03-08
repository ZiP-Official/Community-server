package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.redis.board.BoardRedisHash;
import com.zip.community.platform.adapter.out.redis.board.BoardRedisRepository;
import com.zip.community.platform.application.port.out.board.TempBoardPort;
import com.zip.community.platform.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BoardTempPersistenceAdapter implements TempBoardPort {

    private final BoardRedisRepository redisRepository;

    /// Save 관련 구현체
    @Override
    public Board saveTempBoard(Board board) {
        BoardRedisHash hash = BoardRedisHash.from(board);

        // 레디스에 값 저장하기
        return redisRepository.save(hash)
                .toDomain();
    }

    /// Load 관련 구현체
    // 임시 저장 게시글 상세 조회
    @Override
    public Optional<Board> getTempBoard(Long userId, int index) {

        // 리스트 조회
        List<BoardRedisHash> list = redisRepository.findByMemberId(userId);

        // 인덱스를 통해서 특정 글 가져오기
        BoardRedisHash redisHash = list.get(index - 1);

        return Optional.of(redisHash.toDomain());
    }

    @Override
    public List<Board> getTempBoards(Long userId) {

        return redisRepository.findByMemberId(userId).stream()
                .map(BoardRedisHash::toDomain)
                .toList();
    }

    /// Delete 관련 구현체
    @Override
    public void deleteTempBoard(Long userId, int index) {

        // 리스트 조회
        List<BoardRedisHash> list = redisRepository.findByMemberId(userId);

        // 인덱스를 통해서 특정 글 가져오기
        BoardRedisHash redisHash = list.get(index - 1);

        redisRepository.delete(redisHash);

    }
}
