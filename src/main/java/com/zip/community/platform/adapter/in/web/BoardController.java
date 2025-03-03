package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.pageable.PageRequest;
import com.zip.community.common.response.pageable.PageResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardRequest;
import com.zip.community.platform.adapter.in.web.dto.response.BoardResponse;
import com.zip.community.platform.application.port.in.board.CreateBoardUseCase;
import com.zip.community.platform.application.port.in.board.GetBoardInfoUseCase;
import com.zip.community.platform.application.port.in.board.RemoveBoardUseCase;
import com.zip.community.platform.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final CreateBoardUseCase createService;
    private final GetBoardInfoUseCase getService;
    private final RemoveBoardUseCase removeService;

    // 게시판 생성
    @PostMapping
    public ApiResponse<BoardResponse> saveOne(@RequestBody BoardRequest boardRequest) {
        return ApiResponse.created(BoardResponse.from(createService.createBoard(boardRequest)));
    }

    @GetMapping("{boardId}")
    public ApiResponse<BoardResponse> getOne(@PathVariable Long boardId) {

        return ApiResponse.created(BoardResponse.from(getService.getOneInfo(boardId)));
    }

    // 카테고리 내 게시물 조회
    @GetMapping("/category/list/{categoryId}")
    public ApiResponse<PageResponse<BoardResponse>> getByCategory(@PathVariable Long categoryId, PageRequest pageRequest) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), Sort.by("id").descending());
        Page<Board> result = getService.getByCategoryId(categoryId, pageable);

        List<BoardResponse> dtolist = BoardResponse.from(result.getContent());

        return ApiResponse.created(new PageResponse<>(dtolist, pageRequest, result.getTotalElements()));
    }

}
