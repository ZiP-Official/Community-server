package com.zip.community.platform.application.port.out.chat;

import com.zip.community.platform.domain.chat.ChatRoom;

import java.util.List;

public interface ChatRoomPort {

    ChatRoom save(ChatRoom chatRoom);
    ChatRoom findByChatRoomId(String chatRoomId);
    ChatRoom findChatRoomByParticipants(String userId1, String userId2);
    List<ChatRoom> findChatRoomsByUserId(String userId);
}
