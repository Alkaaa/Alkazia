package fr.thisismac.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

	private static File workDir = null;

	public static String readTxt(String file) {
		String str = "null";
		try {
			URL url = new URL(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));

			while ((str = in.readLine()) != null) {

				break;
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {

		}
		return str;
	}

	public static String getSHA(String message) {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] hash = md.digest(message.getBytes("UTF-8"));

			StringBuilder sb = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				sb.append(String.format("%02x", b & 0xff));
			}

			digest = sb.toString();

		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(StringReader.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(StringReader.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return digest;
	}

	public static String getHTML(URL url) {

		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static File getWorkingDirectory() {
		if (workDir == null)
			workDir = getWorkingDirectory("Alkazia");
		return workDir;
	}
	public static String getPath() {
		  return getWorkingDirectory().getAbsolutePath();
	  }

	public static File getWorkingDirectory(String applicationName) {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		switch (getPlatform().ordinal()) {
		case 0:
		case 1:
			workingDirectory = new File(userHome, '.' + applicationName + '/');
			break;
		case 2:
			String applicationData = System.getenv("APPDATA");
			if (applicationData != null)
				workingDirectory = new File(applicationData, "."
						+ applicationName + '/');
			else
				workingDirectory = new File(userHome,
						'.' + applicationName + '/');
			break;
		case 3:
			workingDirectory = new File(userHome,
					"Library/Application Support/" + applicationName);
			break;
		default:
			workingDirectory = new File(userHome, applicationName + '/');
		}
		if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs()))
			throw new RuntimeException(
					"The working directory could not be created: "
							+ workingDirectory);
		return workingDirectory;
	}

	private static OS getPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win"))
			return OS.windows;
		if (osName.contains("mac"))
			return OS.macos;
		if (osName.contains("solaris"))
			return OS.solaris;
		if (osName.contains("sunos"))
			return OS.solaris;
		if (osName.contains("linux"))
			return OS.linux;
		if (osName.contains("unix"))
			return OS.linux;
		return OS.unknown;
	}

	private static enum OS {
		linux, solaris, windows, macos, unknown;
	}
}