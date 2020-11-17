package com.ithd.chat.chatapp.orders;

import com.ithd.chat.chatapp.connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DateOrderController {
    private Connection ConnectToData() throws SQLException {
        return DBConnection.getConnectionToDatabase();
    }

    public int AddNewDateOrder(DateOrder dateOrder) throws SQLException {
        int rowsAffected = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        try {
            connection = ConnectToData();
            String insertQuery = "INSERT INTO `dateorder`" +
                    "(`id`, `date`,`order_id`) "
                    + "VALUES (Default,?,?)";
            statment = connection.prepareStatement(insertQuery);
            setStatmentOrderDateAdd(dateOrder, statment);
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

    private void setStatmentOrderDateAdd(DateOrder dateOrder, PreparedStatement statment) throws SQLException {
        statment.setString(1,dateOrder.getDate());
        statment.setInt(2,dateOrder.getOrder_id());
    }

    public int getAllOrdersDate() throws SQLException {
        int dateOrderList = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet set = null;

        try {
            connection = ConnectToData();
            String sql = "SELECT id FROM dateorder WHERE id=( SELECT max(id) FROM dateorder );";
            statement = connection.createStatement();
            set = statement.executeQuery(sql);
            while (set.next()){
                dateOrderList = set.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (set != null) {
                set.close();
            }
        }
        return dateOrderList;
    }
}
