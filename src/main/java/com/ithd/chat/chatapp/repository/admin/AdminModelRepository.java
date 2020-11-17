package com.ithd.chat.chatapp.repository.admin;

import com.ithd.chat.chatapp.model.admin.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminModelRepository extends JpaRepository<AdminModel,Long> {
    List<AdminModel> findAllById(Long id);
}
