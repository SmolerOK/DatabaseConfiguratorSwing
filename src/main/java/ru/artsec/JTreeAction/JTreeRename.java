package ru.artsec.JTreeAction;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Objects;

public class JTreeRename {
    public JTreeRename(JTree jTree, JTextField jTextField) {
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();

        selectNode.setUserObject(jTextField.getText());
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) jTree.getModel();
        defaultTreeModel.reload();
    }
}
