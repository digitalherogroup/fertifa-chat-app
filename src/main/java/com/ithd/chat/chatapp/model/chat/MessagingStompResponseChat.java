package com.ithd.chat.chatapp.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagingStompResponseChat {
    private Long id;
    private String messageTo;
    private String messageFrom;
    private String messageBody;
    private String messageType;
    private String created;
    private long received;
    private Long documentId;

}
