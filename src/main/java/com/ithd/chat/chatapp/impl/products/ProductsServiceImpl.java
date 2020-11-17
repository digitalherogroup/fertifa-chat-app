package com.ithd.chat.chatapp.impl.products;

import com.ithd.chat.chatapp.api.products.ProductsResponseDto;
import com.ithd.chat.chatapp.mapper.products.ProductsMapper;
import com.ithd.chat.chatapp.repository.products.ProductsRepository;
import com.ithd.chat.chatapp.service.users.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;
    private final ProductsMapper productsMapper;

    @Override
    public List<ProductsResponseDto> findAll() {
        return productsMapper.fromChatToProductsResponseDeto(productsRepository.findAll());
    }
}
