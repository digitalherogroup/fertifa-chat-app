package com.ithd.chat.chatapp.impl.chatroom;


import com.ithd.chat.chatapp.api.users.UsersResponseDto;
import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.mapper.users.UsersModelMapper;
import com.ithd.chat.chatapp.repository.users.UsersRepository;
import com.ithd.chat.chatapp.service.chatroom.ChatRoomService;
import com.ithd.chat.chatapp.util.GsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final UsersModelMapper usersModelMapper;
    private final UsersRepository usersRepository;
    private final GsonConverter gsonConverter;

    @Override
    public BaseResponse<?> chatRoomExistsByUserId(String id) {
        Map<String,Object> body = new HashMap<>();
        try {
            if (usersRepository.getAllByUserId(Long.parseLong(id)) == null) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("no room for employee id %s", id));
            }
            UsersResponseDto usersResponseDto = usersModelMapper.fromUserModelToUserResponse(usersRepository.getAllByUserId(Long.parseLong(id)));
            body = gsonConverter.convertUsersObjectToMapObject(usersResponseDto);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),body);
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }
}
