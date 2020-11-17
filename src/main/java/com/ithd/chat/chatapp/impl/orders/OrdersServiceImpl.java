package com.ithd.chat.chatapp.impl.orders;


import com.ithd.chat.chatapp.api.actions.orders.OrdersAction;
import com.ithd.chat.chatapp.api.orders.OrdersResponseDto;
import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.exceptions.IdNotFoundException;
import com.ithd.chat.chatapp.mapper.orders.OrdersMapper;
import com.ithd.chat.chatapp.repository.orders.OrdersRepository;
import com.ithd.chat.chatapp.shoppingCard.ShoppingCartController;
import com.ithd.chat.chatapp.service.orders.ChatOrdersService;
import com.ithd.chat.chatapp.util.GsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements ChatOrdersService, OrdersAction {
    private final GsonConverter gsonConverter;
    private final OrdersMapper ordersMapper;
    private final OrdersRepository ordersRepository;

    @Override
    public BaseResponse<?> findOrdersByUserId(String id) {
        if (id == null) throw new IdNotFoundException(id);
        BaseResponse<?> response = getOrdersByIdAction(id);
        return new BaseResponse<>(new Date(),HttpStatus.ACCEPTED,HttpStatus.ACCEPTED.value(),response);
    }

    @Override
    public BaseResponse<?> findById(String id) {
        if(null == id) throw  new IdNotFoundException(id);
        BaseResponse<?>response = getOrderById(id);
        return new BaseResponse<>(new Date(),HttpStatus.ACCEPTED,HttpStatus.ACCEPTED.value(),response);
    }

    private BaseResponse<?> getOrderById(String id) {
        List<Object> body;
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        try {
            OrdersResponseDto ordersModels =ordersMapper.fromOrderIdToShoppingDto(shoppingCartController.getObjectById(Integer.parseInt(id)));
            body = gsonConverter.convertObjectToListObject(ordersModels);
            if (body.isEmpty()){
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s",id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public BaseResponse<?> getOrdersByIdAction(String id) {
        List<Object> body;
        ShoppingCartController shoppingCartController = new ShoppingCartController();
       // ShoppingCartController shoppingCartController = new ShoppingCartController();
        try {
             //List<OrdersResponseDto> ordersModels = ordersMapper.fromOrdersIdToDocumentList(ordersRepository.findAllByUsersModelIdOrderByUpdated(Long.parseLong(id)));
            List<OrdersResponseDto> ordersModels =ordersMapper.fromUserIdAndUserAppControllerToChatOrderListObject(shoppingCartController.getAllOrdersById(Integer.parseInt(id)));
            body = gsonConverter.convertOrdersToListObject(ordersModels);
            if (body.isEmpty()){
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s",id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }
}
