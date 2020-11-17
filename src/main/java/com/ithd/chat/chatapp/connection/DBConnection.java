package com.ithd.chat.chatapp.connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnectionToDatabase() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
           connection = DriverManager.getConnection("jdbc:mysql://204.93.169.168:3306/fertifab_db2?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT", "fertifab_root", "mnbvcxz00A!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
        }
        return connection;
    }
}

