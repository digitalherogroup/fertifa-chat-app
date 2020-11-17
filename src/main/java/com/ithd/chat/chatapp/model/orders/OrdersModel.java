package com.ithd.chat.chatapp.model.orders;


import com.ithd.chat.chatapp.base.BaseEntity;
import com.ithd.chat.chatapp.model.users.UsersModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrdersModel extends BaseEntity {

    private int order_id;
    private int user_id;
    private String serviceName;
    private float price;
    private String order_date;
    private int status;
    private double total;
    private String orderTitle;
    private String quantity;
    private double tax;
    private double finalTotal;
    private int chatId;
    private float serviceTime;
    private String productId;
    private String priceId;
    private String paymentId;
    private String invoiceId;
}
