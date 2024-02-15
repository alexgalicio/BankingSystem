package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private Connection establishConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/bankSystem?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String sqlUsername = "bankSystem";
        String sqlPassword = "1234";
        return DriverManager.getConnection(url, sqlUsername, sqlPassword);
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        return establishConnection();
    }

}
