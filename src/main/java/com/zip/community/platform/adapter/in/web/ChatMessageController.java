package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.chat.MessageDeleteRequest;
import com.zip.community.platform.adapter.in.web.dto.request.chat.MessageReportRequest;
import com.zip.community.platform.adapter.in.web.dto.response.chat.SearchResponse;
import com.zip.community.platform.application.port.in.chat.ChatMessageUseCase;
import com.zip.community.platform.domain.chat.ChatMessage;
import com.zip.community.platform.domain.report.ReportedChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageUseCase chatMessageUseCase;

    // 채팅방 메세지 조회
    // 초기 - 최신 메세지 20개 조회
    // 이전 메세지 조회 - cursor 이전 메세지 pageSize 만큼 조회, direction: older
    // 이후 메세지 조회 - cursor 이후 메세지 pageSize 만큼 조회, direction: newer
    // includeCursor: false -> cursor 미포함, 메세지 조회할 때 사용, 채팅방 내에서 스크롤 시 사용
    // includeCursor: true -> cursor 포함, 메세지 검색 후 해당 메세지 조회할 때 사용
    @GetMapping("/messages")
    public ApiResponse<List<ChatMessage>> getMessages(
            @RequestParam String chatRoomId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "older") String direction,
            @RequestParam(defaultValue = "false") Boolean includeCursor) {

        List<ChatMessage> messages = chatMessageUseCase.getMessages(chatRoomId, cursor, pageSize, direction, includeCursor);
        return ApiResponse.created(messages);
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
    // 키워드 포함된 메세지 모두 조회
    @GetMapping("/search")
    public ApiResponse<List<SearchResponse>> searchMessages(@RequestParam String chatRoomId, @RequestParam String keyword) {
        List<ChatMessage> messages = chatMessageUseCase.searchMessages(chatRoomId, keyword);
        return ApiResponse.created(messages.stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList()));
    }
}