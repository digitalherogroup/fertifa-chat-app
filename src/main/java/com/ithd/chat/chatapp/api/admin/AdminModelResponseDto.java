package com.ithd.chat.chatapp.api.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminModelResponseDto {
    private Long id;
    private String email;
    private String address;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Long adminStatus;
    private String role;
    private Date updated;
    private int count;

    public AdminModelResponseDto(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AdminModelResponseDto(Long id) {
        this.id=id;
    }

    public AdminModelResponseDto(Long id, String firstName, String lastName,  Date updated) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.updated = updated;
    }

    public AdminModelResponseDto(Long id, String firstName, String lastName, Date updated, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.updated = updated;
        this.role=role;
    }

    public AdminModelResponseDto(long id, String firstName, String lastName, String email, String phonenumber, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phonenumber;
        this.role=role;
    }
}
