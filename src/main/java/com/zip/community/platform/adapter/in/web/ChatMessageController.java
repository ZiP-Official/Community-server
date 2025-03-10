package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.chat.MessageDeleteRequest;
import com.zip.community.platform.application.port.in.chat.ChatMessageUseCase;
import com.zip.community.platform.domain.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageUseCase chatMessageUseCase;

    @DeleteMapping("/delete")
    public ApiResponse<ChatMessage> deleteMessage(@RequestBody MessageDeleteRequest request) {
        return ApiResponse.created(chatMessageUseCase.deleteMessage(request.getMessageId(), request.getUserId()));
    }

    /**
     * 메시지 신고
     * 예) POST /api/chat/report?messageId=xxx&reporterId=yyy&reason=zzz
     */
    @PostMapping("/report")
    public ApiResponse<ChatMessage> reportMessage(@RequestParam String messageId,
                                     @RequestParam String reporterId,
                                     @RequestParam String reason) {
        log.info("Report message request - messageId: {}, reporterId: {}, reason: {}", messageId, reporterId, reason);
        return chatMessageUseCase.reportMessage(messageId, reporterId, reason);
    }

    /**
     * 메시지 차단 (관리자 기능)
     * 예) POST /api/chat/block?messageId=xxx
     */
    @PostMapping("/block")
    public ApiResponse<ChatMessage> blockMessage(@RequestParam String messageId) {
        log.info("Block message request - messageId: {}", messageId);
        return chatMessageUseCase.blockMessage(messageId);
    }

    /**
     * 메시지 검색
     * 예) GET /api/chat/search?chatRoomId=xxx&keyword=yyy&from=2025-03-09T00:00:00&to=2025-03-09T23:59:59
     */
    @GetMapping("/search")
    public ApiResponse<List<ChatMessage>> searchMessages(@RequestParam String keyword) {

        return chatMessageUseCase.searchMessages(keyword);
    }
}
