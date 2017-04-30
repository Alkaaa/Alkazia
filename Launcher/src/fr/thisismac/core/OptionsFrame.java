package fr.thisismac.core;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class OptionsFrame extends JFrame implements ActionListener{

	private JLabel remember = new JLabel("Se souvenir des identifiants ?");
	private JLabel shader = new JLabel("Shaders ? ");
	private JLabel ram = new JLabel("Allouage de la RAM :");
	private JButton ok = new JButton("Valider");
	
	String[] choices = {"512m", "1024m","2048m", "3072m","4096m"};

    private final JComboBox ramChoice = new JComboBox(choices);

	private Panel panel;
	private JCheckBox rememberBox = new JCheckBox();
	private JCheckBox shaderBox = new JCheckBox();
	
	public OptionsFrame(Panel p) {
		
		this.setSize(250, 220);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setTitle("Options du Launcher");
		
		this.panel = p;
		
		remember.setForeground(Color.black);
		remember.setBounds(30, 0, 200, 70);
		
		rememberBox.setBounds(200, 25, 20, 20);
		
		shader.setForeground(Color.black);
		shader.setBounds(30, 45, 200, 70);
		
		shaderBox.setBounds(200, 70, 20, 20);
		
		ram.setBounds(30, 95, 200, 70);
		ramChoice.setBounds(150, 117, 70, 25);
		ramChoice.setSelectedIndex(0);
		
		ok.setBounds(90, 155, 70, 30);
		ok.addActionListener(this);
		
		getSaveOptions(p);
		saveOptions(p);
		
		this.add(remember);
		this.add(rememberBox);
		
		this.add(shader);
		this.add(shaderBox);

		this.add(ram);
		this.add(ramChoice);
		this.add(ok);
		
		this.setVisible(true);
	}

	private void saveOptions(Panel p) {
		File file = new File(Util.getWorkingDirectory() + File.separator + "launcherOptions.txt");
		
		if(!file.exists()) { 
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		BufferedWriter bf;
		try {
			bf = new BufferedWriter(new FileWriter(file));
			
			bf.write(rememberBox.isSelected() + ":" + shaderBox.isSelected() + ":" + ramChoice.getSelectedItem().toString());
			bf.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void getSaveOptions(Panel p) {
		File file = new File(Util.getWorkingDirectory() + File.separator + "launcherOptions.txt");
		
		if(!file.exists()) { return; }
		
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(file));
			String[] options = bf.readLine().split(":");
			
			if(options[0].contains("true")) {
				rememberBox.setSelected(true);
			}
			
			if(options[1].contains("true")) {
				shaderBox.setSelected(true);
				p.useShader = true;
			}
			
			ramChoice.setSelectedItem(options[2]);
			
			bf.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ok) {
			saveOptions(panel);
			this.setVisible(false);
		}
		
	}
	
	public String getSelectedRAM() {
		return ramChoice.getSelectedItem().toString();
	}
	
	public boolean isRememberChecked() {
		return rememberBox.isSelected();
	}
	
	public boolean isShaderChecked() {
		return shaderBox.isSelected();
	}

}
