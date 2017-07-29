package com.lehsten.edmund.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sourceforge.tess4j.*;

public class NewImageWindow extends JFrame{
	private File imageLocation;
	
	public NewImageWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanle = new JPanel();
		JPanel filePanle = new JPanel();
		JPanel birdPanle = new JPanel();
		JPanel numbersPanle = new JPanel();
		
		final Canvas image = new Canvas(null);
		
		JLabel fileLabel = new JLabel("File:");
		JLabel birdLabel = new JLabel("Vögel:");
		JLabel fromLabel = new JLabel("Von:");
		JLabel toLabel = new JLabel("Bis:");
		
		JButton changeFileButton = new JButton("Change File Location");
		changeFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			    JFileChooser chooser = new JFileChooser();
//			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			  //  	    "Image files", ImageIO.getReaderFileSuffixes());
			  //  chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			            chooser.getSelectedFile().getName());
			       imageLocation = chooser.getSelectedFile();
			       BufferedImage img = null;
			       try 
			       {
			    	    img = ImageIO.read(chooser.getSelectedFile());
			    	    image.setImage(img);
			    	    repaint();
			       } 
			    	catch (IOException e1) 
			    	{
			    	    e1.printStackTrace();
			    	}
			       
			    }
			}
		});
		JButton save = new JButton("Fertig");
		save.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(getParent());
				try{
					//get lines
					int[][] lines = image.getLines();
					//cut first number and save
					BufferedImage cutImage = image.getImage().getSubimage(0, 0,lines[1][0], lines[0][0]);
					ImageIO.write(cutImage, "png", chooser.getSelectedFile());
					// try number recognition
					ITesseract instance = new Tesseract();
					instance.setDatapath("/home/edmo/Documents/Programing/Java EE workspaces/Langenwerder/Langenwerder Fogle beringung/tessdata/tessdata");
					String result = instance.doOCR(cutImage);
					System.out.println("RECOGNIZED TEXT: "+result);
					
				} catch (TesseractException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		JComboBox birdNameChooser = new JComboBox();
		
		final JTextField fromField = new JTextField(4);
		final JTextField toField = new JTextField(4);
		DocumentListener inputChangeListener = new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				update();
			}
			
			public void insertUpdate(DocumentEvent e) {
				update();
			}
			
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				update();
			}
			
			public void update(){
				if(!fromField.getText().equals("")  && !toField.getText().equals("")){
					int from = Integer.parseInt(fromField.getText());
					int to = Integer.parseInt(toField.getText());
					int number = to-from+1;
					image.setNumHorizontal(number);
				}else{
					
				}
			}
		};
		fromField.getDocument().addDocumentListener(inputChangeListener);
		toField.getDocument().addDocumentListener(inputChangeListener);
		
		
		filePanle.add(fileLabel);
		filePanle.add(changeFileButton);
		
		birdPanle.add(birdLabel);
		birdPanle.add(birdNameChooser);
		
		numbersPanle.add(fromLabel);
		numbersPanle.add(fromField);
		numbersPanle.add(toLabel);
		numbersPanle.add(toField);
		
		mainPanle.setLayout(new BoxLayout(mainPanle, BoxLayout.PAGE_AXIS));
		mainPanle.add(filePanle);
		mainPanle.add(new Box.Filler(new Dimension(100,0)/*min size*/,new Dimension(100,0) /*prefSize*/,new Dimension(100,1)/*maxSize*/ ));
		mainPanle.add(birdPanle);
		mainPanle.add(new Box.Filler(new Dimension(100,0)/*min size*/,new Dimension(100,0) /*prefSize*/,new Dimension(100,1)/*maxSize*/ ));
		mainPanle.add(numbersPanle);
		mainPanle.add(new Box.Filler(new Dimension(100,0)/*min size*/,new Dimension(100,0) /*prefSize*/,new Dimension(100,1)/*maxSize*/ ));
//		mainPanle.add(image);
		add(mainPanle,BorderLayout.PAGE_START);
		add(image);
		add(save,BorderLayout.PAGE_END);
		//set window properties
		setLocationRelativeTo(null);
		setSize(500, 200);
		//open window
		setVisible(true);
	}
}
