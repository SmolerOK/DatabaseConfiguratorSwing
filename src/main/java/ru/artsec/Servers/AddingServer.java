package ru.artsec.Servers;

import ru.artsec.ConnectionDatabase;
import ru.artsec.MainWindow;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
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
    MainWindow mainWindow = new MainWindow();
    Statement statement = ConnectionDatabase.getConnection().createStatement();

    public AddingServer() throws SQLException {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSave);

        buttonSave.addActionListener(e -> {

        });

        buttonCancel.addActionListener(e -> dispose());
    }

    private void saveServer() throws SQLException {
        JTree jTree = mainWindow.getTree1();
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();
        DefaultMutableTreeNode setNode = new DefaultMutableTreeNode(textNameServer.getText());

        statement.execute("INSERT INTO ALL_SERVERS");

        selectNode.add(setNode);
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) jTree.getModel();
        defaultTreeModel.reload();
    }

    public static void main(String[] args) throws SQLException {
        AddingServer dialog = new AddingServer();
        dialog.setTitle("Создать сервер");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
