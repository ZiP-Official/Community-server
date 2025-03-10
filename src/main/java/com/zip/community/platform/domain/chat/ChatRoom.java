package com.zip.community.platform.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    private String id;
    private List<String> participants;
    private LastMessage lastMessage;

    private static ChatRoom of(String id, List<String> participants, LastMessage lastMessage) {
        return ChatRoom.builder()
                .id(id)
                .participants(participants)
                .lastMessage(lastMessage)
                .build();
    }
}
