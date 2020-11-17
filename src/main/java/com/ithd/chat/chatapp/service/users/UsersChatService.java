package com.ithd.chat.chatapp.service.users;


import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.chatserver.model.MessageModel;
import com.ithd.chat.chatapp.model.users.UsersModel;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;

public interface UsersChatService {
    BaseResponse<?> getUsersListByUpdateDateTimeAndAdminId(Long id);
    BaseResponse<?> getUserObjectAction(String id);
    BaseResponse<?> getUserObjectActionByUserId(String id,String adminId);
    BaseResponse<?> getUserObjectByUserId(String id,String adminId);
    BaseResponse<?> createUserAndRoom(String employeeId);


    //Rest
    BaseResponse<?> getUsersObjectsById(String adminId, String id);

    ResponseEntity<BaseResponse<?>> getUserObjectRest(String userid);

    ResponseEntity<BaseResponse<?>> updateDateByUserId(MessageModel message);

    UsersModel getUserById(String userId) throws SQLException;

    ResponseEntity<BaseResponse<?>> getAdminObjectRest(String fromId);

    int getCompanyIdByUserID(long fromId);
}
