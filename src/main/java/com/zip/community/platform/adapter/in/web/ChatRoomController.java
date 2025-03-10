package com.zip.community.platform.adapter.in.web;

import com.zip.community.common.response.ApiResponse;
import com.zip.community.platform.adapter.in.web.dto.request.chat.ChatRoomRequest;
import com.zip.community.platform.adapter.in.web.dto.response.ChatRoomResponse;
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

    @PostMapping("/start")
    public ApiResponse<ChatRoomResponse> startChat(@RequestBody ChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomUseCase.startChat(request.getUserId1(), request.getUserId2());
        return ApiResponse.created(ChatRoomResponse.from(chatRoom));
    }

    @GetMapping("/list/{userId}")
    public ApiResponse<List<ChatRoomResponse>> getChatRooms(@PathVariable String userId) {
        return ApiResponse.created(ChatRoomResponse.from(chatRoomUseCase.getChatRoomsForUser(userId)));
    }
}
