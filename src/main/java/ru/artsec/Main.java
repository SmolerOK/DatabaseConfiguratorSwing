package ru.artsec;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Arrays;

public class Main {

    Servers servers = new Servers();

    JPopupMenu jPopupMenu = new JPopupMenu("JPopupMenu");

    public Main() throws SQLException {
        JFrame jFrame = new JFrame("Artsec");
        jFrame.setSize(300, 300);

        DefaultMutableTreeNode system = new DefaultMutableTreeNode("Система");
        JTree jTree = new JTree(system);
        system.add(servers.servers);

        jTree.addTreeSelectionListener(e -> System.out.println());

//        JMenuItem item = new JMenuItem("Cut");
//        item.addActionListener(e -> System.out.println("13123"));
//        jPopupMenu.add(item);
//        jPopupMenu.show(jTree, jTree.getX(), jTree.getY());

        jFrame.add(jTree);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws SQLException {
        Main main = new Main();
    }
}
