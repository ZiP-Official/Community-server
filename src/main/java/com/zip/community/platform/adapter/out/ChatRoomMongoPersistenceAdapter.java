package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.mongo.chat.ChatRoomDocument;
import com.zip.community.platform.adapter.out.mongo.chat.repository.ChatRoomMongoRepository;
import com.zip.community.platform.application.port.out.chat.ChatRoomPort;
import com.zip.community.platform.domain.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatRoomMongoPersistenceAdapter implements ChatRoomPort {

    private final ChatRoomMongoRepository chatRoomRepository;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        ChatRoomDocument doc = ChatRoomDocument.from(
                chatRoom.getId(),
                chatRoom.getParticipants(),
                chatRoom.getLastMessage()
        );
        ChatRoomDocument savedDoc = chatRoomRepository.save(doc);
        return savedDoc.toDomain();
    }

    @Override
    public ChatRoom findByChatRoomId(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .map(ChatRoomDocument::toDomain)
                .orElse(null);
    }

    @Override
    public ChatRoom findChatRoomByParticipants(String userId1, String userId2) {
        List<ChatRoomDocument> rooms = chatRoomRepository.findByParticipantsContaining(userId1);
        return rooms.stream()
                .filter(room -> room.getParticipants().contains(userId2))
                .findFirst()
                .map(ChatRoomDocument::toDomain)
                .orElse(null);
    }

    @Override
    public List<ChatRoom> findChatRoomsByUserId(String userId) {
        List<ChatRoomDocument> rooms = chatRoomRepository.findByParticipantsContaining(userId);
        return rooms.stream().map(ChatRoomDocument::toDomain).collect(Collectors.toList());
    }
}
