package com.ithd.chat.chatapp.stripe.payments;


import com.ithd.chat.chatapp.util.DateConverter;

public class ShoppingCart {
    private int id;
    private int order_id;
    private int user_id;
    private String serviceName;
    private float price;
    private String order_date;
    private int status;
    private int total;
    private int serviceId;
    private int type;
    private float serviceTime;
    private String priceId;
    private String productId;


    public ShoppingCart(int id, int order_id, int user_id, String serviceName, float price, String order_date, int status) {
        this.id = id;
        this.order_id = order_id;
        this.user_id = user_id;
        this.serviceName = serviceName;
        this.price = price;
        this.order_date = order_date;
        this.status=status;
    }

    public ShoppingCart() {
    }

    public ShoppingCart(int orderid, int userId, String serviceName, float servicePrice, String dates, int status) {
        this.order_id = orderid;
        this.user_id = userId;
        this.serviceName = serviceName;
        this.price = servicePrice;
        this.order_date = dates;
        this.status=status;
    }

    public ShoppingCart(int order_id, int user_id, String serviceName, float price) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.serviceName = serviceName;
        this.price = price;


    }

    public ShoppingCart(int order_id, int user_id, String serviceName, float price, int order_id1) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.serviceName = serviceName;
        this.price = price;
        this.id=order_id;
    }

    public ShoppingCart(int id, String dateString, int order_id, String serviceName, float price, int user_id, int status) {
        this.id=id;
        this.order_date = dateString;
        this.order_id=order_id;
        this.serviceName=serviceName;
        this.price=price;
        this.user_id=user_id;
        this.status=status;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(float serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getGetOrderDate() {
        return DateConverter.convertDateWithRegex(order_date);
    }
}
