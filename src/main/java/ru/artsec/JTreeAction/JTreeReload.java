package ru.artsec.JTreeAction;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Objects;

public class JTreeReload {
    public JTreeReload(JTree jTree, JTextField jTextField) {
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();
        DefaultMutableTreeNode setNode = new DefaultMutableTreeNode(jTextField.getText());

        selectNode.add(setNode);
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) jTree.getModel();
        defaultTreeModel.reload();
    }
}
