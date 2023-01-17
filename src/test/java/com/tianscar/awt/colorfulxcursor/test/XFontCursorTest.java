package com.tianscar.awt.colorfulxcursor.test;

import com.tianscar.awt.x11.ColorfulXCursor;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class XFontCursorTest {

    protected static final String TITLE = "X FontCursor Test";

    protected static void init() {
        Frame frame = new Frame(TITLE);
        frame.setBackground(Color.WHITE);
        frame.setSize(640, 480);
        frame.setCursor(ColorfulXCursor.getFontCursor(ColorfulXCursor.XC_clock));
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

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

}
