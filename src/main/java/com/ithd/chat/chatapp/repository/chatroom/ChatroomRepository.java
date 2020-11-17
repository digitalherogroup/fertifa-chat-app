package com.ithd.chat.chatapp.repository.chatroom;


import com.ithd.chat.chatapp.model.chatroom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<ChatRoom,Long> {

}
