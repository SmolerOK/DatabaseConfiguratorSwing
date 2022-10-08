package ru.artsec.Servers;

import ru.artsec.ConnectionDatabase;
import ru.artsec.JTreeAction.JTreeDelete;
import ru.artsec.JTreeAction.JTreeReload;
import ru.artsec.JTreeAction.JTreeRename;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Server {
    static String name;
    Statement statement = ConnectionDatabase.getConnection().createStatement();
    DefaultMutableTreeNode servers = new DefaultMutableTreeNode("Серверы");

    public Server() throws SQLException {
    }

    public DefaultMutableTreeNode addServers() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM SERVER");
        while (resultSet.next()) {
            DefaultMutableTreeNode temp = new DefaultMutableTreeNode(resultSet.getString("NAME"));
            servers.add(temp);
        }
        return servers;
    }

    public void popupMenu(JTree jTree, JPanel jPanel, JTextField nameServer, JTextField ip, JTextField port, JCheckBox activeCheck, JButton save, JLabel id) {
        JPopupMenu jPopupMenuCreate = new JPopupMenu("Create");
        JMenuItem create = new JMenuItem("Создать сервер");
        jPopupMenuCreate.add(create);

        JPopupMenu jPopupMenuEditAndDelete = new JPopupMenu("Create");
        JMenuItem delete = new JMenuItem("Удалить");
        jPopupMenuEditAndDelete.add(delete);

        create.addActionListener(e -> addedRightMenu(jPanel, nameServer, ip, port, activeCheck, save, id));

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
                        if (SwingUtilities.isRightMouseButton(e) && gettingSelectedServer(select)) {
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

    public void addedRightMenu(JTree jTree, JPanel jPanel, JLabel id, JTextField nameServer, JTextField ip, JTextField port, JCheckBox activeCheck, JButton save, JButton cancel, JButton edit) {
        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();
                    String names = String.valueOf(selectNode);
                    jPanel.setVisible(SwingUtilities.isLeftMouseButton(e) && gettingSelectedServer(names, id, nameServer, ip, port, activeCheck));
                    allElementsDisable(nameServer, ip, port, activeCheck, save);
                } catch (SQLException | UnknownHostException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        save.addActionListener(e -> {
            int selected = 0;
            DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();
            try {
                if (activeCheck.isSelected())
                    selected = 1;
                if (!gettingSelectedServer(String.valueOf(selectNode))) {
                    statement.execute("" +
                            "INSERT INTO SERVER ( NAME, IP, PORT, ACTIVE)" +
                            " VALUES ('" + nameServer.getText() + "', '" + ip.getText() + "', '" + port.getText() + "', " + selected + ");"
                    );
                    JTreeReload jTreeReload = new JTreeReload(jTree, nameServer);
                } else {
                    statement.executeUpdate("" +
                            "UPDATE SERVER " +
                            "SET NAME = '" + nameServer.getText() + "', IP = '" + ip.getText() + "', PORT = " + port.getText() + ", ACTIVE = " + selected + " " +
                            "WHERE NAME = '" + selectNode + "';"
                    );
                    JTreeRename jTreeRename = new JTreeRename(jTree, nameServer);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            jPanel.setVisible(false);
        });

        edit.addActionListener(e -> allElementsEnable(nameServer, ip, port, activeCheck, save));

        cancel.addActionListener(e -> allElementsDisable(nameServer, ip, port, activeCheck, save));
    }

    public void addedRightMenu(JPanel jPanel, JTextField nameServer, JTextField ip, JTextField port, JCheckBox activeCheck, JButton save, JLabel id) {
        nameServer.setText("");
        ip.setText("");
        port.setText("");
        id.setText("");
        allElementsEnable(nameServer, ip, port, activeCheck, save);
        jPanel.setVisible(true);
    }

    public boolean gettingSelectedServer(String names, JLabel id, JTextField nameServer, JTextField ip, JTextField port, JCheckBox activeCheck) throws SQLException, UnknownHostException {

        boolean isName = false;
        ArrayList<String> server = new ArrayList<>();

        //int ipAddress = 0;
        //String ipAddressConvert = null;
        ResultSet resultSet = statement.executeQuery("" +
                "SELECT * FROM SERVER;"
        );
        while (resultSet.next()) {
            server.add(resultSet.getString("NAME"));
            //ipAddress = resultSet.getInt("IP");
            //ipAddressConvert = longToIp(ipAddress);
        }
        for (String name : server) {
            if (names.equals(name)) {
                isName = true;
                break;
            }
        }
        
        /*
        Получаем информацию о выбранном сервере
        */
        ResultSet resultSet1 = statement.executeQuery("" +
                "SELECT * FROM SERVER WHERE NAME = '" + Server.name + "'");
        while (resultSet1.next()) {
            int selected = resultSet1.getInt("ACTIVE");
            if (selected == 1) activeCheck.setSelected(true);
            id.setText("ID SERVER: " + resultSet1.getString("ID_SERVER"));
            nameServer.setText(resultSet1.getString("NAME"));
            ip.setText(longToIp(resultSet1.getInt("IP")));
            port.setText(String.valueOf(resultSet1.getInt("PORT")));
            break;
        }
        return isName;
    }

    public String longToIp(int ip) {

        return
                (ip & 0xFF) + "." +
                        ((ip >> 8) & 0xFF) + "." +
                        ((ip >> 16) & 0xFF) + "." +
                        ((ip >> 24) & 0xFF);
    }

//    public static String longToIp(long ip) {
//        StringBuilder sb = new StringBuilder(15);
//
//        for (int i = 0; i < 4; i++) {
//            sb.insert(0, ip & 0xff);
//
//            if (i < 3) {
//                sb.insert(0, '.');
//            }
//
//            ip >>= 8;
//        }
//
//        return sb.toString();
//    }

    void allElementsEnable(JTextField nameServer, JTextField ip, JTextField port, JCheckBox activeCheck, JButton save) {
        nameServer.setEnabled(true);
        ip.setEnabled(true);
        port.setEnabled(true);
        save.setEnabled(true);
        activeCheck.setEnabled(true);
    }

    void allElementsDisable(JTextField nameServer, JTextField ip, JTextField port, JCheckBox activeCheck, JButton save) {
        nameServer.setEnabled(false);
        ip.setEnabled(false);
        port.setEnabled(false);
        save.setEnabled(false);
        activeCheck.setEnabled(false);
    }

    public boolean gettingSelectedServer(String names) throws SQLException {
        boolean isName = false;
        ArrayList<String> server = new ArrayList<>();

        ResultSet resultSet = statement.executeQuery("" +
                "SELECT * FROM SERVER "
        );
        while (resultSet.next()) {
            server.add(resultSet.getString("NAME"));
        }
        for (String name : server) {
            if (names.equals(name)) {
                isName = true;
                break;
            }
        }
        return isName;
    }
}
