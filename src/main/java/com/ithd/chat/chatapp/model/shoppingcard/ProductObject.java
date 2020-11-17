package com.ithd.chat.chatapp.model.shoppingcard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductObject {
    private String count;
    private String serviceId;
    private String serviceName;
    private String price;
    private int type;
    private int userId;
    private float total;
    private float tax;
    private float finalTotal;
    private int rowId;
    private int orderId;
    private int chatId;
    private float serviceTime;
    private String priceId;
    private String productId;

}
