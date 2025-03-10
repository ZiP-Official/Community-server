package com.zip.community.platform.adapter.in.web;

import com.zip.community.platform.adapter.in.web.dto.request.chat.MessageRequest;
import com.zip.community.platform.application.port.in.chat.ChatMessageUseCase;
import com.zip.community.platform.domain.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageSocketController {

    private final ChatMessageUseCase chatMessageUseCase;

    @MessageMapping("/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, MessageRequest request) {
        log.info("Received message for chatRoom {}: {}", chatRoomId, request.getContent());

        ChatMessage message = ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .content(request.getContent())
                .senderId(request.getSenderId())
                .sentAt(LocalDateTime.now())
                .readYn(false)
                .deletedYn(false)
                .build();
        chatMessageUseCase.sendMessage(message);
    }
}
