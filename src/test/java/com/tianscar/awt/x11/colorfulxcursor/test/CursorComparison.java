package com.tianscar.awt.x11.colorfulxcursor.test;

import com.tianscar.awt.x11.ColorfulXCursor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class CursorComparison {

    private static final String TITLE = "Cursor Comparison";
    private static final String BUTTON = "Click to change the cursor";
    private static final String ORIGINAL = "Original XAWT Custom Cursor";
    private static final String COLORFUL = "Colorful Xcursor";

    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.setBackground(Color.WHITE);
        frame.setSize(640, 480);
        Button button = new Button(BUTTON);
        button.setFont(new JLabel().getFont().deriveFont(32f));
        BufferedImage pencils;
        try {
            pencils = ImageIO.read(Objects.requireNonNull(CursorComparison.class.getResourceAsStream("/pencils.png")));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        Cursor original = Toolkit.getDefaultToolkit().createCustomCursor(pencils, new Point(), ORIGINAL);
        Cursor colorful = new ColorfulXCursor(pencils, new Point(), COLORFUL);
        frame.setCursor(original);
        frame.setTitle(TITLE + " | Current: " + ORIGINAL);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frame.getCursor() == original) {
                    frame.setCursor(colorful);
                    frame.setTitle(TITLE + " | Current: " + COLORFUL);
                }
                else {
                    frame.setCursor(original);
                    frame.setTitle(TITLE + " | Current: " + ORIGINAL);
                }
            }
        });
        frame.add(button);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

}
