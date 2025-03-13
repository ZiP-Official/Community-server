package com.zip.community.platform.application.service.board;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.BoardErrorCode;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardRequest;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardUpdateRequest;
import com.zip.community.platform.application.port.in.board.CreateBoardUseCase;
import com.zip.community.platform.application.port.in.board.GetBoardUseCase;
import com.zip.community.platform.application.port.in.board.RemoveBoardUseCase;
import com.zip.community.platform.application.port.in.board.UpdateBoardUseCase;
import com.zip.community.platform.application.port.out.board.*;
import com.zip.community.platform.application.port.out.comment.LoadCommentPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentPort;
import com.zip.community.platform.application.port.out.comment.RemoveCommentReactionPort;
import com.zip.community.platform.application.port.out.member.MemberPort;
import com.zip.community.platform.domain.board.*;
import com.zip.community.platform.domain.comment.Comment;
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
public class BoardService implements CreateBoardUseCase, GetBoardUseCase, UpdateBoardUseCase, RemoveBoardUseCase {

    private final SaveBoardPort savePort;

    private final LoadBoardPort loadPort;
    private final LoadBoardReactionPort loadReactionPort;
    private final LoadCommentPort loadCommentPort;

    private final RemoveBoardPort removePort;
    private final RemoveBoardReactionPort removeReactionPort;
    private final RemoveCommentPort removeCommentPort;
    private final RemoveCommentReactionPort removeCommentReactionPort;

    /// 예외처리 관련 의존성
    private final MemberPort memberPort;
    private final CategoryPort categoryPort;
    private final SyncService syncService;


    /// CreateUseCase 구현체
    @Override
    public Board createBoard(BoardRequest request) {

        // 멤버 예외처리
        if (!memberPort.getCheckedExistUser(request.getMemberId())) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_USER);
        }

        // 카테고리 값 예외처리
        if (!categoryPort.getCheckedExistCategory(request.getCategoryId())) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_CATEGORY);
        }

        // 게시물 생성
        BoardSnippet snippet = BoardSnippet.of(request.getTitle(), request.getContent(), "링크");
        BoardStatistics statistics = BoardStatistics.of();
        Board board = Board.of(request.getMemberId(), request.getCategoryId(), snippet, statistics, request.isAnonymous());

        // 저장하기
        return savePort.saveBoard(board);
    }

    /// UpdateUseCase 구현체
    @Override
    @Transactional
    public Board updateBoard(BoardUpdateRequest request) {

        // 게시글 가져오기
        Board board = loadPort.loadBoardById((request.getBoardId()))
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_BOARD));

        // 유저가 존재하는지 확인하는 예외처리
        if (!memberPort.getCheckedExistUser(board.getMemberId())) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_USER);
        }

        if (!board.getMemberId().equals(request.getUserId())) {
            throw new CustomException(BoardErrorCode.BAD_REQUEST_UPDATE);
        }

        // 카테고리 값 예외처리
        if (!categoryPort.getCheckedExistCategory(request.getCategoryId())) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_CATEGORY);
        }

        // 인기글이라면 수정할 수 없다.

        // 기존값 DB에 저장하고, 캐시를 삭제한다.
        syncService.syncData(request.getBoardId());

        // 값 수정하기
        board.update(request);

        return savePort.updateBoard(board);
    }


    /// LoadUseCase 구현체
    // 상세조회하는 유즈케이스 이다.
    @Override
    public Board getBoardById(Long boardId) {

        // 예외처리
        if (!loadPort.existBoard(boardId)) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_BOARD);
        }


        // 조회수 증가
        savePort.incrementViewCount(boardId);

        // 조회수 /좋아요 / 싫어요 / 댓글 개수 조회
        long viewCount = loadPort.loadViewCount(boardId);
        long likeCount = loadReactionPort.loadBoardLikeCount(boardId) != null ? loadReactionPort.loadBoardLikeCount(boardId) : 0L;
        long disLikeCount = loadReactionPort.loadBoardDisLikeCount(boardId) != null ? loadReactionPort.loadBoardDisLikeCount(boardId) : 0L;
        long commentCount = loadCommentPort.loadCommentCount(boardId) != null ? loadCommentPort.loadCommentCount(boardId) : 0L;

        // Board를 조회
        Optional<Board> boardOptional = loadPort.loadBoardById(boardId);

        // Board가 있으면 조회수를 변경하고 반환
        boardOptional
                .ifPresent(board -> {
                    board.getStatistics().bindStatistics(viewCount, commentCount, likeCount, disLikeCount);
                });

        // 만약 해당 게시글이 인기 게시글의 조건에 충족하다면, 인기게시글로 만든다.
        long favoriteCondition = viewCount + likeCount + disLikeCount + commentCount;

        // 이미 존재한다면 추가할 필요없음
        if (favoriteCondition > 10 && !loadPort.checkBoardFavorite(boardId)) {
            savePort.saveBoardFavorite(boardId);
        }

        // Board가 없으면 예외를 던짐
        return boardOptional
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_BOARD));
    }


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
        Page<Board> boards = loadPort.loadBoardsFavorite(pageable);
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

        // 카테고리 예외처리
        if (!categoryPort.getCheckedExistCategory(categoryId)) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_CATEGORY);
        }
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
    @Transactional
    public void removeBoard(Long boardId) {

        // 게시글 관련 정보 삭제
        removePort.removeBoard(boardId);

        // 글에 해당하는 글 추천 관련 정보 삭제
        removeReactionPort.removeAllByBoardId(boardId);

        // 글에 해당하는 댓글 정보 삭제
        removeCommentPort.removeAllByBoardId(boardId);

        // 글에 해당하는 댓글 추천 관련 정보 삭제
        List<Comment> comments = loadCommentPort.loadCommentsByBoardId(boardId);
        comments.forEach(comment -> {
            removeCommentReactionPort.removeAllByCommentId(comment.getId());
        });
    }


    /// 내부 함수
    /// 값 업데이트 시키는 함수
    private void updateBoardStatistics(List<Board> boards) {
        boards.forEach(board -> {

            long viewCount = loadPort.loadViewCount(board.getId());
            long likeCount = loadReactionPort.loadBoardLikeCount(board.getId()) != null ? loadReactionPort.loadBoardLikeCount(board.getId()) : 0L;
            long disLikeCount = loadReactionPort.loadBoardDisLikeCount(board.getId()) != null ? loadReactionPort.loadBoardDisLikeCount(board.getId()) : 0L;
            long commentCount = loadCommentPort.loadCommentCount(board.getId()) != null ? loadCommentPort.loadCommentCount(board.getId()) : 0L;

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
