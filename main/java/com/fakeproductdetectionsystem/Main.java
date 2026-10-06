package com.fakeproductdetectionsystem;

import javax.swing.SwingUtilities;

import com.fakeproductdetectionsystem.ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainFrame frame = new MainFrame();

            frame.setVisible(true);
        });
    }
}