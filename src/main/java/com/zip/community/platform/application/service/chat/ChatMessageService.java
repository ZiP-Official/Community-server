package com.zip.community.platform.application.service.chat;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.ChatErrorCode;
import com.zip.community.platform.application.port.in.chat.ChatMessageUseCase;
import com.zip.community.platform.application.port.out.chat.ChatMessageMongoPort;
import com.zip.community.platform.application.port.out.chat.ChatRoomPort;
import com.zip.community.platform.application.port.out.chat.MessageSendPort;
import com.zip.community.platform.application.port.out.member.MemberPort;
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
    private final MemberPort memberPort;

    @Override
    public ChatMessage sendMessage(ChatMessage message) {

        checkExistMember(message.getSenderId());

        // 채팅방 존재 확인
        chatRoomPort.findByChatRoomId(message.getChatRoomId())
                .orElseThrow(() -> new CustomException(ChatErrorCode.NOT_FOUND_CHAT_ROOM));

        ChatMessage savedMessage = chatMessageMongoPort.save(message);
        return messageSendPort.send(savedMessage);
    }

    @Override
    public List<ChatMessage> getMessages(String chatRoomId, Integer page) {
        return chatMessageMongoPort.getMessages(chatRoomId, page);
    }

    @Override
    public ChatMessage deleteMessage(String messageId, Long memberId) {

        checkExistMember(memberId);

        ChatMessage message = chatMessageMongoPort.findById(messageId)
                .orElseThrow(() -> new CustomException(ChatErrorCode.NOT_FOUND_CHAT_MESSAGE))
                .toDomain();
        if (!message.getSenderId().equals(memberId)) return message;

        message.delete();

        return chatMessageMongoPort.save(message);
    }

    @Override
    public ReportedChatMessage reportMessage(String messageId, Long reportMemberId, Long reportedMemberId, String reason) {

        checkExistMember(reportMemberId);

        ChatMessage message = chatMessageMongoPort.findById(messageId)
                .orElseThrow(() -> new CustomException(ChatErrorCode.NOT_FOUND_CHAT_MESSAGE))
                .toDomain();

        // 이미 삭제된 메세지인 경우 예외처리
        if(message.getDeletedYn()) {
            throw new CustomException(ChatErrorCode.ALREADY_DELETED_MESSAGE);
        }

        // 신고한 사람과 신고당한 사람이 같으면 예외처리
        if (reportMemberId.equals(reportedMemberId)) {
            throw new CustomException(ChatErrorCode.REPORT_SAME_MEMBER);
        }

        // 이미 신고한 메시지가 있는지 확인하고, 있으면 예외 처리
        if (chatMessageMongoPort.getByMessageIdAndReportMemberId(messageId, reportMemberId).isPresent()) {
            throw new CustomException(ChatErrorCode.ALREADY_REPORTED_MESSAGE);
        }

        ReportedChatMessage reportedChatMessage = ReportedChatMessage.of(messageId, reportMemberId, reportedMemberId, reason, null);
        return chatMessageMongoPort.reportMessage(reportedChatMessage);
    }

    @Override
    public ChatMessage blockMessage(String messageId) {

        ChatMessage message = chatMessageMongoPort.findById(messageId)
                .orElseThrow(() -> new CustomException(ChatErrorCode.NOT_FOUND_CHAT_MESSAGE))
                .toDomain();
        // 이미 삭제된 메세지인 경우 예외처리
        if(message.getDeletedYn()) {
            throw new CustomException(ChatErrorCode.ALREADY_DELETED_MESSAGE);
        }

        message.block();

        return chatMessageMongoPort.save(message);
    }

    // 해야함
    @Override
    public List<ChatMessage> searchMessages(String chatRoomId, String keyword) {
        return chatMessageMongoPort.searchMessages(chatRoomId, keyword);
    }

    private void checkExistMember(Long memberId) {
        boolean checkedExistUser = memberPort.getCheckedExistUser(memberId);
        if (!checkedExistUser) {
            throw new CustomException(ChatErrorCode.NOT_FOUND_MEMBER);
        }
    }
}
