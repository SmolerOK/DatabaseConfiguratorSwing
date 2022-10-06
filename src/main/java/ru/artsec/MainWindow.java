package ru.artsec;


import ru.artsec.Servers.Server;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.sql.SQLException;
import java.sql.Statement;

public class MainWindow extends JDialog {
    Statement statement = ConnectionDatabase.getConnection().createStatement();
    //AddedRightMenuComponent addMenu = new AddedRightMenuComponent();
    Server servers = new Server();
    private JPanel contentPane;
    private JTree tree1;
    private JScrollPane jScrollPane;
    private JPanel mainJPanel;
    private JScrollPane jScrollPaneMenuServer;
    private JPanel Server;
    private JPanel jPanelInfo;
    private JTextField textNameServer;
    private JTextField textIPServer;
    private JTextField textPortServer;
    private JCheckBox isActiveCheckBox;
    private JLabel idView;
    private JButton buttonSave;
    private JButton buttonCancel;

    public MainWindow() throws SQLException {
        setContentPane(contentPane);
        setModal(true);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Система");
        DefaultTreeModel defaultTreeModelRoot = new DefaultTreeModel(root);
        tree1.setModel(defaultTreeModelRoot);

        root.add(servers.addServers(tree1));
        servers.popupMenuServer(tree1);
        servers.addedRightMenu(tree1, Server);
    }

    public static void main(String[] args) throws SQLException {
        MainWindow dialog = new MainWindow();
        dialog.setTitle("Конфигуратор Artsec");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
