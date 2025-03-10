package com.zip.community.platform.application.port.in.chat;

import com.zip.community.platform.domain.chat.ChatRoom;

import java.util.List;

public interface ChatRoomUseCase {
    ChatRoom startChat(String userId1, String userId2);
    List<ChatRoom> getChatRoomsForUser(String userId);
}
