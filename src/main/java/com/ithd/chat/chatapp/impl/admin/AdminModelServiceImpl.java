package com.ithd.chat.chatapp.impl.admin;


import com.ithd.chat.chatapp.api.admin.AdminModelRequestDto;
import com.ithd.chat.chatapp.api.admin.AdminModelResponseDto;
import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.mapper.admin.AdminModelMapper;
import com.ithd.chat.chatapp.model.admin.AdminModel;
import com.ithd.chat.chatapp.repository.admin.AdminModelRepository;
import com.ithd.chat.chatapp.repository.users.UsersRepository;
import com.ithd.chat.chatapp.service.admin.AdminModelServices;
import com.ithd.chat.chatapp.service.chat.StompChatService;
import com.ithd.chat.chatapp.util.GsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminModelServiceImpl implements AdminModelServices {

    private final AdminModelMapper adminMapper;
    private final GsonConverter gsonConverter;
    private final UsersRepository usersRepository;
    private final StompChatService stompChatService;
    private final AdminModelRepository adminModelRepository;

    @Override
    public BaseResponse<?> getAdminObject(AdminModelRequestDto adminModelRequestDto) {
        //AdminController adminController = new AdminController();
        Map<String, Object> body;
        try {
            AdminModelResponseDto adminModelResponseDto = adminMapper.fromAdminRequestDtoByIdToAdminObject(adminModelRepository.getOne(adminModelRequestDto.getId()));
            //AdminModelResponseDto adminModelResponseDto =adminMapper.fromChatAdminRequestToAdmins(adminController.getAdminObjectById(adminModelRequestDto.getId()));
            body = gsonConverter.convertObjectToMapObject(adminModelResponseDto);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public BaseResponse<?> getAllAdminsForUserId(String employeeId) {
        List<String> body;
        try {
            List<AdminModel> adminModelResponseDtoList =
                    adminMapper.fromUserRequestIdToAdminRequestDtoList(usersRepository.findAdminsByUserId(Long.parseLong(employeeId)));
            body = gsonConverter.gsonObjectConverter(adminModelResponseDtoList);
            log.info("inside getAllAdminsForUserId in class {} data {}", getClass().getName(), null);
            log.info("inside getAllAdminsForUserId in class {} data {}", getClass().getName(), body);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public BaseResponse<?> getAllAdmins() {
        List<String> body;
        List<Object> data = new ArrayList<>();
        //AdminController adminController = new AdminController();
        try {
            List<AdminModelResponseDto> response =
                    adminMapper.toAdminResponseList(adminModelRepository.findAll());
            //adminMapper.toAdminsList(adminController.getAllAdmins());
            for (int i = 0; i < response.size(); i++) {
                response.get(i).setCount(stompChatService.countUnreadMessagesById(response.get(i).getId()));
                data.add(response.get(i));
            }
            body = gsonConverter.gsonObjectConverter(data);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

}
