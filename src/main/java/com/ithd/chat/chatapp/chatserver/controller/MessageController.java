package com.ithd.chat.chatapp.chatserver.controller;


import com.google.gson.Gson;
import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.chatserver.model.MessageModel;
import com.ithd.chat.chatapp.model.chat.MessagingStompChat;
import com.ithd.chat.chatapp.repository.users.UserChatDataController;
import com.ithd.chat.chatapp.service.chat.StompChatService;
import com.ithd.chat.chatapp.service.documents.DocumentsService;
import com.ithd.chat.chatapp.service.users.UsersChatService;
import com.ithd.chat.chatapp.util.GsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Log4j2
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final StompChatService stompChatService;
    private final UsersChatService usersChatService;
    private final GsonConverter gsonConverter;
    private final DocumentsService documentsService;


    @MessageMapping("/chat/{type}/{to}")
    @SendTo("/topic/messages")
    public void getMessages(@DestinationVariable String type,@DestinationVariable String to,MessageModel message) throws SQLException {
        log.info("handling and message " + message + " to " + to);
        String filteredMessage = "";

        if (message.getMessage().contains("\n") && null != message.getMessage()) {
            filteredMessage = message.getMessage().replace("\n", "");
        }else {
            filteredMessage = message.getMessage();
        }
        MessagingStompChat chatData = MessagingStompChat
                .builder()
                .messageFrom(message.getTo())
                .messageTo(message.getFrom())
                .messageBody(filteredMessage)
                .received(0L)
                .messageType(type.toUpperCase())
                .build();

        if(type.equals("upload")){
            Long documentId = documentsService.findDocumentsId();
            chatData.setDocumentId(documentId);
        }

        ResponseEntity<BaseResponse<?>> saveChatResponse = stompChatService.save(chatData);
        log.info("users saveChatResponse date {}", gsonConverter.convertObjectToMapObject(saveChatResponse));

        UserChatDataController userChatDataController = new UserChatDataController();
        int updatedValue = userChatDataController.updateDateByUserId(message);
        log.info("users toObjectResponse date {}", updatedValue);

        BaseResponse<?> toObjectResponse = usersChatService.getUserObjectAction(message.getFrom());
        log.info("users toObjectResponse date {}", gsonConverter.convertObjectToMapObject(toObjectResponse));

        message.setMessageType(type);
        message.setReceived(0l);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
    }

//    @MessageMapping
//    public void getBooking(String to, String fromId, Map<String, Object> booking) throws SQLException {
//        log.info("handling and message " + booking + " to " + to);
//        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, booking);
//    }

    @MessageMapping("/chat/{type}/{to}/{from}")
    public void getMessages(@DestinationVariable String type,@DestinationVariable String to,@DestinationVariable String from,MessageModel message) throws SQLException {
        MessagingStompChat chatData = MessagingStompChat
                .builder()
                .messageFrom(message.getTo())
                .messageTo(message.getFrom())
                .messageBody(message.getMessage())
                .received(0L)
                .messageType(type.toUpperCase())
                .build();
        log.info("chatData================================>{}",chatData);
        ResponseEntity<BaseResponse<?>> saveChatResponse = stompChatService.save(chatData);
        log.info("users saveChatResponse date {}================================>", gsonConverter.convertObjectToMapObject(saveChatResponse));
        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);

    }

}
