package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private final static String HOST = "jdbc:mysql://localhost:3306/jdbc_test";
    private final static String USER = "root";
    private final static String PWD = "1111";
    private final static String DRV = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        try {
            Class.forName(DRV);
            return DriverManager.getConnection(HOST, USER, PWD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}