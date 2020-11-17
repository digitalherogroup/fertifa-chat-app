package com.ithd.chat.chatapp.api.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersResponseDto {
    private Long id;
    private int order_id;
    private int user_id;
    private String serviceName;
    private float price;
    private String order_date;
    private int status;
    private int total;
    private String quantity;
    private double tax;
    private double finalTotal;
    private int chatId;
    private float serviceTime;
    private String priceId;
    private String productId;

    public OrdersResponseDto(Long id, int userId, String serviceName, float price, String orderDate, int status, int total) {
        this.id = id;
        this.user_id = userId;
        this.serviceName = serviceName;
        this.price = price;
        this.order_date = orderDate;
        this.status = status;
        this.total = total;
    }

    public OrdersResponseDto(Long id) {
        this.id = id;
    }
}
