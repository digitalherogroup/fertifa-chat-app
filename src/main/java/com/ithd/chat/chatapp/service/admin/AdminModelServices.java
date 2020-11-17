package com.ithd.chat.chatapp.service.admin;


import com.ithd.chat.chatapp.api.admin.AdminModelRequestDto;
import com.ithd.chat.chatapp.api.admin.AdminModelResponseDto;
import com.ithd.chat.chatapp.base.BaseResponse;

import java.util.List;

public interface AdminModelServices {
    BaseResponse<?> getAdminObject(AdminModelRequestDto adminModelRequestDto);

    BaseResponse<?> getAllAdminsForUserId(String employeeId);

    BaseResponse<?> getAllAdmins();

}
