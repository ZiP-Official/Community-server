package com.zip.community.platform.application.service.chat;

import com.zip.community.platform.application.port.in.chat.ChatRoomUseCase;
import com.zip.community.platform.application.port.out.chat.ChatRoomPort;
import com.zip.community.platform.domain.chat.ChatRoom;
import com.zip.community.platform.domain.chat.LastMessage;
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
    public ChatRoom startChat(String userId1, String userId2) {
        ChatRoom existing = chatRoomPort.findChatRoomByParticipants(userId1, userId2);
        if (existing != null) {
            return existing;
        }
        ChatRoom chatRoom = ChatRoom.builder()
                .participants(List.of(userId1, userId2))
                .lastMessage(LastMessage.builder().content("채팅이 시작되었습니다.").build())
                .build();
        return chatRoomPort.save(chatRoom);
    }

    @Override
    public List<ChatRoom> getChatRoomsForUser(String userId) {
        return chatRoomPort.findChatRoomsByUserId(userId);
    }
}
