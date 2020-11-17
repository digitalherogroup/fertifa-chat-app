package com.ithd.chat.chatapp.model.users;


import com.ithd.chat.chatapp.base.BaseEntity;
import com.ithd.chat.chatapp.model.admin.AdminModel;
import com.ithd.chat.chatapp.model.chatroom.ChatRoom;
import com.ithd.chat.chatapp.model.documents.Documents;
import com.ithd.chat.chatapp.model.orders.OrdersModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_chat")
public class UsersModel extends BaseEntity {
    private String email;
    private String fullName;
    private String phoneNumber;
    private long status;
    private String userImage;
    private String companyName;
    private long age;
    private String role;
    @Column(name = "user_id")
    private long userId;
    private int companyId;
    private String address;
    private String firstName;
    private String lastName;
    private int branchId;
    private Timestamp creationDate;
    private Timestamp updateDate;
    private Timestamp lastLoginDate;
    private String domain;
    private String PackagName;
    private String discription;
    private int gender;
    private String comapny;
    private int packagId;
    private String DateString;
    private int count;
    private String token;
    private int affiliateId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private ChatRoom chatRoom;

//    @OneToMany(mappedBy = "id",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
//    private List<Documents> documents;

//    @OneToMany(mappedBy = "id",cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
//    private List<OrdersModel> ordersModels;

    @ManyToOne
    private AdminModel adminModels;
}
