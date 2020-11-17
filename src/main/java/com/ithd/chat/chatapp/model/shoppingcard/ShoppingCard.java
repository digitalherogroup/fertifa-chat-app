package com.ithd.chat.chatapp.model.shoppingcard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCard {
    private int id;
    private int order_id;
    private long user_id;
    private String serviceName;
    private float price;
    private String order_date;
    private int status;
    private int total;
    private String firstname;
    private String lastName;
    private String phoneNumber;
    private String email;
    private int serviceId;
    private int type;
    private int chatId;
    private float serviceTime;
    private String productId;
    private String priceId;

    public ShoppingCard(int order_id, int user_id, String serviceName, float getServicePrice, String s, int status) {
     this.order_id = order_id;
     this.user_id = user_id;
     this.serviceName = serviceName;
     this.price = getServicePrice;
     this.order_date = s;
     this.status = status;
    }
}
