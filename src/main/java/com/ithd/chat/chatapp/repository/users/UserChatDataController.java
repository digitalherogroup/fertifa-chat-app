package com.ithd.chat.chatapp.repository.users;


import com.ithd.chat.chatapp.chatserver.model.MessageModel;
import com.ithd.chat.chatapp.connection.DBConnection;
import com.ithd.chat.chatapp.model.users.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserChatDataController {

    private Connection ConnectToData() throws SQLException {
        return DBConnection.getConnectionToDatabase();
    }

    public List<Users> getAllUsersForChat() throws SQLException {
        List<Users> usersList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT * FROM `users` WHERE status=1 order by updated_date desc ";
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            ListOfUsersForChat(set, usersList);

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
        return usersList;
    }

    private void ListOfUsersForChat(ResultSet set, List<Users> usersList) throws SQLException {
        Users users = null;

        while (set.next()) {
            users = new Users();
            users.setId(set.getInt("id"));

            if (set.getString("role") == null) {
                users.setRole("role");
            } else {
                users.setRole(set.getString("role"));
            }

            if (set.getString("company") == null) {
                users.setComapny("company");
            } else {
                users.setComapny(set.getString("company"));
            }

            if (set.getString("email") == null) {
                users.setEmail("email");
            } else {
                users.setEmail(set.getString("email"));
            }
            if (set.getInt("age") == 0) {
                users.setAge(18);
            } else {
                users.setAge(set.getInt("age"));
            }

            if (set.getInt("status") == 0) {
                users.setStatus(0);
            } else {
                users.setStatus(set.getInt("status"));
            }

            if (set.getTimestamp("updated_date") == null) {
                users.setUpdateDate(new Timestamp(new Date().getTime()));
            } else {
                users.setUpdateDate(set.getTimestamp("updated_date"));
            }

            if (set.getString("imagelink") == null) {
                users.setUserImage("");
            } else {
                users.setUserImage(set.getString("imagelink"));
            }

            if (set.getString("phonenumber") == null) {
                users.setPhoneNumber("Unknown ");
            } else {
                users.setPhoneNumber(set.getString("phonenumber"));
            }
            if(set.getInt("companyId") == 0 ){
                users.setCompanyId(0);
            }else {
                users.setCompanyId(set.getInt("companyId"));
            }
            if (null != set.getString("firstname")) {
                users.setFirstName(set.getString("firstname"));
            }else{
                users.setFirstName("Unknown ");
            }

            if (null != set.getString("lastname")) {
                users.setLastName(set.getString("lastname"));
            }else{
                users.setLastName("Unknown ");
            }
            users.setCount(0);
            usersList.add(users);
        }
    }

    public boolean userIsExistsById(long parseLong) throws SQLException {
        boolean emailIsExists = false;
        PreparedStatement statment = null;
        ResultSet set = null;
        Connection connection = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT EXISTS (SELECT * FROM users WHERE id = '" + parseLong + "' )";
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            while (set.next()) {
                emailIsExists = true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("SQLException exception message : " + exception);
        } finally {
            if (statment != null) {
                statment.close();
            }
            if (set != null) {
                set.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return emailIsExists;
    }

    public Users getUserObjectById(String id) throws SQLException {
        Users users = null;
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT * FROM `users` WHERE `id`=" + id;
            statment = connection.prepareStatement(sql);
            set = statment.executeQuery();
            if (set.next()) {
                users = UsersObjectUsersResponse(set);
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
        return users;
    }

    private Users UsersObjectUsersResponse(ResultSet set) throws SQLException {
        return Users.builder()
                .id(set.getInt("id"))
                .firstName(set.getString("firstName"))
                .lastName(set.getString("lastName"))
                .comapny(set.getString("company"))
                .role(set.getString("role"))
                .phoneNumber(set.getString("phonenumber"))
                .address(set.getString("address"))
                .creationDate(set.getTimestamp("creation_date"))
                .companyId(set.getInt("companyid"))
                .status(set.getInt("status"))
                .email(set.getString("email"))
                .age(set.getInt("age"))
                .userImage(set.getString("imagelink"))
                .updateDate(set.getTimestamp("updated_date"))
                .build();

    }

    public int updateDateByUserId(MessageModel message) throws SQLException {
        int rowsUpdated = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        try {
            connection = ConnectToData();
            String sql = "UPDATE `users`  " +
                    "SET `updated_date`=? WHERE id=" + message.getFrom();
            statment = connection.prepareStatement(sql);
            UpdateUser(statment);

            rowsUpdated = statment.executeUpdate();
            if (rowsUpdated > 0) {

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statment != null) {
                statment.close();
            }
        }
        return rowsUpdated;
    }

    private void UpdateUser(PreparedStatement statement) throws SQLException {
        statement.setString(1, new Timestamp(new Date().getTime()).toString());
    }


    public int findCompanyIdByUserId(long fromId) {
        int companyId = 0;
        Connection connection = null;
        PreparedStatement statment = null;
        ResultSet set = null;
        try {
            connection = ConnectToData();
            String sql = "SELECT *  FROM `users` WHERE `id`=?";
            statment = connection.prepareStatement(sql);
            statment.setLong(1, fromId);
            set = statment.executeQuery();
            while (set.next()) {
                companyId = set.getInt("companyid");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("SQLException exception message : " + exception);
        }
        return companyId;
    }
}