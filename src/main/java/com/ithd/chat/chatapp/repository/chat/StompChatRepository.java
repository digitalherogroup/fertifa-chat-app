package com.ithd.chat.chatapp.repository.chat;


import com.ithd.chat.chatapp.model.chat.MessagingStompChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface StompChatRepository extends JpaRepository<MessagingStompChat, Long> {
    @Query(value = "select * from stomp_chat e WHERE e.message_from=?1 and e.message_to=?2 or e.message_from=?2 and e.message_to=?1 ORDER BY e.id ASC", nativeQuery = true)
    List<MessagingStompChat> getDataByQuery(String to, String from);

    @Query(value = "select count(*) from stomp_chat where message_from=?1 and received = 0", nativeQuery = true)
    int countById(long id);

    //UPDATE stomp_chat SET stomp_chat.received = 1 WHERE message_to = 8941 and message_from =1
    @Modifying
    @Transactional
    @Query(value = "UPDATE stomp_chat SET received = 1 WHERE message_to =?1 and message_from =?2", nativeQuery = true)
    int updateChatByIds(String toId, String fromId);

    @Query(value = "SELECT `id` FROM `stomp_chat` WHERE `id`=( SELECT max(`id`) FROM `stomp_chat` )",nativeQuery = true)
    int getFinalId();
    @Transactional
    @Modifying
    @Query(value = "UPDATE stomp_chat SET message_body = ?1 WHERE id =?2", nativeQuery = true)
    int updateChatById(String gson, String id);

    int getStatusById(Long id);

    @Query(value = "select count(*) from stomp_chat where message_from=?1 and message_to=?2 and received = 0",nativeQuery = true)
    int countAllMessagesAdminAndUser(Long admin, Long user);
}
