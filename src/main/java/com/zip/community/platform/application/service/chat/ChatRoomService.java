package com.zip.community.platform.application.service.chat;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.ChatErrorCode;
import com.zip.community.platform.application.port.in.chat.ChatRoomUseCase;
import com.zip.community.platform.application.port.out.chat.ChatRoomPort;
import com.zip.community.platform.domain.chat.ChatRoom;
import com.zip.community.platform.domain.chat.LastMessage;
import com.zip.community.platform.domain.chat.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService implements ChatRoomUseCase {

    private final ChatRoomPort chatRoomPort;

    @Override
    public ChatRoom startChat(Long senderId, Long receiverId, String senderName, String receiverName) {

        ChatRoom existing = chatRoomPort.findChatRoomByParticipants(senderId, receiverId);
        if (existing != null) {
            return existing;
        }
        Participant p1 = Participant.builder().id(senderId).name(senderName).build();
        Participant p2 = Participant.builder().id(receiverId).name(receiverName).build();
        ChatRoom chatRoom = ChatRoom.builder()
                .participants(List.of(p1, p2))
                .lastMessage(LastMessage.builder().content("채팅이 시작되었습니다.").build())
                .build();
        return chatRoomPort.save(chatRoom);
    }

    @Override
    public List<ChatRoom> getChatRoomsForUser(Long memberId) {
        return chatRoomPort.findChatRoomsByUserId(memberId);
    }
}
