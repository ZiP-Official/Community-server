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
    private List<Participant> participants;
    private LastMessage lastMessage;
}
