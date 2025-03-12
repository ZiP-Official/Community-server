package com.zip.community.platform.adapter.out;

import com.zip.community.common.util.RedisKeyGenerator;
import com.zip.community.platform.adapter.out.jpa.board.BoardFavoriteJpaEntity;
import com.zip.community.platform.adapter.out.jpa.board.BoardJpaEntity;
import com.zip.community.platform.adapter.out.jpa.board.repository.BoardFavoriteJpaRepository;
import com.zip.community.platform.adapter.out.jpa.board.repository.BoardJpaRepository;
import com.zip.community.platform.adapter.out.redis.board.BoardRedisHash;
import com.zip.community.platform.adapter.out.redis.board.BoardRedisRepository;
import com.zip.community.platform.application.port.out.board.LoadBoardPort;
import com.zip.community.platform.application.port.out.board.RemoveBoardPort;
import com.zip.community.platform.application.port.out.board.SaveBoardPort;
import com.zip.community.platform.domain.board.Board;
import com.zip.community.platform.domain.board.BoardFavorite;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardPersistenceAdapter implements SaveBoardPort, LoadBoardPort, RemoveBoardPort {

    private final BoardJpaRepository repository;

    private final BoardRedisRepository redisRepository;

    private final RedisTemplate<String, Long> redisTemplate;

    /// 외부 의존성 존재
    private final BoardFavoriteJpaRepository favoriteRepository;


    /// SavePort 구현체
    @Override
    public Board saveBoard(Board board) {
        var boardJpaEntity = BoardJpaEntity.from(board);
        return repository.save(boardJpaEntity)
                .toDomain();
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
    public void syncData(Long boardId) {
        repository.findById(boardId)
                .ifPresent(board -> {
                    Long viewCount = redisTemplate.opsForValue().get(RedisKeyGenerator.getBoardViewCountKey(boardId));
                    Long likeCount = redisTemplate.opsForValue().get(RedisKeyGenerator.getBoardLikeKey(boardId));
                    Long diskLikeCount = redisTemplate.opsForValue().get(RedisKeyGenerator.getBoardDisLikeKey(boardId));

                    board.updateBoardStatics(viewCount,likeCount,diskLikeCount);

                    // 해당 값을 db에 저장한다.
                    repository.save(board);

                    redisTemplate.opsForSet().remove(RedisKeyGenerator.getBoardViewCountKey(boardId));
                    redisTemplate.opsForSet().remove(RedisKeyGenerator.getBoardLikeKey(boardId));
                    redisTemplate.opsForSet().remove(RedisKeyGenerator.getBoardDisLikeKey(boardId));
                });
    }

    // 게시글을 레디스의 인기게시물 목록에 저장하기 & Entity 저장하기
    @Override
    public void saveBoardFavorite(Long boardId) {

        repository.findById(boardId)
                .map(BoardJpaEntity::toDomain)
                .ifPresent(board -> {

                    /// 레디스에 저장한다.
                    // 해당 글은 더 이상 수정할 수 없다.
                    BoardRedisHash hash = BoardRedisHash.from(board);
                    redisRepository.save(hash);

                    /// JpaRepository 에 저장한다.
                    favoriteRepository.save(BoardFavoriteJpaEntity.from(BoardFavorite.of(boardId)));
                });

        // RedisKey를 불러온다.
        String boardList = RedisKeyGenerator.getBoardList();

        // 현재 시간을 밀리초로 가져옴
        long currentTimeMillis = Instant.now().toEpochMilli();

        // 시간과 함께 저장한다.
        redisTemplate.opsForZSet().add(boardList, boardId, currentTimeMillis);
    }

    

    /// LoadPort 구현체
    @Override
    public boolean existBoard(Long boardId) {

        return repository.existsById(boardId);
    }

    @Override
    public boolean checkBoardFavorite(Long boardId) {
        // Redis에서 해당 boardId가 존재하는지 확인
        String boardList = RedisKeyGenerator.getBoardList();
        Double score = redisTemplate.opsForZSet().score(boardList, boardId);

        // DB에서 존재하는지 확인
        boolean existsInDatabase = favoriteRepository.existsByBoardId(boardId);

        // Redis 또는 DB 중 하나라도 존재하면 true 반환
        return (score != null) || existsInDatabase;
    }

    // 게시물 상세 조회
    @Override
    public Optional<Board> loadBoardById(Long boardId) {
        // 레디스에서 게시글을 먼저 가져오되, 없으면 JPA에서 가져오도록 한다.
        return redisRepository.findById(boardId)
                // redis cache hit
                .map(BoardRedisHash::toDomain)
                .or(() -> {
                    // redis cache miss, JPA에서 값을 가져온다.
                    var optionalBoard = repository.findById(boardId);

                    return optionalBoard
                            .map(BoardJpaEntity::toDomain);
                });
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

    // 인기게시글 조회하기,
    @Override
    public Page<Board> loadBoardsFavorite(Pageable pageable) {

        /// 레디스에서 조회하기
        String boardList = RedisKeyGenerator.getBoardList();

        // 해당 페이지에 맞는 게시글 ID들 가져오기 (ZSet은 순서가 정해져 있으므로, 스코어를 기준으로 범위 가져옴)
        var opsZet = redisTemplate.opsForZSet();

        // 최신 등록 시간 기준으로 정렬하여 가져오기 (게시글 ID와 score(등록 시간) 함께 조회)
        Set<ZSetOperations.TypedTuple<Long>> boardTuples = opsZet.reverseRangeWithScores(boardList, pageable.getOffset(), pageable.getOffset() + pageable.getPageSize() - 1);

        // GPT 도움
        List<Board> boards = boardTuples.stream()
                .map(tuple -> {
                    Optional<Board> boardOpt = loadBoardById(tuple.getValue());
                    if (boardOpt.isPresent()) {
                        Board board = boardOpt.get();

                        // score 값을 밀리초에서 초 단위로 변환
                        long epochMillis = tuple.getScore().longValue();
                        long epochSeconds = epochMillis / 1000;  // 밀리초를 초로 변환

                        // UTC 기준 시간에서 한국 시간(KST)으로 변환
                        LocalDateTime createdAt = Instant.ofEpochSecond(epochSeconds)
                                .atZone(ZoneId.of("Asia/Seoul")) // KST 변환
                                .toLocalDateTime();

                        board.setCreatedAt(createdAt);
                        return board;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        /// 레디스가 비어있다면 DB에서 조회하기
        /*
            인기글 목록 DB 내에서 글 목록을 가져오고
            해당하는 글에 대해서 loadById를 통해 글을 읽는다.
         */

        // 게시글이 없으면 빈 페이지 반환
        if (boardTuples.isEmpty()) {
            System.out.println("인기게시글 조회가 DB에서 실행되었습니다");

            // Redis가 비어있으면 DB에서 조회하기
            Page<BoardFavorite> favoritePage = favoriteRepository.findAllByOrderByCreatedAtDesc(pageable)
                    .map(BoardFavoriteJpaEntity::toDomain);

            // BoardFavorite 목록에서 Board 조회
            List<Board> favoriteBoards = favoritePage.getContent().stream()
                    .map(fav -> loadBoardById(fav.getBoardId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            return new PageImpl<>(favoriteBoards, pageable, favoritePage.getTotalElements());
        }
        System.out.println("인기게시글 조회가 레디스에서 실행되었습니다");
        return new PageImpl<>(boards, pageable, boardTuples.size());
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
    public Page<Board> loadBoardsByCategoryIdFavorite(Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Long> loadWriterIdByBoardId(Long boardId) {
        return repository.getMemberIdByBoardId(boardId);
    }

    // 게시물 수정


    /// DeletePort 구현체
    @Override
    public void removeBoard(Long boardId) {


        /// 레디스 삭제
        // 레디스에서 삭제한다.
        repository.findById(boardId)
                        .ifPresent(board -> {
                            BoardRedisHash redisHash = BoardRedisHash.from(board.toDomain());
                            redisRepository.delete(redisHash);
                        });

        // 인기게시글 목록에서도 삭제
        String boardList = RedisKeyGenerator.getBoardList();
        redisTemplate.opsForZSet().remove(boardList, boardId);

        // 해당 부분은 softDelete 진행한다.
        repository.deleteById(boardId);
    }
}
