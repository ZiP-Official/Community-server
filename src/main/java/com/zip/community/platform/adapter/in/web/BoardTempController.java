package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.TempBoardRequest;
import com.zip.community.platform.adapter.in.web.dto.response.TempBoardDetailResponse;
import com.zip.community.platform.adapter.in.web.dto.response.TempBoardListResponse;
import com.zip.community.platform.application.port.in.board.TempBoardUseCase;
import com.zip.community.platform.domain.board.Board;
import jakarta.persistence.EntityNotFoundException;
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
    @GetMapping
    public ApiResponse<List<TempBoardListResponse>> getTempBoardsByUserId(@RequestParam("userId") Long userId) {
        List<Board> tempBoards = tempService.getTempBoards(userId);
        return ApiResponse.ok(TempBoardListResponse.from(tempBoards));
    }

    /// 특정 임시저장글 조회
    @GetMapping("/{userId}/{index}")
    public ApiResponse<TempBoardDetailResponse> getTempBoardByUserIdAndIndex(
            @PathVariable("userId") Long userId,
            @PathVariable("index") int index) {

        Board board = tempService.getTempBoard(userId, index)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 게시글이 존재하지 않습니다."));

        return ApiResponse.ok(TempBoardDetailResponse.from(board));
    }

    /// 특정 임시글 삭제
    @DeleteMapping("/{userId}/{index}")
    public ApiResponse<String> deleteTempBoardByUserIdAndIndex(
            @PathVariable("userId") Long userId,
            @PathVariable("index") int index) {

        tempService.deleteTempBoard(userId, index);

        return ApiResponse.ok("임시 저장글이 삭제되었습니다.");

    }

}


