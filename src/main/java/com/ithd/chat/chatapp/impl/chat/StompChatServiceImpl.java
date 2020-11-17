package com.ithd.chat.chatapp.impl.chat;


import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.exceptions.IdNotFoundException;
import com.ithd.chat.chatapp.exceptions.ObjectNullException;
import com.ithd.chat.chatapp.mapper.chat.StompChatMapper;
import com.ithd.chat.chatapp.model.chat.MessagingStompChat;
import com.ithd.chat.chatapp.model.chat.MessagingStompResponseChat;
import com.ithd.chat.chatapp.repository.chat.StompChatRepository;
import com.ithd.chat.chatapp.service.chat.StompChatService;
import com.ithd.chat.chatapp.util.GsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class StompChatServiceImpl implements StompChatService {

    private final StompChatRepository stompChatRepository;
    private final GsonConverter gsonConverter;
    private final StompChatMapper stompChatMapper;

    @Override
    public MessagingStompChat getChatById(long chatId) {
        return stompChatRepository.getOne(chatId);
    }


    @Override
    public ResponseEntity<BaseResponse<?>> saveAndFlush(MessagingStompChat chatData) {
        if (null == chatData) throw new NullPointerException();
        BaseResponse<?> response = saveFlushAction(chatData);
        return new ResponseEntity<>(response, response.getStatus());
    }

    private BaseResponse<?> saveFlushAction(MessagingStompChat chatData) {
        Map<String, Object> body = new HashMap<>();
        try {
            MessagingStompChat messagingStompChat = stompChatRepository.saveAndFlush(chatData);
            body = gsonConverter.convertObjectToMapObject(messagingStompChat);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }


    @Override
    public ResponseEntity<BaseResponse<?>> save(MessagingStompChat chatData) {
        if (null == chatData) throw new NullPointerException();
        log.info("ResponseEntity<BaseResponse<?>> save(MessagingStompChat chatData)======>{}",chatData);
        BaseResponse<?> response = saveAction(chatData);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @Override
    public BaseResponse<?> getAllChatBodyWithUserIdAndAdminId(String toId, String fromId) {
        if (null == toId || null == fromId) throw new ObjectNullException();
        List<String> body;
        try {
            List<MessagingStompResponseChat> messagingStompChat = new ArrayList<>();
            for (int i = 1; i <= 12 ; i++) {
                List<MessagingStompResponseChat> responseChats = stompChatMapper.fromUserIDToChatBody(stompChatRepository.getDataByQuery(fromId, toId));
                messagingStompChat.addAll(responseChats);
            }
            body = gsonConverter.gsonObjectConverter(messagingStompChat);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public int countUnreadMessagesById(Long id) {
        return stompChatRepository.countById(id);
    }

    @Override
    public BaseResponse<?> updateChatListById(String toId,String fromId) {
        int statusUpdated=0;
        if (null == toId) throw new ObjectNullException();
        try {
            statusUpdated = stompChatRepository.updateChatByIds(toId,fromId);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), statusUpdated);
    }

    @Override
    public int getLastId() {
        return stompChatRepository.getFinalId();
    }

    @Override
    public BaseResponse<?> updateChatById(Map<String, Object> productObject, String id) {
        int statusUpdated=0;
        if(null == id) throw new IdNotFoundException(id);
        try{
            String gson= gsonConverter.toJson(productObject);
            statusUpdated = stompChatRepository.updateChatById(gson,id);
        }catch (Exception e){
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), statusUpdated);
    }

    @Override
    public int countUnreadMessagesAdminUserById(Long admin, Long user) {
        return stompChatRepository.countAllMessagesAdminAndUser(admin,user);
    }


    public BaseResponse<?> saveAction(MessagingStompChat messageModel) {
        Map<String, Object> body = new HashMap<>();
        try {
            MessagingStompChat messagingStompChat = stompChatRepository.save(messageModel);
            body = gsonConverter.convertObjectToMapObject(messagingStompChat);
            log.info("BaseResponse<?> saveAction(MessagingStompChat messageModel) ========> {}",body);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }
}
