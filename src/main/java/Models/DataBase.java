package Models;

import java.sql.*;

public class DataBase {
    public static final String URL = "jdbc:mysql://localhost/userjava";
    public static final String USER = "root";
    public static final String PASSWORD = "";

    public static Connection connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

