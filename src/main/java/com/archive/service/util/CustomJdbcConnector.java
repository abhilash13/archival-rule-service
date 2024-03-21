package com.archive.service.util;

import java.sql.*;

public class CustomJdbcConnector {
    private Connection connection;

    public CustomJdbcConnector(String url, String userName, String password) {
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch ( SQLException e) {
            e.printStackTrace();
            // Handle any exceptions
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any exceptions
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any exceptions
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
