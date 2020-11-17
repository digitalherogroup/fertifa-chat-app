package com.ithd.chat.chatapp.model.products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StripePrice {
    private String userId;
    private String productId;
    private float price;
}
