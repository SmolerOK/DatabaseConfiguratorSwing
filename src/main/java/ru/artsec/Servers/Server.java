package ru.artsec.Servers;

import ru.artsec.AddedRightMenuComponent;
import ru.artsec.ConnectionDatabase;
import ru.artsec.JTreeAction.JTreeDelete;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class Server implements AddedRightMenuComponent {

    static boolean flagCreateOrEdit = false;
    static String name;
    Statement statement = ConnectionDatabase.getConnection().createStatement();
    DefaultMutableTreeNode servers = new DefaultMutableTreeNode("Серверы");

    public Server() throws SQLException {
    }

    public DefaultMutableTreeNode addServers(JTree jTree) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM ALL_SERVERS");
        while (resultSet.next()) {
            DefaultMutableTreeNode temp = new DefaultMutableTreeNode(resultSet.getString("NAME_SERVER"));
            servers.add(temp);
        }
        return servers;
    }

    public void popupMenuServer(JTree jTree) {
        JPopupMenu jPopupMenuCreate = new JPopupMenu("Create");
        JMenuItem create = new JMenuItem("Создать сервер");
        jPopupMenuCreate.add(create);

        JPopupMenu jPopupMenuEditAndDelete = new JPopupMenu("Create");
        JMenuItem edit = new JMenuItem("Изменить");
        JMenuItem delete = new JMenuItem("Удалить");
        jPopupMenuEditAndDelete.add(edit);
        jPopupMenuEditAndDelete.add(delete);

        create.addActionListener(e -> {
            flagCreateOrEdit = false;
            AddingServersUp(jTree);
        });

        edit.addActionListener(e -> {
            flagCreateOrEdit = true;
            AddingServersUp(jTree);
        });

        delete.addActionListener(e -> {
            try {
                statement.execute("" +
                        "DELETE FROM ALL_SERVERS " +
                        "WHERE NAME_SERVER = '" + name + "';"
                );
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            JTreeDelete jTreeDelete = new JTreeDelete(jTree);
        });

        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();
                String select = String.valueOf(selectNode);
                if (SwingUtilities.isRightMouseButton(e) && select.equals("Серверы")) {
                    jPopupMenuCreate.show(jTree, e.getX(), e.getY());
                } else {
                    try {
                        if (SwingUtilities.isRightMouseButton(e) && gettingSelectedServer(jTree)) {
                            jPopupMenuEditAndDelete.show(jTree, e.getX(), e.getY());
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                name = select;
            }
        });
    }

    @Override
    public void addedRightMenu(JTree jTree, JPanel jPanel) {
        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    jPanel.setVisible(SwingUtilities.isLeftMouseButton(e) && gettingSelectedServer(jTree));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public boolean gettingSelectedServer(JTree jTree) throws SQLException {
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();
        String names = String.valueOf(selectNode);
        boolean isName = false;
        ArrayList<String> server = new ArrayList<>();

        ResultSet resultSet = statement.executeQuery("" +
                "SELECT * FROM ALL_SERVERS "
        );
        while (resultSet.next()) {
            server.add(resultSet.getString("NAME_SERVER"));
        }
        for (String name : server) {
            if (names.equals(name)) {
                isName = true;
                break;
            }
        }
        return isName;
    }

    private void AddingServersUp(JTree jTree) {
        AddingServer addingServer;
        try {
            addingServer = new AddingServer(jTree);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        addingServer.pack();
        addingServer.setVisible(true);
    }
}
