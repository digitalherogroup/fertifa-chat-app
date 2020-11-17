package com.ithd.chat.chatapp.model.users;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private int branchId;
    private Timestamp creationDate;
    private Timestamp updateDate;
    private Timestamp lastLoginDate;
    private String domain;
    private String PackagName;
    private String discription;
    private int gender;
    private int status;
    private String userImage;
    private String comapny;
    private int packagId;
    private int companyId;
    private int age;
    private String DateString;
    private int count;
    private String token;
    private int affiliateId;
    private String role;

}