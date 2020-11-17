package com.ithd.chat.chatapp.model.products;

import com.ithd.chat.chatapp.base.BaseEntity;
import com.ithd.chat.chatapp.model.categories.Categories;
import com.ithd.chat.chatapp.model.paymenttype.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Products extends BaseEntity {
    private String imageLink;
    @Column(columnDefinition = "LONGTEXT")
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    @Column(columnDefinition = "LONGTEXT")
    private String shortDescription;
    @Column(columnDefinition = "double default 0.0")
    private Double price;
    @Column(columnDefinition = "varchar(255)")
    private String published;
    @ManyToMany
    private List<Categories> categories;
    @ManyToMany
    private List<PaymentType> paymentType;
}
