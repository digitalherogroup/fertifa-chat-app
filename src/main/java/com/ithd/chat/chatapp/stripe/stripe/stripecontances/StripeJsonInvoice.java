package com.ithd.chat.chatapp.stripe.stripe.stripecontances;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripeJsonInvoice {
    @SerializedName("id")
    @Expose
    public String id;
}
