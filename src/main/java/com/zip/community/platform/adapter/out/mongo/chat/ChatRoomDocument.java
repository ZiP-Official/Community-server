package com.zip.community.platform.adapter.out.mongo.chat;

import com.zip.community.platform.adapter.out.mongo.BaseDocument;
import com.zip.community.platform.domain.chat.ChatRoom;
import com.zip.community.platform.domain.chat.LastMessage;
import com.zip.community.platform.domain.chat.Participant;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "chat_room")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDocument extends BaseDocument {

    @Id
    private String id;
    private List<Participant> participants;
    private LastMessage lastMessage;

    public static ChatRoomDocument from(ChatRoom chatRoom) {
        return ChatRoomDocument.builder()
                .id(chatRoom.getId())
                .participants(chatRoom.getParticipants())
                .lastMessage(chatRoom.getLastMessage())
                .build();
    }

    public ChatRoom toDomain() {
        return ChatRoom.builder()
                .id(this.id)
                .participants(this.participants)
                .lastMessage(this.lastMessage)
                .build();
    }
}
