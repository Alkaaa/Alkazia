package fr.thisismac.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTextField;

public class StyledText extends JTextField implements FocusListener {

	private static final long serialVersionUID = 1L;

	private Image img;
	public boolean clicked;
	public boolean show;
	private boolean password;
	public boolean selectAll;

	public StyledText(int x, int y, int w, int h, String file, boolean pass) {

		try {
			img = ImageIO.read(Panel.class.getResource(file));

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setBounds(x, y, w, h);
		this.addFocusListener(this);
		this.setBorder(null);
		this.password = pass;
	}

	public void paintComponent(Graphics g) {

		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana", Font.BOLD, 11));
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		FontMetrics fm = g.getFontMetrics();

		String str;
		String text = this.getText();
		if (password) {
			String temp = "";
			for (int i = 0; i < text.length(); i++) {
				temp += "•";
			}
			text = temp;
		}
		if (show) {
			str = ">" + text + "<";
		} else {
			str = text;
		}

		if (this.selectAll) {
			g.setColor(Color.GRAY);
			g.fillRect((getWidth() / 2) - (fm.stringWidth(str) / 2),
					(getHeight() / 2) + (fm.getHeight() / 2) - 18,
					fm.stringWidth(str), 16);
			g.setColor(Color.WHITE);
		}
		g.drawString(str, (getWidth() / 2) - (fm.stringWidth(str) / 2),
				(getHeight() / 2) + (fm.getHeight() / 2) / 2);

	}

	@Override
	public void focusGained(FocusEvent arg0) {

		clicked = true;
		new Thread() {
			@Override
			public void run() {

				while (clicked) {

					if (show) {
						show = false;
					} else {
						show = true;

					}
					repaint();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}

			}
		}.start();
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		clicked = false;
		show = false;
		selectAll = false;

	}

}
