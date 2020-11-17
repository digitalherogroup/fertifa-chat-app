package com.ithd.chat.chatapp.service.documents;


import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.chatserver.model.MessageModel;
import org.springframework.http.ResponseEntity;

public interface DocumentsService {

    BaseResponse<?> findDocumentsByUserId(String id);

    Long findDocumentsId();
}
