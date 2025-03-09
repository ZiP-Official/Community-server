package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.pageable.PageRequest;
import com.zip.community.common.response.pageable.PageResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.CommentRequest;
import com.zip.community.platform.adapter.in.web.dto.response.CommentResponse;
import com.zip.community.platform.application.port.in.comment.CreateCommentUseCase;
import com.zip.community.platform.application.port.in.comment.GetCommentUseCase;
import com.zip.community.platform.application.port.in.comment.RemoveCommentUseCase;
import com.zip.community.platform.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Log4j2
public class CommentController {

    private final CreateCommentUseCase createService;
    private final GetCommentUseCase getService;
    private final RemoveCommentUseCase removeService;

    // 댓글 작성하기
    @PostMapping
    ApiResponse<CommentResponse> createOne(@RequestBody CommentRequest commentRequest) {
        return ApiResponse.created(CommentResponse.from(createService.createComment(commentRequest)));
    }

    /// 게시글별 댓글 조회하기
    @GetMapping("/{boardId}")
    ApiResponse<PageResponse<CommentResponse>> read(
            @PathVariable("boardId") Long boardId,
            PageRequest pageRequest) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage() - 1, pageRequest.getSize(), Sort.by("id").descending());

        Page<Comment> result = getService.getByBoardId(boardId, pageable);

        List<CommentResponse> dtolist = CommentResponse.from(result.getContent());

        return ApiResponse.created(new PageResponse<>(dtolist, pageRequest, result.getTotalElements()));
    }

    // 댓글 삭제하기
}
