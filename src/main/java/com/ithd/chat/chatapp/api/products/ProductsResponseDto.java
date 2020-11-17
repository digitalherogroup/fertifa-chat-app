package com.ithd.chat.chatapp.api.products;

import com.ithd.chat.chatapp.model.categories.Categories;
import com.ithd.chat.chatapp.model.paymenttype.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsResponseDto {
    private Long id;
    private Date created;
    private Date updated;
    private String imageLink;
    private String title;
    private String description;
    private String shortDescription;
    private Double price;
    private String published;
    private List<Categories> categories;
    private List<PaymentType> paymentType;
}
