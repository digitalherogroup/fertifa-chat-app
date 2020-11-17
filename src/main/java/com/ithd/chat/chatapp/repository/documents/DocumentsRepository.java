package com.ithd.chat.chatapp.repository.documents;


import com.ithd.chat.chatapp.api.documents.DocumentsResponseDto;
import com.ithd.chat.chatapp.model.documents.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentsRepository extends JpaRepository<Documents, Long> {
    //List<Documents> findAllByUsersModelIdOrderByUpdated(Long id);
    @Query(value = "select * from documents where user_id=?1", nativeQuery = true)
    List<Documents> findAllByUserIdOrderByUpdated(Long id);

    @Query(value = "SELECT id FROM documents WHERE id=( SELECT max(id) FROM documents )", nativeQuery = true)
    Long getLastId();

    Documents findAllById(Long id);
}
