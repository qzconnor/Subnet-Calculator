package de.sc;

import de.sc.views.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainView::new);

    }
}
