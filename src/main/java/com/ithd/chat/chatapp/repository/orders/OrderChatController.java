package com.ithd.chat.chatapp.repository.orders;

import com.ithd.chat.chatapp.connection.DBConnection;
import com.ithd.chat.chatapp.model.orders.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderChatController {
    private Connection ConnectToData() throws SQLException {
        return DBConnection.getConnectionToDatabase();
    }

    public int AddNewOrder(Orders orders) throws SQLException {
        int rowsAffected = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        try {
            connection = ConnectToData();
            String insertQuery = "INSERT INTO `orders`" +
                    "(`id`, `user_id`,`date_id`,`service_id`,`coast`,`status`,`timesid`,`creation_date`,`session_token`,`priceId`,`productId`) "
                    + "VALUES (Default,?,?,?,?,?,?,?,?,?,?)";
            statment = connection.prepareStatement(insertQuery);
            setStatmentOrderAdd(orders, statment);
            rowsAffected = statment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statment != null) {
                statment.close();
            }
        }

        return rowsAffected;
    }

    private void setStatmentOrderAdd(Orders orders, PreparedStatement statment) throws SQLException {
        statment.setInt(1, orders.getUser_id());
        statment.setInt(2, orders.getDate_id());
        statment.setInt(3, orders.getService_id());
        statment.setFloat(4, orders.getCoast());
        statment.setInt(5, orders.getStatus());
        statment.setInt(6, orders.getTimeId());
        statment.setTimestamp(7, orders.getCreation_date());
        statment.setString(8, orders.getSessionToken());
        statment.setString(9,orders.getPriceId());
        statment.setString(10,orders.getProductId());
    }

    public int lastId(int userId) throws SQLException {
        int orderID = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT `id` FROM `orders` WHERE `id`=( SELECT max(`id`) FROM `orders` )";
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            while (set.next()) {
                orderID = set.getInt("id");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("SQLException exception message : " + exception);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statment != null) {
                statment.close();
            }
            if (set != null) {
                set.close();
            }
        }
        return orderID;
    }
}
