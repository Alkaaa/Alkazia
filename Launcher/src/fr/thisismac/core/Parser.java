package fr.thisismac.core;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Parser {

	public static void prepareJars() {
		try {
			URL xmlURL = new URL("http://launcher.alkazia.fr/launcher/bin.xml");
			InputStream xml = xmlURL.openStream();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);
			Node node = doc.getFirstChild();

			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node parse = node.getChildNodes().item(i);
				if (parse.getNodeName().equals("Contents")) {

					String key = parse.getChildNodes().item(1).getTextContent().replace("\\", "/");
					String md5 = parse.getChildNodes().item(3).getTextContent()
							.replace("\"", "");
					double size = Double.parseDouble(parse.getChildNodes().item(5).getTextContent());
					
					if(size <= 0) {
						
					}
					else {
						File local = new File(Util.getWorkingDirectory(), key);
						if (!local.isDirectory()) {
							boolean correct = true;
							if (!local.exists()	|| !(md5.equals(Hasheur.getMD5(local)))) {
								correct = false;
							}
							if (!correct) {
								System.out.println(local.getName() + " : "
										+ correct + " : " + size + " : "
										+ local.getAbsolutePath());
							}
							Updater.files.put(key, new Fichier(size, "http://launcher.alkazia.fr/launcher/" + key,
									local.getAbsolutePath(), correct));
	
						}
					}
				}

			}
			xml.close();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void prepareRessources() {
		try {
			URL xmlURL = new URL("http://launcher.alkazia.fr/launcher/assets.xml");
			InputStream xml = xmlURL.openStream();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);
			Node node = doc.getFirstChild();
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node parse = node.getChildNodes().item(i);
				if (parse.getNodeName().equals("Contents")) {

					String key = parse.getChildNodes().item(1).getTextContent().replace("\\", "/");
					String md5 = parse.getChildNodes().item(3).getTextContent()
							.replace("\"", "");
					double size = Double.parseDouble(parse.getChildNodes()
							.item(5).getTextContent());
					if(size <= 0) {
						
					}
						else {
						File local = new File(Util.getWorkingDirectory(), key);
						boolean correct = true;
						if (!local.exists() || !(md5.equals(Hasheur.getMD5(local)))) {
							correct = false;
						}
						if (!correct) {
							System.out.println(local.getName() + " : " + correct
									+ " : " + size + " : "
									+ local.getAbsolutePath());
						}
	
						Updater.files.put(key,
								new Fichier(size,
										"http://launcher.alkazia.fr/launcher/"
												+ key, local.getAbsolutePath(),
										correct));
					}
				}

			}
			xml.close();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
