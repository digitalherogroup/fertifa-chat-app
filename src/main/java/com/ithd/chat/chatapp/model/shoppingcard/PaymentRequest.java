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
public class PaymentRequest {
    private int chatId;
    private int tax;
    private int userId;
}
