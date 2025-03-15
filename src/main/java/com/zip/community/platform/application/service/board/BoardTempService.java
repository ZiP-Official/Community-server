package com.zip.community.platform.application.service.board;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.BoardErrorCode;
import com.zip.community.platform.adapter.in.web.dto.request.board.TempBoardRequest;
import com.zip.community.platform.application.port.in.board.TempBoardUseCase;
import com.zip.community.platform.application.port.out.board.CategoryPort;
import com.zip.community.platform.application.port.out.board.TempBoardPort;
import com.zip.community.platform.application.port.out.member.MemberPort;
import com.zip.community.platform.domain.board.Board;
import com.zip.community.platform.domain.board.BoardSnippet;
import com.zip.community.platform.domain.board.BoardStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.testcontainers.shaded.org.apache.commons.lang3.StringUtils.isAllEmpty;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardTempService implements TempBoardUseCase {

    private final TempBoardPort tempPort;

    // 의존성
    private final MemberPort memberPort;
    private final CategoryPort categoryPort;

    /// 저장 관련 구현체
    @Override
    public void createTempBoard(TempBoardRequest request) {

        // 유저 예외처리
        if (!memberPort.getCheckedExistUser(request.getMemberId())) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_USER);
        }

        // 카테고리 예외처리
        if (!categoryPort.getCheckedExistCategory(request.getCategoryId())) {
            throw new CustomException(BoardErrorCode.NOT_FOUND_CATEGORY);
        }

        // 제목, 내용, 썸네일 URL이 모두 비어있으면 예외 처리
        if (isAllEmpty(request.getTitle(), request.getContent(), request.getThumbnailUrl())) {
            throw new CustomException(BoardErrorCode.BAD_REQUEST, "제목, 내용, 썸네일 URL이 모두 비어있습니다. 최소 하나는 채워야 합니다.");
        }

        // 정보
        BoardSnippet snippet = BoardSnippet.of(request.getTitle(), request.getContent(), request.getThumbnailUrl());


        // 통계
        BoardStatistics statistics = BoardStatistics.of();

        // 게시글
        Board board = Board.of(request.getMemberId(), request.getCategoryId(), snippet, statistics, request.isAnonymous());

        tempPort.saveTempBoard(board);
    }

    /// 조회 관련 구현체
    @Override
    public List<Board> getTempBoards(Long userId) {
        return tempPort.getTempBoards(userId);
    }

    /// 삭제 관련 구현체
    @Override
    public Optional<Board> getTempBoard(Long boardId) {

        return tempPort.getTempBoard(boardId);
    }

    @Override
    public void deleteTempBoard(Long boardId, Long userId) {

        if (!tempPort.loadWriterIdByBoardId(boardId).equals(userId)) {
            throw new CustomException(BoardErrorCode.BAD_REMOVE_BOARD);
        }

        tempPort.deleteTempBoard(boardId);

    }

}
