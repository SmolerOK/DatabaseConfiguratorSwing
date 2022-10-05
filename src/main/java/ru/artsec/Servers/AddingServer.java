package ru.artsec.Servers;

import ru.artsec.ConnectionDatabase;
import ru.artsec.JTreeReload;
import ru.artsec.MainWindow;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddingServer extends JDialog {
    private JPanel contentPane;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JTextField textNameServer;
    private JTextField textIPServer;
    private JTextField textPortServer;
    private JCheckBox isActiveCheckBox;
    Statement statement = ConnectionDatabase.getConnection().createStatement();
    MainWindow mainWindow = new MainWindow();
    public AddingServer(JTree jTree) throws SQLException {
        setContentPane(contentPane);
        setModal(true);

        /*
        Получаем информацию о выбранном сервере
        */
        ResultSet resultSet1 = statement.executeQuery("" +
                "SELECT * FROM ALL_SERVERS WHERE NAME_SERVER = '" + Server.name + "'");
        while (resultSet1.next()) {
            textNameServer.setText(resultSet1.getString("NAME_SERVER"));
            textIPServer.setText(resultSet1.getString("IP_SERVER"));
            textPortServer.setText(String.valueOf(resultSet1.getInt("PORT_SERVER")));
        }

        buttonSave.addActionListener(e -> {
            try {
                saveServer(jTree);
                dispose();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonCancel.addActionListener(e -> {

        });
    }

    /*
    Метод, который добавляет в БД строку с информацие о сервере
     */
    private void saveServer(JTree jTree) throws SQLException {
        int value = 0;
        int selected = 0;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM ALL_SERVERS");
        while (resultSet.next()) {
            value = resultSet.getInt("ID_SERVER");
        }

        if (isActiveCheckBox.isSelected())
            selected = 1;

        statement.execute("" +
                "INSERT INTO ALL_SERVERS (ID_SERVER, NAME_SERVER, IP_SERVER, PORT_SERVER, IS_ACTIVE_SERVER)" +
                " VALUES (" + (value + 1) + ", '" + textNameServer.getText() + "', '" + textIPServer.getText() + "', '" + textPortServer.getText() + "', " + selected + ");"
        );
        JTreeReload jTreeReload = new JTreeReload(jTree, textNameServer);
    }

    public static void main(JTree jTree) throws SQLException {
        AddingServer dialog = new AddingServer(jTree);
        dialog.setTitle("Создать сервер");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

