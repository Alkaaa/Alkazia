package fr.thisismac.core;

public class Fichier {

	public double size;
	public String url;
	public boolean hasToDowload;
	public String path;

	public Fichier(double s, String u, String p, boolean b) {
		this.size = s;
		this.url = u;
		this.hasToDowload = b;
		this.path = p;
	}
}
