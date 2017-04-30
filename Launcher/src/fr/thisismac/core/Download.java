package fr.thisismac.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Download {
	public static void dl(final String adresse, final File dest) {

		new Thread() {
			@Override
			public void run() {
				FileOutputStream fos = null;
				BufferedReader reader = null;

				try {
					// Création de la connexion
					URL url = new URL(adresse);

					URLConnection conn = url.openConnection();

					int fileLength = conn.getContentLength();
					System.out.println("Lenght : " + fileLength);

					if (fileLength == -1) {
						throw new IOException("Fichier non valide.");
					}

					// Lecture de la réponse

					InputStream in = conn.getInputStream();
					reader = new BufferedReader(new InputStreamReader(in));

					if (dest == null) {
						String fileName = url.getFile();
						fileName = fileName
								.substring(fileName.lastIndexOf('/') + 1);
						fos = new FileOutputStream(new File(fileName));
					} else
						fos = new FileOutputStream(dest);

					byte[] buff = new byte[1024];

					int n;
					while ((n = in.read(buff)) != -1) {
						fos.write(buff, 0, n);

						Updater.dld += (double) n;

					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						fos.flush();
						fos.close();
						Updater.dl = false;
					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						reader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}