package ru.artsec.Servers;

import ru.artsec.ConnectionDatabase;
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
    MainWindow mainWindow = new MainWindow();
    Servers servers = new Servers();
    Statement statement = ConnectionDatabase.getConnection().createStatement();

    public AddingServer() throws SQLException {
        setContentPane(contentPane);
        setModal(true);

        buttonSave.addActionListener(e -> {
            try {
                saveServer();
                this.dispose();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonCancel.addActionListener(e -> dispose());
    }

    private void saveServer() throws SQLException {
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
                " VALUES (" + value + 1 + ", '" + textNameServer.getText() + "', '" + textIPServer.getText() + "', '" + textPortServer.getText() + "', " + selected + ");"
        );
    }

    public JTextField getTextNameServer() {
        return textNameServer;
    }

    public static void main(String[] args) throws SQLException {
        AddingServer dialog = new AddingServer();
        dialog.setTitle("Создать сервер");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
