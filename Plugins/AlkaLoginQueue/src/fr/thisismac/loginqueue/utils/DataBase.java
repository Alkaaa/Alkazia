package fr.thisismac.loginqueue.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class DataBase implements Serializable {

    private static final long serialVersionUID = 4364142227662752242L;
    private static ArrayList<String> perm = new ArrayList<String>();

    public static boolean hasPermission(String key) {
        return perm.contains(key);
    }
    
    public static void addPermission(String key) {
        perm.add(key);
    }

    public static void load() {
        File f = new File("loginvip.txt");
        try {
            if(!f.exists())
            	f.createNewFile();
            
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
			while ((line = br.readLine()) != null) {
			   perm.add(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void save() {
        File f = new File("loginvip.txt");
        try {
            if(!f.exists())
            	f.createNewFile();
            
            BufferedWriter br = new BufferedWriter(new FileWriter(f));
			for (String s : perm) {
			   br.write(s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
