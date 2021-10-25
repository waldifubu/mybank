package alittlexample;

import javax.swing.*;
import java.awt.*;

class MVCView extends JFrame {

    JLabel label;

    public MVCView() {
        init();
    }

    private void init() {
        label = new JLabel(" ");
        label.setHorizontalAlignment(JLabel.CENTER);
        JButton button = new JButton("klick");
        button.addActionListener(new MVCController(this));
        this.add(label, BorderLayout.NORTH);
        this.add(button, BorderLayout.CENTER);
        this.pack();
        this.setTitle("MVC");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setText(String s) {
        label.setText(s);
    }
}