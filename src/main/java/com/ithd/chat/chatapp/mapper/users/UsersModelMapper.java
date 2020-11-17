package com.ithd.chat.chatapp.mapper.users;


import com.ithd.chat.chatapp.api.users.UsersResponseDto;
import com.ithd.chat.chatapp.model.chatroom.ChatRoom;
import com.ithd.chat.chatapp.model.users.Users;
import com.ithd.chat.chatapp.model.users.UsersModel;
import com.ithd.chat.chatapp.repository.chat.StompChatRepository;
import com.ithd.chat.chatapp.service.chat.StompChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsersModelMapper {

    public UsersResponseDto fromUserIdToUserObject(UsersModel usersModel) {
        return UsersResponseDto.builder()
                .id(usersModel.getId())
                .fullName(usersModel.getFullName())
                .status(usersModel.getStatus())
                .userImage(usersModel.getUserImage())
                .age(usersModel.getAge())
                .companyName(usersModel.getCompanyName())
                .userId(usersModel.getUserId())
                .updated(usersModel.getUpdated().toString())
                .email(usersModel.getEmail())
                .phoneNumber(usersModel.getPhoneNumber())
                .build();
    }

//    public UsersModel fromUsersEEToUserResponseObject(Users users) {
//        return UsersModel
//                .builder()
//                .email(users.getEmail())
//                .userId(Long.valueOf(String.valueOf(users.getId())))
//                .phoneNumber(users.getPhoneNumber())
//                .age((long) users.getAge())
//                .fullName(users.getFirstName() + " " + users.getLastName())
//                .status((long) users.getStatus())
//                .userImage(users.getUserImage())
//                .chatRoom(ChatRoom.builder().chatRoomId(randomString(20)).build())
//                .role("EMPLOYEE")
//                .build();
//    }

    public UsersResponseDto fromUserRequestDtoToCreateNewUserResponse(UsersModel savedUsersModel) {
        return UsersResponseDto
                .builder()
                .email(savedUsersModel.getEmail())
                .userId(savedUsersModel.getId())
                .phoneNumber(savedUsersModel.getPhoneNumber())
                .age(savedUsersModel.getAge())
                .fullName(savedUsersModel.getFullName())
                .status(savedUsersModel.getStatus())
                .userImage(savedUsersModel.getUserImage())
                .chatRoom(savedUsersModel.getChatRoom())
                .adminModels(savedUsersModel.getAdminModels())
                .build();
    }

//    private String randomString(int count) {
//        return RandomStringUtils.randomAlphabetic(count);
//    }

    public UsersResponseDto fromUserModelToUserResponse(@Nullable UsersModel usersModel) {
        return UsersResponseDto
                .builder()
                .email(usersModel.getEmail())
                .userId(usersModel.getId())
                .phoneNumber(usersModel.getPhoneNumber())
                .age(usersModel.getAge())
                .fullName(usersModel.getFullName())
                .status(usersModel.getStatus())
                .userImage(usersModel.getUserImage())
                .role(usersModel.getRole())
                .chatRoom(usersModel.getChatRoom())
                .address(usersModel.getAddress())
                .companyId(usersModel.getCompanyId())
                .firstName(usersModel.getFirstName())
                .lastName(usersModel.getLastName())
                .build();
    }

    public UsersModel fromUserResponseToUserModel(@Nullable UsersResponseDto usersResponseDto) {
        UsersModel usersModel = new UsersModel();
        usersModel.setEmail(usersResponseDto.getEmail());
        usersModel.setPhoneNumber(usersResponseDto.getPhoneNumber());
        usersModel.setAddress(usersResponseDto.getAddress());
        usersModel.setStatus(usersResponseDto.getStatus());
        usersModel.setUserImage(usersResponseDto.getUserImage());
        usersModel.setCompanyName(usersModel.getCompanyName());
        usersModel.setCompanyId(usersModel.getCompanyId());
        usersModel.setAge(usersResponseDto.getAge());
        usersModel.setRole(usersResponseDto.getRole());
        usersModel.setUserId(usersResponseDto.getId());
        usersModel.setAdminModels(usersResponseDto.getAdminModels());
        usersModel.setChatRoom(usersResponseDto.getChatRoom());
        return usersModel;
    }

    public List<UsersResponseDto>
    fromUsersEEToUserListResponseObject(List<Users> allUsers) {
        return allUsers.stream()
                .map(users -> new UsersResponseDto(
                        users.getId(),
                        users.getFirstName(),
                        users.getLastName(),
                        users.getPhoneNumber(),
                        users.getStatus(),
                        users.getEmail(),
                        users.getAge(),
                        users.getUserImage(),
                        users.getUpdateDate(),
                        users.getCompanyId(),
                        users.getComapny(),
                        users.getCount())
                ).collect(Collectors.toList());
    }


    public UsersResponseDto fromChatUserIdToUserObject(Users users) {
        UsersResponseDto usersResponseDto = new UsersResponseDto();
        usersResponseDto.setId(users.getId());
        usersResponseDto.setUserId(Long.valueOf(users.getId()));
        usersResponseDto.setFullName(users.getFirstName() + " " + users.getLastName());
        usersResponseDto.setPhoneNumber(users.getPhoneNumber());
        usersResponseDto.setStatus(Long.valueOf(users.getStatus()));
        usersResponseDto.setEmail(users.getEmail());
        usersResponseDto.setAge(Long.valueOf(users.getAge()));
        usersResponseDto.setUserImage(users.getUserImage());
        usersResponseDto.setUpdated(String.valueOf(users.getUpdateDate()));
        usersResponseDto.setRole("EMPLOYEE");
        usersResponseDto.setCompanyId(users.getCompanyId());
        usersResponseDto.setFirstName(users.getFirstName());
        usersResponseDto.setLastName(users.getLastName());
        return usersResponseDto;
    }


}
