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
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardPersistenceAdapter implements SaveBoardPort, LoadBoardPort, RemoveBoardPort {

    private final BoardJpaRepository repository;
    private final BoardRedisRepository redisRepository;

    private final RedisTemplate<Long, Long> redisTemplate;
    private final RedisTemplate<String, Long> stringRedisTemplate;

    /// 게시물 저장
    @Override
    public Board saveBoard(Board board) {
        var boardJpaEntity = BoardJpaEntity.from(board);
        Board domain = repository.save(boardJpaEntity).toDomain(); // JPA를 통해 ID가 포함되어있음

        return redisRepository.save(BoardRedisHash.from(domain)) // 해당하는 ID가 Redis에 들어가게 된다.
                .toDomain();
    }

    @Override
    public void saveTemporalBoard(Board board) {

        /*
            레디스를 활용하여 임시저장한다.
            memberId와 매핑을 하면 될 것 같다.
         */
    }

    /// 게시물 상세 조회
    @Override
    public Optional<Board> loadBoardById(Long boardId) {

         /*
            레디스에서 게시글을 먼저 가져오되, 없으면 JPA에서 가져오도록 한다.
         */

        return redisRepository.findById(boardId)
                // redis cache hit
                .map(BoardRedisHash::toDomain)
                .or(() -> { // redis cache miss, Redis 에 값을 저장한다.
                    var optionalBoard = repository.findById(boardId);
                    redisRepository.save(BoardRedisHash.from(optionalBoard.get().toDomain()));
                    return optionalBoard
                            .map(BoardJpaEntity::toDomain);
                });
    }

    /// 조회수 가져오기
    @Override
    public Long getViewCount(Long boardId) {
        var boardViewCountKey = RedisKeyGenerator.getBoardViewCountKey(boardId);
        var viewCont = redisTemplate.opsForValue().get(boardViewCountKey);
        return viewCont == null ? 0 : viewCont;
    }

    /// 조회수 증가시키기
    @Override
    public void incrementViewCount(Long boardId) {
        // key 값 가져오기
        var videoViewCountKey = RedisKeyGenerator.getBoardViewCountKey(boardId);

        stringRedisTemplate.opsForValue().increment(videoViewCountKey);
    }

    // Redis의 사라짐을 방지하여, 매핑해주는 역할을 수행한다.
    @Override
    public void syncViewCount(Long boardId) {
        repository.findById(boardId)
                .ifPresent(board -> {
                    Long viewCount = stringRedisTemplate.opsForValue().get(RedisKeyGenerator.getBoardViewCountKey(boardId));
                    board.updateViewCount(viewCount);

                    repository.save(board);
                    stringRedisTemplate.opsForSet().remove(RedisKeyGenerator.getBoardViewCountKey(boardId));
                });
    }



    ///  전체 최신 게시물 조회 (작성일자 기준 내림차순)
    @Override
    public Page<Board> loadBoards(Pageable pageable) {

        return repository.findAll(pageable)
                .map(BoardJpaEntity::toDomain);
    }

    ///  전체 인기 게시물 조회 (조회수 100 이상, 좋아요가 50개가 된 최신순)
    @Override
    public Page<Board> loadBoardsFavorite(Pageable pageable) {

        /*
            레디스에서 인기 게시물이 무엇인지 파악하고 가져온 후에
         */

        return null;
    }

    /// 카테고리별 게시물 조회
    @Override
    public Page<Board> loadByCategoryId(Long categoryId, Pageable pageable) {
        Page<BoardJpaEntity> result = repository.findBoardJpaEntitiesByCategoryId(categoryId, pageable);

        List<Board> boards = result.stream()
                .map(BoardJpaEntity::toDomain)
                .toList();

        return new PageImpl<>(boards, pageable, result.getTotalElements());
    }

    /// 게시물 수정


    /// 게시물 삭제
    @Override
    public void removeBoard(Long boardId) {
        /*
            해당 부분은 softDelete 진행한다.
        */
        repository.deleteById(boardId);
    }
}
