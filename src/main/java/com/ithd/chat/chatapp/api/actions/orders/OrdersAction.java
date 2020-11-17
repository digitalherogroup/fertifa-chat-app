package com.ithd.chat.chatapp.api.actions.orders;


import com.ithd.chat.chatapp.base.BaseResponse;

public interface OrdersAction {
    BaseResponse<?> getOrdersByIdAction(String id);
}
