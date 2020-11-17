package com.ithd.chat.chatapp.api.chat;


import com.ithd.chat.chatapp.base.BaseEntity;
import com.ithd.chat.chatapp.model.users.UsersModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class StompChatResponseDto extends BaseEntity {
    private String from;
    private String to;
    private String message;
    private String messageType;
    private Long received;
    private UsersModel usersModel;
}
