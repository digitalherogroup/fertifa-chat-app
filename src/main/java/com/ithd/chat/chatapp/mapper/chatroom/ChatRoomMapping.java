package com.ithd.chat.chatapp.mapper.chatroom;

import com.ithd.chat.chatapp.api.chatroom.ChatRoomResponseDto;
import com.ithd.chat.chatapp.api.users.UsersResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapping {
    public ChatRoomResponseDto fromUserIdToChatRoomObject(UsersResponseDto chatroomRepositoryOne) {
        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
        chatRoomResponseDto.setId(chatroomRepositoryOne.getUserId());
       return chatRoomResponseDto;
    }
}
