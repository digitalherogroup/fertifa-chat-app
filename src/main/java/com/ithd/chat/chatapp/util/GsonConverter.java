package com.ithd.chat.chatapp.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ithd.chat.chatapp.api.admin.AdminModelResponseDto;
import com.ithd.chat.chatapp.api.chatroom.ChatRoomResponseDto;
import com.ithd.chat.chatapp.api.documents.DocumentsResponseDto;
import com.ithd.chat.chatapp.api.orders.OrdersResponseDto;
import com.ithd.chat.chatapp.api.users.UsersResponseDto;
import com.ithd.chat.chatapp.model.products.Products;
import com.ithd.chat.chatapp.model.shoppingcard.ProductObject;
import com.ithd.chat.chatapp.orders.DateDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GsonConverter {

    private final Gson gson;

    public Map<String, Object> convertObjectToMapObject(Object obj) {
        String responseJson = gson.toJson(obj);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }
    public Map<String, String> convertObjectToMapObjectw(Object obj) {
        String responseJson = gson.toJson(obj);
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }


    public Map<String, String> convertStringToMapToMapObject(Object obj) {
        String responseJson = gson.toJson(obj);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public Map<Object, Object> convertObjectToMapObjectObject(Object obj) {
        String responseJson = gson.toJson(obj);
        Type type = new TypeToken<Map<Object, Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public List<Object> convertAdminListToListObject(List<AdminModelResponseDto> adminModelList) {
        String responseJson = gson.toJson(adminModelList);
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public List<Object> convertUsersListToListObject(List<UsersResponseDto> usersResponseDtoList) {
        String responseJson = gson.toJson(usersResponseDtoList);
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public Map<String, Object> convertUsersObjectToMapObject(UsersResponseDto usersResponseDto) {
        String responseJson = gson.toJson(usersResponseDto);
        Type type = new TypeToken<Map<String, Object>>() {

        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public List<Object> convertDocumentToListObject(List<DocumentsResponseDto> documentsResponseDtoList) {
        String responseJson = gson.toJson(documentsResponseDtoList);
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public List<Object> convertOrdersToListObject(List<OrdersResponseDto> ordersModels) {
        String responseJson = gson.toJson(ordersModels);
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public List<Object> convertChatRoomResponseDtoToObject(ChatRoomResponseDto charRoomResponseDto) {
        String responseJson = gson.toJson(charRoomResponseDto);
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public List<String> convertAdminResponseObjectToListObject(List<AdminModelResponseDto> adminModelResponseDtoList) {
        String responseJson = gson.toJson(adminModelResponseDtoList);
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public List<String> gsonObjectConverter(Object obj) {
        String responseJson = gson.toJson(obj);
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public List<ProductObject> gsonObjectConverterToProducts(Products obj) {
        String responseJson = gson.toJson(obj);
        Type type = new TypeToken<List<Products>>() {
        }.getType();
        return gson.fromJson(responseJson, type);
    }


    public List<Object> convertObjectToListObject(Object obj) {
        String responseJson = gson.toJson(obj);
        Type type = new TypeToken<List<Object>>() {

        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public List<ProductObject> convertProductObjectToListObject(Object obj) {
        String responseJson = gson.toJson(obj);
        Type type = new TypeToken<List<ProductObject>>() {

        }.getType();
        return gson.fromJson(responseJson, type);
    }

    public Map<String, DateDate> convertDateDateObjectToListObject(Object obj) {
        String responseJson = gson.toJson(obj);
        Type type = new TypeToken<Map<String,DateDate>>() {

        }.getType();
        return gson.fromJson(responseJson, type);
    }


    public String toJson(Object obj){
        return gson.toJson(obj);
    }
}
