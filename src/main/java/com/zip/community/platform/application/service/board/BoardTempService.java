package com.zip.community.platform.application.service.board;

import com.zip.community.platform.adapter.in.web.dto.request.board.TempBoardRequest;
import com.zip.community.platform.application.port.in.board.TempBoardUseCase;
import com.zip.community.platform.application.port.out.board.CategoryPort;
import com.zip.community.platform.application.port.out.board.TempBoardPort;
import com.zip.community.platform.application.port.out.user.LoadUserPort;
import com.zip.community.platform.domain.board.Board;
import com.zip.community.platform.domain.board.BoardSnippet;
import com.zip.community.platform.domain.board.BoardStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.testcontainers.shaded.org.apache.commons.lang3.StringUtils.isAllEmpty;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardTempService implements TempBoardUseCase {

    private final TempBoardPort tempPort;

    // 의존성
    private final LoadUserPort loadUserPort;
    private final CategoryPort categoryPort;

    /// 저장 관련 구현체
    @Override
    public void createTempBoard(TempBoardRequest request) {

        // 유저 예외처리
        if (!loadUserPort.getCheckedExistUser(request.getMemberId())) {
            throw new NoSuchElementException("해당 ID의 멤버가 존재하지 않습니다: " + request.getMemberId());
        }

        // 카테고리 예외처리
        if (!categoryPort.getCheckedExistCategory(request.getCategoryId())) {
            throw new NoSuchElementException("해당 ID의 멤버가 존재하지 않습니다: " + request.getMemberId());
        }

        // 제목, 내용, 썸네일 URL이 모두 비어있으면 예외 처리
        if (isAllEmpty(request.getTitle(), request.getContent(), request.getThumbnailUrl())) {
            throw new IllegalArgumentException("제목, 내용, 썸네일 URL이 모두 비어있습니다. 최소 하나는 채워야 합니다.");
        }

        // 정보
        BoardSnippet snippet = BoardSnippet.of(request.getTitle(), request.getContent(), request.getThumbnailUrl());


        // 통계
        BoardStatistics statistics = BoardStatistics.of();

        // 게시글
        Board board = Board.of(request.getMemberId(), request.getCategoryId(), snippet, statistics);

        tempPort.saveTempBoard(board);
    }

    /// 조회 관련 구현체
    @Override
    public List<Board> getTempBoards(Long userId) {
        return tempPort.getTempBoards(userId);
    }

    /// 삭제 관련 구현체
    @Override
    public Optional<Board> getTempBoard(Long boardId, int index) {

        return tempPort.getTempBoard(boardId, index);
    }

    @Override
    public void deleteTempBoard(Long boardId, int index) {
        tempPort.deleteTempBoard(boardId, index);
    }

}
