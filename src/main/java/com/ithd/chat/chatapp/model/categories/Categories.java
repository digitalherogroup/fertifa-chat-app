package com.ithd.chat.chatapp.model.categories;

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
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Categories extends BaseEntity {
    private String imageLink;
    private String categoryName;

    @ManyToMany
    private List<Products> products;
}
