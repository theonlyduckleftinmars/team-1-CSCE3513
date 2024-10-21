package view;

import javax.swing.*;
import java.awt.*;

public class SplashScreen {

    JFrame frame;
    JLabel image;

    public SplashScreen() {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(441, 152);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        image = new JLabel(new ImageIcon("assets/photonlogo.png"));
        frame.add(image);
    }

    public void display() {
        frame.setVisible(true);


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        frame.setVisible(false);
    }
}
