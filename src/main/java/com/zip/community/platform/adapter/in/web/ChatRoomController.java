package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.chat.ChatRoomRequest;
import com.zip.community.platform.adapter.in.web.dto.response.chat.ChatRoomResponse;
import com.zip.community.platform.application.port.in.chat.ChatRoomUseCase;
import com.zip.community.platform.domain.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatroom")
public class ChatRoomController {

    private final ChatRoomUseCase chatRoomUseCase;

    // 채팅방 생성
    @PostMapping("/start")
    public ApiResponse<ChatRoomResponse> startChat(@RequestBody ChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomUseCase.startChat(request.getSenderId(), request.getReceiverId(), request.getSenderName(), request.getReceiverName());
        return ApiResponse.created(ChatRoomResponse.from(chatRoom));
    }

    // 채팅방 목록 조회
    @GetMapping("/list/{memberId}")
    public ApiResponse<List<ChatRoomResponse>> getChatRooms(@PathVariable Long memberId) {
        return ApiResponse.created(ChatRoomResponse.from(chatRoomUseCase.getChatRoomsForUser(memberId)));
    }
}
