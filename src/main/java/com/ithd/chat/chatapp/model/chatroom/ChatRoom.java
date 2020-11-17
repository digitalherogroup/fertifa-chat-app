package com.ithd.chat.chatapp.model.chatroom;


import com.ithd.chat.chatapp.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chats_room")
public class ChatRoom extends BaseEntity {

    @Column(name = "chat_room_id")
    private String chatRoomId;
}
