package com.ithd.chat.chatapp.service.users;

import com.ithd.chat.chatapp.api.products.ProductsResponseDto;

import java.util.List;

public interface ProductsService {
    List<ProductsResponseDto> findAll();
}
