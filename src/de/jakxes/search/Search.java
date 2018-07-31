package de.jakxes.search;

import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
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
	TextField tf = new TextField("", 30);
	JButton button = new JButton("suchen");
	File selectedRoot = (File) cmbMessageList.getSelectedItem();
	Thread thread;
	
	
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
					textfeld.setText(str2.toString());
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
		ArrayList<File> matchesregex = new ArrayList<>();
 		for (File file : files) {
			if(file.toString().matches(find)) {
				matchesregex.add(file);
			}
		}
		ArrayList<File> matches = new ArrayList<File>();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				
				if (files[i].getName().contains(find)) {
					matches.add(files[i]);
				}
				if (files[i].isDirectory())
					matches.addAll(searchFile(files[i], find)); 
				}
			}
		if(matchesregex.contains(find) && matches.contains(find)) {
			return matchesregex;
		} else {
			return matches;
		}
		
	}

}