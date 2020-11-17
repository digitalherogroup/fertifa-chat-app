package com.ithd.chat.chatapp.model.documents;


import com.ithd.chat.chatapp.base.BaseEntity;
import com.ithd.chat.chatapp.model.users.UsersModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "documents")
public class Documents extends BaseEntity {
    private String documentName;
    private String documentUrlLink;
    private String documentType;
    private String documentSize;
    private String userId;

}
