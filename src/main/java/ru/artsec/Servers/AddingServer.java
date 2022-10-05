package ru.artsec.Servers;

import ru.artsec.ConnectionDatabase;
import ru.artsec.JTreeReload;
import ru.artsec.JTreeRename;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class AddingServer extends JDialog {
    private JPanel contentPane;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JTextField textNameServer;
    private JTextField textIPServer;
    private JTextField textPortServer;
    private JCheckBox isActiveCheckBox;
    private JLabel idView;
    Statement statement = ConnectionDatabase.getConnection().createStatement();
    Server server = new Server();
    public AddingServer(JTree jTree) throws SQLException {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Создать сервер");

        /*
        Получаем информацию о выбранном сервере
        */
        ResultSet resultSet1 = statement.executeQuery("" +
                "SELECT * FROM ALL_SERVERS WHERE NAME_SERVER = '" + Server.name + "'");
        while (resultSet1.next()) {
            int selected = resultSet1.getInt("IS_ACTIVE_SERVER");
            idView.setText("ID SERVER: " + resultSet1.getString("ID_SERVER"));
            textNameServer.setText(resultSet1.getString("NAME_SERVER"));
            textIPServer.setText(resultSet1.getString("IP_SERVER"));
            textPortServer.setText(String.valueOf(resultSet1.getInt("PORT_SERVER")));
            if (selected == 1) isActiveCheckBox.setSelected(true);
        }

        buttonSave.addActionListener(e -> {
            try {
                saveServer(jTree);
                dispose();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonCancel.addActionListener(e -> dispose());
    }

    /*
    Добавляет/Переименовывает в базе строку с информацие о сервере
     */
    private void saveServer(JTree jTree) throws SQLException {
        int value = 0;
        int selected = 0;
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM ALL_SERVERS");
        while (resultSet.next()) {
            value = resultSet.getInt("ID_SERVER");
        }

        if (isActiveCheckBox.isSelected())
            selected = 1;

        if (!Server.flagCreateOrEdit) {
            statement.execute("" +
                    "INSERT INTO ALL_SERVERS (ID_SERVER, NAME_SERVER, IP_SERVER, PORT_SERVER, IS_ACTIVE_SERVER)" +
                    " VALUES (" + (value + 1) + ", '" + textNameServer.getText() + "', '" + textIPServer.getText() + "', '" + textPortServer.getText() + "', " + selected + ");"
            );
            JTreeReload jTreeReload = new JTreeReload(jTree, textNameServer);
        } else {
            statement.executeUpdate("" +
                    "UPDATE ALL_SERVERS " +
                    "SET NAME_SERVER = '" + textNameServer.getText() + "', IP_SERVER = '" + textIPServer.getText() + "', PORT_SERVER = " + textPortServer.getText() + ", IS_ACTIVE_SERVER = " + selected + " " +
                    "WHERE NAME_SERVER = '" + selectNode + "';"
            );
            JTreeRename jTreeRename = new JTreeRename(jTree, textNameServer);
        }

    }

//    public static void main(JTree jTree) throws SQLException {
//        AddingServer dialog = new AddingServer(jTree);
//        dialog.setTitle("Создать сервер");
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}

