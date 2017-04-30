package fr.thisismac.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class ImgCheckBox extends JButton implements ActionListener {

	private static final long serialVersionUID = 8402412970611710434L;
	private Image img;

	private Panel panel;

	String file[];

	private boolean isChecked;

	public ImgCheckBox(Panel p, int x, int y, int w, int h, String... file) {

		this.addActionListener(this);
		setBounds(x, y, w, h);
		this.panel = p;

		this.file = file;

		try {
			img = ImageIO.read(Panel.class.getResource(file[1]));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		this.setBorderPainted(false);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setVisible(true);

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);

		panel.home.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (isChecked) {
			this.isChecked = false;
			try {
				img = ImageIO.read(Panel.class.getResource(file[1]));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			this.isChecked = true;
			try {
				img = ImageIO.read(Panel.class.getResource(file[0]));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		panel.home.repaint();

	}

}
