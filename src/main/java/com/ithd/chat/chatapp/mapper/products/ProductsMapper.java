package com.ithd.chat.chatapp.mapper.products;

import com.ithd.chat.chatapp.api.products.ProductsRequestDto;
import com.ithd.chat.chatapp.api.products.ProductsResponseDto;
import com.ithd.chat.chatapp.model.products.Products;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductsMapper {
    public List<ProductsResponseDto> fromChatToProductsResponseDeto(List<Products> productsList) {
        return productsList.stream()
                .map(products -> new ProductsResponseDto(
                        products.getId(),
                        products.getCreated(),
                        products.getUpdated(),
                        products.getImageLink(),
                        products.getTitle(),
                        products.getShortDescription(),
                        products.getDescription(),
                        products.getPrice(),
                        products.getPublished(),
                        products.getCategories(),
                        products.getPaymentType()
                ))
                .collect(Collectors.toList());
    }

    public ProductsResponseDto fromProductIdChatToProductResponseDto(Products products) {
        return ProductsResponseDto
                .builder()
                .id(products.getId())
                .created(products.getCreated())
                .updated(products.getUpdated())
                .title(products.getTitle())
                .description(products.getDescription())
                .shortDescription(products.getShortDescription())
                .imageLink(products.getImageLink())
                .price(products.getPrice())
                .categories(products.getCategories())
                .paymentType(products.getPaymentType())
                .published(products.getPublished())
                .build();
    }

    public Products fromProductRequestToProductsObject(ProductsRequestDto productsRequestDto) {
        Products products = new Products();
        return products;
    }
}
