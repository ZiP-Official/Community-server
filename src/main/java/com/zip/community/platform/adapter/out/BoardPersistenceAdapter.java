package com.zip.community.platform.adapter.out;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.BoardErrorCode;
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
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
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
    private final LoadCommentPort loadCommentPort;

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

        /// REDIS가 없다면 repository에서 viewCount값을 받고, 그 값에 추가하는 형식
        // key 값 가져오기
        var boardViewCountKey = RedisKeyGenerator.getBoardViewCountKey(boardId);

        if (redisTemplate.hasKey(boardViewCountKey)) {
            redisTemplate.opsForValue().increment(boardViewCountKey);

        } else {
            repository.findById(boardId)
                    .ifPresent(board -> {
                        redisTemplate.opsForValue().set(boardViewCountKey, board.getBoardStatistics().getViewCount());
                        redisTemplate.opsForValue().increment(boardViewCountKey);
                    });
        }
    }

    // Redis의 매핑이 사라져도 JPA에 그 값들을 저장하는 로직
    @Override
    public void syncData(Long boardId, long viewCount, long likeCount, long dislikeCount, long commentCount) {

        // JPA에 업데이트하는 로직 구현
        repository.findById(boardId).ifPresent(board -> {
            board.updateBoardStatics(viewCount, likeCount, dislikeCount, commentCount);
            repository.save(board);
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

    // 업데이트 하기
    @Override
    public Board updateBoard(Board board) {

        /// 기존 board의 id를 가져와서 값을 업데이트 하는 것
        /// 게시글 업데이트 하는 로직
        /// 통계적인 부분은 이미 Service 계층에서 이전이 된 상태이다.

        BoardJpaEntity entity = repository.findById(board.getId())
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_BOARD));

        entity.updateBoardSnippet(board.getSnippet());


        // 새롭게 db에 업데이트 한다.
        return repository.save(entity)
                .toDomain();
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

    @Override
    public Long loadViewCount(Long boardId) {
        var boardViewCountKey = RedisKeyGenerator.getBoardViewCountKey(boardId);

        // Redis에서 조회
        Long viewCount = redisTemplate.opsForValue().get(boardViewCountKey);

        // Redis에 데이터가 없거나 값이 0이면 DB에서 조회
        if (viewCount == null || viewCount == 0) {
            var boardJpaEntityOptional = repository.findById(boardId);

            // DB에서 조회 후 Redis에 캐싱
            if (boardJpaEntityOptional.isPresent()) {
                long dbView = boardJpaEntityOptional.get().getBoardStatistics().getViewCount();
                viewCount = dbView;

                // Redis에 캐시 저장
                redisTemplate.opsForValue().set(boardViewCountKey, viewCount);
            } else {
                // Board가 없을 경우 기본값 0 설정
                viewCount = 0L;
            }
        }

        return viewCount;
    }


    //  전체 최신 게시물 조회 (작성일자 기준 내림차순)
    @Override
    public Page<Board> loadBoards(Pageable pageable) {

        return repository.findBoardsByRecent(pageable)
                .map(BoardJpaEntity::toDomain);
    }

    // 인기게시글 조회하기
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
        removeCache(boardId);

        // 인기게시글 목록에서도 삭제
        String boardList = RedisKeyGenerator.getBoardList();
        redisTemplate.opsForZSet().remove(boardList, boardId);

        // 해당 부분은 softDelete 진행한다.
        removeEntity(boardId);
    }

    @Override
    public void removeEntity(Long boardId) {

        // 게시글 관련하여 영속성 삭제를 진행한다.
        // softDelete, Batch 통해 삭제할 예정이다.
        repository.deleteById(boardId);
    }

    @Override
    public void removeCache(Long boardId) {

        String boardViewCountKey = RedisKeyGenerator.getBoardViewCountKey(boardId);

        // 레디스에서 삭제한다.
        repository.findById(boardId)
                .ifPresent(board -> {
                    BoardRedisHash redisHash = BoardRedisHash.from(board.toDomain());
                    redisRepository.delete(redisHash);
                    redisTemplate.delete(boardViewCountKey);
                });

    }


}
