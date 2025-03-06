package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.ErrorCode;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardReactionRequest;
import com.zip.community.platform.application.port.in.board.AddReactionUseCase;
import com.zip.community.platform.application.port.in.board.RemoveReactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reaction")
@RequiredArgsConstructor
public class BoardReactionController {

    private final AddReactionUseCase addService;
    private final RemoveReactionUseCase removeService;

    @PostMapping("/{reactionType}")
    public ApiResponse<String> addReaction(@PathVariable("reactionType") String reactionType,
                                           @RequestBody BoardReactionRequest request) {
        if ("like".equalsIgnoreCase(reactionType)) {
            addService.addLikeReaction(request);
            return ApiResponse.created("좋아요가 성공으로 저장되었습니다.");
        } else if ("dislike".equalsIgnoreCase(reactionType)) {
            addService.addDisLikeReaction(request);

            return ApiResponse.created("싫어요가 성공으로 저장되었습니다.");
        }
        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/{reactionType}/none")
    public ApiResponse<String> removeReaction(@PathVariable("reactionType") String reactionType,
                                              @RequestBody BoardReactionRequest request) {
        if ("like".equalsIgnoreCase(reactionType)) {
            removeService.removeLikeReaction(request);
            return ApiResponse.created("좋아요가 성공적으로 삭제되었습니다.");
        } else if ("dislike".equalsIgnoreCase(reactionType)) {
            removeService.removeDisLikeReaction(request);
            return ApiResponse.created("싫어요가 성공적으로 삭제되었습니다.");
        }
        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
