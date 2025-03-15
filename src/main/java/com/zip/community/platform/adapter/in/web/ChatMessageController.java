package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.chat.MessageDeleteRequest;
import com.zip.community.platform.adapter.in.web.dto.request.chat.MessageReportRequest;
import com.zip.community.platform.application.port.in.chat.ChatMessageUseCase;
import com.zip.community.platform.domain.chat.ChatMessage;
import com.zip.community.platform.domain.report.ReportedChatMessage;
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

    // 채팅방 메세지 조회
    @GetMapping("/messages")
    public ApiResponse<List<ChatMessage>> getMessages(@RequestParam String chatRoomId,
                                                      @RequestParam(defaultValue = "0") Integer page) {
        return ApiResponse.created(chatMessageUseCase.getMessages(chatRoomId, page));
    }

    // 채팅방 특정 메세지 삭제
    @DeleteMapping("/delete")
    public ApiResponse<ChatMessage> deleteMessage(@RequestBody MessageDeleteRequest request) {
        return ApiResponse.created(chatMessageUseCase.deleteMessage(request.getMessageId(), request.getMemberId()));
    }

    // 채팅방 메세지 신고
    @PostMapping("/report")
    public ApiResponse<ReportedChatMessage> reportMessage(@RequestBody MessageReportRequest request) {
        return ApiResponse.created(chatMessageUseCase.reportMessage(request.getMessageId(), request.getReportMemberId(), request.getReportedMemberId(), request.getReason()));
    }

    // 채팅방 메세지 차단 -> 관리자만 가능
    @PostMapping("/block")
    public ApiResponse<ChatMessage> blockMessage(@RequestParam String messageId) {
        return ApiResponse.created(chatMessageUseCase.blockMessage(messageId));
    }

    // 채팅방 메세지 검색
    @GetMapping("/search")
    public ApiResponse<List<ChatMessage>> searchMessages(@RequestParam String chatRoomId,
                                                         @RequestParam String keyword) {

        return ApiResponse.created(chatMessageUseCase.searchMessages(chatRoomId, keyword));
    }
}
