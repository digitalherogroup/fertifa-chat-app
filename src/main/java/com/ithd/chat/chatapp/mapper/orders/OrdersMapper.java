package com.ithd.chat.chatapp.mapper.orders;


import com.ithd.chat.chatapp.api.orders.OrdersRequestDto;
import com.ithd.chat.chatapp.api.orders.OrdersResponseDto;
import com.ithd.chat.chatapp.api.users.UsersResponseDto;
import com.ithd.chat.chatapp.model.orders.OrdersModel;
import com.ithd.chat.chatapp.model.shoppingcard.ProductObject;
import com.ithd.chat.chatapp.model.shoppingcard.ShoppingCard;
import com.ithd.chat.chatapp.model.users.UsersModel;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdersMapper {

    public List<OrdersResponseDto> fromOrdersIdToDocumentList(List<OrdersModel> ordersModelList) {
        return ordersModelList.stream()
                .map(ordersModel -> new OrdersResponseDto(
                        ordersModel.getId()
                )).collect(Collectors.toList());
    }


    public List<OrdersResponseDto> fromUserIdAndUserAppControllerToChatOrderListObject(List<ShoppingCard> orderLists) {
        return orderLists.stream()
                .map(ordersModel -> new OrdersResponseDto(
                        (long) ordersModel.getId(),
                        (int) ordersModel.getUser_id(),
                        ordersModel.getServiceName(),
                        ordersModel.getPrice(),
                        ordersModel.getOrder_date(),
                        ordersModel.getStatus(),
                        ordersModel.getTotal()
                ))
                .collect(Collectors.toList());
    }

    public OrdersResponseDto fromOrderIdToShoppingDto(ShoppingCard shoppingCard) {
       OrdersResponseDto ordersResponseDto = new OrdersResponseDto();
       ordersResponseDto.setId((long) shoppingCard.getId());
       ordersResponseDto.setOrder_date(shoppingCard.getOrder_date());
       ordersResponseDto.setOrder_id(shoppingCard.getOrder_id());
       ordersResponseDto.setPrice(shoppingCard.getPrice());
       ordersResponseDto.setServiceName(shoppingCard.getServiceName());
       ordersResponseDto.setTotal(shoppingCard.getTotal());
       ordersResponseDto.setStatus(shoppingCard.getStatus());
       ordersResponseDto.setUser_id((int) shoppingCard.getUser_id());
       return ordersResponseDto;

    }

    public OrdersModel fromOrderRequestToOrderResponseDto(OrdersRequestDto ordersRequestDto) {
        OrdersModel ordersModel = new OrdersModel();
        ordersModel.setOrderTitle(ordersModel.getOrderTitle());
        //ordersModel.setUsersModel(UsersModel.builder().userId((long) ordersRequestDto.getUser_id()).build());
        ordersModel.setPrice(ordersModel.getPrice());
        ordersModel.setServiceName(ordersRequestDto.getServiceName());
        ordersModel.setStatus(0);
        ordersModel.setQuantity("1");
        ordersModel.setTax(ordersModel.getTax());
        ordersModel.setFinalTotal(ordersModel.getFinalTotal());
        return ordersModel;
    }

    public OrdersResponseDto fromOrderModelToOrderResponseDto(OrdersModel orders) {
        return OrdersResponseDto
                .builder()
                .id(orders.getId())
                .price(orders.getPrice())
                .serviceName(orders.getServiceName())
                .status(orders.getStatus())
                .quantity(orders.getQuantity())
                .tax(orders.getTax())
                .finalTotal(orders.getTotal())
                .chatId(orders.getChatId())
                .build();
    }

}
