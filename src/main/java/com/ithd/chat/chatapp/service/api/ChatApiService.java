package com.ithd.chat.chatapp.service.api;

import com.ithd.chat.chatapp.base.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface ChatApiService {

    ResponseEntity<BaseResponse<?>> getDataById(String adminId, String userid);
}
