package com.ithd.chat.chatapp.mapper.chat;


import com.ithd.chat.chatapp.api.chat.StompChatResponseDto;
import com.ithd.chat.chatapp.model.chat.MessagingStompChat;
import com.ithd.chat.chatapp.model.chat.MessagingStompResponseChat;
import com.ithd.chat.chatapp.util.DateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StompChatMapper {
    private final DateConverter dateConverter;

    public StompChatResponseDto fromStompChatRequestToSaveToStompChatResponse(MessagingStompChat messageModel) {
        return StompChatResponseDto.builder()
                .from(messageModel.getMessageFrom())
                .to(messageModel.getMessageTo())
                .message(messageModel.getMessageBody())
                .messageType("CHAT")
                .received(0L)
                .build();
    }

    public MessagingStompChat fromMessageModelToStompChatRequest(MessagingStompChat messageModel) {
        return MessagingStompChat.builder()
                .messageFrom(messageModel.getMessageFrom())
                .messageTo(messageModel.getMessageTo())
                .messageBody(messageModel.getMessageBody())
                .received(messageModel.getReceived())
                .messageType(messageModel.getMessageType())
                .build();
    }

    public List<MessagingStompResponseChat> fromUserIDToChatBody(List<MessagingStompChat> allByMessageFromOrMessageToOrderByCreated) {
        return allByMessageFromOrMessageToOrderByCreated.stream()
                .map(messagingStompChat -> new MessagingStompResponseChat(
                        messagingStompChat.getId(),
                        messagingStompChat.getMessageTo(),
                        messagingStompChat.getMessageFrom(),
                        messagingStompChat.getMessageBody(),
                        messagingStompChat.getMessageType(),
                        dateConverter.formatter(messagingStompChat.getCreated()),
                        messagingStompChat.getReceived(),
                        messagingStompChat.getDocumentId()
                )).collect(Collectors.toList());
    }
}
