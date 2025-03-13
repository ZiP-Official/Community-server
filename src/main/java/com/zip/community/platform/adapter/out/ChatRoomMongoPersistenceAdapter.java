package com.zip.community.platform.adapter.out;

import com.zip.community.platform.adapter.out.mongo.chat.ChatRoomDocument;
import com.zip.community.platform.adapter.out.mongo.chat.repository.ChatRoomMongoRepository;
import com.zip.community.platform.application.port.out.chat.ChatRoomPort;
import com.zip.community.platform.domain.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatRoomMongoPersistenceAdapter implements ChatRoomPort {

    private final ChatRoomMongoRepository chatRoomRepository;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        ChatRoomDocument doc = ChatRoomDocument.from(chatRoom);
        ChatRoomDocument savedDoc = chatRoomRepository.save(doc);
        return savedDoc.toDomain();
    }

    @Override
    public Optional<ChatRoom> findByChatRoomId(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .map(ChatRoomDocument::toDomain);
    }

    @Override
    public List<ChatRoom> findChatRoomsByUserId(Long memberId) {
        List<ChatRoomDocument> rooms = chatRoomRepository.findByParticipantsId(memberId);
        return rooms.stream()
                .map(ChatRoomDocument::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoom findChatRoomByParticipants(Long senderId, Long receiverId) {
        List<ChatRoomDocument> rooms = chatRoomRepository.findByParticipantsContainingBoth(senderId, receiverId);
        return rooms.stream().findFirst().map(ChatRoomDocument::toDomain).orElse(null);
    }
}
