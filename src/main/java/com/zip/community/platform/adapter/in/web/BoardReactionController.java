package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.ErrorCode;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardReactionRequest;
import com.zip.community.platform.application.port.in.board.ReactionUseCase;
import com.zip.community.platform.application.port.in.board.response.ReactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reaction")
@RequiredArgsConstructor
public class BoardReactionController {

    private final ReactionUseCase addService;

    @PostMapping("/{reactionType}")
    public ApiResponse<String> toggleReaction(@PathVariable("reactionType") String reactionType,
                                              @RequestBody BoardReactionRequest request) {
        ReactionStatus status;

        switch (reactionType.toLowerCase()) {
            case "like":
                status = addService.addLikeReaction(request);
                reactionType = "좋아요";
                break;
            case "dislike":
                status = addService.addDisLikeReaction(request);
                reactionType = "싫어요";
                break;
            default:
                return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST));
        }

        String message = (status == ReactionStatus.CREATED)
                ? reactionType + "가 생성되었습니다."
                : reactionType + "가 삭제되었습니다.";

        return ApiResponse.created(message);
    }
}
