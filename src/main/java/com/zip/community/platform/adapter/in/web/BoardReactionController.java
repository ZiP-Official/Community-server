package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.board.BoardReactionRequest;
import com.zip.community.platform.adapter.in.web.dto.response.BoardReactionResponse;
import com.zip.community.platform.application.port.in.board.AddReactionUseCase;
import com.zip.community.platform.application.port.in.board.RemoveReactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reaction")
@RequiredArgsConstructor
public class BoardReactionController {

    private final AddReactionUseCase addService;
    private final RemoveReactionUseCase removeService;

    @PostMapping
    public ApiResponse<BoardReactionResponse> addOne(@RequestBody BoardReactionRequest request) {

        return ApiResponse.created(BoardReactionResponse.from(addService.addReaction(request)));
    }

    @DeleteMapping
    public ApiResponse<String> removeOne(@RequestBody BoardReactionRequest request) {
        removeService.removeReaction(request);

        return ApiResponse.created("성공적으로 삭제되었습니다");
    }

}
