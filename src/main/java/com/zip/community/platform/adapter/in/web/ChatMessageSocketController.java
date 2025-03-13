package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.chat.MessageSendRequest;
import com.zip.community.platform.application.port.in.chat.ChatMessageUseCase;
import com.zip.community.platform.domain.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageSocketController {

    private final ChatMessageUseCase chatMessageUseCase;

    @MessageMapping("/{chatRoomId}")
    public ApiResponse<ChatMessage> sendMessage(@DestinationVariable String chatRoomId, MessageSendRequest request) {
        ChatMessage message = ChatMessage.of(chatRoomId, request.getContent(), request.getSenderId(), request.getSenderName(), LocalDateTime.now(), false, false);
        log.info("받은 메세지 {}: {}", chatRoomId, request.getContent());

        return ApiResponse.created(chatMessageUseCase.sendMessage(message));
    }
}
