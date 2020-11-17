package com.ithd.chat.chatapp.api.actions.documents;


import com.ithd.chat.chatapp.base.BaseResponse;

public interface DocumentsAction {
    BaseResponse<?> getDocumentsByIdAction(String id);
}
