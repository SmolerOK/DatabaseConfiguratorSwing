package ru.artsec.Servers;

import ru.artsec.ConnectionDatabase;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Servers {

    Statement statement = ConnectionDatabase.getConnection().createStatement();
    DefaultMutableTreeNode servers = new DefaultMutableTreeNode("Серверы");


    public Servers() throws SQLException {
    }

    public DefaultMutableTreeNode addServers(JTree jTree) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM ALL_SERVERS");
        while (resultSet.next()) {
            DefaultMutableTreeNode temp = new DefaultMutableTreeNode(resultSet.getString("NAME_SERVER"));
            servers.add(temp);
        }
        popupMenuServer(jTree);
        return servers;
    }

    public void popupMenuServer(JTree jTree) {
        JPopupMenu jPopupMenu = new JPopupMenu("Create");
        JMenuItem create = new JMenuItem("Создать сервер");
        jPopupMenu.add(create);

        create.addActionListener(e -> {
            AddingServer addingServer = null;
            try {
                addingServer = new AddingServer();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            addingServer.pack();
            addingServer.setVisible(true);
        });

        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();
                String select = String.valueOf(selectNode);
                if (SwingUtilities.isRightMouseButton(e) && select.equals("Серверы")) {
                    jPopupMenu.show(jTree, e.getX(), e.getY());
                }
            }
        });
    }
}