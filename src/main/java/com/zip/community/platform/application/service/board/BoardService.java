package com.zip.community.platform.application.service.board;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.BoardErrorCode;
import com.zip.community.platform.application.port.in.board.CreateBoardUseCase;
import com.zip.community.platform.application.port.in.board.GetBoardUseCase;
import com.zip.community.platform.application.port.in.board.RemoveBoardUseCase;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardRequest;
import com.zip.community.platform.application.port.out.board.*;
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentPort;
import com.zip.community.platform.application.port.out.member.MemberPort;
import com.zip.community.platform.domain.board.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService implements CreateBoardUseCase, GetBoardUseCase, RemoveBoardUseCase {

    private final SaveBoardPort savePort;
    private final LoadBoardPort loadPort;
    private final RemoveBoardPort removePort;

    private final MemberPort memberPort;
    private final CategoryPort categoryPort;

    /// 레디스 관련 의존성
    private final LoadBoardReactionPort loadReactionPort;
    private final RemoveBoardReactionPort removeReactionPort;
    private final LoadCommentPort loadCommentPort;
    private final RemoveCommentPort removeCommentPort;

    /// CreateUseCase 구현체
    @Override
    public Board createBoard(BoardRequest request) {

        if (!memberPort.getCheckedExistUser(request.getMemberId())) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_USER);
        }

        // 게시물 생성
        BoardSnippet snippet = BoardSnippet.of(request.getTitle(), request.getContent(), "링크");

        BoardStatistics statistics = BoardStatistics.of();

        // 카테고리와 연결
        Category category = categoryPort.loadCategory(request.getCategoryId())
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_CATEGORY));

        Board board = Board.of(request.getMemberId(),category.getId() ,snippet, statistics);
        return savePort.saveBoard(board);
    }


    /// LoadUseCase 구현체
    // 상세조회하는 유즈케이스 이다.
    @Override
    public Board getBoardById(Long boardId) {

        // 조회수를 증가시키는 로직
        savePort.incrementViewCount(boardId);

        // 조회수 /좋아요 / 싫어요 / 댓글 개수 조회하기
        Long viewCount = loadPort.loadViewCount(boardId);
        Long likeCount = loadReactionPort.loadBoardLikeCount(boardId);
        Long disLikeCount = loadReactionPort.loadBoardDisLikeCount(boardId);
        Long commentCount = loadCommentPort.loadCommentCount(boardId);

        // Board를 조회
        Optional<Board> boardOptional = loadPort.loadBoardById(boardId);

        // Board가 있으면 조회수를 변경하고 반환
        boardOptional
                .ifPresent(board -> {
                    board.getStatistics().bindStatistics(viewCount, commentCount, likeCount, disLikeCount);
        });

        // Board가 없으면 예외를 던짐
        return boardOptional
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_BOARD));}


    // 최신 목록글 조회
    @Override
    public Page<Board> getBoards(Pageable pageable) {

        Page<Board> boards = loadPort.loadBoards(pageable);
        List<Board> list = boards.getContent();

        // 내부함수 적용
        updateBoardStatistics(list);

        return new PageImpl<>(list, pageable, boards.getTotalElements());

    }

    // 인기 목록글 조회
    @Override
    public Page<Board> getBoardsFavorite(Pageable pageable) {
        Page<Board> boards = loadPort.loadBoardsView(pageable);
        List<Board> list = boards.getContent();

        // 내부함수 적용
        updateBoardStatistics(list);

        return new PageImpl<>(list, pageable, boards.getTotalElements());
    }

    // 카테고리 기반 목록 조회
    // GPT 도움
    @Override
    public Page<Board> getBoardsByCategoryId(Long categoryId, Pageable pageable) {
        /*
            카테고리의 하위 결과까지 가져와야한다.
         */

        // 카테고리 id와 children 카테고리 목록 가져오기
        List<Long> categoryIds = getLongs(categoryId);

        // 결과값 가져오기
        Page<Board> boards = loadPort.loadBoardsByCategories(categoryIds, pageable);

        List<Board> list = boards.getContent();

        // 내부함수 적용
        updateBoardStatistics(list);

        return new PageImpl<>(list, pageable, boards.getTotalElements());
    }

    /// RemoveUseCase 구현체
    @Override
    public void removeBoard(Long boardId) {
        // 게시글 관련 레디스 + DB 삭제
        removePort.removeBoard(boardId);

        // 추천 관련 레디스 정보 삭제
        removeReactionPort.removeAllByBoardId(boardId);

        // 댓글 관련 레디스 정보 삭제
        removeCommentPort.removeCommentByBoardId(boardId);
    }


    /// 내부 함수
    /// 값 업데이트 시키는 함수
    private void updateBoardStatistics(List<Board> boards) {
        boards.forEach(board -> {

            Long viewCount = loadPort.loadViewCount(board.getId());
            Long likeCount = loadReactionPort.loadBoardLikeCount(board.getId());
            Long disLikeCount = loadReactionPort.loadBoardDisLikeCount(board.getId());
            Long commentCount = loadCommentPort.loadCommentCount(board.getId());

            board.getStatistics().bindStatistics(viewCount, commentCount, likeCount, disLikeCount);
        });
    }

    ///  내부 함수
    private @NotNull List<Long> getLongs(Long categoryId) {
        Category category = categoryPort.loadCategory(categoryId)
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_CATEGORY));

        List<Category> loadChildrenByParentId = categoryPort.loadChildrenByParentId(categoryId);

        // 수정 가능한 리스트로 변환
        List<Category> categories = new ArrayList<>(loadChildrenByParentId);
        categories.add(category);  // 상위 카테고리도 추가

        // Category에서 id를 추출하여 List<Long>으로 수정
        List<Long> categoryIds = categories.stream()
                .map(Category::getId)  // Category 객체에서 id 추출
                .collect(Collectors.toList());

        return categoryIds;
    }
}
