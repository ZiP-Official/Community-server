package com.zip.community.platform.application.service.chat;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.ChatErrorCode;
import com.zip.community.platform.application.port.in.chat.ChatMessageUseCase;
import com.zip.community.platform.application.port.out.chat.ChatMessageMongoPort;
import com.zip.community.platform.application.port.out.chat.ChatRoomPort;
import com.zip.community.platform.application.port.out.chat.MessageSendPort;
import com.zip.community.platform.domain.chat.ChatMessage;
import com.zip.community.platform.domain.chat.ChatRoom;
import com.zip.community.platform.domain.report.ReportedChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ChatMessage sendMessage(ChatMessage message) {

        // 채팅방 존재 확인
        ChatRoom byChatRoomId = chatRoomPort.findByChatRoomId(message.getChatRoomId());
        if (byChatRoomId == null) {
            throw new CustomException(ChatErrorCode.NOT_FOUND_CHAT_ROOM);
        }

        ChatMessage savedMessage = chatMessageMongoPort.save(message);
        return messageSendPort.send(savedMessage);
    }

    @Override
    public List<ChatMessage> getMessages(String chatRoomId, Integer page) {
        return chatMessageMongoPort.getMessages(chatRoomId, page);
    }

    @Override
    public ChatMessage deleteMessage(String messageId, Long memberId) {

        ChatMessage message = chatMessageMongoPort.findById(messageId);
        if (!message.getSenderId().equals(memberId)) return message;

        message.setContent("삭제된 메세지입니다");
        message.setDeletedYn(true);

        return chatMessageMongoPort.save(message);
    }

    @Override
    public ReportedChatMessage reportMessage(String messageId, Long reportMemberId, Long reportedMemberId, String reason) {

        ChatMessage message = chatMessageMongoPort.findById(messageId);

        // 이미 삭제된 메세지인 경우 예외처리
        if(message.getDeletedYn()) {
            throw new CustomException(ChatErrorCode.ALREADY_DELETED_MESSAGE);
        }

        // 신고한 사람과 신고당한 사람이 같으면 예외처리
        if (reportMemberId.equals(reportedMemberId)) {
            throw new CustomException(ChatErrorCode.REPORT_SAME_MEMBER);
        }
        return chatMessageMongoPort.reportMessage(messageId, reportMemberId, reportedMemberId, reason);
    }

    @Override
    public ChatMessage blockMessage(String messageId) {

        ChatMessage message = chatMessageMongoPort.findById(messageId);
        // 이미 삭제된 메세지인 경우 예외처리
        if(message.getDeletedYn()) {
            throw new CustomException(ChatErrorCode.ALREADY_DELETED_MESSAGE);
        }

        message.setContent("부적절한 내용으로 차단된 메세지입니다");
        message.setDeletedYn(true);

        return chatMessageMongoPort.save(message);
    }

    // 해야함
    @Override
    public List<ChatMessage> searchMessages(String chatRoomId, String keyword) {
        return chatMessageMongoPort.searchMessages(chatRoomId, keyword);
    }
}
