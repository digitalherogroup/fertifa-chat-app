package com.ithd.chat.chatapp.model.paymenttype;

import com.ithd.chat.chatapp.base.BaseEntity;
import com.ithd.chat.chatapp.model.products.Products;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentType extends BaseEntity {

    private String paymentTypeName;

    @ManyToMany
    private List<Products> products;
}
