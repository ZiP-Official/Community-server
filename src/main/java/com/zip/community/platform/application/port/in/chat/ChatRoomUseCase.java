package com.zip.community.platform.application.port.in.chat;

import com.zip.community.platform.domain.chat.ChatRoom;

import java.util.List;

public interface ChatRoomUseCase {
    ChatRoom startChat(Long senderId, Long receiverId, String senderName, String receiverName);
    List<ChatRoom> getChatRoomsForUser(Long memberId);
}
