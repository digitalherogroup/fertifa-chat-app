package com.ithd.chat.chatapp.orders;

import com.ithd.chat.chatapp.connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TimesOrderController {

    private Connection ConnectToData() throws SQLException {
        return DBConnection.getConnectionToDatabase();
    }


    public int AddNewTimeOrder(TimesOrder timesOrder, int dateid) throws SQLException {
        int rowsAffected = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        try {
            connection = ConnectToData();
            String insertQuery = "INSERT INTO `tiimesorder`" +
                    "(`id`, `time`,`date_id`,`order_id`) "
                    + "VALUES (Default,?,?,?)";
            statment = connection.prepareStatement(insertQuery);
            statment.setString(1,timesOrder.getTime());
            statment.setInt(2,timesOrder.getDate_id());
            statment.setInt(3,timesOrder.getOrder_id());
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

}
