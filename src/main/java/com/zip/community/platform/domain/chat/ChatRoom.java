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
}
