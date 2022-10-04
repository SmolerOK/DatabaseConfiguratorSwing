package ru.artsec;

import ru.artsec.Servers.Servers;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    Servers servers = new Servers();

    public MainWindow() throws SQLException {
        setContentPane(contentPane);
        setModal(true);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Система");
        DefaultTreeModel defaultTreeModelRoot = new DefaultTreeModel(root);
        tree1.setModel(defaultTreeModelRoot);

        root.add(servers.addServers(tree1));

        buttonCreate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(tree1.getSelectionPath()).getLastPathComponent();
                DefaultMutableTreeNode setNode = new DefaultMutableTreeNode(textArea1.getText());

                try {
                    ResultSet result = statement.executeQuery("" +
                            "SELECT ID_SERVERS" +
                            " FROM SERVERS");
                    int res = 0;
                    while (result.next()) {
                        res = result.getInt(1);
                    }

                    statement.execute("" +
                            "INSERT INTO SERVERS (ID_SERVERS,SERVER_NAME)" +
                            " VALUES ('" + (res + 1) + "','" + setNode + "')");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                selectNode.add(setNode);
                DefaultTreeModel defaultTreeModel = (DefaultTreeModel) tree1.getModel();
                defaultTreeModel.reload();
            }
        });

        renameButton.addActionListener(e -> {

            DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(tree1.getSelectionPath()).getLastPathComponent();

            try {
                ResultSet result = statement.executeQuery("" +
                        "SELECT ID_SERVERS " +
                        "FROM SERVERS " +
                        "WHERE SERVER_NAME = '" + selectNode + "';");
                int res = 0;
                while (result.next()) {
                    res = result.getInt(1);
                }

                statement.executeUpdate("" +
                        " UPDATE servers" +
                        " SET server_name = '" + textArea1.getText() + "'" +
                        " WHERE id_servers = " + res + ";");

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            selectNode.setUserObject(textArea1.getText());
            DefaultTreeModel defaultTreeModel = (DefaultTreeModel) tree1.getModel();
            defaultTreeModel.reload();
        });

        buttonRemove.addActionListener(e -> {

            DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(tree1.getSelectionPath()).getLastPathComponent();

            try {
                ResultSet result = statement.executeQuery("" +
                        "SELECT ID_SERVERS " +
                        "FROM SERVERS " +
                        "WHERE SERVER_NAME = '" + selectNode + "';");
                int res = 0;
                while (result.next()) {
                    res = result.getInt(1);
                }

                statement.execute("" +
                        "DELETE FROM SERVERS" +
                        " WHERE ID_SERVERS = '" + res + "'");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            DefaultTreeModel defaultTreeModel = (DefaultTreeModel) tree1.getModel();
            defaultTreeModel.removeNodeFromParent(selectNode);
            defaultTreeModel.reload();
        });
    }

    public JTree getTree1() {
        return tree1;
    }

    public static void main(String[] args) throws SQLException {
        MainWindow dialog = new MainWindow();
        dialog.setTitle("Конфигуратор Artsec");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
