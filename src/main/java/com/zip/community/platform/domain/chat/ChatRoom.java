package com.zip.community.platform.domain.chat;

import com.zip.community.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class ChatRoom extends BaseDomain {

    private String id;
    private List<Participant> participants;
    private LastMessage lastMessage;

    public static ChatRoom of(List<Participant> participants, LastMessage lastMessage) {
        return ChatRoom.builder()
                .participants(participants)
                .lastMessage(lastMessage)
                .build();
    }
}
