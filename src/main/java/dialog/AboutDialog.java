package dialog;

import model.MyConstants;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends ScreenDialog {

	public AboutDialog(JFrame mainWindow) {
		super(mainWindow, true);
		setTitle("About myBank");
		Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "info2.png"));
		setIconImage(img);
		ImageIcon icon = new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "mybank.jpg"), "Zur Information");

		JLabel textforinfo = new JLabel("<html><div style='padding:10px'><h1>Verwendung auf eigene Gefahr!</h1><br>"
				+ "<p style='font-size:15px'>Dieses Projekt ist durch Langeweile entstanden.<br>" + "Eigentümer der Software ist:<br>"
				+ "<big>Waldemar Dell</big></p>&copy; 2021 Version 1.1</div></html>", icon, JLabel.CENTER);
		add(textforinfo);
		setResizable(false);
		setBounds(mainWindow.getX(), mainWindow.getY(), 400, 200);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
