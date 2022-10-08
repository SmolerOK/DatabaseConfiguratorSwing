package ru.artsec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDatabase {

    private static final String URL = "jdbc:firebirdsql://192.168.8.101:3050/C:\\111\\222.gdb?encoding=WIN1251";
    private static final String LOGIN = "SYSDBA";
    private static final String PASSWORD = "temp";
    public static Connection connection;

    public static Connection getConnection() throws SQLException {
        return connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
    }
}
