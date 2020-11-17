package com.ithd.chat.chatapp.api.users;

import com.ithd.chat.chatapp.model.admin.AdminModel;
import com.ithd.chat.chatapp.model.chatroom.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersResponseDto implements Serializable {
    private long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private long status;
    private String userImage;
    private String companyName;
    private Long age;
    private String role;
    private long userId;
    private ChatRoom chatRoom;
    private String updated;
    private AdminModel adminModels;
    private int count;
    private String address;
    private int companyId;
    private String firstName;
    private String lastName;


    public UsersResponseDto(long id, String firstName, String lastName, String phoneNumber,
                            long status, String email, long age, String userImage, Timestamp updateDate,int companyId,String comapny, int count) {
        this.id = id;
        this.fullName = firstName + " " + lastName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.email = email;
        this.age = age;
        this.userImage = userImage;
        this.updated = updateDate.toString();
        this.companyId = companyId;
        if(this.companyId == 0){
            setFullName(comapny);
        }else {
            this.companyName = comapny;
        }
        this.count = count;
    }

}
