package com.ithd.chat.chatapp.model.chat;


import com.ithd.chat.chatapp.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stomp_chat")
public class MessagingStompChat extends BaseEntity {
    private String messageTo;
    private String messageFrom;
    @Column(columnDefinition = "LONGTEXT")
    private String messageBody;

    @Column(columnDefinition = "varchar(255) default 'CHAT'")
    private String messageType;
    @Column(columnDefinition = "integer default 0")
    private long received;
    private Long documentId;
    private String role;

}
