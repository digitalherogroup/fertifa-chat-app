package com.ithd.chat.chatapp.mapper.documents;

import com.ithd.chat.chatapp.api.documents.DocumentsResponseDto;
import com.ithd.chat.chatapp.chatserver.model.MessageModel;
import com.ithd.chat.chatapp.model.documents.Documents;
import com.ithd.chat.chatapp.model.users.UsersModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocumentsMapper {
    public List<DocumentsResponseDto> fromDocumentIdToDocumentList(List<Documents> documentsList) {
        return documentsList.stream()
                .map(documents -> new DocumentsResponseDto(
                        documents.getId(),
                        documents.getDocumentName(),
                        documents.getDocumentUrlLink(),
                        documents.getDocumentType(),
                        documents.getDocumentSize(),
                        documents.getCreated()
                )).collect(Collectors.toList());
    }


    public Documents fromMultipartFileToDocument(Map<String, Object> file, String username) {
        Documents documents = new Documents();
        documents.setDocumentName(file.get("documentName").toString());
        documents.setDocumentUrlLink(file.get("documentUrlLink").toString());
        documents.setDocumentType(file.get("documentType").toString());
        documents.setDocumentSize(file.get("documentSize").toString());
        documents.setUserId(username);
        return documents;
    }

    public DocumentsResponseDto fromDocumentToDocumentResponseDto(Documents documents) {
        return DocumentsResponseDto
                .builder()
                .id(documents.getId())
                .documentName(documents.getDocumentName())
                .documentUrlLink(documents.getDocumentUrlLink())
                .documentType(documents.getDocumentType())
                .documentSize(documents.getDocumentSize())
                .userId(documents.getUserId())
                .createdDate(documents.getCreated())
                .build();
    }
}
