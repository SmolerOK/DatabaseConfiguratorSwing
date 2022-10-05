package ru.artsec;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Objects;

public class JTreeDelete {
    public JTreeDelete(JTree jTree) {
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) Objects.requireNonNull(jTree.getSelectionPath()).getLastPathComponent();

        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) jTree.getModel();
        defaultTreeModel.removeNodeFromParent(selectNode);
        defaultTreeModel.reload();
    }
}
