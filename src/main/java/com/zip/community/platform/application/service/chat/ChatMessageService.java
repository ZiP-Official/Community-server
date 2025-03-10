package com.zip.community.platform.application.service.chat;

import com.zip.community.platform.application.port.in.chat.ChatMessageUseCase;
import com.zip.community.platform.application.port.out.chat.ChatMessageMongoPort;
import com.zip.community.platform.application.port.out.chat.ChatRoomPort;
import com.zip.community.platform.application.port.out.chat.MessageSendPort;
import com.zip.community.platform.domain.chat.ChatMessage;
import com.zip.community.platform.domain.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService implements ChatMessageUseCase {

    private final ChatRoomPort chatRoomPort;
    private final ChatMessageMongoPort chatMessageMongoPort;
    private final MessageSendPort messageSendPort;

    @Override
    public void sendMessage(ChatMessage message) {

        // 채팅방 존재 확인
        ChatRoom byChatRoomId = chatRoomPort.findByChatRoomId(message.getChatRoomId());
        if (byChatRoomId == null) {
            throw new IllegalArgumentException("채팅방이 존재하지 않습니다.");
        }

        ChatMessage savedMessage = chatMessageMongoPort.save(message);
        messageSendPort.send(savedMessage);
    }

    @Override
    public ChatMessage deleteMessage(String messageId, String userId) {

        ChatMessage message = chatMessageMongoPort.findById(messageId);
        if (message.getSenderId().equals(userId)) {
            message.setContent("삭제된 메세지입니다");
            message.setDeletedYn(true);
            return chatMessageMongoPort.save(message);
        }
        return message;
    }

    @Override
    public ChatMessage reportMessage(String messageId, String reporterId, String reason) {

        ChatMessage message = chatMessageMongoPort.findById(messageId);
        log.info("Message " + messageId + " reported by user " + reporterId + " for: " + reason);

        // 신고 처리 로직 추가
        // ...

        return message;
    }

    @Override
    public ChatMessage blockMessage(String messageId) {
        ChatMessage message = chatMessageMongoPort.findById(messageId);
        message.setContent("차단된 메세지입니다");
        return chatMessageMongoPort.save(message);
    }

    @Override
    public List<ChatMessage> searchMessages(String chatRoomId, String keyword, LocalDateTime from, LocalDateTime to) {
        return chatMessageMongoPort.searchMessages(chatRoomId, keyword, from, to);
    }
}
