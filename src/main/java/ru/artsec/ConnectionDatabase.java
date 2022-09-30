package ru.artsec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDatabase {

    private static final String URL = "jdbc:firebirdsql://192.168.8.100:3050/C:\\ttt\\111.gdb?encoding=WIN1251";
    private static final String LOGIN = "SYSDBA";
    private static final String PASSWORD = "temp";
    Connection connection;

    public ConnectionDatabase() {
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
