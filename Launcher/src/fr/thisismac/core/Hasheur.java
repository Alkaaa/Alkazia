package fr.thisismac.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasheur {

	public static String getMD5(File file) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		byte[] dataBytes = new byte[1024];

		int nread = 0;

		try {
			while ((nread = fis.read(dataBytes)) != -1) {

				md.update(dataBytes, 0, nread);

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		byte[] mdbytes = md.digest();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < mdbytes.length; i++) {

			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
					.substring(1));

		}
		String md5 = sb.toString();
		try {
			fis.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return md5;
	}
}
