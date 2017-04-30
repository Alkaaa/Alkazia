package fr.thisismac.core;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class ImgButton extends JButton implements MouseListener {

	private static final long serialVersionUID = 1L;
	private Image img;

	private Panel panel;

	String file[];

	public ImgButton(Panel p, int x, int y, int w, int h, String... file) {

		setBounds(x, y, w, h);
		this.panel = p;

		this.file = file;

		try {
			img = ImageIO.read(Panel.class.getResource(file[1]));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.addMouseListener(this);

		this.setBorderPainted(false);
		this.setVisible(true);
		this.setBackground(new Color(0, 0, 0, 0));

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);

		panel.home.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		try {
			img = ImageIO.read(Panel.class.getResource(file[0]));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {

		try {
			img = ImageIO.read(Panel.class.getResource(file[1]));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
