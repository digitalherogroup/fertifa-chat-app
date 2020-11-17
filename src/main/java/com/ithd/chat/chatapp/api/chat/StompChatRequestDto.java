package com.ithd.chat.chatapp.api.chat;


import com.ithd.chat.chatapp.model.users.UsersModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StompChatRequestDto {
    private String from;
    private String to;
    private String message;
    private String messageType;
    private Long received;
    private UsersModel usersModel;
}
