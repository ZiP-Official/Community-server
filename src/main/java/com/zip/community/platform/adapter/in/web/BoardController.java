package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.pageable.PageRequest;
import com.zip.community.common.response.pageable.PageResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardUpdateRequest;
import com.zip.community.platform.adapter.in.web.dto.response.board.BoardListResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardRequest;
import com.zip.community.platform.adapter.in.web.dto.response.board.BoardDetailResponse;
import com.zip.community.platform.application.port.in.board.*;
import com.zip.community.platform.domain.board.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final CreateBoardUseCase createService;
    private final GetBoardUseCase getService;
    private final UpdateBoardUseCase updateService;
    private final SyncUseCase syncService;
    private final RemoveBoardUseCase removeService;

    // 게시글 생성
    @PostMapping
    public ApiResponse<BoardDetailResponse> saveBoard(@RequestBody BoardRequest boardRequest) {
        return ApiResponse.created(BoardDetailResponse.from(createService.createBoard(boardRequest)));
    }

    // 게시글 조회
    @GetMapping("/{boardId}")
    public ApiResponse<BoardDetailResponse> getBoard(@PathVariable Long boardId) {

        return ApiResponse.created(BoardDetailResponse.from(getService.getBoardById(boardId)));
    }


    // 최신 게시글 목록 조회하기
    @GetMapping("/list")
    public ApiResponse<PageResponse<BoardListResponse>> getBoards(PageRequest pageRequest) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), Sort.by("id").descending());

        Page<Board> result = getService.getBoards(pageable);

        log.info(result.toString());
        List<BoardListResponse> dtolist = BoardListResponse.from(result.getContent());

        return ApiResponse.ok(new PageResponse<>(dtolist, pageRequest, result.getTotalElements()));
    }

    // 화제 게시글 목록 조회하기
    @GetMapping("/list/favorite")
    public ApiResponse<PageResponse<BoardListResponse>> getBoardsFavorite(PageRequest pageRequest) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), Sort.by("id").descending());

        Page<Board> result = getService.getBoardsFavorite(pageable);
        List<BoardListResponse> dtolist = BoardListResponse.from(result.getContent());

        return ApiResponse.ok(new PageResponse<>(dtolist, pageRequest, result.getTotalElements()));
    }

    // 좋아요 기반 게시글 목록 조회하기




    // 카테고리 내 게시글 조회
    @GetMapping("/category/list/{categoryId}")
    public ApiResponse<PageResponse<BoardListResponse>> getByCategory(@PathVariable Long categoryId, PageRequest pageRequest) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), Sort.by("id").descending());
        Page<Board> result = getService.getBoardsByCategoryId(categoryId, pageable);

        List<BoardListResponse> dtolist = BoardListResponse.from(result.getContent());

        return ApiResponse.created(new PageResponse<>(dtolist, pageRequest, result.getTotalElements()));
    }

    //글 수정하기
    @PutMapping("/{boardId}")
    public ApiResponse<String> updateBoard(@PathVariable Long boardId, @RequestBody BoardUpdateRequest request) {
        request.setBoardId(boardId);
        updateService.updateBoard(request);

        return ApiResponse.ok("글이 수정되었습니다.");
    }

    // 싱크맞추기
    @PostMapping("/sync/{boardId}")
    public ApiResponse<String> syncBoard(@PathVariable Long boardId) {
        syncService.syncData(boardId);
        return ApiResponse.created("데이터의 이전이 완료되었습니다.");
    }

}
