package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.BoardErrorCode;
import com.zip.community.platform.adapter.in.web.dto.request.board.TempBoardRequest;
import com.zip.community.platform.adapter.in.web.dto.request.deleteRequest;
import com.zip.community.platform.adapter.in.web.dto.response.board.TempBoardDetailResponse;
import com.zip.community.platform.adapter.in.web.dto.response.board.TempBoardListResponse;
import com.zip.community.platform.application.port.in.board.TempBoardUseCase;
import com.zip.community.platform.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board/temp")
@RequiredArgsConstructor
public class BoardTempController {

    private final TempBoardUseCase tempService;

    /// 저장
    @PostMapping
    public ApiResponse<String> saveTempBoard(@RequestBody TempBoardRequest request) {
        tempService.createTempBoard(request);
        return ApiResponse.created("임시저장 되었습니다.");
    }

    /// 전체 조회
    @GetMapping("/list")
    public ApiResponse<List<TempBoardListResponse>> getTempBoardsByUserId(@RequestParam("userId") Long userId) {
        List<Board> tempBoards = tempService.getTempBoards(userId);
        return ApiResponse.ok(TempBoardListResponse.from(tempBoards));
    }

    /// 특정 임시저장글 조회
    @GetMapping("/{boardId}")
    public ApiResponse<TempBoardDetailResponse> getTempBoardByUserIdAndIndex(
            @PathVariable("boardId") Long boardId) {

        Board board = tempService.getTempBoard(boardId)
                .orElseThrow(() -> new CustomException(BoardErrorCode.NOT_FOUND_BOARD));

        return ApiResponse.ok(TempBoardDetailResponse.from(board));
    }

    /// 특정 임시글 삭제
    @DeleteMapping("/{boardId}")
    public ApiResponse<String> deleteTempBoardByUserIdAndIndex(@PathVariable("boardId") Long boardId, @RequestBody deleteRequest request) {

        tempService.deleteTempBoard(boardId, request.getUserId());

        return ApiResponse.ok("임시 저장글이 삭제되었습니다.");

    }
}


