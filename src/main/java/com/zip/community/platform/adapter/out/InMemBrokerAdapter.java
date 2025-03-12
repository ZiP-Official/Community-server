package com.zip.community.platform.adapter.out;

import com.zip.community.platform.application.port.out.chat.MessageSendPort;
import com.zip.community.platform.domain.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Slf4j
@Component
@Profile("test")
@RequiredArgsConstructor
public class InMemBrokerAdapter implements MessageSendPort {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public ChatMessage send(ChatMessage message) {
        simpMessagingTemplate.convertAndSend(format("/%s", message.getChatRoomId()), message);
        log.info("인메모리 메세지 브로커 메세지 전송: " + message.getChatRoomId() + ": " + message.getContent());
        return message;
    }
}
