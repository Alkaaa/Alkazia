package fr.thisismac.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Home extends JFrame {

	private static final long serialVersionUID = 1L;
	private Panel panel = new Panel(this);

	public Home() {

		super("Alkazia - Launcher");
		this.setSize(1068, 577);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setUndecorated(true);
		this.addKeyListener(panel);
		setFocusTraversalKeysEnabled(false);
		this.setFocusable(true);
		this.setBackground(new Color(0,0,0,0));
		this.add(panel);
		
		
		  try { 
			 Image ico = ImageIO.read(Panel.class.getResource("/favicon.png"));
		  this.setIconImage(ico);
		  } catch (Exception e2) {
		  e2.printStackTrace(); }

		this.setVisible(true);

	}

}
