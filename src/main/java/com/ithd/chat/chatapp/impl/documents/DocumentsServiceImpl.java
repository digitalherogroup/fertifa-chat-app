package com.ithd.chat.chatapp.impl.documents;


import com.ithd.chat.chatapp.api.actions.documents.DocumentsAction;
import com.ithd.chat.chatapp.api.documents.DocumentsResponseDto;
import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.chatserver.model.MessageModel;
import com.ithd.chat.chatapp.exceptions.IdNotFoundException;
import com.ithd.chat.chatapp.exceptions.ObjectNullException;
import com.ithd.chat.chatapp.mapper.documents.DocumentsMapper;
import com.ithd.chat.chatapp.model.documents.Documents;
import com.ithd.chat.chatapp.repository.documents.DocumentsRepository;
import com.ithd.chat.chatapp.service.documents.DocumentsService;
import com.ithd.chat.chatapp.util.GsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentsServiceImpl implements DocumentsService, DocumentsAction {
    private final GsonConverter gsonConverter;
    private final DocumentsMapper documentsMapper;
    private final DocumentsRepository documentsRepository;

    @Override
    public BaseResponse<?> findDocumentsByUserId(String id) {
        if (id == null) throw new IdNotFoundException(id);
        BaseResponse<?> response = getDocumentsByIdAction(id);
        return new BaseResponse<>(new Date(),HttpStatus.ACCEPTED,HttpStatus.ACCEPTED.value(), response);
    }

    @Override
    @Transient
    public Long findDocumentsId() {
        return documentsRepository.getLastId();
    }

    @Override
    public BaseResponse<?> getDocumentsByIdAction(String id) {
        List<Object> body;
        try {
            List<DocumentsResponseDto> documentsResponseDtoList =
                    documentsMapper.fromDocumentIdToDocumentList(
                            documentsRepository.findAllByUserIdOrderByUpdated(
                                    Long.parseLong(id)));
            body = gsonConverter.convertDocumentToListObject(documentsResponseDtoList);
            if (body.isEmpty()){

                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s",id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }
}
