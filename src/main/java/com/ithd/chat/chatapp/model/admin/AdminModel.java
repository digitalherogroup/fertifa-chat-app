package com.ithd.chat.chatapp.model.admin;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ithd.chat.chatapp.base.BaseEntity;
import com.ithd.chat.chatapp.model.users.UsersModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "admin")
public class AdminModel extends BaseEntity implements Serializable {
    @SerializedName("email")
    @Expose
    private String email;
    private String address;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Long adminStatus;
    private String role;

    @ManyToMany
    private List<UsersModel> usersModelList;

    public AdminModel(Long id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AdminModel(Long id, String email, String role, String firstName, String lastName) {
        super(id);
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
