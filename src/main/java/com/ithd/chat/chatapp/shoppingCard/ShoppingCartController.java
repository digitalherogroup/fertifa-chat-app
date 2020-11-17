package com.ithd.chat.chatapp.shoppingCard;

import com.ithd.chat.chatapp.api.users.UsersResponseDto;
import com.ithd.chat.chatapp.connection.DBConnection;
import com.ithd.chat.chatapp.model.shoppingcard.ShoppingCard;
import com.ithd.chat.chatapp.model.users.Users;
import com.ithd.chat.chatapp.model.users.UsersModel;
import com.ithd.chat.chatapp.stripe.payments.ShoppingCart;
import com.ithd.chat.chatapp.util.Constances;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCartController {
    private Connection ConnectToData() throws SQLException {
        return DBConnection.getConnectionToDatabase();
    }

    private void setStatmentsShopping(ShoppingCart shoppingCart, PreparedStatement statment) throws SQLException {
        statment.setInt(1, shoppingCart.getUser_id());
        statment.setInt(2, shoppingCart.getOrder_id());
        statment.setString(3, shoppingCart.getServiceName());
        statment.setFloat(4, shoppingCart.getPrice());
        statment.setString(5, shoppingCart.getOrder_date());
        statment.setInt(6, shoppingCart.getStatus());
    }

    public List<ShoppingCard> getAllOrdersById(int parseInt) throws SQLException {
        List<ShoppingCard> shoppingCartList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT *  FROM `shoppingcart` WHERE `user_id`=" + parseInt ;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            ListOfShoppingCard(set, shoppingCartList);

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
        return shoppingCartList;
    }

    private void ListOfShoppingCard(ResultSet set, List<ShoppingCard> shoppingCartList) throws SQLException {
        ShoppingCard shoppingCart = null;
        while (set.next()) {
            shoppingCart = new ShoppingCard();
            shoppingCart.setId(set.getInt("id"));
            shoppingCart.setOrder_date(set.getString("order_dates"));
            shoppingCart.setOrder_id(set.getInt("order_id"));
            shoppingCart.setServiceName(set.getString("serviceName"));
            shoppingCart.setPrice(set.getFloat("price"));
            shoppingCart.setUser_id(set.getInt("user_id"));
            shoppingCart.setStatus(set.getInt("status"));
            shoppingCart.setServiceId(set.getInt("serviceId"));
            shoppingCart.setType(set.getInt("type"));
            shoppingCart.setServiceTime(set.getFloat("serviceTime"));
            shoppingCart.setPriceId(set.getString("priceId"));
            shoppingCart.setProductId(set.getString("productId"));
            shoppingCartList.add(shoppingCart);
        }
    }

    public ShoppingCard getObjectById(int orderid) throws SQLException {
        ShoppingCard shoppingCart = null;
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT *  FROM `shoppingcart` WHERE `id`=" + orderid;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            while (set.next()) {
                shoppingCart = new ShoppingCard();
                shoppingCart.setId(set.getInt("id"));
                shoppingCart.setOrder_date(set.getString("order_dates"));
                shoppingCart.setOrder_id(set.getInt("order_id"));
                shoppingCart.setServiceName(set.getString("serviceName"));
                shoppingCart.setPrice(set.getFloat("price"));
                shoppingCart.setStatus(set.getInt("status"));
                shoppingCart.setUser_id(set.getInt("user_id"));
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
        return shoppingCart;
    }


    public int saveFromChat(ShoppingCard shoppingCart) throws SQLException {
        int rowsAffected = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        try {
            connection = ConnectToData();

            String insertQuery = "INSERT INTO `shoppingcartfinal` (" +
                    "`id`,`order_id`, `serviceName`,`total_price`,`order_dates`,`status`," +
                    "`cardholderfirstname`,`cardholderlastname`, `cardholderphone`,`cardholderemail`," +
                    "`cardholderaddress`,`cardholdercity`,`cardholdercountry`,\n" +
                    "            `cardholderstat`, `cardholderpostcode`," +
                    "`stripe_order_id`,`stripe_order_amount`,`stripe_order_datecreated`," +
                    "`stripe_user_id`,`price`,`user_id`,`service_id`,`company_id`) " +
                    "VALUES (Default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            statment = connection.prepareStatement(insertQuery);

            statment.setInt(1, shoppingCart.getOrder_id());
            statment.setString(2, shoppingCart.getServiceName());
            statment.setInt(3, shoppingCart.getTotal());
            statment.setTimestamp(4, new Timestamp(new Date().getTime()));
            statment.setInt(5, 1);
            statment.setString(6, shoppingCart.getFirstname());
            statment.setString(7, shoppingCart.getLastName());
            statment.setString(8, shoppingCart.getPhoneNumber());
            statment.setString(9, shoppingCart.getEmail());
            statment.setString(10, "");
            statment.setString(11, "");
            statment.setString(12, "");
            statment.setString(13, String.valueOf(shoppingCart.getStatus()));
            statment.setString(14, "");
            statment.setString(15, String.valueOf(0));
            statment.setInt(16, 0);
            statment.setTimestamp(17, new Timestamp(new Date().getTime()));
            statment.setString(18, "");
            statment.setFloat(19, shoppingCart.getPrice());
            statment.setInt(20, (int) shoppingCart.getUser_id());
            statment.setInt(21, shoppingCart.getServiceId());
            statment.setInt(22, 0);

            rowsAffected = statment.executeUpdate();
        } catch (Exception e) {
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

    public int AddNewOrder(ShoppingCard shoppingCart) throws SQLException {
        int rowsAffected = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        try {
            connection = ConnectToData();
            String insertQuery = "INSERT INTO `shoppingcart`" +
                    "(`id`, `user_id`,`order_id`,`serviceName`,`price`,`order_dates`,`status`,`chatId`,`type`,`serviceId`,`serviceTime`,`priceId`,`productId`) "
                    + "VALUES (Default,?,?,?,?,?,?,?,?,?,?,?,?)";
            statment = connection.prepareStatement(insertQuery);
            setStatmentShopping(shoppingCart, statment);
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

    private void setStatmentShopping(ShoppingCard shoppingCart, PreparedStatement statment) throws SQLException {
        statment.setInt(1, (int) shoppingCart.getUser_id());
        statment.setInt(2, shoppingCart.getOrder_id());
        statment.setString(3, shoppingCart.getServiceName());
        statment.setFloat(4, shoppingCart.getPrice());
        statment.setString(5, shoppingCart.getOrder_date());
        statment.setInt(6, shoppingCart.getStatus());
        statment.setInt(7,shoppingCart.getChatId());
        statment.setInt(8,shoppingCart.getType());
        statment.setInt(9,shoppingCart.getServiceId());
        statment.setFloat(10,shoppingCart.getServiceTime());
        statment.setString(11,shoppingCart.getPriceId());
        statment.setString(12,shoppingCart.getProductId());
    }

    public int getLastId() throws SQLException {
        int orderID = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT `id` FROM `shoppingcart` WHERE `id`=( SELECT max(`id`) FROM `shoppingcart`)";
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

    public List<ShoppingCard> getAllOrdersAfterChat(int parseInt) throws SQLException {
        List<ShoppingCard> shoppingCartList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT *  FROM `shoppingcart` WHERE `id`=" + parseInt ;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            ListOfShoppingCard(set, shoppingCartList);

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
        return shoppingCartList;
    }

    public List<ShoppingCart> getAllByChatId(int chatId) throws SQLException {
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT *  FROM `shoppingcart` WHERE `chatId`=" + chatId ;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            ListOfShoppingCart(set, shoppingCartList);

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
        return shoppingCartList;
    }

    private void ListOfShoppingCart(ResultSet set, List<ShoppingCart> shoppingCartList) throws SQLException {
        ShoppingCart shoppingCart = null;
        while (set.next()) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setId(set.getInt("id"));
            shoppingCart.setOrder_date(set.getString("order_dates"));
            shoppingCart.setOrder_id(set.getInt("order_id"));
            shoppingCart.setServiceName(set.getString("serviceName"));
            shoppingCart.setPrice(set.getFloat("price"));
            shoppingCart.setUser_id(set.getInt("user_id"));
            shoppingCart.setStatus(set.getInt("status"));
            shoppingCart.setServiceId(set.getInt("serviceId"));
            shoppingCart.setType(set.getInt("type"));
            shoppingCart.setServiceTime(set.getFloat("serviceTime"));
            shoppingCart.setPriceId(set.getString("priceId"));
            shoppingCart.setProductId(set.getString("productId"));
            shoppingCartList.add(shoppingCart);
        }
    }

    public int updateById(ShoppingCard shoppingCard, int shoppingCardId) throws SQLException {
        int updateID = 0;
        PreparedStatement statment = null;
        Connection connection = null;
        try {
            connection = ConnectToData();
            String sql = "UPDATE `shoppingcart` SET `user_id`=?," +
                    " `order_id`=?," +
                    " `serviceName`=?," +
                    " `price`=?," +
                    " `order_dates`=?," +
                    " `status`=?," +
                    " `type`=? ," +
                    " `chatId`=?," +
                    " `serviceId`=?," +
                    " `serviceTime`=?," +
                    " `priceId`=?," +
                    " `productId`=?" +
                    " WHERE `id`=" + shoppingCardId;
            statment = connection.prepareStatement(sql);
            statment.setLong(1,shoppingCard.getUser_id());
            statment.setInt(2,shoppingCard.getOrder_id());
            statment.setString(3,shoppingCard.getServiceName());
            statment.setFloat(4,shoppingCard.getPrice());
            statment.setString(5,new Timestamp(new Date().getTime()).toString());
            statment.setInt(6,shoppingCard.getStatus());
            statment.setInt(7,shoppingCard.getType());
            statment.setInt(8,shoppingCard.getChatId());
            statment.setInt(9,shoppingCard.getServiceId());
            statment.setFloat(10,shoppingCard.getServiceTime());
            statment.setString(11,shoppingCard.getPriceId());
            statment.setString(12,shoppingCard.getProductId());

            updateID = statment.executeUpdate();
            if (updateID > 0) {
                System.out.println("A shoppingCart was Updated successfully!");
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

    public ShoppingCart getObjectByUserId(int orderid) throws SQLException {
        ShoppingCart shoppingCart = null;
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT *  FROM `shoppingcart` WHERE `user_id`=" + orderid;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            while (set.next()) {
                shoppingCart = new ShoppingCart();
                shoppingCart.setId(set.getInt("id"));
                shoppingCart.setOrder_date(set.getString("order_dates"));
                shoppingCart.setOrder_id(set.getInt("order_id"));
                shoppingCart.setServiceName(set.getString("serviceName"));
                shoppingCart.setPrice(set.getFloat("price"));
                shoppingCart.setUser_id(set.getInt("user_id"));
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
        return shoppingCart;
    }

    public List<ShoppingCart> getAllById(int userId) throws SQLException {
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT *  FROM `shoppingcart` WHERE `user_id`=" + userId + " AND `status` = 0" ;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            ListOfShoppingCart(set, shoppingCartList);

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
        return shoppingCartList;
    }




    public int AddToFinal(String paymentId, int serviceId, long UserId, Users user, int orderId, ShoppingCart shoppingCart, float total, String stripeId) throws SQLException {
        int rowsAffected = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        try {
            connection = ConnectToData();

            String insertQuery = "INSERT INTO `shoppingcartfinal` (`id`,`order_id`, `serviceName`,`total_price`,`order_dates`,`status`,`cardholderfirstname`,`cardholderlastname`, " +
                    "`cardholderphone`,`cardholderemail`,`cardholderaddress`," +
                    "           `stripe_order_id`,`stripe_order_amount`,`stripe_order_datecreated`,`stripe_user_id`,`price`,`user_id`,`service_id`,`company_id`) " +
                    "VALUES (Default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            statment = connection.prepareStatement(insertQuery);

            statment.setInt(1, shoppingCart.getOrder_id());
            statment.setString(2, shoppingCart.getServiceName());
            statment.setFloat(3, total);
            statment.setTimestamp(4, new Timestamp(new Date().getTime()));
            statment.setInt(5, Constances.STIPEUSERPAYEDSSTATUS);
            statment.setString(6, user.getFirstName());
            statment.setString(7, user.getLastName());
            statment.setString(8, user.getPhoneNumber());
            statment.setString(9, user.getEmail());
            statment.setString(10, user.getAddress());
            statment.setString(11, String.valueOf(paymentId));
            statment.setInt(12, (int) shoppingCart.getPrice());
            statment.setTimestamp(13, new Timestamp(new Date().getTime()));
            statment.setString(14, stripeId);
            statment.setFloat(15, shoppingCart.getPrice());
            statment.setInt(16, (int) UserId);
            statment.setInt(17, serviceId);
            statment.setInt(18, user.getCompanyId());


            rowsAffected = statment.executeUpdate();
        } catch (Exception e) {
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

    public int UpdateStatusById(int orderIdFroShop, int userId) throws SQLException {
        int updateID = 0;
        PreparedStatement statment = null;
        Connection connection = null;
        try {
            connection = ConnectToData();

            String sql = "UPDATE `shoppingcart` SET `status`=1 WHERE `order_id`=" + orderIdFroShop;
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

    public int updateShoppingCardStatus(int id) throws SQLException {
        int updateID = 0;
        PreparedStatement statment = null;
        Connection connection = null;
        try {
            connection = ConnectToData();

            String sql = "UPDATE `shoppingcart` SET `status`=1 WHERE `id`=" + id;
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

    public int getStatusById(String chatId) throws SQLException {
        int status = -1;
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT status  FROM `shoppingcart` WHERE `chatId`=" + chatId  ;
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
}
