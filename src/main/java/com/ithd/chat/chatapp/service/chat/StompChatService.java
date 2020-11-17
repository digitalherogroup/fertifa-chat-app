package com.ithd.chat.chatapp.service.chat;


import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.model.chat.MessagingStompChat;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface StompChatService {
    MessagingStompChat getChatById(long chatId);

    ResponseEntity<BaseResponse<?>> save(MessagingStompChat chatData);

    BaseResponse<?> getAllChatBodyWithUserIdAndAdminId(String toId, String fromId);

    int countUnreadMessagesById(Long id);

    BaseResponse<?> updateChatListById(String toId, String fromId);

    int getLastId();

    BaseResponse<?> updateChatById(Map<String, Object> productObject, String id);

    int countUnreadMessagesAdminUserById(Long id, Long valueOf);

    ResponseEntity<BaseResponse<?>> saveAndFlush(MessagingStompChat chatData);
}
