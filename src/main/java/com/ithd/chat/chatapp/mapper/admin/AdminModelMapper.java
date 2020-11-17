package com.ithd.chat.chatapp.mapper.admin;


import com.ithd.chat.chatapp.api.admin.AdminModelResponseDto;
import com.ithd.chat.chatapp.model.admin.AdminModel;
import com.ithd.chat.chatapp.model.users.UsersModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminModelMapper {

    public AdminModelResponseDto fromAdminRequestDtoByIdToAdminObject(AdminModel adminChatRequestDto) {
        AdminModelResponseDto adminModelResponseDto = new AdminModelResponseDto();
        adminModelResponseDto.setId(adminChatRequestDto.getId());
        adminModelResponseDto.setFirstName(adminChatRequestDto.getFirstName());
        adminModelResponseDto.setLastName(adminChatRequestDto.getLastName());
        adminModelResponseDto.setPhoneNumber(adminChatRequestDto.getPhoneNumber());
        adminModelResponseDto.setAdminStatus(adminChatRequestDto.getAdminStatus());
        adminModelResponseDto.setAddress(adminChatRequestDto.getAddress());
        adminModelResponseDto.setEmail(adminChatRequestDto.getEmail());
        adminModelResponseDto.setRole(adminChatRequestDto.getRole());
        return adminModelResponseDto;
    }

    public List<AdminModelResponseDto> toAdminResponseList(List<AdminModel> adminModelRequestDtoList) {
        return adminModelRequestDtoList.stream()
                .map(adminModelRequestDto -> new AdminModelResponseDto(
                        adminModelRequestDto.getId(),
                        adminModelRequestDto.getFirstName(),
                        adminModelRequestDto.getLastName(),
                        adminModelRequestDto.getUpdated(),
                        adminModelRequestDto.getRole()
                )).collect(Collectors.toList());
    }

    public List<AdminModel> toAdminModelResponseList(List<AdminModel> adminModelRequestDtoList) {
        return adminModelRequestDtoList.stream()
                .map(adminModelRequestDto -> new AdminModel(
                        adminModelRequestDto.getId(),
                        adminModelRequestDto.getFirstName(),
                        adminModelRequestDto.getLastName()
                )).collect(Collectors.toList());
    }


    public List<AdminModel> fromUserRequestIdToAdminRequestDtoList(List<UsersModel> allByUserId) {
//        return allByUserId.stream()
//                .map(adminResponseDto -> new AdminModel(
//                        adminResponseDto.getAdminsModel().getId(),
//                        adminResponseDto.getAdminsModel().getEmail(),
//                        adminResponseDto.getAdminsModel().getRole(),
//                        adminResponseDto.getAdminsModel().getFirstName(),
//                        adminResponseDto.getAdminsModel().getLastName()
//                ))
//                .collect(Collectors.toList());
        return null;
    }

//    public AdminModelResponseDto fromChatAdminRequestToAdmins(Admins admins) {
//        return AdminModelResponseDto
//                .builder()
//                .id((long) admins.getId())
//                .firstName(admins.getFirstName())
//                .lastName(admins.getLastName())
//                .address(admins.getAddress())
//                .email(admins.getEmail())
//                .phoneNumber(admins.getPhonenumber())
//                .role(admins.getRole())
//                .build();
//    }
//
//    public List<AdminModelResponseDto> toAdminsList(List<Admins> allAdmins) {
//        return allAdmins.stream()
//                .map(adminModelRequestDto -> new AdminModelResponseDto(
//                        adminModelRequestDto.getId(),
//                        adminModelRequestDto.getFirstName(),
//                        adminModelRequestDto.getLastName(),
//                        adminModelRequestDto.getEmail(),
//                        adminModelRequestDto.getPhonenumber(),
//                        adminModelRequestDto.getRole()
//                )).collect(Collectors.toList());
//    }
}
