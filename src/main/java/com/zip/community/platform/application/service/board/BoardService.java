package com.zip.community.platform.application.service.board;

import com.zip.community.platform.application.port.in.board.CreateBoardUseCase;
import com.zip.community.platform.application.port.in.board.GetBoardUseCase;
import com.zip.community.platform.application.port.in.board.RemoveBoardUseCase;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardRequest;
import com.zip.community.platform.application.port.out.board.*;
import com.zip.community.platform.application.port.out.user.LoadUserPort;
import com.zip.community.platform.domain.board.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService implements CreateBoardUseCase, GetBoardUseCase, RemoveBoardUseCase {

    private final SaveBoardPort saveBoardPort;
    private final LoadBoardPort loadBoardPort;

    private final LoadBoardReactionPort reactionPort;

    private final LoadUserPort loadUserPort;
    private final CategoryPort categoryPort;
    private final RemoveBoardPort removeBoardPort;

    /// CreateUseCase 구현체
    @Override
    public Board createBoard(BoardRequest request) {

        if (!loadUserPort.existsById(request.getMemberId())) {
            throw new NoSuchElementException("해당 ID의 멤버가 존재하지 않습니다: " + request.getMemberId());
        }

        // 게시물 생성
        BoardSnippet snippet = BoardSnippet.of(request.getTitle(), request.getContent(), "링크");

        BoardStatistics statistics = BoardStatistics.of();

        // 카테고리와 연결
        Category category = categoryPort.loadCategory(request.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다"));

        Board board = Board.of(request.getMemberId(),category.getId() ,snippet, statistics);
        return saveBoardPort.saveBoard(board);
    }


    /// LoadUseCase 구현체
    // 상세조회하는 유즈케이스 이다.
    @Override
    public Board getBoardById(Long boardId) {

        // 조회수를 증가시키는 로직
        saveBoardPort.incrementViewCount(boardId);

        // 조회수 조회하기
        Long viewCount = loadBoardPort.loadViewCount(boardId);

        // 좋아요 / 싫어요 조회하기
        long likeCount = reactionPort.loadBoardLikeCount(boardId);
        long disLikeCount = reactionPort.loadBoardDisLikeCount(boardId);

        // 리액션 값은 좋아요 - 싫어요
        long reaction = likeCount - disLikeCount;

        // Board를 조회
        Optional<Board> boardOptional = loadBoardPort.loadBoardById(boardId);

        // Board가 있으면 조회수를 변경하고 반환
        boardOptional
                .ifPresent(board -> {
                    board.getStatistics().changeViewCount(viewCount);
                    board.getStatistics().changeLikeCount(reaction);
        });

        // Board가 없으면 예외를 던짐
        return boardOptional.orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    @Override
    public Page<Board> getByCategoryId(Long categoryId, Pageable pageable) {
        return loadBoardPort.loadBoardsByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Board> getBoards(Pageable pageable) {
        return loadBoardPort.loadBoards(pageable);
    }

    @Override
    public Page<Board> getBoardsView(Pageable pageable) {
        return loadBoardPort.loadBoardsView(pageable);
    }

    /// RemoveUseCase 구현체
    @Override
    public void removeBoard(Long boardId) {
        removeBoardPort.removeBoard(boardId);
    }
}
