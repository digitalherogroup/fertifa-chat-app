package com.ithd.chat.chatapp.api.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrdersRequestDto {
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
}
