package com.ithd.chat.chatapp.api.admin;


import com.ithd.chat.chatapp.base.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AdminModelRequestDto extends BaseResponse {
    private Long id;
    private String email;
    private String address;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Long adminStatus;

    public AdminModelRequestDto(long id) {
        this.id=id;
    }

}
