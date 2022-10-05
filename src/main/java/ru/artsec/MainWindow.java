package ru.artsec;


import ru.artsec.Servers.Server;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class MainWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonCreate;
    private JButton buttonRemove;
    private JTree tree1;
    private JTextArea textArea1;
    private JButton renameButton;
    private JScrollPane jScrollPane;
    private JPanel jPanel;
    Statement statement = ConnectionDatabase.getConnection().createStatement();
    Server servers = new Server();


    public MainWindow() throws SQLException {
        setContentPane(contentPane);
        setModal(true);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Система");
        DefaultTreeModel defaultTreeModelRoot = new DefaultTreeModel(root);
        tree1.setModel(defaultTreeModelRoot);

        root.add(servers.addServers(tree1));
        servers.popupMenuServer(tree1);
    }

    public static void main(String[] args) throws SQLException {
        MainWindow dialog = new MainWindow();
        dialog.setTitle("Конфигуратор Artsec");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
