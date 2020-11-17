package com.ithd.chat.chatapp.repository.products;

import com.ithd.chat.chatapp.model.products.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products,Long> {
}
