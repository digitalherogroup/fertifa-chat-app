package com.ithd.chat.chatapp.repository.users;


import com.ithd.chat.chatapp.model.users.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<UsersModel,Long> {
    UsersModel getAllByUserId(Long id);
    List<UsersModel> findAllByAdminModelsIdOrderByUpdatedDesc(Long id);
    UsersModel getAllUsersModelByUserIdAndAdminModelsId(Long id,Long adminId);
    List<UsersModel> findAdminsByUserId(Long id);
    boolean existsByUserId(Long id);
}
