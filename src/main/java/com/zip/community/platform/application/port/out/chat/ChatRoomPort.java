package com.zip.community.platform.application.port.out.chat;

import com.zip.community.platform.domain.chat.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomPort {

    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findByChatRoomId(String chatRoomId);
    List<ChatRoom> findChatRoomsByUserId(Long memberId);
    ChatRoom findChatRoomByParticipants(Long senderId, Long receiverId);
}
