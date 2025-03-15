package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.redis.board.temp.TempBoardRedisHash;
import com.zip.community.platform.adapter.out.redis.board.temp.TempBoardRedisRepository;
import com.zip.community.platform.application.port.out.board.TempBoardPort;
import com.zip.community.platform.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardTempPersistenceAdapter implements TempBoardPort {

    private final TempBoardRedisRepository redisRepository;

    /// Save 관련 구현체
    @Override
    public Board saveTempBoard(Board board) {
        TempBoardRedisHash hash = TempBoardRedisHash.from(board);

        // 레디스에 값 저장하기
        return redisRepository.save(hash)
                .toDomain();
    }

    // 임시 저장한 글의 작성자 조회
    @Override
    public Optional<Long> loadWriterIdByBoardId(Long boardId) {

        return redisRepository.findMemberIdById(boardId);
    }

    /// Load 관련 구현체
    // 임시 저장 게시글 상세 조회
    @Override
    public Optional<Board> getTempBoard(Long boardId) {

        // 리스트 조회
        return redisRepository.findById(boardId)
                .map(TempBoardRedisHash::toDomain);
    }

    @Override
    public List<Board> getTempBoards(Long userId) {

        // 리스트 조회 후 가변 리스트로 변환하여 정렬
        return redisRepository.findByMemberId(userId)
                .stream().map(TempBoardRedisHash::toDomain)
                .sorted(Comparator.comparing(Board::getUpdatedAt))
                .collect(Collectors.toList());
    }

    /// Delete 관련 구현체
    @Override
    public void deleteTempBoard(Long boardId) {

        redisRepository.deleteById(boardId);

    }
}
