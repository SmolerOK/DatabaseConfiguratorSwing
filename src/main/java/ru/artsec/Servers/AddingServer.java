package ru.artsec.Servers;

import ru.artsec.ConnectionDatabase;
import ru.artsec.JTreeAction.JTreeReload;
import ru.artsec.JTreeAction.JTreeRename;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class AddingServer extends JDialog {
    Statement statement = ConnectionDatabase.getConnection().createStatement();
    private JPanel contentPane;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JTextField textNameServer;
    private JTextField textIPServer;
    private JTextField textPortServer;
    private JCheckBox isActiveCheckBox;
    private JLabel idView;
    private JPanel jPanelInfo;

    public AddingServer(JTree jTree) throws SQLException {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Создать сервер");

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


    }
}

