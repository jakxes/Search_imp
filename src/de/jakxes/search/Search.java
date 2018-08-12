 package de.jakxes.search;


import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.lang.Object;



@SuppressWarnings("serial")
public class Search extends JFrame implements ActionListener {

	File[] messageStrings = File.listRoots();
	JComboBox<?> cmbMessageList = new JComboBox<Object>(messageStrings);
	JTextArea textfeld = new JTextArea("Nicht gesetzt.", 0, 0);
	JLabel lblText = new JLabel();
	JLabel label2 = new JLabel();
	JScrollPane scrollpane = new JScrollPane(textfeld);
	static TextField tf = new TextField("", 30);
	JButton button = new JButton("Find");
	File selectedRoot = (File) cmbMessageList.getSelectedItem();
	Thread thread;
	static String acceptedFiles = "";
	static char[] strc = tf.getText().toCharArray();
	
	
	public static void main(String[] args) {
		Search fr = new Search();
		fr.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cmbMessageList) {
			JComboBox<?> cb = (JComboBox<?>) e.getSource();
			selectedRoot = (File) cb.getSelectedItem();

		} else if (e.getSource() == button) {
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					textfeld.setText("Wird geladen...");
					ArrayList<File> files = searchFile(selectedRoot, tf.getText());
					ArrayList<String> filesstr = new ArrayList<>();
					for (int i = 0; i < files.size(); i++) 
						filesstr.add(files.get(i).toString());
					StringBuilder str2 = new StringBuilder();
					for (String s : filesstr) 
						str2.append(s + "\n");
					if(!files.isEmpty()) {
						textfeld.setText(str2.toString());
					} else 
						textfeld.setText("Es wurden keine Dateien gefunden werden.");
					textfeld.setLineWrap(true);
					textfeld.setWrapStyleWord(true);
					int size = 0;
					for (int i = 0; i < filesstr.size(); i++) 
						if (filesstr.get(i).length() > size) size = filesstr.get(i).length();
					textfeld.setColumns(size / 2);
					for (int i = 0; i < files.size(); i++) {
						textfeld.setRows(i);
						setSize(1920, i * 25);
					}
					textfeld.setEditable(false);
					JScrollPane scroll = new JScrollPane(textfeld);
					scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);		
					add(scroll);

				}
			});
			thread.start();

		}

	}

	public Search() {
		
		setLayout(new FlowLayout());
		setSize(400, 300);
		setTitle("Computer Suche by Jakxes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		cmbMessageList.setSelectedIndex(1);
		cmbMessageList.addActionListener(this);
		button.addActionListener(this);

		add(cmbMessageList);
		add(tf);
		add(scrollpane);
		add(button);
		add(label2);
		add(textfeld);
		add(lblText);
		
	}

	public static ArrayList<File> searchFile(File dir, String find) {
		File[] files = dir.listFiles();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < strc.length; i++) {
			
			if(strc[i] == '?') {
				builder.append(".");
			} else if(strc[i] == '*') {
				builder.append(".*");
			} else {
				builder.append(strc[i]);
			}
		}
		Pattern p = Pattern.compile( builder.toString() );
		Matcher m;

		ArrayList<File> matches = new ArrayList<File>();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				m = p.matcher(files[i].getName());
				System.out.println(m.toMatchResult().toString());
 				
				
				if (files[i].isDirectory()) matches.addAll(searchFile(files[i], find)); 
			}
		}
		return matches;
	}

}