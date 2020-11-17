package com.ithd.chat.chatapp.service.chatroom;


import com.ithd.chat.chatapp.base.BaseResponse;

public interface ChatRoomService {

    BaseResponse<?> chatRoomExistsByUserId(String id);
}
