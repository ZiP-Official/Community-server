package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.pageable.PageRequest;
import com.zip.community.common.response.pageable.PageResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardRequest;
import com.zip.community.platform.adapter.in.web.dto.response.BoardDetailResponse;
import com.zip.community.platform.application.port.in.board.CreateBoardUseCase;
import com.zip.community.platform.application.port.in.board.GetBoardUseCase;
import com.zip.community.platform.application.port.in.board.RemoveBoardUseCase;
import com.zip.community.platform.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final CreateBoardUseCase createService;
    private final GetBoardUseCase getService;
    private final RemoveBoardUseCase removeService;

    // 게시글 생성
    @PostMapping
    public ApiResponse<BoardDetailResponse> saveOne(@RequestBody BoardRequest boardRequest) {
        return ApiResponse.created(BoardDetailResponse.from(createService.createBoard(boardRequest)));
    }

    // 게시글 조회
    @GetMapping("/{boardId}")
    public ApiResponse<BoardDetailResponse> getOne(@PathVariable Long boardId) {

        return ApiResponse.created(BoardDetailResponse.from(getService.getBoardById(boardId)));
    }

    // 카테고리 내 게시글 조회
    @GetMapping("/category/list/{categoryId}")
    public ApiResponse<PageResponse<BoardDetailResponse>> getByCategory(@PathVariable Long categoryId, PageRequest pageRequest) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), Sort.by("id").descending());
        Page<Board> result = getService.getByCategoryId(categoryId, pageable);

        List<BoardDetailResponse> dtolist = BoardDetailResponse.from(result.getContent());

        return ApiResponse.created(new PageResponse<>(dtolist, pageRequest, result.getTotalElements()));
    }

}
