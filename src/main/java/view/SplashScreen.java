package view;

import model.MyConstants;

import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;

/**
 * Just for fun - loading screen
 */
public class SplashScreen extends JWindow implements Runnable {

    private static final long serialVersionUID = 154354353L;
    private final JProgressBar bar;

    public SplashScreen(int duration) {
        setAlwaysOnTop(true);
        bar = new JProgressBar(0, duration * 1000);
        bar.setStringPainted(true);
    }

    @Override
    public void run() {
        JPanel content = (JPanel) getContentPane();
        int width = 500;
        int height = 340;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);
        JLabel image = new JLabel(new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "splash.jpg")));

        content.add(image, BorderLayout.CENTER);
        content.add(bar, BorderLayout.SOUTH);

        content.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        setVisible(true);
        pack();

        try {
            IntStream.rangeClosed(1, bar.getMaximum()).forEach(i -> {
                bar.setValue(i);
                Thread.yield();
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        setAlwaysOnTop(false);
        dispose();
    }
}
