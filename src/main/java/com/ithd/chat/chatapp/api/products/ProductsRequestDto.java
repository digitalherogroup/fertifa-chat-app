package com.ithd.chat.chatapp.api.products;

import com.ithd.chat.chatapp.model.categories.Categories;
import com.ithd.chat.chatapp.model.paymenttype.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsRequestDto {
    private Long id;
    private String imageLink;
    private String title;
    private String description;
    private String shortDescription;
    private Double price;
    private String published;
    private Categories categories;
    private PaymentType paymentType;
}
