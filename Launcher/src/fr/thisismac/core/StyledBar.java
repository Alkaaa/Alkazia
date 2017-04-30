package fr.thisismac.core;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;

public class StyledBar extends JProgressBar {

	private static final long serialVersionUID = 1L;
	private Image cadre, bar;

	public boolean started;
	public String displayLoad = "";
	public boolean showDisplay;
	public int fileLeft;
	public int perc;
	private int x, y, w, h;

	public StyledBar(int x, int y, int w, int h, String f1, String f2, int x2,	int y2, int w2, int h2) {

		try {
			cadre = ImageIO.read(Panel.class.getResource(f1));
			bar = ImageIO.read(Panel.class.getResource(f2));

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setBounds(x, y, w, h);
		this.x = x2;
		this.y = y2;
		this.w = w2;
		this.h = h2;

		this.setBorder(null);

	}

	public void paintComponent(Graphics g) {
		g.drawImage(cadre, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Color.white);
		int max = this.getMaximum();

		int cur = this.getValue();
		if (max == 0) {
			max = 1;
		}

		perc = (cur * 100) / max;
		g.drawImage(bar, x, y, (int) ((double) w * ((double) perc / 100.0)), h,
				this);


	}

}
