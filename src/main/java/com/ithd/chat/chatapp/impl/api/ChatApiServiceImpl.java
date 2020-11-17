package com.ithd.chat.chatapp.impl.api;


import com.ithd.chat.chatapp.api.actions.api.ChatApiAction;
import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.exceptions.IdNotFoundException;
import com.ithd.chat.chatapp.impl.documents.DocumentsServiceImpl;
import com.ithd.chat.chatapp.impl.orders.OrdersServiceImpl;
import com.ithd.chat.chatapp.impl.users.UsersServiceImpl;
import com.ithd.chat.chatapp.service.api.ChatApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatApiServiceImpl implements ChatApiService, ChatApiAction {
    private final DocumentsServiceImpl documentsService;
    private final UsersServiceImpl usersService;
    private final OrdersServiceImpl ordersService;

    @Override
    public ResponseEntity<BaseResponse<?>> getDataById(String adminId, String id) {
        if (id == null) throw new IdNotFoundException(id);
        BaseResponse<?> response = getUsersFullDetails(adminId,id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @Override
    public BaseResponse<?> getUsersFullDetails(String adminId, String id) {
        Map<String, Object> body = new HashMap<>();
        try {
            BaseResponse<?> documents = documentsService.findDocumentsByUserId(id);
            BaseResponse<?> user = usersService.getUserObjectActionByUserId(adminId,id);
            BaseResponse<?> orders = ordersService.findOrdersByUserId(id);
            body.put("user", user);
            body.put("documents", documents);
            body.put("orders", orders);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }
}
