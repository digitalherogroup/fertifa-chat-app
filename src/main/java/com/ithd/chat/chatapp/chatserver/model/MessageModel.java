package com.ithd.chat.chatapp.chatserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class MessageModel {

    private String message;
    private String from;
    private String to;
    private String messageType;
    private Long received;
}
