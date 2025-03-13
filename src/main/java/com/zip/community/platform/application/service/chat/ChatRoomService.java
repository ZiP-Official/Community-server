package com.zip.community.platform.application.service.chat;

import com.zip.community.common.response.CustomException;
import com.zip.community.common.response.errorcode.ChatErrorCode;
import com.zip.community.platform.application.port.in.chat.ChatRoomUseCase;
import com.zip.community.platform.application.port.out.chat.ChatRoomPort;
import com.zip.community.platform.application.port.out.member.MemberPort;
import com.zip.community.platform.domain.chat.ChatRoom;
import com.zip.community.platform.domain.chat.LastMessage;
import com.zip.community.platform.domain.chat.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService implements ChatRoomUseCase {

    private final ChatRoomPort chatRoomPort;
    private final MemberPort memberPort;

    @Override
    public ChatRoom startChat(Long senderId, Long receiverId, String senderName, String receiverName) {

        checkExistMember(senderId);

        ChatRoom existing = chatRoomPort.findChatRoomByParticipants(senderId, receiverId);
        if (existing != null) {
            return existing;
        }
        Participant p1 = Participant.of(senderId, senderName);
        Participant p2 = Participant.of(receiverId, receiverName);
        ChatRoom chatRoom = ChatRoom.of(List.of(p1, p2), LastMessage.of("채팅이 시작되었습니다.", "system", LocalDateTime.now()));

        return chatRoomPort.save(chatRoom);
    }

    @Override
    public List<ChatRoom> getChatRoomsForUser(Long memberId) {

        checkExistMember(memberId);

        List<ChatRoom> chatRooms = chatRoomPort.findChatRoomsByUserId(memberId);
        if(chatRooms.isEmpty()) {
            throw new CustomException(ChatErrorCode.NOT_FOUND_CHAT_ROOM);
        }
        return chatRooms;
    }

    private void checkExistMember(Long memberId) {
        boolean checkedExistUser = memberPort.getCheckedExistUser(memberId);
        if (!checkedExistUser) {
            throw new CustomException(ChatErrorCode.NOT_FOUND_MEMBER);
        }
    }
}
