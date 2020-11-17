package com.ithd.chat.chatapp.api.documents;

import com.ithd.chat.chatapp.model.users.UsersModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DocumentsResponseDto {
    private Long id;
    private String documentName;
    private String documentUrlLink;
    private String documentType;
    private String documentSize;
    private UsersModel usersModel;
    private String userId;
    private Date createdDate;
    private String documentId;

    public DocumentsResponseDto(Long id) {
        this.id = id;
    }

    public DocumentsResponseDto(Long id, String documentName, String documentUrlLink, String documentType, String documentSize,Date created) {
        this.id=id;
        this.documentName = documentName;
        this.documentUrlLink = documentUrlLink;
        this.documentType = documentType;
        this.documentSize = documentSize;
        this.createdDate = created;
    }

}
