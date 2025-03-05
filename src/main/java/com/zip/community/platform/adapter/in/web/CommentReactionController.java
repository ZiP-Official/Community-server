package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.CommentReactionRequest;
import com.zip.community.platform.adapter.in.web.dto.response.CommentReactionResponse;
import com.zip.community.platform.application.port.in.comment.AddLikeReactionUseCase;
import com.zip.community.platform.application.port.in.comment.RemoveLikeReactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment/reaction")
@RequiredArgsConstructor
public class CommentReactionController {

    private final AddLikeReactionUseCase addService;
    private final RemoveLikeReactionUseCase removeService;

    @PostMapping
    public ApiResponse<CommentReactionResponse> addOne(@RequestBody CommentReactionRequest request) {

        return ApiResponse.created(CommentReactionResponse.from(addService.addReaction(request)));
    }

    @DeleteMapping
    public ApiResponse<String> delete(@RequestBody CommentReactionRequest request) {
        removeService.removeReaction(request);
        return ApiResponse.created("성공적으로 삭제되었습니다.");
    }

}
