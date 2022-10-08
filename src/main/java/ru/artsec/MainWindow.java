package ru.artsec;


import ru.artsec.Servers.Server;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.sql.SQLException;
import java.sql.Statement;

public class MainWindow extends JDialog {
    Statement statement = ConnectionDatabase.getConnection().createStatement();
    Server servers = new Server();
    private JPanel contentPane;
    private JTree jTree;
    private JScrollPane jScrollPane;
    private JPanel mainJPanel;
    private JScrollPane jScrollPaneMenuServer;
    private JPanel server;
    private JPanel jPanelInfo;
    private JTextField textNameServer;
    private JTextField textIPServer;
    private JTextField textPortServer;
    private JCheckBox isActiveCheckBox;
    private JLabel idView;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JButton buttonEdit;

    public MainWindow() throws SQLException {
        setContentPane(contentPane);
        setModal(true);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Система");
        DefaultTreeModel defaultTreeModelRoot = new DefaultTreeModel(root);
        jTree.setModel(defaultTreeModelRoot);

        root.add(servers.addServers());
        servers.popupMenu(jTree, server, textNameServer, textIPServer, textPortServer, isActiveCheckBox, buttonSave, idView);
        servers.addedRightMenu(jTree, server, idView, textNameServer, textIPServer, textPortServer, isActiveCheckBox, buttonSave, buttonCancel, buttonEdit);
    }

    public static void main(String[] args) throws SQLException {
        MainWindow dialog = new MainWindow();
        dialog.setTitle("Конфигуратор Artsec");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
