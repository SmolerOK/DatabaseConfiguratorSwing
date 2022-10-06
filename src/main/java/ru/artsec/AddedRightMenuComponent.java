package ru.artsec;

import javax.swing.*;
import java.sql.SQLException;

public interface AddedRightMenuComponent {
    void addedRightMenu(JTree jTree, JPanel jPanel);
    boolean gettingSelectedServer(JTree jTree) throws SQLException;
}
