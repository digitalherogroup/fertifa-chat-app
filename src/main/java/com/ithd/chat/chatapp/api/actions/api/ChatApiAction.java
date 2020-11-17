package com.ithd.chat.chatapp.api.actions.api;


import com.ithd.chat.chatapp.base.BaseResponse;

public interface ChatApiAction {
    BaseResponse<?> getUsersFullDetails(String adminId, String id);
}
