package ru.artsec;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Servers {

    ConnectionDatabase connectDB = new ConnectionDatabase();
    Statement statement = connectDB.connection.createStatement();
    DefaultMutableTreeNode servers = new DefaultMutableTreeNode("Серверы");

    public Servers() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM SERVER");
        while (resultSet.next()) {
            DefaultMutableTreeNode temp = new DefaultMutableTreeNode(resultSet.getString("NAME"));
            servers.add(temp);
        }

    }


}
