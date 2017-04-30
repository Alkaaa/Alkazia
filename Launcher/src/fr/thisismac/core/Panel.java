package fr.thisismac.core;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Panel extends JPanel implements ActionListener, KeyListener {

	private Point initialClick;

	public static final long serialVersionUID = 1L;
	public Image bg, field;

	public JTextField user = new JTextField();

	boolean showError = false;
	String errorMSG = "";
	
	public Font font;
	
	public OptionsFrame of;

	public JPasswordField password = new JPasswordField();

	public ImgButton quit = new ImgButton(this, 1000, 7, 50, 28,
			"/quit_normal.png",
			"/quit_normal.png");

	public ImgButton reduce = new ImgButton(this, 958, 7, 45, 29,
			"/minimize_normal.png",
			"/minimize_normal.png");

	public ImgButton play = new ImgButton(this, 355, 380, 166, 47,
			"/login_hover.png",
			"/login_normal.png");
	
	public ImgButton options = new ImgButton(this, 555, 380, 166, 47,
			"/options_hover.png",
			"/options_normal.png");
	

	public StyledBar bar = new StyledBar(365, 460, 351, 10, "/void.png", "/progress.png", 1, 3, 351, 8);

	public Home home;

	public ThreadUpdate thread = new ThreadUpdate(this);

	public String displayString = "";
	
	public boolean useShader = false;
	public final File lastlogin = new File(Util.getWorkingDirectory() + File.separator + "lastlogin");

	public Panel(final Home home) {
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				initialClick = e.getPoint();
				getComponentAt(initialClick);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {

				// get location of Window
				int thisX = home.getLocation().x;
				int thisY = home.getLocation().y;

				// Determine how much the mouse moved since the initial click
				int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
				int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

				// Move window to this position
				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				home.setLocation(X, Y);
			}
		});

		this.home = home;
		
		try {
			checkForVersion();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		try {
			Font.createFont(Font.TRUETYPE_FONT, Panel.class.getResourceAsStream("/roboto.ttf"));
			this.font = new Font("Roboto Light", Font.TRUETYPE_FONT, 18);
		} catch (FontFormatException e1) {
			e1.printStackTrace();
			this.font = Font.getFont("Arial");
		} catch (IOException e) {
			e.printStackTrace();
			this.font = Font.getFont("Arial");
		}
		
		
		try {
			bg = ImageIO.read(Panel.class
					.getResource("/background.png"));
			field = ImageIO.read(Panel.class
					.getResource("/field.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setBounds(0, 0, 1068, 577);

		this.setLayout(null);
		quit.addActionListener(this);
		quit.setOpaque(false);
		reduce.addActionListener(this);
		reduce.setOpaque(false);
		play.addActionListener(this);
		play.setOpaque(false);
		options.addActionListener(this);
		options.setOpaque(false);
		
		user.addKeyListener(this);
		user.setFocusTraversalKeysEnabled(true);

		user.setBorder(null);
		user.setFont(font);
		user.setBackground(new Color(0, 0, 0, 0));
		user.setBounds(460, 226, 166, 47);
		user.setHorizontalAlignment(JTextField.CENTER);
		user.setForeground(Color.BLACK);
		user.setOpaque(false);

		password.addKeyListener(this);
		password.setFocusTraversalKeysEnabled(true);
		password.setBackground(new Color(0, 0, 0, 0));
		password.setOpaque(false);

		password.setBorder(null);
		password.setFont(font);
		password.setForeground(Color.BLACK);

		password.setBounds(460, 292, 166, 47);
		password.setHorizontalAlignment(JTextField.CENTER);
		
		bar.setFont(font);
		bar.setOpaque(false);
		
		this.add(quit);
		this.add(reduce);
		this.add(user);
		this.add(password);
		this.add(play);
		this.add(options);

		this.add(bar);

		this.setBackground(new Color(0, 0, 0, 0));
		
		if(lastlogin.exists()) {
			this.readLastLogin();
		}
		this.setVisible(true);
		this.setOpaque(false);

	}

	private void checkForVersion() throws IOException {
		File version = new File(Util.getWorkingDirectory() + File.separator + "version");
		
		if(version.exists()) {
			if(!(new FileReader(version).read() == 4)) {
				deleteOldFolder();
				new FileOutputStream(version).write(4);
				System.out.println("Nettoyage de la version 4 effectué avec succés !");
			}
		}
		else {
			deleteOldFolder();
			version.createNewFile();
			new FileOutputStream(version).write(4);
			System.out.println("Nettoyage de la version 4 effectué avec succés !");
		}
		
	}

	private void deleteOldFolder() throws IOException {
		File oldres = new File(Util.getWorkingDirectory() + File.separator + "ressources");
		if(oldres.exists()) deleteDirectory(oldres);
		
		File oldassets = new File(Util.getWorkingDirectory() + File.separator + "assets");
		if(oldassets.exists()) deleteDirectory(oldassets);
		
		File oldlib = new File(Util.getWorkingDirectory() + File.separator + "bin" + File.separator + "libs");
		if(oldlib.exists()) deleteDirectory(oldlib);
		
		File oldversions = new File(Util.getWorkingDirectory() + File.separator + "versions");
		if(oldversions.exists()) deleteDirectory(oldversions);
		

		File oldlogs = new File(Util.getWorkingDirectory() + File.separator + "logs");
		if(oldlogs.exists()) deleteDirectory(oldlogs);
	}
	
	public void deleteDirectory(File file) throws IOException{
	 
	    	if(file.isDirectory()) {
	    		if(file.list().length == 0){
	    		   file.delete();
	    		   System.out.println("Directory is deleted : " + file.getAbsolutePath());
	    		}
	    		else {
	        	   String files[] = file.list();
	 
	        	   for (String temp : files) {
	        	      File fileDelete = new File(file, temp);
	        	      deleteDirectory(fileDelete);
	        	   }
	 
	        	   if(file.list().length == 0) {
	           	     file.delete();
	        	     System.out.println("Directory is deleted : " + file.getAbsolutePath());
	        	   }
	    		}
	    	}
	    	else {
	    		file.delete();
	    		System.out.println("File is deleted : " + file.getAbsolutePath());
	    	}
	    }

	@Override
	public void paintComponent(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		if(g != null && fm != null) {
			g.setColor(Color.white);
			g.setFont(font);
			g.drawImage(bg, 0, 0, 1068, 577, this);
			g.drawImage(field, 355, 225, 369, 114,this);
			
			if (showError) {
				g.setColor(Color.red);
				g.drawString(errorMSG, (getWidth() / 2) - (fm.stringWidth(bar.displayLoad) / 2) - 70, 493);
			}
			else if (bar.started) {
				String str = bar.perc + " % / " + bar.fileLeft + " fichiers restants";
				g.drawString(str, (getWidth() / 2) - (fm.stringWidth(str) / 2) - 25, 493);
			}
			else if (bar.showDisplay) {
				g.drawString(bar.displayLoad, (getWidth() / 2) - (fm.stringWidth(bar.displayLoad) / 2) - 25, 493);
			}
		}
		this.setOpaque(false);
		this.setBackground(Color.black);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == quit) {
			System.exit(0);
		} else if (e.getSource() == reduce) {
			this.home.setState(JFrame.ICONIFIED);
		} else if (e.getSource() == play) {
			this.play();
		} else if(e.getSource() == options) {
			of = new OptionsFrame(this);
		}
		
	}

	public void refresh() {

		this.home.repaint();
	}

	@SuppressWarnings("deprecation")
	void play() {
		
		URL url;
		try {
			url = new URL("http://api.alkazia.net/login.php?user=" + URLEncoder.encode(user.getText(), "UTF-8") + "&pwd="+ Util.getSHA(password.getText()) + "&uuid=" + getMACAdress());
			String resp = Util.getHTML(url);
			
			if (resp.contains("false")) {
				JOptionPane.showMessageDialog(new Frame(), resp.split(":")[1], "Problème de connexion !", 0);
				showError = true;
				errorMSG = "Problème de connexion à votre compte";
			} else if (resp.contains("true")) {
				

				showError = false;
				
				bar.displayLoad = "Identifiants corrects, connexion ...";
				
				
				if(isRememberActived()) {
					this.saveLastLogin();
				}
				else if(lastlogin.exists()){
					lastlogin.delete();
				}
				
				String[] respo = resp.split(":");
				new Updater(this, respo[1]);
			}
			 else {
				showError = true;
				errorMSG = "Un problème a été détecté, veuillez contacter un admin.";
				}
		} catch (MalformedURLException e) {
			System.out.println(e.getLocalizedMessage());
		} catch ( UnsupportedEncodingException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.play();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	private void readLastLogin() {
		final File lastLogin = new File(Util.getWorkingDirectory() + File.separator + "lastlogin");
		if (!lastLogin.exists()) {
			try {
				lastLogin.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			final Cipher ciph = openCipher(2);
			final DataInputStream dis = new DataInputStream(
					new CipherInputStream(new FileInputStream(lastLogin), ciph));

			user.setText(dis.readUTF());
			password.setText(dis.readUTF());
			dis.close();
			bar.displayLoad = "Identifiants sauvegardés";
			bar.showDisplay = true;

		} catch (Exception e) {
			System.out.println("Failed to read lastLogin file");
			displayString = "En attente d'authentification..";
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void saveLastLogin() {
		try {
			File lastLogin = new File(Util.getWorkingDirectory()
					+ File.separator + "lastlogin");

			final Cipher ciph = openCipher(1);
			final DataOutputStream dos = new DataOutputStream(
					new CipherOutputStream(new FileOutputStream(lastLogin),
							ciph));

			dos.writeUTF(user.getText());
			dos.writeUTF(password.getText());
			dos.close();
		} catch (final Exception e) {
			System.out.println("Failed to save lastLogin file");
			e.printStackTrace();
		}
	}

	private Cipher openCipher(final int mode) throws Exception {
		final Random rnd = new Random(43287234L);
		final byte[] data = new byte[8];

		rnd.nextBytes(data);
		final PBEParameterSpec spec = new PBEParameterSpec(data, 5);
		final SecretKey key = SecretKeyFactory
				.getInstance("PBEWithMD5AndDES")
				.generateSecret(new PBEKeySpec("passwordAlkazia".toCharArray()));

		final Cipher ret = Cipher.getInstance("PBEWithMD5AndDES");
		ret.init(mode, key, spec);
		return ret;
	}
	
	public boolean isRememberActived() {
		File file = new File(Util.getWorkingDirectory() + File.separator + "launcherOptions.txt");
		
		if(!file.exists()) { return false; }
		
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(file));
			String[] options = bf.readLine().split(":");
			bf.close();
			
			if(options[0].contains("true")) {
				return true;
			}
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
		
	}
	
	public boolean isShaderActived() {
		File file = new File(Util.getWorkingDirectory() + File.separator + "launcherOptions.txt");
		
		if(!file.exists()) { return false; }
		
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(file));
			String[] options = bf.readLine().split(":");
			bf.close();
			
			if(options[1].contains("true")) {
				return true;
			}
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
		
	}
	
	public String getRAMSelected() {
		File file = new File(Util.getWorkingDirectory() + File.separator + "launcherOptions.txt");
		
		if(!file.exists()) { return "512M"; }
		
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(file));
			String[] options = bf.readLine().split(":");
			bf.close();
			return options[2];
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return "512M";
		
	}
	
	public String getMACAdress() {
		    InetAddress ip;
			try {
				ip = InetAddress.getLocalHost();
				NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			      byte[] mac = network.getHardwareAddress();
			      if(mac != null) {
			        StringBuilder sb = new StringBuilder();
			        for (int i = 0; i < mac.length; i++) {
			          sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			        }
			        return sb.toString();
			      }
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		
		return "null";
	}

}
