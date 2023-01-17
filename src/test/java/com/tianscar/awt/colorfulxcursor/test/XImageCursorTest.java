package com.tianscar.awt.colorfulxcursor.test;

import com.tianscar.awt.x11.ColorfulXCursor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class XImageCursorTest {

    protected static final String TITLE = "X ImageCursor Test";

    protected static void init() {
        Frame frame = new Frame(TITLE);
        frame.setBackground(Color.WHITE);
        frame.setSize(640, 480);
        BufferedImage pencils;
        try {
            pencils = ImageIO.read(Objects.requireNonNull(CursorComparison.class.getResourceAsStream("/pencils.png")));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        frame.setCursor(ColorfulXCursor.createImageCursor(pencils, new Point(), "Colorful Xcursor"));
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
