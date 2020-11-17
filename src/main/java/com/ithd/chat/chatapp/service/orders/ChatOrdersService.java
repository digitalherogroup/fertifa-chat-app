package com.ithd.chat.chatapp.service.orders;


import com.ithd.chat.chatapp.api.orders.OrdersResponseDto;
import com.ithd.chat.chatapp.base.BaseResponse;

public interface ChatOrdersService {
    BaseResponse<?> findOrdersByUserId(String id);

    BaseResponse<?> findById(String id);
}
