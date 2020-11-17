package com.ithd.chat.chatapp.repository.orders;

import com.ithd.chat.chatapp.model.orders.OrdersModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<OrdersModel,Long> {
    //List<OrdersModel> findAllByUsersModelIdOrderByUpdated(Long id);
}
