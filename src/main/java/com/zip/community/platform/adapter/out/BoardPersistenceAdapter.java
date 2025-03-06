package com.zip.community.platform.adapter.out;

import com.zip.community.common.util.RedisKeyGenerator;
import com.zip.community.platform.adapter.out.jpa.board.BoardJpaEntity;
import com.zip.community.platform.adapter.out.jpa.board.repository.BoardJpaRepository;
import com.zip.community.platform.adapter.out.redis.board.BoardRedisHash;
import com.zip.community.platform.adapter.out.redis.board.BoardRedisRepository;
import com.zip.community.platform.application.port.out.board.LoadBoardPort;
import com.zip.community.platform.application.port.out.board.RemoveBoardPort;
import com.zip.community.platform.application.port.out.board.SaveBoardPort;
import com.zip.community.platform.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardPersistenceAdapter implements SaveBoardPort, LoadBoardPort, RemoveBoardPort {

    private final BoardJpaRepository repository;
    private final BoardRedisRepository redisRepository;

    private final RedisTemplate<String, Long> redisTemplate;

    /// SavePort 구현체
    @Override
    public Board saveBoard(Board board) {
        var boardJpaEntity = BoardJpaEntity.from(board);
        return repository.save(boardJpaEntity)
                .toDomain();
    }

    @Override
    public void saveTemporalBoard(Board board) {

        /*
            레디스를 활용하여 임시저장한다.
            memberId와 매핑을 하면 될 것 같다.
         */
    }

    // 조회수 증가시키기
    @Override
    public void incrementViewCount(Long boardId) {

        // 존재하지 않는다면, 조회수를 증가시키면 안된다.
        if (!repository.existsById(boardId)) {
            return;
        }

        // key 값 가져오기
        var boardViewCountKey = RedisKeyGenerator.getBoardViewCountKey(boardId);

        redisTemplate.opsForValue().increment(boardViewCountKey);
    }

    // Redis의 사라짐을 방지하여, 매핑해주는 역할을 수행한다.
    @Override
    public void syncViewCount(Long boardId) {
        repository.findById(boardId)
                .ifPresent(board -> {
                    Long viewCount = redisTemplate.opsForValue().get(RedisKeyGenerator.getBoardViewCountKey(boardId));
                    board.updateViewCount(viewCount);

                    repository.save(board);
                    redisTemplate.opsForSet().remove(RedisKeyGenerator.getBoardViewCountKey(boardId));
                });
    }

    /// LoadPort 구현체
    @Override
    public boolean existBoard(Long boardId) {

        return repository.existsById(boardId);
    }

    // 게시물 상세 조회
    @Override
    public Optional<Board> loadBoardById(Long boardId) {

         /*
            레디스에서 게시글을 먼저 가져오되, 없으면 JPA에서 가져오도록 한다.
            초기 MVP에서는 조회수 10을 인기게시물로 정의한다.
            인기게시물을 레디스에 저장하도록 한다.
         */

        return redisRepository.findById(boardId)
                // redis cache hit
                .map(BoardRedisHash::toDomain)
                .or(() -> { // redis cache miss, Redis 에 값을 저장한다.
                    var optionalBoard = repository.findById(boardId);

                    // 조회수 가져오기
                    Long viewCount = loadViewCount(boardId);

                    // 인기게시물일 경우, 성능을 위해서 레디스에 저장
                    if (viewCount >= 10) {
                        redisRepository.save(BoardRedisHash.from(optionalBoard.get().toDomain()));
                        saveBoardFavorite(boardId, viewCount);
                    }

                    return optionalBoard
                            .map(BoardJpaEntity::toDomain);
                });
    }

    // 인기게시물 목록 저장하기
    private void saveBoardFavorite(Long boardId, Long viewCount) {

        // 아래에서 인기게시물의 조건을 설정한다.
        long score = viewCount;

        // Sorted Set 추가
        String countSetKey = RedisKeyGenerator.getBoardViewCountSetKey();
        redisTemplate.opsForZSet().add(countSetKey, boardId, score);

    }

    // 조회수 가져오기
    @Override
    public Long loadViewCount(Long boardId) {
        var boardViewCountKey = RedisKeyGenerator.getBoardViewCountKey(boardId);
        var viewCont = redisTemplate.opsForValue().get(boardViewCountKey);
        return viewCont == null ? 0 : viewCont;
    }

    //  전체 최신 게시물 조회 (작성일자 기준 내림차순)
    @Override
    public Page<Board> loadBoards(Pageable pageable) {

        return repository.findBoardsByRecent(pageable)
                .map(BoardJpaEntity::toDomain);
    }

    // 전체 인기 게시물 조회 (조회수 10 이상)
    // GPT 바탕의 코드
    @Override
    public Page<Board> loadBoardsView(Pageable pageable) {

        String countSetKey = RedisKeyGenerator.getBoardViewCountSetKey();

        // ZSET에서 인기 게시물 조회 (내림차순으로 정렬)
        Set<ZSetOperations.TypedTuple<Long>> range = redisTemplate.opsForZSet()
                .reverseRangeWithScores(countSetKey, pageable.getOffset(), pageable.getOffset() + pageable.getPageSize() - 1);

        // Redis에서 게시물 ID로 Board 객체를 가져오는 로직
        List<Board> boards = range.stream()
                .map(tuple -> {

                    // 게시물 ID
                    Long boardId = tuple.getValue();

                    // Board 객체를 Redis를 통해서 조회한다.
                    Board board = loadBoardById(boardId).get();

                    // Board의 조회수를 Redis를 통해서 조회한다.
                    Long viewCount = loadViewCount(boardId);

                    board.getStatistics().changeViewCount(viewCount);

                    return board;
                })
                .collect(Collectors.toList());

        // 전체 ZSET 크기 (전체 인기 게시물 개수)
        long totalCount = redisTemplate.opsForZSet().size(countSetKey);

        // PageImpl로 페이지 처리된 결과 반환
        return new PageImpl<>(boards, pageable, totalCount);
    }

    @Override
    public Page<Board> loadBoardsLike(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Board> loadBoardsFavorite(Pageable pageable) {
        return null;
    }


    // 카테고리별 게시물 조회
    @Override
    public Page<Board> loadBoardsByCategoryId(Long categoryId, Pageable pageable) {
        return repository.findBoardJpaEntitiesByCategoryId(categoryId, pageable)
                .map(BoardJpaEntity::toDomain);
    }

    @Override
    public Page<Board> loadBoardsByCategories(List<Long> categories, Pageable pageable) {
        return repository.findBoardByCategories(categories, pageable)
                .map(BoardJpaEntity::toDomain);
    }

    @Override
    public Page<Board> loadBoardsByCategoryIdView(Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Board> loadBoardsByCategoryIdLike(Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Board> loadBoardsByCategoryIdFavorite(Long categoryId, Pageable pageable) {
        return null;
    }

    // 게시물 수정


    /// DeletePort 구현체
    @Override
    public void removeBoard(Long boardId) {
        /*
            해당 부분은 softDelete 진행한다.
        */
        repository.deleteById(boardId);
    }
}
