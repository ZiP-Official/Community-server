package com.zip.community.platform.adapter.in.web.dto.response;

import com.zip.community.platform.domain.chat.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {

    private String id;
    private String userId1;
    private String userId2;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    // 단일 반환
    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .userId1(chatRoom.getParticipants().getFirst())
                .userId2(chatRoom.getParticipants().getLast())
                .lastMessage(chatRoom.getLastMessage().getContent())
                .lastMessageTime(chatRoom.getLastMessage().getSentAt())
                .build();
    }

    // List 반환
    public static List<ChatRoomResponse> from(List<ChatRoom> chatRooms) {
        return chatRooms.stream()
                .map(ChatRoomResponse::from)
                .collect(Collectors.toList());
    }
}
