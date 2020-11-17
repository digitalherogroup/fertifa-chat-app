package com.ithd.chat.chatapp.orders;

import com.ithd.chat.chatapp.connection.DBConnection;
import com.ithd.chat.chatapp.model.orders.Orders;
import com.ithd.chat.chatapp.time.ServiceDateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersController {

    private Connection ConnectToData() throws SQLException {
        return DBConnection.getConnectionToDatabase();
    }

    public int getStatusByOrderId(String chatId) throws SQLException {
        int status = -1;
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT status  FROM `orders` WHERE `id`=" + chatId  ;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            while (set.next()){
                status = set.getInt("status");
            }

        } catch (Exception exception) {
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
        return status;
    }

    public int changeStatusToDecline(String orderId) throws SQLException {
        int updateID = 0;
        PreparedStatement statment = null;
        Connection connection = null;
        try {
            connection = ConnectToData();

            String sql = "UPDATE `orders` SET `status`=2 WHERE `id`=" + orderId;
            statment = connection.prepareStatement(sql);
            updateID = statment.executeUpdate();
            if (updateID > 0) {
                System.out.println("A FeedBack was deleted successfully!");
            }

        } catch (SQLException exception) {
            System.out.println("sqlException in Application in CATEGORY DELETE  Section  : " + exception);
            exception.printStackTrace();
        } finally {
            if (statment != null) {
                statment.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return updateID;
    }

    public List<Orders> orderById(int id) throws SQLException {
        List<Orders> ordersList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT *  FROM `orders` where `id`=" + id;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            orderStatment(set, ordersList);

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
        return ordersList;
    }


    private void orderStatment(ResultSet set, List<Orders> newsModelList) throws SQLException {
        Orders orders = null;
        while (set.next()) {
            orders = new Orders();
            orders.setId(set.getInt("id"));
            orders.setUser_id(set.getInt("user_id"));
            orders.setDate_id(set.getInt("date_id"));
            orders.setAppointment(set.getString("appointment"));
            orders.setClinic(set.getString("clinic"));
            orders.setAddress(set.getString("address"));
            orders.setCoast(set.getFloat("coast"));
            orders.setStatus(set.getInt("status"));
            orders.setService_id(set.getInt("service_id"));
            orders.setTimeId(set.getInt("timesid"));
            orders.setStatus(set.getInt("status"));
            orders.setSessionToken(set.getString("session_token"));
            newsModelList.add(orders);
        }
    }

    public int changeStatusToApprove(String orderId) throws SQLException {
        int updateID = 0;
        PreparedStatement statment = null;
        Connection connection = null;
        try {
            connection = ConnectToData();

            String sql = "UPDATE `orders` SET `status`=1 WHERE `id`=" + orderId;
            statment = connection.prepareStatement(sql);
            updateID = statment.executeUpdate();
            if (updateID > 0) {
                System.out.println("A FeedBack was deleted successfully!");
            }

        } catch (SQLException exception) {
            System.out.println("sqlException in Application in CATEGORY DELETE  Section  : " + exception);
            exception.printStackTrace();
        } finally {
            if (statment != null) {
                statment.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return updateID;
    }

    public List<Orders> getAllByOrderId(int dataId) throws SQLException {
        List<Orders> ordersList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT *  FROM `orders` where `id`=" + dataId;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            orderStatment(set, ordersList);

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
        return ordersList;
    }

    public List<ServiceDateTime> getByUserIdFullDetails(int userId,int orderId) throws SQLException {
        List<ServiceDateTime> serviceDateTimeArrayList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;

        try {
            connection = ConnectToData();
            String sql = " SELECT\n" +
                    " orders.*,\n" +
                    " users.firstname,\n" +
                    " users.lastname,\n" +
                    " GROUP_CONCAT( CONCAT_WS( '  ', `date`, `time` ) SEPARATOR ' & ' ) AS 'date_time' \n" +
                    " FROM\n" +
                    " orders\n" +
                    " LEFT JOIN `dateorder` ON `orders`.`id` = `dateorder`.`order_id`\n" +
                    " LEFT JOIN `tiimesorder` ON ( `orders`.`id` = `tiimesorder`.order_id AND `dateorder`.`id` = `tiimesorder`.date_id )\n" +
                    " INNER JOIN `users` ON `orders`.`user_id` = `users`.`id` \n" +
                    " WHERE\n" +
                    " `user_id` = ? and orders.id = ?\n" +
                    " GROUP BY\n" +
                    " `orders`.`id` \n" +
                    " ORDER BY\n" +
                    " `orders`.`id` DESC \n" +
                    " LIMIT 1";
            statment = connection.prepareStatement(sql);
            statment.setInt(1, userId);
            statment.setInt(2, orderId);
            set = statment.executeQuery();
            ListOfTimeOrders(set, serviceDateTimeArrayList);
        } catch (SQLException e) {
            e.printStackTrace();
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
        return serviceDateTimeArrayList;
    }

    private void ListOfTimeOrders(ResultSet set, List<ServiceDateTime> serviceDateTimeArrayList) throws SQLException {
        ServiceDateTime serviceDateTime = null;
        while (set.next()) {
            serviceDateTime = new ServiceDateTime();
            serviceDateTime.setId(set.getInt("id"));
            //serviceDateTime.setOrder_id(set.getInt("order_id"));
            serviceDateTime.setService_id(set.getInt("service_id"));
            serviceDateTime.setUser_id(set.getInt("user_id"));
            serviceDateTime.setDate_time(set.getString("date_time"));
            serviceDateTime.setStatus(set.getInt("status"));
            serviceDateTime.setFirstName(set.getString("firstname"));
            serviceDateTime.setLastName(set.getString("lastname"));
            serviceDateTime.setGetServicePrice(set.getFloat("coast"));

            serviceDateTimeArrayList.add(serviceDateTime);
        }
    }
}
