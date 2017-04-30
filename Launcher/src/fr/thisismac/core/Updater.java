package fr.thisismac.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

import fr.thisismac.process.JavaProcessLauncher;

public class Updater {

	public static HashMap<String, Fichier> files = new HashMap<String, Fichier>();
	Panel p;
	private String username;

	public Updater(Panel pa, String us) {
		this.p = pa;
		p.play.setEnabled(false); 
		this.username = us;

		p.displayString = "Téléchargement en cours !";
		p.repaint();
		p.thread.start();

		new Thread() {
			@Override
			public void run() {

				p.bar.showDisplay = true;
				p.bar.displayLoad = "Vérification de mise-à-jour 1/2";
				System.out.println("Preparation du dossier bin...");
				Parser.prepareJars();
				
				p.bar.displayLoad = "Vérification de mise-à-jour 2/2";
				System.out.println("Preparation des ressources...");
				Parser.prepareRessources();
				
				p.bar.displayLoad = "Calcul des fichiers à re-téléchargés.";
				System.out.println("Calcul de la taille à télécharger...");
				
				double size = calculeSize();

				p.bar.setValue(0);
				p.bar.setMaximum((int) size);

				p.repaint();

				p.bar.started = true;
				downloadAll(size);
			}
		}.start();

	}

	private double calculeSize() {
		double size = 0;
		int i = 0;
		for (Entry<String, Fichier> entry : files.entrySet()) {
			if (!entry.getValue().hasToDowload) {
				size += entry.getValue().size;
				i++;
			}
		}
		System.out.println(i + " files to download !");
		nb = i;
		return size / 100.0;
	}

	int nb;
	public static boolean dl;
	public static double dld;

	public void downloadAll(double size) {
		int left = nb;
		for (Entry<String, Fichier> entry : files.entrySet()) {
			if (!entry.getValue().hasToDowload) {
				Fichier f = entry.getValue();
				File file = new File(f.path);
				String str = file.getAbsolutePath();
				System.out.println("DL : " + file.getAbsolutePath());
				System.out.println(f.url);
				str = str.substring(0, str.lastIndexOf(File.separator) + 1);
				File dos = new File(str);
				dos.mkdirs();
				dl = true;
				p.bar.fileLeft = left;
				Download.dl(
						f.url.replace(" ", "%20"),
						file);

				p.bar.setValue((int) (dld / 100.0));
				p.bar.displayLoad = file.getName();

				while (dl) {
					p.bar.setValue((int) (dld / 100.0));

				}
				p.bar.setValue((int) (dld / 100.0));
				left--;
			}
		}
		p.bar.started = false;
		p.play.setEnabled(true);
		p.bar.setValue(p.bar.getMaximum());
		p.bar.displayLoad = "";
		p.displayString = "Lancement de Alkazia !";
		p.repaint();
		
		System.out.println("------- Launcher has completed his work, now Minecraft is starting ------");
		launchGame(username);
	}
	public void launchGame(String user){
		
		StringBuilder sb = new StringBuilder();
		File xml = new File(Util.getWorkingDirectory(), "log4j2.xml");
		if (!xml.exists()) {
			try {
				xml.createNewFile();
			} catch (IOException e) {
		
				e.printStackTrace();
			}
		}
		
		File libFolder = new File(Util.getWorkingDirectory(), "" + File.separator + "bin" + File.separator + "libraries");
		File[] libs = libFolder.listFiles();
		String str = "";
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			for (int i = 0; i < libs.length; i++) {
				str += libs[i].getAbsolutePath() + ";";
			}
		}
		else {
			for (int i = 0; i < libs.length; i++)
				str += libs[i].getAbsolutePath() + ":";
		}


		JavaProcessLauncher processLauncher = new JavaProcessLauncher(null, new String[0]);
		  processLauncher.directory(Util.getWorkingDirectory());
		  
		  processLauncher.addCommands(new String[] {"-Xms512m"});
		  processLauncher.addCommands(new String[] {"-Xmx" + p.getRAMSelected()});
		  processLauncher.addCommands(new String[] {"-Dlog4j.configurationFile=" + Util.getPath() + "" + File.separator + "log4j2.xml"});
		  processLauncher.addCommands(new String[] {"-Djava.library.path=" + Util.getPath() + "" + File.separator + "bin" + File.separator + "natives" + File.separator});
		  processLauncher.addCommands(new String[] {"-cp", str + Util.getPath() + "" + File.separator + "bin" + File.separator + "minecraft.jar"});
		  processLauncher.addCommands(new String[] {"net.minecraft.client.main.Main"});
		  processLauncher.addCommands(new String[] {"--username", user});
		  processLauncher.addCommands(new String[] {"--alkazia", "alkazia"});
		  processLauncher.addCommands(new String[] {"--version", "1.8"});
		  processLauncher.addCommands(new String[] {"--gameDir", Util.getPath()});
		  processLauncher.addCommands(new String[] {"--assetIndex", "1.8"});
		  processLauncher.addCommands(new String[] {"--assetsDir", Util.getPath() + File.separator + "assets"});
		  processLauncher.addCommands(new String[] {"--uuid", "151655454"});
		  processLauncher.addCommands(new String[] {"--userProperties", "{}"});
		  processLauncher.addCommands(new String[] {"--accessToken", "fdshg7843y78gsdf9fd"});
		try {
			processLauncher.start();
			
			p.setVisible(false);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	boolean performed;

	public void setPerformed() {
		performed = true;
	}

}
